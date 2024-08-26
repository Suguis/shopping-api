# shopping-api

This is an API for an imaginary e-commerce app. It manages carts, which you can create, delete, and add products in them. Carts are automatically deleted after a configurable inactivity period.

It was built using **Java 17** and **Spring Boot 3.3.3**. It has Docker support, allowing running and deploying the application easily.

To achieve the best code quality, the application was built using:

- **Test Driven Development** (TDD) practices.
- **Lombok**, to reduce boilerplate code and validate entities.
- **Immutability** when possible, to reduce unwanted side effects.

## Set up

First, you need to have either **Docker or a JDK 17** installed on your computer. Ensure you **clone the repository and move to it**:

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

You can run the application via Maven Wrapper or via Docker. If you need to change the port in which the application is running, set the environment variable `PORT` accordingly.

You can specify a custom time for cart deletion by setting the environment variable `CART_DELETION_TIME`.

### Maven Wrapper

```sh
chmod +x mvnw  # You could need to give it execution permisions
./mvnw clean package  # Build the package first
./mvnw spring-boot:run  # Run the application

# If you want to add a custom deletion time, ie. 10 seconds:
CART_DELETION_TIME=10s ./mvnw spring-boot:test-run
```

### Docker

```sh
docker build . --tag "suguis:shopping-api"  # Build the image first
docker docker run -p8080:8080 "suguis:shopping-api"  # Launch a container from the image built earlier

# If you want to add a custom deletion time, ie. 10 seconds:
docker run -e CART_DELETION_TIME='10s' -p 8080:8080 suguis:shopping-api

```

## Interacting with the API

When running, the API will be available at http://localhost:8080.

You can interact with the API using the Swagger UI, at http://localhost:8080/swagger-ui/index.html.

There are four available endpoints:

- POST `/carts`: to create a cart
- POST `/carts/{id}/products`: to create a product in a cart
- GET `/carts/{id}`: to get a cart
- DELETE `/carts/{id}`: to delete a cart
