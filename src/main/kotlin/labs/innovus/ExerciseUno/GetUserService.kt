package labs.innovus.ExerciseUno

import io.grpc.Status
import io.grpc.stub.StreamObserver
import user.UserServiceOuterClass.GetUserRequest
import user.UserServiceOuterClass.UserResponse

class GetUserService(private val users: List<UserResponse>) {
    fun execute(request: GetUserRequest, responseObserver: StreamObserver<UserResponse>) {
        val user = users.find { it.userId == request.userId }
        if (user != null) {
            responseObserver.onNext(user)
            responseObserver.onCompleted()
        } else {
            val status = Status.NOT_FOUND.withDescription("User con ID ${request.userId} no encontrado")
            responseObserver.onError(status.asRuntimeException())
        }
    }
}