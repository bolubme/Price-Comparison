package org.bolu.config;


import org.bolu.model.Laptop;
import org.bolu.model.LaptopDao;
import org.bolu.scraper.*;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.hibernate.cfg.Environment;

import java.util.ArrayList;


/**
 * Configuration class responsible for setting up the application components.
 * This class initializes and configures beans required for the application, including scrapers, DAOs, and SessionFactory.
 */

@Configuration
public class AppConfiguration {
    SessionFactory sessionFactory;


    @Bean
    public ScraperManager scraperManager() {
        ScraperManager scraperManager = new ScraperManager();
        ArrayList<Scraper> scraperArrayList = new ArrayList<>();
        scraperArrayList.add(amazon());
        scraperArrayList.add(box());
        scraperArrayList.add(ebuyerScraper());
        scraperArrayList.add(laptopdirect());
        scraperArrayList.add(stockmustgo());
        scraperManager.setScrapperArrayList(scraperArrayList);
        return scraperManager;
    }

    /**
     * Creates and configures the Amazon scraper bean.
     *
     * @return Initialized Amazon scraper instance.
     */
    @Bean
    public Amazon amazon(){
        Amazon scraper1 = new Amazon();
        scraper1.setLaptopDao(laptopDao());
        scraper1.setCrawlDelay(10000);
        return scraper1;
    }


    /**
     * Creates and configures the Box scraper bean.
     *
     * @return Initialized Box scraper instance.
     */
    @Bean
    public Box box(){
        Box scraper2 = new Box();
        scraper2.setLaptopDao(laptopDao());
        scraper2.setCrawlDelay(20000);
        return scraper2;
    }

    /**
     * Creates and configures the EbuyerScraper bean.
     *
     * @return Initialized EbuyerScraper instance.
     */

    @Bean
    public EbuyerScraper ebuyerScraper(){
        EbuyerScraper scraper3 = new EbuyerScraper();
        scraper3.setLaptopDao(laptopDao());
        scraper3.setCrawlDelay(30000);
        return scraper3;
    }

    /**
     * Creates and configures the LaptopsDirect scraper bean.
     *
     * @return Initialized LaptopsDirect scraper instance.
     */
    @Bean
    public LaptopsDirect laptopdirect(){
        LaptopsDirect scraper4 = new LaptopsDirect();
        scraper4.setLaptopDao(laptopDao());
        scraper4.setCrawlDelay(40000);
        return scraper4;
    }

    /**
     * Creates and configures the StockMustGo scraper bean.
     *
     * @return Initialized StockMustGo scraper instance.
     */
    @Bean
    public StockMustGo stockmustgo(){
        StockMustGo scraper5 = new StockMustGo();
        scraper5.setLaptopDao(laptopDao());
        scraper5.setCrawlDelay(50000);
        return scraper5;
    }

    /**
     * Creates and configures the LaptopDao bean.
     *
     * @return Initialized LaptopDao instance that all scrappers will use
     */
    @Bean
    public LaptopDao laptopDao(){
        LaptopDao laptopDao = new LaptopDao();
        laptopDao.setSessionFactory(sessionFactory());

        return laptopDao;
    }

    /**
     * Creates and configures the SessionFactory bean for Hibernate for connecting database.
     * @return Initialized SessionFactory instance.
     */
    @Bean
    public SessionFactory sessionFactory(){
        if(sessionFactory == null) {

            try {
                StandardServiceRegistryBuilder standardServiceRegistryBuilder = new StandardServiceRegistryBuilder();
                standardServiceRegistryBuilder.configure("hibernate.cfg.xml");


                StandardServiceRegistry registry = standardServiceRegistryBuilder.build();

                try {

                    sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
                }
                catch (Exception e) {
                    System.err.println("Session Factory build failed.");
                    e.printStackTrace();
                    StandardServiceRegistryBuilder.destroy( registry );
                }

                //Ouput result
                System.out.println("Session factory built.");

            }
            catch (Throwable ex) {
                System.err.println("SessionFactory creation failed." + ex);
                ex.printStackTrace();
            }
        }
        return sessionFactory;
    }
}