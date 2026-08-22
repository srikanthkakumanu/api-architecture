# Spring Framework
RESTful API exploration of Spring Core, Spring Boot, JDBC, and REST API design.

> [!NOTE]
> All REST API design guidelines, URI naming conventions, HTTP status codes, and RFC 7807 error patterns are driven from the master [API Design Guide](../docs/api-design.md).

<div style="text-align: justify">

## 1. **Brief History about Java EE**

**Jarkarta EE** (formerly known as **Java EE**) is an Eclipse Foundation Project and **TomEE** (Apache foundation) shortly called as "Tommy" is an Jakarta EE certified application server. Oracle donated/contributed Java EE to Eclipse foundation (Java EE/JEE became open-source) and it is now rebranded as Jakarta EE. The major difference between Jakarta EE and Java EE/JEE is **governance model**. Eclipse Enterprise for Java (**EE4J**) is an top-level project for Jakarta EE.  

## 2. **Spring Framework**

The Spring framework was created in 2003 by Rod Johnson. Spring is a complimentary technology to Java EE or Jakarta EE but not a replacement. The Spring framework supports the *devependency injection* and *common annotation* make development easier and it integrates several technologies such as Servlet API, WebSocket API, concurrency utilities, JSON binding API, bean validation, JPA, JMS and JTA/JCA.

Spring along with Java, it also offers excellent support of **Groovy** and **Kotlin** as alternative languages on the JVM.

**Principles of Spring Framework**:

- *Provide choice at every level*: Spring lets you defer design decisions at late as possible. e.g. we can switch persistance providers through configuration without changing our code.
- *Accomodate diverse perspectives*: Spring embraces flexibility and is not opinionated about how things should be done.
- *Maintains strong backward compatibility*: Spring supports carefully chosen range of JDK versions and third party libraries to facilitate maintenance of applications and libraries that depend on Spring.
- *Care about API design*: The Spring team puts a lot of thought and time into making APIs that are intuitive and that hold up across many versions and many years.
- *Set high standards for code quality*: The Spring Framework puts a strong emphasis on meaningful, current, and accurate Javadocs and can claim clean code structure with no circular dependencies between packages.

The Spring framework can be considered as a collection of sub-frameworks also called as layers such as Spring Core, Spring AOP, Spring Web MVC, Spring DAO, Spring ORM, Spring Context and Spring WebFlow.

### 2.1 **Spring Core and IoC container**

Spring Core module is the core component of Spring framework and provides **Inversion of Control (IoC) container**.

IoC container does the following:

- Creates the objects, configures, assembles their dependencies and manages their entire life cycle.
- It uses **Dependency Injection (DI)** to manage the components that makeup the application. It gets the information about the objects from a configuration file(XML) or Java Code or Java Annotations and Java POJO class. These objects are called **Beans**. Since the Controlling of Java objects and their lifecycle is not done by the developers, hence the name **Inversion Of Control (IoC)**.

#### **Inversion of Control (IoC) and Dependency Injection (DI)**

**IoC is also known as dependency injection (DI)**. It is a process whereby objects define their dependencies (that is, the other objects they work with) only through constructor arguments, arguments to a factory method, or properties that are set on the object instance after it is constructed or returned from a factory method. The container then injects those dependencies when it creates the bean. This process is fundamentally the inverse (hence the name, Inversion of Control) of the bean itself controlling the instantiation or location of its dependencies by using direct construction of classes or a mechanism such as the ***Service Locator*** pattern.

There are **two types of Spring IoC Containers**:

**BeanFactory Container**: This is the simplest container providing the basic support for **Dependency Injection (DI)** and it  is defined by `org.springframework.beans.factory.BeanFactory` interface. The BeanFactory and related interfaces, such as *BeanFactoryAware*, *InitializingBean*, *DisposableBean*, are still present in Spring for the purpose of backward compatibility with a large number of third-party frameworks that integrate with Spring.

