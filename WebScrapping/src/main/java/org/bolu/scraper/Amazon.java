package org.bolu.scraper;


import org.bolu.model.Comparison;
import org.bolu.model.Laptop;
import org.bolu.model.LaptopVariation;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import javax.crypto.spec.PSource;
import java.awt.*;
import java.awt.desktop.SystemEventListener;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;


/**
 * A scraper implementation for Amazon website to retrieve laptop information and adding them to the database
 * This scraper collects HP, APPLE AND LENOVO product from the amazon store
 */
public class Amazon extends Scraper{

    /**
     * Generates a list of Amazon URLs for the specified brand and number of pages.
     *
     * @param brand      The brand of laptops to generate URLs for.
     * @param totalPages The total number of pages to generate URLs for.
     * @return An ArrayList of Amazon URLs for the specified brand.
     */
    public static ArrayList<String> generateAmazonURLs(String brand, int totalPages) {
        ArrayList<String> urlList = new ArrayList<>();
        String baseUrl = "https://www.amazon.co.uk/s?i=computers&bbn=429886031&rh=n%3A429886031%2Cp_89%3A" + brand + "&dc&page=";
        String suffix = "&qid=1700911455&rnid=1632651031&ref=sr_pg_";

        for (int pageNum = 1; pageNum <= totalPages; pageNum++) {
            String dynamicURL = baseUrl + pageNum + suffix + pageNum;
            urlList.add(dynamicURL);
        }

        return urlList;
    }


    /**
     * Runs the scraper to extract laptop information from Amazon.
     * This method navigates through Amazon pages, extracts laptop details, and stores them in the database.
     */
    @Override
    public void run(){
        int totalPages = 17;
        // 17
        ArrayList<ArrayList<String>> allURLs = new ArrayList<>();
        allURLs.add(generateAmazonURLs("Apple", totalPages));
        allURLs.add(generateAmazonURLs("Lenovo", totalPages));
        allURLs.add(generateAmazonURLs("HP", totalPages));



        // Looping over all URLs
        for (ArrayList<String> brandURLs : allURLs) {
            for (String url : brandURLs) {
                // Navigates to the Amazon page
                getDriver().get(url);

                // Wait for page to load
                try {
                    Thread.sleep(getCrawlDelay());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                // Extract product cards from the page
                List<WebElement> productCards = getDriver().findElements(By.cssSelector(".s-card-container"));


                // Loop through each product card
                for (WebElement productCard : productCards) {
                    try {
                        String productPrice = null;
                        String productPriceDec = null;

                        try {
                            productPrice = productCard.findElement(By.className("a-price-whole")).getText();
                            productPriceDec = productCard.findElement(By.cssSelector(".a-link-normal .a-price .a-price-fraction")).getText();
                        } catch (Exception ex) {
                        }

                        String productUrl = productCard.findElement(By.cssSelector("a.a-link-normal.s-no-outline")).getAttribute("href");
                        productUrl = productUrl.substring(0, productUrl.indexOf('?'));

                        String priceDecimalNumber = productPrice + "." + productPriceDec;
                        if(!priceDecimalNumber.equals("null.null")) {
                        String prodDetails = productCard.findElement(By.cssSelector("h2.a-color-base.a-size-mini")).getText();

                        if (prodDetails.isEmpty()) {
                            System.out.println("details Empty");
                        } else {
                            final String productBrand = parseBrand(prodDetails);
                            if(productBrand != "Unknown") {
                                final String productModel = parseModel(prodDetails);
                                if (productModel != "Unknown") {
                                final int productYear = parseYear(prodDetails);
                                final String productOperatingSystem = parseOperatingSystem(prodDetails);
                                final String productImageUrl = productCard.findElement(By.cssSelector(".s-product-image-container div.a-section .s-image")).getAttribute("src");
                                final String productDisplay = parseDisplaySize(prodDetails);
                                final String productProcessor = parseProcessor(prodDetails);
                                final int productStorage = extractStorage(prodDetails);
                                final int productMemory = extractRam(prodDetails);
                                final String productColor = parseColor(prodDetails);

                                String stringWithoutComma = priceDecimalNumber.replace(",", "");
                                double priceWhole = Double.parseDouble(stringWithoutComma);

                                // Create a new laptop instance
                                Laptop newLaptop = new Laptop();
                                newLaptop.setDescription(prodDetails);
                                newLaptop.setImageUrl(productImageUrl);


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
                                    laptopComparison.setStoreName("Amazon");
                                    laptopComparison.setLaptopVariation(laptopVariations);
                                    this.getLaptopDao().addComparison(laptopComparison);

                                    System.out.println("New laptop, variation, and comparison added to the database.");
                                } else {
                                    // Laptop with the same attributes exists in the database
                                    System.out.println("Laptop with similar attributes already exists in the database. Not adding duplicate data.");
                                }
                            }else {
                                    System.out.println("Model not found");
                                }
                            }else{
                                System.out.println("Brand not found");
                            }
                        }

                    }else{
                            System.out.println("No price found");
                        }
                    }catch (Exception exx){
                        exx.printStackTrace();
                    }
                }
            }
        }

        getDriver().close();
    }
}










