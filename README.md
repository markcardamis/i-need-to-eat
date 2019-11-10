# I need to eat
An API to retrieve a list of recipes based on the ingredients in your fridge.

## Getting Started
* Clone the repository using Git:  
``` git clone https://github.com/markcardamis/i-need-to-eat.git```
* Change into the directory using ```cd i-need-to-eat```
* Run the program using ```./gradlew bootRun```
* Make a GET request to ```http://localhost:8080/api/v1/lunch```

## Project Structure
### Build Gradle
The dependencies for the project are installed using Gradle with the build.gradle file below. 
A few of the Spring starter packages were implemented along with Jackson for Json annotations and Lombok for boilerplate code. An in memory database H2 was used in order to benefit from some of the JPA queries and as a provision to easily add an external database in the future.

```
plugins {
	id 'org.springframework.boot' version '2.2.0.RELEASE'
	id 'io.spring.dependency-management' version '1.0.8.RELEASE'
	id 'java'
}

group = 'com.majoapps'
version = '0.0.1-SNAPSHOT'
sourceCompatibility = '1.8'

configurations {
	compileOnly {
		extendsFrom annotationProcessor
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
	implementation 'org.springframework.boot:spring-boot-starter-data-rest'
	implementation 'org.springframework.boot:spring-boot-starter-web'
	implementation 'com.fasterxml.jackson.core:jackson-core:2.10.0'
	implementation 'com.fasterxml.jackson.core:jackson-databind:2.10.0'
	compileOnly 'org.projectlombok:lombok'
	runtimeOnly 'com.h2database:h2'
	annotationProcessor 'org.projectlombok:lombok'
	testImplementation('org.springframework.boot:spring-boot-starter-test') {
		exclude group: 'org.junit.vintage', module: 'junit-vintage-engine'
	}
}

test {
	useJUnitPlatform()
}
```


### Directory Structure
Using Domain Driven Design the following folder structure is a skeleton of the project implementation.
```
└── src
    └── main
        └── java
            └── com.majoapps.lunchapp
                └── business
                    └── domain
                    └── service
                └── data
                    └── entity
                    └── repository
                └── exception
                └── web
```
