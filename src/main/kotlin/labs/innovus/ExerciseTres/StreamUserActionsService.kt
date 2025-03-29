package labs.innovus.ExerciseTres

import io.grpc.stub.StreamObserver
import user.UserServiceOuterClass.UserAction
import user.UserServiceOuterClass.UserResponse

class StreamUserActionsService(
    private val users: MutableList<UserResponse>,
    private val responseObserver: StreamObserver<UserResponse>
) {
    fun getObserver(): StreamObserver<UserAction> {
        return object : StreamObserver<UserAction> {
            override fun onNext(action: UserAction) {
                println("Acción recibida del usuario ${action.userId}: ${action.action}")
                val response = UserResponse.newBuilder()
                    .setUserId(action.userId)
                    .setName("User ${action.userId}")
                    .setEmail("user${action.userId}@example.com")
                    .build()
                users.add(response)
                responseObserver.onNext(response)
            }

            override fun onError(t: Throwable) {
                println("Error en la transmisión: ${t.message}")
            }

            override fun onCompleted() {
                println("Transmisión completada")
                responseObserver.onCompleted()
            }
        }
    }
}