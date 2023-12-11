package org.bolu.scraper;

import org.bolu.model.Comparison;
import org.bolu.model.Laptop;
import org.bolu.model.LaptopVariation;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

/**
 * A scraper implementation for Box website to retrieve laptop information and adding them to the database
 * This scraper collects HP, APPLE AND LENOVO product from the box store
 */

public class Box extends Scraper {

    /**
     * Runs the scraper to extract laptop information from Box.
     * This method navigates through Box pages, extracts laptop details, and stores them in the database.
     */
    @Override
    public void run(){
        // Loop through different brands and URLs
        ArrayList<String> allURLs = new ArrayList<>();
        String[] urls = {
                "https://www.box.co.uk/laptops/page/",
        };
        String[] queries = {
                "/refine/m~apple$m~hp$m~lenovo"
        };
        int[] pageLimits = {3}; //3

        for (int i = 0; i < urls.length; i++) {
            for (int pageNum = 1; pageNum <= pageLimits[i]; pageNum++) {
                String dynamicURL = urls[i] + pageNum + queries[i];
                allURLs.add(dynamicURL);
            }
        }

        for (String url : allURLs) {
            // Navigates to the Box page
            getDriver().get(url);

            // Wait for page to load
            try {
                Thread.sleep(getCrawlDelay());
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            // Extract product cards from the page
            List<WebElement> productCards = getDriver().findElements(By.cssSelector(".product-list-item .p-list"));

            // Loop through each product card
            for(WebElement productCard : productCards){
                try{
                    String prodDetails = productCard.findElement(By.cssSelector(".p-list-content-wrapper .p-list-title-wrapper h3")).getText();
                    final String productBrand = parseBrand(prodDetails);
                    final String productModel = parseModel(prodDetails);
                    final int productYear = parseYear(prodDetails);
                    final String productOperatingSystem = parseOperatingSystem(prodDetails);
                    final String productImageUrl = productCard.findElement(By.cssSelector(".p-list-second-image .p-second-a img")).getAttribute("src");
                    final String productColor = parseColor(prodDetails);

                    // Find all <li> tags to scrape
                    java.util.List<WebElement> items = productCard.findElements(By.cssSelector(".p-list-points-wrapper .p-list-points"));

                    String productDisplay = null;
                    String productProcessor = null;
                    int productStorage = 0;
                    int productMemory = 0;

                    for(WebElement item: items){
                        productDisplay = parseDisplaySize(item.getText());
                        productProcessor = parseProcessor(item.getText());
                        productStorage = extractStorage(item.getText());
                        productMemory = extractRam(item.getText());
                    }

                    final String productUrl = productCard.findElement(By.cssSelector(".p-list-title-wrapper h3 a")).getAttribute("href");
                    final String productPriceWhole = productCard.findElement(By.cssSelector(".p-list-sell .pq-price")).getText();


                    // Remove the comma and convert to a decimal format
                    String stringWithoutComma = productPriceWhole.replace(",", "");
                    String stringWithouCurrency = stringWithoutComma.replace("£", "");
                    double priceDecimalNumber = Double.parseDouble(stringWithouCurrency);


                    // Create a new laptop instance
                    Laptop newLaptop = new Laptop();
                    newLaptop.setDescription(prodDetails);
                    newLaptop.setImageUrl(productImageUrl);

                    System.out.println("-------------------------------------------------------------------------");

                    Laptop existingLaptop = this.getLaptopDao().findLaptop(newLaptop);

                    if (existingLaptop == null) {
                        // Laptop doesn't exist, so add it to the database
                        newLaptop.setBrand(productBrand);
                        newLaptop.setModelName(productModel);
                        newLaptop.setReleaseYear(productYear);
                        newLaptop.setOperatingSystem(productOperatingSystem);
                        existingLaptop = this.getLaptopDao().addLaptop(newLaptop);

                        // Create and add variation
                        LaptopVariation laptopVariations = new LaptopVariation();
                        laptopVariations.setDisplay(productDisplay);
                        laptopVariations.setProcessor(productProcessor);
                        laptopVariations.setStorage(productStorage);
                        laptopVariations.setMemory(productMemory);
                        laptopVariations.setColor(productColor);
                        laptopVariations.setLaptop(newLaptop);
                        laptopVariations.setLaptop(existingLaptop);
                        this.getLaptopDao().addLaptopVariation(laptopVariations);

                        // Create and add comparison
                        Comparison laptopComparison = new Comparison();
                        laptopComparison.setPriceUrl(productUrl);
                        laptopComparison.setPrice(priceDecimalNumber);
                        laptopComparison.setStoreName("Box");
                        laptopComparison.setLaptopVariation(laptopVariations);
                        this.getLaptopDao().addComparison(laptopComparison);

                        System.out.println("New laptop, variation, and comparison added to the database.");
                    } else {
                        // Laptop with the same attributes exists in the database
                        System.out.println("Laptop with similar attributes already exists in the database. Not adding duplicate data.");
                    }
                }catch (Exception exx){
                    exx.printStackTrace();
                }
            }
        }
        getDriver().close();
    }
}
