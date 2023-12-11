package org.bolu.scraper;

import org.bolu.model.Comparison;
import org.bolu.model.Laptop;
import org.bolu.model.LaptopVariation;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * A scraper implementation for Ebuyer website to retrieve laptop information and adding them to the database
 * This scraper collects HP, APPLE AND LENOVO product from the ebuyer store
 */
public class EbuyerScraper extends Scraper {


    /**
     * Runs the scraper to extract laptop information from Ebuyer.
     * This method navigates through Ebuyer pages, extracts laptop details, and stores them in the database.
     */
    @Override
    public void run(){
        // Loop through different brands and URLs
        ArrayList<String> allURLs = new ArrayList<>();
        String[] urls = {
                "https://www.ebuyer.com/store/Computer/cat/Laptops/Apple?page=",
                "https://www.ebuyer.com/store/Computer/cat/Laptops/Lenovo?page=",
                "https://www.ebuyer.com/store/Computer/cat/Laptops?page="
        };
        String[] queries = {
                "&q=mac+book",
                "&q=lenovo",
                "&q=hp"
        };
        int[] pageLimits = { 3, 6, 6 }; // Limits for Apple, Lenovo, HP respectively

        for (int i = 0; i < urls.length; i++) {
            for (int pageNum = 1; pageNum <= pageLimits[i]; pageNum++) {
                String dynamicURL = urls[i] + pageNum + queries[i];
                allURLs.add(dynamicURL);
            }
        }

        for (String url : allURLs) {
            // Navigates to the Ebuyer page
            getDriver().get(url);

            // Wait for page to load
            try {
                Thread.sleep(getCrawlDelay());
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            // Extract product cards from the page
            List<WebElement> productCards = getDriver().findElements(By.cssSelector("#grid-view .grid-item"));

            // Loop through each product card
            for(WebElement productCard : productCards){
                try {
                    String productPriceWhole = null;
                    double priceWhole = 0.0;
                    try {
                        productPriceWhole = productCard.findElement(By.cssSelector(".grid-item__price .inc-vat .price")).getText();
                    } catch (Exception ex) {
                    }
                    if (productPriceWhole != null) {
                        String stringWithoutComma = productPriceWhole.replace(",", "");
                        String stringWithoutCurrency = stringWithoutComma.replace("£", "");
                        String stringWithoutCharacters = stringWithoutCurrency.replace("  inc. vat", "");
                        priceWhole = Double.parseDouble(stringWithoutCharacters);
                    }
                    if (priceWhole > 20) {



                    String prodDetails = productCard.findElement(By.cssSelector("h3.grid-item__title")).getText();
                    final String productBrand = parseBrand(prodDetails);
                    final String productModel = parseModel(prodDetails);
                    if (productModel != "Unknown") {

                        final int productYear = parseYear(prodDetails);
                        final String productImageUrl = productCard.findElement(By.cssSelector(".grid-item__img img")).getAttribute("src");
                        final String productColor = parseColor(prodDetails);
                        final String productUrl = productCard.findElement(By.cssSelector(".grid-item__title a")).getAttribute("href");

                        final int productStorage = extractStorage(prodDetails);
                        final int productMemory = extractRam(prodDetails);

                        // Find all <li> tags to scrape
                        java.util.List<WebElement> items = productCard.findElements(By.cssSelector("ul.grid-item__ksp"));
//

                        String productDisplay = null;
                        String productOperatingSystem = null;
                        String productProcessor = null;

                        // Loop through each item to extract required information.
                        for (WebElement item : items) {
                            productDisplay = parseDisplaySize(item.getText());
                            productProcessor = parseProcessor(item.getText());
                            productOperatingSystem = parseOperatingSystem(item.getText());
                        }

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
                            laptopComparison.setPrice(priceWhole);
                            laptopComparison.setStoreName("Ebuyer");
                            laptopComparison.setLaptopVariation(laptopVariations);
                            this.getLaptopDao().addComparison(laptopComparison);

                            System.out.println("New laptop, variation, and comparison added to the database.");
                        } else {
                            // Laptop with the same attributes exists in the database
                            System.out.println("Laptop with similar attributes already exists in the database. Not adding duplicate data.");
                        }

                    } else {
                        System.out.println("Product Model Unknown");
                    }
                }else{
                        System.out.println("Price Unknown");
                    }

                }catch (Exception exx){
                    exx.printStackTrace();
                }
            }
        }
        getDriver().close();
    }
}