**ApplicationContext Container**: This container adds more enterprise specific functionality such as the ability to resolve textual messages from a properties file and the ability to publish application events to interested event listeners. This container is defined by the `org.springframework.context.ApplicationContext` interface. The *ApplicationContext* container includes all functionality of the *BeanFactory* container, so it is generally recommended over *BeanFactory*. *BeanFactory* can still be used for lightweight applications like mobile devices or applet-based applications where data volume and speed is significant.

### 2.2 **Spring Context**

Spring works with Plain Old Java Objects (POJO) by making it easy to exend. We need to add a configuration to wire up all dependencies and inject what's needed to create Spring ***beans*** to execute an application.

Spring Context that creates all the Spring beans using a configuration (XML or annotation or Java Config) that references all our classes which makes our application run.

<img src="https://github.com/srikanthkakumanu/apis/blob/main/rest-spring/spring-context.png" width=500 height=300></img>

### 2.3 **Spring WebFlux**

The **Spring WebFlux** module was introduced in version 5 with a fully non-blocking stack that supports Reactive Streams back-pressure and runs on servers such as Netty, Undertow and Servlet 3.1+ containers. It is aimed for a non-blocking stack that handles concurrency with a small number of threads that can scale with less hardware.

The WebFlux module depends on another Spring project known as **Project Reactor** (https://projectreactor.io).

**Reactor** is the reactive library of choice for Spring WebFlux. Reactor is a fourth-generation reactive library based on Reactive Streams specification for building *non-blocking backpressure-ready applications on JVM*.

Reactor offers two reactive and composable APIs called **Mono and Flux**. The *Mono* and *Flux* types are used to work on data sequences of 0..1 and 0..N through a rich set of operators aligned with the *ReactiveX* vocabulary of operators. Reactor has a strong focus on server-side Java. It is developed in close collaboration with Spring.

## 3. **Spring Boot Overview and Internals**

Spring Boot makes it easy to create stand-alone, production-grade Spring based Applications that you can "just run".

With Spring Boot:

- Create stand-alone Spring applications.

- Embed **Tomcat** (by default), **Jetty** or **Undertow** directly (no need to deploy WAR files) or **Netty** (If we are using the new **web-reactive modules**).

- Provide opinionated 'starter' dependencies to simplify your build configuration.

- Automatically configure Spring and 3rd party libraries whenever possible.

- Provide production-ready features such as metrics, health checks, and externalized configuration.

- Absolutely no code generation and no requirement for XML configuration (favors *convention over configuration*).

### 3.1 **Spring Boot features and Internals**

Spring Boot is highly customizable, from the auto-configuration that sets up our application (based on the *classpath*) to customizing how it starts, what to show, and what to enable or disable based on its own properties.

Some of the features below given:

- Auto-configuration
- SpringApplication class
- SpringApplicationBuilder class
  - ApplicationContext hierarchy
  - Logging at startup
  - Profiles activation/de-activation
  - Events and Listeners
  - Define Application Type(Servlet/Reactive/Non-Web)
- ApplicationRunner and CommandLineRunner
- Application configuration
  - Custom properties
  - Profiles

There are two important Spring Boot components:

**`@SpringBootApplication` annotation**: This annotation is combination of **`@ComponentScan`**, **`@Configuration`** and **`@EnableAutoConfiguration`** annotations.

**SpringApplication**: This class provides the bootstrap for the Spring Boot application that is executed in the main() method. We need to pass the class that is executed.

e.g.

```java
@SpringBootApplication
public class RestApplication {

	public static void main(String[] args) {
	// SpringApplication.run(RestApplication.class, args);
	// Or, alternative approach below
      SpringApplication app = 
              new SpringApplication(RestApplication.class);
		app.run(args);
	}
}    
```

### 3.1.1 **Auto-Configuration**

**Auto-configuration** is one of the important features in Spring Boot because it configures our Spring Boot application according to our classpath, annotations, any other configuration declarations such as JavaConfig classes or XML.

Spring Boot won't generate any source code but it adds some on the fly.

- Spring Boot starts by importing missing dependencies, such as the **`org.springframework.web.bind.annotation.RestController`** annotation, among other imports.
- Next, it identifies that you need a **`spring-boot-starter-web`** because you marked your class and method with the `@RestController` and the `@GetMapping` annotations.
- Next, it adds the necessary annotation that triggers auto-configuration, the **`@EnableAutoConfiguration`** annotation.
- Then it adds the main method that is the entry point for the application.

The **`@SpringBootApplication`** annotation, which is one of the main components of a Spring Boot app. This annotation is equivalent to declaring the **`@Configuration`**, **`@ComponentScan`**, and **`@EnableAutoConfiguration`** annotations (**`@SpringBootApplication`** inherits all three).

#### **Disable or exclude specific Auto-configuration**

You can disable a specific auto-configuration by adding the **`exclude`** parameter by using either the **`@EnableAutoConfiguration`** or the **`@SpringBootApplication`** annotations in your class.

This is a **very useful technique** when we want Spring Boot to skip certain and unnecessary auto-configurations.

e.g.

```java
@SpringBootApplication(exclude={ActiveMQAutoConfiguration.class,DataSourceAutoConfiguration.class})
```

#### **@EnableAutoConfiguration and @Enable(Technology) annotations**

The Spring framework and some of its modules such as SpringData, SpringAMQP and SpringIntegration provide @Enable<Technology> annotations e.g. ***`@EnableTransactionManagement`***, ***`@EnableRabbit`*** and ***`@EnableIntegration`*** etc.

We can use these annotations to follow the ***convention over configuration*** pattern. They help making our apps easy to develop without worrying about its configuration.

As discussed above, ***@EnableAutoConfiguration*** annotation to do the auto-configuration.

*spring-boot-autoconfigure-<version>.jar/META-INF/**spring.factories*** contains all the auto-configuration classes that are used to setup any configuration that our application needs for running.

Useful Link: https://devwithus.com/customize-spring-boot-auto-configuration/ </br>

### 3.1.2 **SpringApplication class**

We can have more advanced configuration using the **`SpringApplication`** such as custom banner, banner mode etc. It allows us to configure the way our app behaves and we have control over the main **`ApplicationContext` (Spring IoC container)** where all our beans are used.

### 3.1.3 **SpringApplicationBuilder class**

The **`SpringApplicationBuilder`** class provides a fluent API and is a builder for the `SpringApplication` and the `ApplicationContext` instances.

Using **`SpringApplicationBuilder`** we can do:

- Define and run **`SpringApplication`** using **`SpringApplicationBuilder.sources()`** method. We can also get current SpringApplication instance using `application()`.
- **ApplicationContext Hierarchy**: It's possible to create separate contexts and organize them in a parent-child hierarchy in Spring Boot.
  - We can have multiple application contexts that share a parent-child relationship.
  - We can define a hierarchy when creating a Spring application using `.child()` method.
  - A context hierarchy can be defined in different ways in a Spring Boot application.
  - A context hierarchy allows multiple child contexts to share beans which reside in the parent context.
  - Each child context can override configuration inherited from the parent context.
  - A context can have only one parent while a parent context can have multiple child contexts. Also, a child context can access beans in the parent context but not vice-versa.
  - If we have a web configuration, make sure that it's being declared as a child.
  - Also parent and child must share the same `org.springframework.core.env.Environment` interface (this represents the environment in which the current application is running; it is related to profiles and properties declarations)
- **Logging at startup**: We can log the information at startup. By default it is set to true e.g. `.logStartupInfo()`.
- **Profiles activation/de-activation**: We can activate/de-activate profiles e.g. `.profiles("prod", "cloud")`.
- **Events and Listeners**: we can also attach listeners for some of the **`ApplicationEvent`** events. e.g. `.listeners(event -> logger.info("#### > " + event.getClass().getCanonicalName()))`. We can also have these additional events.
  - **`ApplicationEvent`**: generic application event.
  - **`ApplicationStartedEvent`**: This is sent at the start.
  - **`ApplicationEnvironmentPreparedEvent`**: This is sent when the environment is known.
  - **`ApplicationPreparedEvent`**: This is sent after the bean definitions.
  - **`ApplicationReadyEvent`**: This is sent when the application is ready.
  - **`ApplicationFailedEvent`**: This is sent incase of exception during the startup.
- **Define Application Type(Servlet/Reactive/Non-Web)**: We can remove any web environment auto-configuration from happening. e.g. `.web(WebApplicationType.NONE)` or `.web(WebApplicationType.SERVLET)` or `.web(WebApplicationType.REACTIVE)`.

### 3.1.4 **ApplicationRunner and CommandLineRunner**

Spring Boot provides two functional interfaces i.e. **`ApplationRunner`** and **`CommandLineRunner`** to run specific pieces of code when an application is fully started.

If we have need to run a Scheduled batch Job, set some system environment properties, or need to perform some DB operation just before the Spring Boot run() method is finished, so in this kind of scenario `ApplicationRunner` Interface comes handy. It allows you to do such operations before the Spring Boot’s run() method finishes its execution.

ApplicationRunner can be used in the following scenarios: –

Need to add some additional logger information </br>
Schedule a batch job </br>
Database operation i.e. cleanup script, status update </br>

- These runners are used to run the logic on application startup i.e. just after **ApplicationContext** is created and before spring boot application starts up.
- Both these interfaces expose a `run()` method.
- We can create multiple `ApplicationRunner` or `CommandLineRunner` beans.
- **Ordering**: We can control the ordering by implementing either `org.springframework.core.Ordered` interface or using `org.springframework.core.annotation.Order` annotation.
- **Difference**: `ApplicationRunner.run()` method takes **`ApplicationArguments`** instance as argument and `CommandLineRunner.run()` takes string varargs (string array) as an argument.

### 3.1.5 **Application Configuration**

To configure application specific properties, **Spring** offers the following options:

- Use XML and the `xml <context:property-placeholder/>` tag.
- Use `@PropertySource` annotation to declare our properties pointing to a file that has declared them.

**Spring Boot** offers different options for saving application configuration:

- Application.properties that should be located in root classpath of the application.
- Application.yml that should be located in root classpath of the application.
- Use environment variables (This is becoming the default practice for cloud scenarios).
- Use command-line arguments.

Note: If none are specified it already has those properties values as defaults. Refer to https://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html

**Spring and Spring Boot** has access to the properties values:

- By using @Value annotation with the name of the property.
- They can be accessed from the org.springframework.core.env.Environment interface ( extends
from the org.springframework.core.env.PropertyResolver interface).

Note: all below approaches require `./gradlew build` to execute first.

#### **Inject via application.properties or application.yml**

Spring Boot injects the property (server.ip) value (192.158.12.1) from the application.properties file.

e.g.

```java
application.properties
----------------------
server.ip = 192.158.12.1

Java Code
---------
public class MyService {
  @Value("${server.ip}")
  private String serverIp;
    //...
  }
}
```

To pickup configruation parameter from application.properties/YAML file: `./gradlew bootRun`

#### **Inject via command-line arguments**

e.g:

To pass a configuration parameter via command-line: </br>

`java -jar build/libs/rest-spring-0.0.1.jar --server.ip=192.158.12.1`

#### **Inject via Environment variables**

 We can use a specialized environment variable named **SPRING_APPLICATION_JSON** to expose same properties and its values.

 e.g.

 To use SPRING_APPLICATION_JSON for configuration parameters: `java -jar build/libs/rest-spring-0.0.1.jar --spring.application.json='{"server":{"ip":"192.158.12.3"}}'`

 To use via Environment variable, we can use either of the approaches: </br>

 A) SPRING_APPLICATION_JSON='{"server":{"ip":"192.158.12.4"}}' java -jar build/libs/rest-spring-0.0.1.jar
 B) SERVER_IP=192.158.12.5 ./gradlew bootRun
 C) SERVER_IP=192.158.12.6 java -jar build/libs/rest-spring-0.0.1.jar

#### **Configuration Properties Precedence**

Here is the order for overriding the application properties:

- Command Line arguments
- SPRING_APPLICATION_JSON
- JNDI (java:comp/env)
- System.getProperties()
- OS Environment variables
- RandomValuePropertySource(random.*)
- Profile specific (application-{profile}.jar) outside of the package jar
- Profile specific (application-{profile}.jar) inside of the package jar
- Application properties (application.properties) outside of the package jar
- Application properties (application.properties) inside of the package jar
- Application properties (application.properties) inside the package jar
- @PropertySource
- SpringApplication.setDefaultProperties

### 3.1.6 **Spring Boot Custom Properties and Profiles**

Spring Boot allows developers to create custom properties and beans based on profiles. It is useful when there is a need for separate environments without having to recompile or package a Spring app.

#### **Profiles**

- Specify the active profile with `@ActiveProfiles` annotation.
- Getting the current Environment and use the `setActiveProfiles()` method.

Or

- Use the environment variable *SPRING_PROFILES_ACTIVE* or the *spring.profiles.active* property.

We can use the properties file using this format: **application-{profile}.properties**.

Spring Boot has an *order* for finding the `application.properties` or YAML file.

1. It looks at the /config subdirectory located in the current directory.
2. The current directory
3. The classpath /config package
4. The classpath root

e.g.

- Create *application-qa.properties* and *application-prod.properties*
- Set *spring.profiles.active=qa* or *spring.profiles.active=prod* in *application.properties*
- Execute `./gradlew bootRun`

#### **Custom Properties**

Spring Boot allows you to write and use your own custom properties prefix for your properties by using `@Component` and `@ConfigurationProperties` annotation in a Java class which has setters and getters as its properties.

## 4. **Web Applications with Spring Boot**

The spring framework offers **`spring-web`**, **`spring-webmvc`**, **`spring-webflux`** and **`spring-websocket`** modules to support web technology.

The **`spring-web`** module has basic web integration features such as:

- Multi-part file upload functionality.
- Initialization of of Spring container (by using Servlet listeners).
- A web oriented application context.

### 4.1 **Spring MVC**

The **`spring-mvc`** module (a.k.a the web server) has features such as:

- Spring MVC (Model-View-Controller)
- RESTful services implementation for web applications

#### **DispatcherServlet**

The **Spring MVC** module is designed around the **`org.springframework.web.servlet.DispatcherServlet`** class. This servlet is very flexible and has a very robust functionality. With the **`DispatcherServlet`**, you have several out-of-the-box resolutions strategies, including view resolvers, locale resolvers, theme resolvers, and exception handlers. In other words, the **`DispatcherServlet`** take a HTTP request and redirect it to the right handler (the class marked with the `@Controller` or `@RestController` and the methods that use the `@RequestMapping` annotations) and the right view (your JSPs).

#### **Controller Annotations**

Spring MVC offers **`@Controller`** and **`@RestController`** annotations to express request mappings, request input, exception handling and more.

**`@Controller`**:

- `@Controller` is used to mark a class as controller class and it indicates that class as web controller.
- It is simply a specialization of the `@Component` (allows us to auto-detect implmentation classes through the classpath scanning).
- We typically use `@Controller` in combination with a `@RequestMapping` annotation for request handling methods.
- It need to be declared with **`@ResponseBody`** explicitly to deal with responses when needed.
- It creates a Map of Model objects and finds a view.

e.g.

```java
@Controller
@RequestMapping("todo")
public class ToDoController {

    @GetMapping("/{id}", produces = "application/json")
    public @ResponseBody ToDo getToDo(@PathVariable int id) {
        return findToDoById(id);
    }

    private ToDo findToDoById(int id) {
        // ...
    }
}
```

**Note**: We annotated the request handling method with `@ResponseBody`. This annotation enables automatic serialization of the return object into the HttpResponse.

**`@RestController`**:

- Spring 4.0 introduced this annotation to simplify the creation of RESTful web services. 
- It combines **`@Controller`** *+* **`@ResponseBody`**, meaning we do not need to use `@ResponseBody` explicitly on every handler method once we annotate the class with `@RestController`. 
- It simply returns an object (i.e. object data directly written into HTTP response as JSON or XML).

e.g.

```java
@RestController
@RequestMapping("todo")
public class ToDoRestController {
    
    @GetMapping("/{id}", produces = "application/json")
    public ToDo getToDo(@PathVariable int id) {
        return findToDoById(id);
    }

    private ToDo findToDoById(int id) {
        // ...
    }
}
```

**Note**: The controller is annotated with the `@RestController` annotation. Therefore `@ResponseBody` isn't required. Every request handling method of the controller class automatically serializes return objects into `HttpResponse`.

#### **Other Annotations**

**@Autowired**:

@Autowired meaning it injects an implementation (see below: injects CommonRepository<ToDo> implementation object). However, it can be omitted. Spring automatically injects any declared dependency since 4.3.

e.g.

```java
@Autowired  
public ToDoController(CommonRepository<ToDo> repository) { 
  this.repository = repository; 
}
```

**@GetMapping**:

It is a shortcut version of @RequestMapping and is equivalent to
`@RequestMapping(value="/todo", method={RequestMethod.GET})`.

**@PathVariable**:

It helps in declaring an endpoint that contains a URL expression.
e.g. */api/todo/{id}*
*Note*: ID must match the name of the method parameter.

**@RequestBody**:

It sends a request with a body. When a form or a particular content is submitted, this class receives a JSON format of model/POJO object (e.g. todo as shown below). Then **`HttpMessagerConverter`** de-serializes the JSON into relevant model/POJO instance. Spring Boot automatically performs this operation with the help of auto-configuration because it registers the `MappingJackson2HttpMessageConverter` by default.

e.g.

```java
public ResponseEntity<?> createToDo(
                            @Valid @RequestBody ToDo todo, 
                            Errors errors) { ... }
```

**@Valid**:

This annotation validates incoming data and is used as a method’s parameters (see above code example).

To trigger a validator, it is necessary to annotate the data we want to validate with `@NotNull`, `@NotBlank` and other annotations. If the validator finds errors, they are collected in the `Errors` class.  Then we can inspect and add the necessary logic to send back an error response.

**@ResponseEntity<E>**:

This class returns a full response, including HTTP headers, and the body is converted through `HttpMessageConverters` and written to the HTTP response. It supports a *fluent API* therefore it is easy to create the response.

**@ResponseStatus**:

 This annotation is normally used when a method has a *void* return type (or *null* return value). This annotation sends back the HTTP status code specified in the response.

**@ExceptionHandler**:

 The Spring MVC automatically declares built-in resolvers for exceptions and adds the support to this annotation.

 We can use this annotation inside a controller class or can also be used within a `@ControllerAdvice` interceptor and any exception is redirected to the `handleException()' method.

## **Important Commands**

**CURL commands:**

GET request: `curl -s http://localhost:8080/api/todo | jq` (jq is a tool for JSON format viewing - sudo apt install jq)
POST request: `curl -i -X POST -H "Content-Type: application/json" -d '{"description": "Practice Hard Dont stop"}' http://localhost:8080/api/todo`

PUT request: `curl -i -H PUT -H "Content-Type: application/json" -d '{"description": "Take the dog and the cat for a walk", "id": "178424c8-675d-479d-9130-137c57672faf"}' http://localhost:8080/api/todo`

PATCH request: `curl -i -X PATCH http://localhost:8080/api/todo/178424c8-675d-479d-9130-137c57672faf`

DELETE request: `curl -i -X DELETE http://localhost:8080/api/todo/f0ee14cd-b43a-4676-bd4a-6c4cd396a069`

To test validations:
`curl -i -X POST -H "Content-Type: application/json" -d '{"description":""}' http://localhost:8080/api/todo`
`curl -i -X POST -H "Content-Type: application/json" http://localhost:8080/api/todo`

**Docker commands:**
To start/stop application via docker-compose: `docker-compose up` and `docker-compose down`

To Know IP address of a docker container: `docker inspect -f '{{range.NetworkSettings.Networks}}{{.IPAddress}}{{end}}' <container id or name>`

To login to Docker container: `docker exec -it <container id or name> /bin/bash`


</div>