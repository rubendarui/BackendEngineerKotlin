package labs.innovus

import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.grpc.inprocess.InProcessChannelBuilder
import io.grpc.inprocess.InProcessServerBuilder
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import user.UserServiceGrpc

import user.UserServiceOuterClass.ListUsersRequest
import user.UserServiceOuterClass.UserAction
import user.UserServiceOuterClass.UserResponse
import user.UserServiceOuterClass.GetUserRequest
import io.grpc.stub.StreamObserver
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.fail

class UserServiceImplTest {

    private lateinit var service: UserServiceImpl
    private lateinit var blockingStub: UserServiceGrpc.UserServiceBlockingStub
    private lateinit var asyncStub: UserServiceGrpc.UserServiceStub

    @BeforeEach
    fun setup() {
        service = UserServiceImpl()
        val serverName = InProcessServerBuilder.generateName()
        val server = InProcessServerBuilder.forName(serverName).directExecutor().addService(service).build().start()
        val channel = InProcessChannelBuilder.forName(serverName).directExecutor().build()
        blockingStub = UserServiceGrpc.newBlockingStub(channel)
        asyncStub = UserServiceGrpc.newStub(channel)
    }
    @Test
    fun getUserSuccess() {
        val request = GetUserRequest.newBuilder().setUserId("123").build()
        val response = blockingStub.getUser(request)
        assertEquals("Alice", response.name)
        assertEquals("alice@example.com", response.email)
    }
    @Test
    fun getUserNotFound() {
        val request = GetUserRequest.newBuilder().setUserId("999").build()
        try {
            blockingStub.getUser(request)
            fail("Expected an exception to be thrown")
        } catch (e: StatusRuntimeException) {
            assertEquals(Status.NOT_FOUND.code, e.status.code)
            assertTrue(e.status.description!!.contains("User con ID 999 no encontrado"))
        }
    }

    @Test
    fun listUsers() {
        val request = ListUsersRequest.newBuilder().setFilter("").build()
        val responses = mutableListOf<UserResponse>()
        val responseObserver = object : StreamObserver<UserResponse> {
            override fun onNext(value: UserResponse) {
                responses.add(value)
            }

            override fun onError(t: Throwable) {
                fail("Error en la transmisión: ${t.message}")
            }

            override fun onCompleted() {
                assertEquals(3, responses.size)
            }
        }
        asyncStub.listUsers(request, responseObserver)
    }

    @Test
    fun streamUserActions() {
        val actions = listOf(
            UserAction.newBuilder().setUserId("2323").setAction("login").build(),
            UserAction.newBuilder().setUserId("9999").setAction("logout").build()
        )
        val responses = mutableListOf<UserResponse>()
        val responseObserver = object : StreamObserver<UserResponse> {
            override fun onNext(value: UserResponse) {
                responses.add(value)
            }

            override fun onError(t: Throwable) {
                fail("Error en la transmisión: ${t.message}")
            }

            override fun onCompleted() {
                assertEquals(2, responses.size)
            }
        }
        val requestObserver = asyncStub.streamUserActions(responseObserver)
        for (action in actions) {
            requestObserver.onNext(action)
        }
        requestObserver.onCompleted()
    }
}