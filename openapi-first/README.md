# OpenAPI-First Spring Boot Application

This is a Spring Boot application built with an OpenAPI-first approach.
The API specification is defined in `src/main/resources/api.yaml` and the code is automatically generated using OpenAPI Generator.


## Usage

```bash
mvn clean spring-boot:run
```

### Generating API Code

Since this is an OpenAPI-first project, you need to generate the API code:

```bash
mvn clean compile
```

## IntelliJ IDEA Development Setup

### Marking Target Directory as Sources Root

The OpenAPI Generator creates generated code in the `target/generated-sources/openapi` directory.
To make IntelliJ recognize this as source code:

1. **After running Maven build**, right-click on the `target/generated-sources/openapi` directory
2. Select **Mark Directory as** → **Sources Root**

## Project Structure

```
src/
├── main/
│   ├── java/com/gautier/apifirst/
│   │   ├── DemoApplication.java      # Main application class
│   │   └── controller/              # Custom controllers
│   └── resources/
│       ├── application.properties   # Spring configuration
│       └── api.yaml                  # OpenAPI specification
target/
└── generated-sources/openapi/        # Generated API code (after build)
    ├── com/gautier/apifirst/api/     # Generated API interfaces
    └── com/gautier/apifirst/model/   # Generated DTO models
```

## API Documentation

Once the application is running, you can access:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs
