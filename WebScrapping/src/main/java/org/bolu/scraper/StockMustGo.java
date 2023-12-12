package org.bolu.scraper;

import org.bolu.model.Comparison;
import org.bolu.model.Laptop;
import org.bolu.model.LaptopVariation;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;


/**
 * A scraper implementation for StockMustGo website to retrieve laptop information and adding them to the database
 * This scraper collects HP, APPLE AND LENOVO product from the stockMustGo store
 */
public class StockMustGo extends Scraper {

    /**
     * Runs the scraper to extract laptop information from StockMustGo.
     * This method navigates through StockMustGo pages, extracts laptop details, and stores them in the database.
     */
    @Override
    public void run(){
        // Loop through different brands and URLs
        ArrayList<String> allURLs = new ArrayList<>();
        String[] urls = {
                "https://www.stockmustgo.co.uk/collections/refurbished-macs?page=",
                "https://www.stockmustgo.co.uk/search?sort_by=relevance&q=lenovo+laptops&type=product&filter.p.product_type=Laptops&filter.v.price.gte=&filter.v.price.lte=&page=",
                "https://www.stockmustgo.co.uk/collections/hp-refurbished-laptops?page="
        };
        int[] pageLimits = {4, 17, 5}; //{4, 17, 5}

        for (int i = 0; i < urls.length; i++) {
            for (int pageNum = 1; pageNum <= pageLimits[i]; pageNum++) {
                String dynamicURL = urls[i] + pageNum;
                allURLs.add(dynamicURL);
            }
        }

        for(String url : allURLs){
            // Navigates to the StockMustGo page
            getDriver().get(url);

            // Wait for page to load
            try{
                Thread.sleep(getCrawlDelay());
            }catch (Exception ex){
                ex.printStackTrace();
            }

            // Extract product cards from the page
            List<WebElement> productCards = getDriver().findElements(By.cssSelector(".product-item.product-item--vertical"));


            // Loop through each product card
            for(WebElement productCard : productCards) {
                try {
                    String prodDetails = productCard.findElement(By.cssSelector(".product-item__info-inner a.product-item__title")).getText();
                    final String productBrand = parseBrand(prodDetails);
                    if (productBrand != "Unknown") {
                    final String productModel = parseModel(prodDetails);
                    if (productModel != "Unknown") {
                        final int productYear = parseYear(prodDetails);
                        final String productOperatingSystem = parseOperatingSystem(prodDetails);
                        final String productImageUrl = productCard.findElement(By.cssSelector(".product-item__image-wrapper .product-item__secondary-image")).getAttribute("src");
                        final String productDisplay = parseDisplaySize(prodDetails);
                        final String productProcessor = parseProcessor(prodDetails);
                        final int productStorage = extractStorage(prodDetails);
                        final int productMemory = extractRam(prodDetails);
                        final String productColor = parseColor(prodDetails);

                        final String productPriceWhole = productCard.findElement(By.cssSelector("span.price.price--highlight")).getText();
                        String productUrl = productCard.findElement(By.cssSelector(".product-item__info-inner .product-item__title")).getAttribute("href");
                        double priceWhole = extractPrice(productPriceWhole);


                        if (priceWhole > 20) {

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
                            laptopComparison.setStoreName("StockMustGo");
                            laptopComparison.setLaptopVariation(laptopVariations);
                            this.getLaptopDao().addComparison(laptopComparison);

                            System.out.println("New laptop, variation, and comparison added to the database.");
                        } else {
                            // Laptop with the same attributes exists in the database
                            System.out.println("Laptop with similar attributes already exists in the database. Not adding duplicate data.");
                        }
                    }else{
                            System.out.println("Price Unknown");
                        }
                }else{
                        System.out.println("Product Model Unknown");
                    }
            }else{
                        System.out.println("Product Brand Unknown");
                    }

                }catch (Exception ex){

                }
            }
        }
        getDriver().close();
    }
}
