
Money Transfer Service
======================

A REST API to accept money transfers to other accounts

---

### Running

* To run using maven spring boot plugin:
```bash
mvn spring-boot:run 
```
* To run from java commandline:
```bash
java -jar ./target/*.jar
```
* To run using Docker, execute the bash script `run-docker.sh`
```bash
./run-docker.sh
```
>make sure you have docker daemon running
----

## Tech Stack

------
### Kotlin 1.7.10

### spring-boot framework 
* spring-data
* spring-web

### Docker
### Maven

--------------
## Testing
The project used Junit5 for testing and spring-boot-starter test for initializing tests
There is no POJO unit test, But there should be.

Test Coverage is over 80% for the Domain and Data Layer, 
the REST controllers Still needs to be tested

-----
## Design
I was aiming in my design to demonstrate DDD trying to separate business logic.
Mapping Entities to DTOs, I look at it in the following way:
* Domain Layer is where most of the core logic is,
and has direct access to the data layer (Repositories, JPA Entities).

* Services, they hold the 'use cases' of the application, like inquire or processing multiple 
things using different parts of the system.

* API which are the exposed endpoints to the outside world, they have the least logic as the logic
is hidden from it and only can call functions that the services provide, a good place to implement 
soft validations ( validations that don't require IO to a database) like IBAN validations, transaction issue date, status etc..

