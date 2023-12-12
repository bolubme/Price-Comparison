package org.bolu.test;


import org.bolu.config.AppConfiguration;
import org.bolu.model.Laptop;
import org.bolu.model.LaptopDao;
import org.bolu.model.LaptopVariation;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Test class for validating database operations related to laptops, laptop variations, and comparisons.
 * It contains individual test methods to verify the functionalities of methods within the LaptopDao class.
 */

@DisplayName("Test Scrapers and Database functionality")
public class DatabaseTest {
    private LaptopDao laptopDao;
    private SessionFactory sessionFactory;

    @BeforeEach
    public void setup() {
        // Set up Hibernate SessionFactory
        sessionFactory = new AppConfiguration().sessionFactory();
        laptopDao = new LaptopDao();
        laptopDao.setSessionFactory(sessionFactory);
    }

    /**
     * Test to validate the addition of a laptop entity to the database.
     */
    @Test
    public void testAddLaptopToDatabase() {
        // Create a sample Laptop object
        Laptop laptop = new Laptop();
        laptop.setDescription("Apple 2020 MacBook Air Laptop M1 Chip, 13” Retina Display, 8GB RAM, 256GB SSD Storage, Backlit Keyboard, FaceTime HD Camera, Touch ID; Space Grey");
        laptop.setImageUrl("https://m.media-amazon.com/images/I/71jG+e7roXL._AC_UL320_.jpg");
        laptop.setBrand("Apple");
        laptop.setModelName("MacBook");
        laptop.setReleaseYear(2020);

        // Add the laptop to the database
        Laptop addedLaptop = laptopDao.addLaptop(laptop);

        // Check if the addition was successful
        assertNotNull(addedLaptop, "Laptop should not be null after addition");
        assertNotNull(addedLaptop.getId(), "Laptop ID should not be null after addition");
        assertEquals("Apple 2020 MacBook Air Laptop M1 Chip, 13” Retina Display, 8GB RAM, 256GB SSD Storage, Backlit Keyboard, FaceTime HD Camera, Touch ID; Space Grey", addedLaptop.getDescription(), "Descriptions should match");
        assertEquals("https://m.media-amazon.com/images/I/71jG+e7roXL._AC_UL320_.jpg", addedLaptop.getImageUrl(), "Image should Match");
        assertEquals("Apple", addedLaptop.getBrand(), "Brand should Match");
        assertEquals("MacBook", addedLaptop.getModelName(), "Model should Match");
        assertEquals(2020, addedLaptop.getReleaseYear(), "Release should Match");
    }


    /**
     * Test to validate the addition of a laptop variation entity to the database.
     */
    @Test
    public void testAddLaptopVariationToDatabase() {
        // Create a sample Laptop object
        Laptop laptop = new Laptop();

        // Save the laptop to the database
        laptop = laptopDao.addLaptop(laptop);

        // Create a sample LaptopVariation object
        LaptopVariation laptopVariation = new LaptopVariation();
        laptopVariation.setDisplay("13");
        laptopVariation.setProcessor("M1 chip");
        laptopVariation.setStorage(256);
        laptopVariation.setMemory(8);
        laptopVariation.setColor("Space Grey");

        // Set the Laptop entity for the LaptopVariation
        laptopVariation.setLaptop(laptop);

        // Add the laptop variation to the database
        LaptopVariation addedLaptopVariation = laptopDao.addLaptopVariation(laptopVariation);

        // Check if the addition was successful
        assertNotNull(addedLaptopVariation, "Laptop Variation should not be null after addition");
        assertNotNull(addedLaptopVariation.getId(), "Laptop Variation ID should not be null after addition");
        assertEquals("13", addedLaptopVariation.getDisplay(), "Display should match");
        assertEquals("M1 chip", addedLaptopVariation.getProcessor(), "Processor should match");
        assertEquals(256, addedLaptopVariation.getStorage(), "Storage should match");
        assertEquals(8, addedLaptopVariation.getMemory(), "Memory should match");
        assertEquals("Space Grey", addedLaptopVariation.getColor(), "Color should match");
        assertEquals(laptop.getId(), addedLaptopVariation.getLaptop().getId(), "Laptop ID should match");
    }

    /**
     * Test to find a laptop entity in the database based on specific criteria.
     */
    @Test
    public void testFindLaptopInDatabase() {
        // Create a sample Laptop object for searching
        Laptop laptopToFind = new Laptop();
        laptopToFind.setDescription("Apple 2020 MacBook Air Laptop M1 Chip, 13” Retina Display, 8GB RAM, 256GB SSD Storage, Backlit Keyboard, FaceTime HD Camera, Touch ID; Space Grey");
        laptopToFind.setImageUrl("https://m.media-amazon.com/images/I/71jG+e7roXL._AC_UL320_.jpg");
        laptopToFind.setBrand("Apple");
        laptopToFind.setModelName("MacBook");
        laptopToFind.setReleaseYear(2020);

        // Attempt to find the laptop in the database
        Laptop foundLaptop = laptopDao.findLaptop(laptopToFind);

        // Check if the laptop was found
        assertNotNull(foundLaptop, "Found laptop should not be null");
        assertEquals(laptopToFind.getDescription(), foundLaptop.getDescription(), "Descriptions should match");
        assertEquals(laptopToFind.getImageUrl(), foundLaptop.getImageUrl(), "Image URLs should match");
        assertEquals(laptopToFind.getBrand(), foundLaptop.getBrand(), "Brands should match");
        assertEquals(laptopToFind.getModelName(), foundLaptop.getModelName(), "Model names should match");
        assertEquals(laptopToFind.getReleaseYear(), foundLaptop.getReleaseYear(), "Release years should match");
    }


    @AfterEach
    public void tearDown() {
        // Close the session factory after tests
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}
