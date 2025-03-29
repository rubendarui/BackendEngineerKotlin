package labs.innovus

import io.grpc.ManagedChannelBuilder
import io.grpc.StatusRuntimeException
import user.UserServiceGrpc
import user.UserServiceOuterClass.ListUsersRequest
import user.UserServiceOuterClass.UserAction
import user.UserServiceOuterClass.UserResponse
import user.UserServiceOuterClass.GetUserRequest
import java.util.concurrent.TimeUnit
import io.grpc.stub.StreamObserver
import java.util.logging.Level
import java.util.logging.Logger


fun main() {
    val channel = ManagedChannelBuilder.forAddress("localhost", 50051)
        .usePlaintext() // Desactivar SSL por simplicidad
        .build()

     val blockingStub = UserServiceGrpc.newBlockingStub(channel) // Usar newBlockingStub para llamadas síncronas
     val logger = Logger.getLogger("Client")

// 1. exercise-1
    try {
        val requestUserId="123"
        val requestGetUser=GetUserRequest.newBuilder().setUserId(requestUserId).build()
        val responseGetUsuar= blockingStub.getUser(requestGetUser) // Usar blockingStub síncronas
        logger.info("Usuario obtenido: ${responseGetUsuar.name}, ${responseGetUsuar.email}")

    } catch (e: StatusRuntimeException) {
        logger.log(Level.SEVERE, "Error en obtener usuario: {0}", e.status)
    }

// 2. exercise-2
    try {
        val filter = "" // Filtrar name
        val request = ListUsersRequest.newBuilder().setFilter(filter).build()
        blockingStub.listUsers(request).forEach { user -> // Usar blockingStub síncronas
            logger.info("Usuario recibido: ${user.name}, ${user.email}")
        }
        logger.info("Finalizada la recepción de usuarios")
    } catch (e: StatusRuntimeException) {
        logger.log(Level.SEVERE, "Error en ListUsers: {0}", e.status)
    }

// 3. exercise-3
    val stub = UserServiceGrpc.newStub(channel) // Usar newStub para llamadas asíncronas
    try {
        val responseObserver = object : StreamObserver<UserResponse> {
            override fun onNext(value: UserResponse) {
                logger.info("Received response: ${value.name}, ${value.email}")
            }
            override fun onError(t: Throwable) {
                logger.log(Level.SEVERE, "Error receiving response: ${t.message}")
            }
            override fun onCompleted() {
                logger.info("Finished receiving responses")
            }
        }
        val requestObserver = stub.streamUserActions(responseObserver)
        // Enviar acciones del usuario
        val actions = listOf(
            UserAction.newBuilder().setUserId("1").setAction("login").build(),
            UserAction.newBuilder().setUserId("2").setAction("logout").build(),
            UserAction.newBuilder().setUserId("3").setAction("purchase").build()
        )
        for (action in actions) {
            requestObserver.onNext(action)
        }
        requestObserver.onCompleted()
        // Esperar un poco para recibir respuestas del servidor (ajustar según sea necesario)
        Thread.sleep(1000)
    } catch (e: StatusRuntimeException) {
        logger.log(Level.SEVERE, "Error en StreamUserActions: {0}", e.status)
    }

    // Cerrar el canal
    channel.shutdown()
    try {
        channel.awaitTermination(5, TimeUnit.SECONDS)
    } catch (e: InterruptedException) {
        logger.log(Level.WARNING, "Error al cerrar el canal: {0}", e.message)
    }
}