# Saleor-Akekeno PIM Integration App
This software was made as a part of my thesis.  
It is meant to be hosted as an AWS Lambda function but has never been actually deployed.

It exposes a REST API and its structure has been designed and specified as an external Swagger OpenAPI specification.  
The specification can be found in [Saleor-Akeneo Integration App Spec](https://github.com/villejuutila/saleor-akeneo-integration-app-spec).  
From the specification is then generated the structure of the of this Apps with Gradles OpenAPI Generator plugin.

It provides endpoints for
- registering the app with Saleor
- importing product information from Akeneo PIM to Saleor.

Same logic could be applied for integrating Saleor with basically any PIM that exposes a REST API.

It also contains a very simple React application that demonstrates the extending capabilities of Saleor Dashboard.  
Actual product information is importing is also triggered via the React application.  
React application can be found in [Saleor-Akeneo Integration UI](https://github.com/villejuutila/saleor-akeneo-integration-ui).

## Quarkus instructions for development and building
This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./gradlew quarkusDev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Packaging and running the application

The application can be packaged using:
```shell script
./gradlew build
```
It produces the `quarkus-run.jar` file in the `build/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `build/quarkus-app/lib/` directory.

The application is now runnable using `java -jar build/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:
```shell script
./gradlew build -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar build/*-runner.jar`.

## Creating a native executable

You can create a native executable using: 
```shell script
./gradlew build -Dquarkus.package.type=native
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./gradlew build -Dquarkus.package.type=native -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./build/saleor-akeneo-integration-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/gradle-tooling.

## Related Guides

- AWS Lambda ([guide](https://quarkus.io/guides/amazon-lambda)): Write AWS Lambda functions
- Kotlin ([guide](https://quarkus.io/guides/kotlin)): Write your services in Kotlin

## Provided Code

### RESTEasy Reactive

Easily start your Reactive RESTful Web Services

[Related guide section...](https://quarkus.io/guides/getting-started-reactive#reactive-jax-rs-resources)
