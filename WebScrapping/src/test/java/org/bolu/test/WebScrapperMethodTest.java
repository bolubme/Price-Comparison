package org.bolu.test;

import org.bolu.scraper.Scraper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for validating methods in the Scraper class, focusing on web scraping functionalities.
 */

@DisplayName("Test Scrapers Methods")
public class WebScrapperMethodTest {
    private Scraper scraper;

    @BeforeEach
    void setUp() {
        scraper = new Scraper();
    }

    /**
     * Test method to validate color extraction from a given laptop description.
     */
    @Test
    void testParseColor() {
        assertEquals("Black", scraper.parseColor("Apple 2020 MacBook Air Laptop M1 Chip, 13” Retina Display, 8GB RAM, 256GB SSD Storage, Backlit Keyboard, FaceTime HD Camera, Touch ID;  black "));
    }

    /**
     * Test method to validate storage extraction from a given laptop description.
     */
    @Test
    void testExtractStorage(){
        assertEquals(256, scraper.extractStorage("Apple 2020 MacBook Air Laptop M1 Chip, 13” Retina Display, 8GB RAM, 256GB SSD Storage, Backlit Keyboard, FaceTime HD Camera, Touch ID; black"));
    }


    /**
     * Test method to validate RAM extraction from a given laptop description.
     */
    @Test
    void testExtractRam(){
        assertEquals(8, scraper.extractRam("Apple 2020 MacBook Air Laptop M1 Chip, 13” Retina Display, 8GB RAM, 256GB SSD Storage, Backlit Keyboard, FaceTime HD Camera, Touch ID; black"));

    }

    /**
     * Test method to validate brand extraction from a given laptop description.
     */
    @Test
    void testParseBrand() {
        assertEquals("Apple", scraper.parseBrand("Apple 2020 MacBook Air Laptop M1 Chip, 13” Retina Display, 8GB RAM, 256GB SSD Storage, Backlit Keyboard, FaceTime HD Camera, Touch ID; black"));
    }

    /**
     * Test method to validate year extraction from a given laptop description.
     */

    @Test
    void testParseYear() {
        assertEquals(2020, scraper.parseYear("Apple 2020 MacBook Air Laptop M1 Chip, 13” Retina Display, 8GB RAM, 256GB SSD Storage, Backlit Keyboard, FaceTime HD Camera, Touch ID; black"));
    }


    /**
     * Test method to validate model extraction from a given laptop description.
     */
    @Test
    void testParseModel() {
        assertEquals("MacBook Air", scraper.parseModel("Apple 2020 MacBook Air Laptop M1 Chip, 13” Retina Display, 8GB RAM, 256GB SSD Storage, Backlit Keyboard, FaceTime HD Camera, Touch ID; black"));
    }

    /**
     * Test method to validate processor extraction from a given laptop description.
     */
    @Test
    void testParseProcessor() {
        assertEquals("Apple M1", scraper.parseProcessor("Apple 2020 MacBook Air Laptop M1 Chip, Mac OS, 13” Retina Display, 8GB RAM, 256GB SSD Storage, Backlit Keyboard, FaceTime HD Camera, Touch ID; black"));
    }

    /**
     * Test method to validate operating system extraction from a given laptop description.
     */
    @Test
    void testParseOperatingSystem() {
        assertEquals("Mac Os", scraper.parseOperatingSystem("Apple 2020 MacBook Air Laptop M1 Chip, Mac OS, 13” Retina Display, 8GB RAM, 256GB SSD Storage, Backlit Keyboard, FaceTime HD Camera, Touch ID; black"));
    }


}
