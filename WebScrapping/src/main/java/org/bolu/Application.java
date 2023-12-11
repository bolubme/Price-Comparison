package org.bolu;


import org.bolu.config.AppConfiguration;
import org.bolu.scraper.ScraperManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;

public class Application {
    /**
     * Main application class responsible for initializing and running the scraper manager.
     */
    public static void main(String[] args){
        // Initialize the Spring application context using the configuration class
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfiguration.class);

        // Retrieve the ScraperManager bean from the application context
        ScraperManager scraperManager = (ScraperManager) context.getBean("scraperManager");
        
        // Start the scraper manager to initiate scraping
        scraperManager.startScrapers();
    }
}
