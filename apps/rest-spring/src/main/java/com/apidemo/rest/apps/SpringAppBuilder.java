package com.apidemo.rest.apps;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

/**
 * This class provides the bootstrap for the Spring Boot application that is 
 * executed in the main method. You need to pass the class that is executed.
 */ 
@SpringBootApplication(excludeName={
    "org.springframework.boot.activemq.autoconfigure.ActiveMQAutoConfiguration",
    "org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration",
    "org.springframework.boot.security.autoconfigure.servlet.SecurityAutoConfiguration"
})
//@EnableCaching
public class SpringAppBuilder {

	public static void main(String[] args) {
		customBannerWithSpringApplication(args);
        //customBannerWithSpringAppBuilder(args);
        //defineContextHierarchy(args);
        //setLogStartupInfo(args);
        //useProfiles(args);
        //addListenersAndEvents(args);
        //addRemoveWeb(args);
	}

	/**
	 * customize Spring Boot banner using SpringApplication
	 * @param args
	 */
	private static void customBannerWithSpringApplication(String[] args) {
		// SpringApplication.run(RestApplication.class, args);
		// Or, alternative approach below
		SpringApplication app = new SpringApplication(SpringAppBuilder.class);

		// customizing banner programmatically
		// app.setBanner(new Banner() {
		// 	@Override
		// 	public void printBanner(Environment environment, Class<?>
		// 	sourceClass, PrintStream out) {
		// 	out.print("\n\n\tThis is my own banner!\n\n".toUpperCase());
		// 	}
		// 	});
        
        // customize banner programmatically in functional way
		app.setBanner((environment, sourceClass, out) ->  
			out.print("\n\n\tThis is my own banner!\n\n".toUpperCase())
		);
		// app.setBannerMode(Banner.Mode.OFF);
		app.run(args);
	}

	/**
	 * customize Spring Boot banner using SpringApplicationBuilder
	 * @param args
	 */
	private static void customBannerWithSpringAppBuilder(String[] args) {

        SpringApplicationBuilder builder = new SpringApplicationBuilder(SpringAppBuilder.class);
        // you can pass Spring boot app in constructor of SpringApplicationBuilder or use sources() method
        // builder.sources(SpringAppBuilder.class);

        SpringApplication app = builder.application();
        
        // customize banner programmatically in functional way
		app.setBanner((environment, sourceClass, out) ->  
			out.print("\n\n\tThis is my own banner!\n\n".toUpperCase())
        );
		// app.setBannerMode(Banner.Mode.OFF);
        // app.setBannerMode(Banner.Mode.CONSOLE);
        // app.setBannerMode(Banner.Mode.LOG);
		app.run(args);
	}

    /**
     * Defines ApplicationContext Hierarchy (i.e. multiple application contexts - parent-child hierarchy):
     * 1. It is possible to create separate contexts and organize them in a hierarchy in Spring Boot.
     * 2. We can have multiple application contexts that share a parent-child relationship.
     * 3. We can define hierarchy when creating a Spring Boot app. A context hierarchy can be defined in
     *      different ways in Spring Boot application.
     * 4. A context hierarchy allows multiple child contexts to share beans which reside in the parent 
     *      context. Each child context can override configuration inherited from the parent context.
     * 5. We can use contexts to prevent beans registered in one context from being accessible in another. 
     *      This facilitates the creation of loosely coupled modules.
     * 6. A context can have only one parent while a parent context can have multiple child contexts. 
     *      Also, a child context can access beans in the parent context but not vice-versa.
     * 7. If we have a web configuration, make sure that it's being declared as a child.
     * 8. Also parent-child must share the same org.springframework.core.env.Environment interface.
     *      (this represents the environment in which the current application is running; it
			 is related to profiles and properties declarations).
     * @param args
     */
    private static void defineContextHierarchy(String[] args) {

        new SpringApplicationBuilder(SpringAppBuilder.class)
			.child(ChildConfig.class)
			.run(args);

            // Other examples
            // .parent(ParentConfig.class).web(WebApplicationType.NONE)
            // .child(WebConfig.class).web(WebApplicationType.SERVLET)
            // .sibling(RestConfig.class).web(WebApplicationType.SERVLET)
            // .child(RestConfig.class).web(WebApplicationType.REACTIVE)
    }

    /**
     * We can log the information at startup. By default it is set to true. 
     * Enable/Disable log info during startup
     * @param args
     */
    private static void setLogStartupInfo(String[] args) {
        new SpringApplicationBuilder(SpringAppBuilder.class)
			.logStartupInfo(false)
			.run(args);
    }

    /**
     * We can activate/de-activate profiles
     */
    private static void useProfiles(String[] args) {
		new SpringApplicationBuilder(SpringAppBuilder.class)
			// .profiles("prod", "cloud")
			.run(args);
    }

    /**
     * We can attach listeners for some of the ApplicationEvent events
     * @param args
     */
    private static void addListenersAndEvents(String[] args) {
		// we can attach listeners for some of the ApplicationEvent events
		Logger logger = LoggerFactory.getLogger(SpringAppBuilder.class);
		new SpringApplicationBuilder(SpringAppBuilder.class)
			// .listeners(new ApplicationListener<ApplicationEvent>() {
			// 	@Override
			// 	public void onApplicationEvent(ApplicationEvent event) {
			// 		logger.info("#### > " + event.getClass().getCanonicalName());
			// 	}
			// })
            // functional way
			.listeners(event -> logger.info("#### > " + event.getClass().getCanonicalName()))
			.run(args);
    }

    /**
     * 1. We can remove any web environment auto-configuration from happening.
     * 2. Spring boot guesses which kind of app we are running based on the classpath.
     * 3. For a web app, the algorithm is very simple WebApplicationType.SERVLET or WebApplicationType.REACTIVE; 
     *      but imagine that you are using libraries that actually run without a web environment, and your app 
     *      is not a web app, but Spring Boot configures it as shown below.
     *      i.e. stand-along/non-web app WebApplicationType.NONE
     * @param args
     */
    private static void addRemoveWeb(String[] args) {
		new SpringApplicationBuilder(SpringAppBuilder.class)
			.web(WebApplicationType.NONE)
			.run(args);
    }
}

// To Test ApplicationContext hierarchy
@Configuration
class ChildConfig {
    @Bean
    String text() { return "This is ChildConfig"; }
}
