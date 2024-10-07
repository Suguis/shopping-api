# Shopping API

*NOTE: (This was a technical test for a selection process).*

This API is designed for an imaginary e-commerce application, allowing it to manage shopping carts.

## Features

- **Cart Management**: create, delete, and manage products in carts.
- **Automatic Deletion**: carts are automatically deleted after a configurable inactivity period.

## Tech stack

This application is built using:

- **Java 17**
- **Spring Boot 3.3.3**
- **Docker** for easy deployment and running

To achieve high code quality, the application follows:

- **Test Driven Development (TDD)** practices.
- **Lombok**, to reduce boilerplate code.
- **Immutability** when possible, to minimize unwanted side effects.

## Set up

Before you begin, ensure you have one of the following installed on your machine:

- **Docker**
- **JDK 17**

Clone the repository:

```sh
git clone https://github.com/Suguis/shopping-api.git
cd shopping-api
```

## Tests

You can run the application tests via Maven Wrapper or via Docker.

### Maven Wrapper

```sh
chmod +x mvnw  # You could need to give it execution permisions
./mvnw clean test
```

### Docker

```sh
docker build --progress=plain --no-cache --target=test .
```

## Running

You can run the application via Maven Wrapper or via Docker. There are two environment variables you can customize:

- `PORT` to set a custom port to run the application. Remember to change the port accordingly if you use the commands below.
- `CART_DELETION_TIME` to specify a custom time for cart deletion.

### Maven Wrapper

```sh
./mvnw clean spring-boot:run  # Run the application

# If you want to add a custom deletion time, ie. 10 seconds:
CART_DELETION_TIME='10s' ./mvnw spring-boot:test-run
```

### Docker

```sh
docker build . --tag 'suguis:shopping-api'  # Build the image first
docker docker run -p8080:8080 'suguis:shopping-api'  # Launch a container from the image built earlier

# If you want to add a custom deletion time, ie. 10 seconds:
docker run -e CART_DELETION_TIME='10s' -p 8080:8080 'suguis:shopping-api'

```

## Interacting with the API

Once the application is running, the API will be available at http://localhost:8080.

You can interact with the API using the Swagger UI at http://localhost:8080/swagger-ui/index.html.

There are four available endpoints:

- POST `/carts`: to create a cart.
- POST `/carts/{id}/products`: to create a product in a cart.
- GET `/carts/{id}`: to get a cart.
- DELETE `/carts/{id}`: to delete a cart.
