package labs.innovus.ExerciseDos

import io.grpc.stub.StreamObserver
import user.UserServiceOuterClass.ListUsersRequest
import user.UserServiceOuterClass.UserResponse

class ListUsersService(private val users: List<UserResponse>) {
    fun execute(request: ListUsersRequest, responseObserver: StreamObserver<UserResponse>) {
        if (request.filter.isNotEmpty()) {
            val user = users.find { it.name.contains(request.filter, ignoreCase = true) }
            if (user != null) {
                responseObserver.onNext(user)
            } else {
                responseObserver.onError(Exception("No se encontró ningún usuario con el filtro dado"))
                return
            }
        } else {
            for (user in users) {
                responseObserver.onNext(user)
            }
        }
        responseObserver.onCompleted()
    }
}