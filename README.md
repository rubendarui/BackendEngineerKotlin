# gRPC Kotlin Exercises


Este proyecto contiene 3   implementación con gRPC en Kotlin.


## Estructura del Proyecto


El proyecto está organizado en los siguientes directorios:

**Estructura del Proyecto**






                                    
                                    BackendEngineerKotlin/
                                    └── grpc-kotlin-server-client/
                                    ├── pom.xml // Archivo de configuración Maven
                                    ├── src/
                                    │ ├── main/
                                    │ │ ├── kotlin/
                                    │ │ │ └── labs/innovus/
                                    │ │ │ ├── exerciseUno/
                                    │ │ │ │ └── GetUserService.kt // Servicio GetUser
                                    │ │ │ ├── exerciseDos/
                                    │ │ │ │ └── ListUsersService.kt // Servicio ListUsers
                                    │ │ │ ├── exerciseTres/
                                    │ │ │ │ └── StreamUserActionsService.kt // Servicio StreamUserActions
                                    │ │ │ ├── UserServiceClient.kt // Cliente gRPC
                                    │ │ │ └── UserServiceServer.kt // Servidor gRPC
                                    │ │ └── proto/
                                    │ │ └── user_service.proto // Definición de servicios y mensajes Protobuf
                                    │ └── test/
                                    │ └── kotlin/
                                    │ └── labs/innovus/
                                    │ └── UserServiceImplTest.kt // Pruebas
                                    └── ...









* `exercise-1`: Implementación de GetUser (Unary RPC).
* `exercise-2`: Implementación de ListUsers (Server Streaming RPC).
* `exercise-3`: Implementación de StreamUserActions (Bidirectional Streaming RPC).
* `src/main/proto`:  Archivos `.proto` que definen los servicios y mensajes.
* `pom.xml`: Archivo de configuración de Maven.


## Build & Run Instructions (General)

1.  **Limpiar y instalar dependencia del proyecto:**


    ```bash
     mvn clean install
    ```
1.  **Compilar el proyecto:**


    ```bash
    mvn compile
    ```


### Ejercicio 1: GetUser (Unary RPC)
### Ejercicio 2: ListUsers (Server Streaming RPC)
### Ejercicio 3: StreamUserActions (Bidirectional Streaming RPC)


  
* **Ejecutar el servidor:**

* **(Opcional) Ejecutar el cliente:**




## Notas Adicionales


* Asegúrate de tener Java >9 (en este proyecto se uso 21 y en el pom una de las dependencias de anotaciones esta declarda ya que al ser un java mayor no logro reconocer nativamente por estar deprecada y se la agrego para poder ejecutar el proyecto) y Maven instalados.
* El servidor debe estar en ejecución antes de ejecutar el cliente.
