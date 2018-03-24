## REST Service Template

#### JAX-RS (with JJWT, Log4j2 and OpenAPI) Template Application

This is a template for a lightweight JAX-RS application integrated with JJWT, Log4j2 and OpenAPI. The sample code does Celsius and Fahrenheit conversions.

#### Purpose

Providing a lightweight template for creating JAX-RS REST applications. At a time of creation, versions of the libraries used are mostly as new as possible.

#### Running the application locally

Application can be run with:

    $mvn clean package jetty:run-war

Embedded Jetty server runs on 8080

#### Webpages

/RS/rest/token?username={user}&password={pass}
	Used for generating token, can be accessed from browser

/RS/
	SwaggerUI page of the application

/RS/rest/openapi.json
	Json file internally used by OpenAPI

#### Author

Developed by [Toma Krstić](https://github.com/TomasCruz/)

#### Licensing

This project is open-source via the [Apache 2.0 License](http://www.apache.org/licenses/LICENSE-2.0).

This code uses JJWT which is licensed under the Apache 2.0 License, and can be obtained on [JJWT](https://github.com/jwtk/jjwt)
This code uses Log4j2 which is licensed under the Apache 2.0 License, and can be obtained on [Log4j2](https://logging.apache.org/log4j/2.0/)
This code uses OpenAPI which is licensed under the Apache 2.0 License, and can be obtained on [OpenAPI-Specification](https://github.com/OAI/OpenAPI-Specification)
This code uses Jetty which is licensed under the Apache 2.0 License (also EPL1), and can be obtained on [Jetty](https://github.com/eclipse/jetty.project)

