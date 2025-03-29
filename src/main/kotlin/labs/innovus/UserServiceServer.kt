package labs.innovus

import io.grpc.Server
import io.grpc.ServerBuilder

import io.grpc.stub.StreamObserver
import labs.innovus.ExerciseUno.GetUserService
import labs.innovus.ExerciseDos.ListUsersService
import labs.innovus.ExerciseTres.StreamUserActionsService
import user.UserServiceGrpc
import user.UserServiceOuterClass.UserResponse
import user.UserServiceOuterClass.GetUserRequest
import user.UserServiceOuterClass.ListUsersRequest
import user.UserServiceOuterClass.UserAction


class UserServiceImpl : UserServiceGrpc.UserServiceImplBase() {

    private val users = mutableListOf(
         UserResponse.newBuilder().setUserId("123").setName("Alice").setEmail("alice@example.com").build(),
         UserResponse.newBuilder().setUserId("456").setName("Bob").setEmail("bob@example.com").build(),
        UserResponse.newBuilder().setUserId("3").setName("Charlie").setEmail("charlie@example.com").build()
    )
    // Implementación de GetUserService
    override fun getUser(request: GetUserRequest, responseObserver: StreamObserver<UserResponse>) {
        GetUserService(users).execute(request, responseObserver)
    }

    // Implementación de ListUsersService
    override fun listUsers(request: ListUsersRequest, responseObserver: StreamObserver<UserResponse>) {
        ListUsersService(users).execute(request, responseObserver)
    }

    // Implementación de StreamUserActionsService
    override fun streamUserActions(responseObserver: StreamObserver<UserResponse>): StreamObserver<UserAction> {
        return StreamUserActionsService(users, responseObserver).getObserver()
    }

}

fun main() {
    val server: Server = ServerBuilder.forPort(50051)
        .addService(UserServiceImpl())
        .build()
        .start()

    println("Servidor gRPC iniciado en el puerto ${server.port}")
    Runtime.getRuntime().addShutdownHook(Thread {
        println("Apagando el servidor gRPC...")
        server.shutdown()
        println("Servidor apagado.")
    })
    server.awaitTermination()
}