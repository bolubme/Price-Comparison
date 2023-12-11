package org.bolu.scraper;


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Manages the execution and control of multiple Scraper instances.
 * Allows starting, stopping, and coordinating the termination of scraping threads.
 */
public class ScraperManager {
    List<Scraper> scrapperArrayList = getScrapperArrayList();

    /**
     * Initiates the execution of all configured scrapers.
     * Allows the user to stop the scraping process by entering 'stop'.
     * Waits for all scraper threads to finish before marking scraping as complete.
     */

    public void startScrapers(){
        // Start all scraper threads
        for (Scraper scraper : scrapperArrayList){
            scraper.start();
        }

        //Read input from user until they type 'stop'
        Scanner scanner = new Scanner(System.in);
        String userInput = scanner.nextLine();
        while (!userInput.equals("stop")) {
            userInput = scanner.nextLine();
        }

        //Stop threads
        for (Scraper scraper : scrapperArrayList){
            scraper.stopThread();
        }

        try{
            // Wait for all scraper threads to finish
            for(Scraper scraper : scrapperArrayList){
                scraper.join();
            }
        }
        catch(InterruptedException ex){
            System.out.println("Interrupted exception thrown: " + ex.getMessage());
        }

        System.out.println("Web scraping complete");

    }

    /**
     * Getter method to retrieve the list of Scraper instances.
     *
     * @return The list containing Scraper instances.
     */
    public List<Scraper> getScrapperArrayList() {
        return scrapperArrayList;
    }

    /**
     * Setter method to set the list of Scraper instances.
     *
     * @param scrapperArrayList The list containing Scraper instances.
     */

    public void setScrapperArrayList(List<Scraper> scrapperArrayList) {
        this.scrapperArrayList = scrapperArrayList;
    }


}

