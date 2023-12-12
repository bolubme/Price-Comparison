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
 * A scraper implementation for LaptopsDirect website to retrieve laptop information and adding them to the database
 * This scraper collects HP, APPLE AND LENOVO product from the laptopsDirect store
 */
public class LaptopsDirect extends Scraper {

    /**
     * Runs the scraper to extract laptop information from LaptopsDirect.
     * This method navigates through LaptopsDirect pages, extracts laptop details, and stores them in the database.
     */
    @Override
    public void run(){
        // Loop through different brands and URLs
        ArrayList<String> allURLs = new ArrayList<>();
        String[] urls = {
                "https://www.laptopsdirect.co.uk/bct/laptops-and-netbooks/laptops/apple?pageNumber=",
                "https://www.laptopsdirect.co.uk/bct/laptops-and-netbooks/hewlett-packard-laptops?pageNumber=",
                "https://www.laptopsdirect.co.uk/bct/laptops-and-netbooks/lenovo-laptops?pageNumber="
        };
//        int[] pageLimits = { 2, 3, 4 };
        int[] pageLimits = { 2, 3, 4 };

        for (int i = 0; i < urls.length; i++) {
            for (int pageNum = 1; pageNum <= pageLimits[i]; pageNum++) {
                String dynamicURL = urls[i] + pageNum;
                allURLs.add(dynamicURL);
            }
        }


        for (String url : allURLs) {
            // Navigates to the LaptosDirect page
            getDriver().get(url);

            // Wait for page to load
            try {
                Thread.sleep(getCrawlDelay());
            } catch (Exception ex) {
                ex.printStackTrace();
            }


            // Extract product cards from the page
            List<WebElement> productCards = getDriver().findElements(By.cssSelector("#productList .OfferBox"));


            // Loop through each product card
            for(WebElement productCard : productCards){
                try {

                    String prodDetails = productCard.findElement(By.cssSelector(".OfferBoxTitle .offerboxtitle")).getText();
                    final String productBrand = parseBrand(prodDetails);
                    final String productModel = parseModel(prodDetails);
                    if (productModel != "Unknown") {
                    final int productYear = parseYear(prodDetails);
                    final String productOperatingSystem = parseOperatingSystem(prodDetails);
                    final String productImageUrl = productCard.findElement(By.cssSelector(".sr_image .offerImage")).getAttribute("src");
                    final String productProcessor = parseProcessor(prodDetails);
                    final String productColor = parseColor(prodDetails);
                    final String productPriceWhole = productCard.findElement(By.cssSelector(".OfferBoxPrice .offerprice")).getText();
                    final String productUrl = productCard.findElement(By.cssSelector(".OfferBox .offerboxlink")).getAttribute("href");


                    // Find all <li> tags to scrape
                    java.util.List<WebElement> items = productCard.findElements(By.cssSelector(".productInfo ul"));

                    String productDisplay = null;
                    int productStorage = 0;
                    int productMemory = 0;

                    // Loop through each item to extract required information
                    for (WebElement item : items) {
                        productDisplay = parseDisplaySize(item.getText());
                        productStorage = extractStorage(item.getText());
                        productMemory = extractRamDirect(item.getText());
                    }


                    String stringWithoutCurrency = productPriceWhole.replace("£", "");
                    double priceWhole = Double.parseDouble(stringWithoutCurrency);

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
                        laptopComparison.setStoreName("LaptopsDirect");
                        laptopComparison.setLaptopVariation(laptopVariations);
                        this.getLaptopDao().addComparison(laptopComparison);

                        System.out.println("New laptop, variation, and comparison added to the database.");
                    } else {
                        // Laptop with the same attributes exists in the database
                        System.out.println("Laptop with similar attributes already exists in the database. Not adding duplicate data.");
                    }
                }else{
                        System.out.println("Product Unknown");
                    }

                }catch (Exception exx){
                    exx.printStackTrace();
                }
            }
        }
        getDriver().close();
    }
}
