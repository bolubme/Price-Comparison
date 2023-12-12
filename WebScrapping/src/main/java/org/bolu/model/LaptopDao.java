package org.bolu.model;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * Data Access Object (DAO) handling database operations related to laptops, laptop variations, and comparisons.
 * This class performs CRUD operations for entities such as Laptop, LaptopVariation, and Comparison.
 */

public class LaptopDao {
    private SessionFactory sessionFactory;


    /**
     * Adds a Laptop entity to the database.
     *
     * @param laptop The Laptop object to be added to the database.
     * @return The added Laptop object or null if an error occurs.
     */
    public Laptop addLaptop(Laptop laptop) {
        //Get a new Session instance from the SessionFactory
        Session session = sessionFactory.getCurrentSession();
        try {
            // Begin a database transaction
            session.beginTransaction();

            // Save the laptop entity into the database
            session.save(laptop);

            // Commit the transaction
            session.getTransaction().commit();
            System.out.println("Laptop added to database with ID: " + laptop.getId());
            return laptop;
        } catch (Exception e) {
            // If an exception occurs, perform rollback, print the error, and return null
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
            return null;
        } finally {
            // Ensure the session is closed after the operation
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }


    /**
     * Adds a LaptopVariation entity to the database.
     *
     * @param laptopVariation The LaptopVariation object to be added to the database.
     * @return The added LaptopVariation object.
     */
    public LaptopVariation addLaptopVariation(LaptopVariation laptopVariation) {
        // Get a new Session instance from the SessionFactory
        Session session = sessionFactory.getCurrentSession();
        try {
            // Begin a database transaction
            session.beginTransaction();

            // Save the laptop entity into the database
            session.save(laptopVariation);

            // Commit the transaction
            session.getTransaction().commit();
            System.out.println("Laptop variation added to database with ID: " + laptopVariation.getId());
            return laptopVariation;
        } catch (Exception e) {
            // If an exception occurs, perform rollback, print the error, and return null
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
            return null;
        } finally {
            // Ensure the session is closed after the operation
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    /**
     * Adds a Comparison entity to the database.
     *
     * @param comparison The Comparison object to be added to the database.
     * @return The added Comparison object.
     */
    public Comparison addComparison(Comparison comparison) {
        // Get a new Session instance from the SessionFactory
        Session session = sessionFactory.getCurrentSession();
        try {
            // Begin a database transaction
            session.beginTransaction();

            // Save the laptop entity into the database
            session.save(comparison);

            // Commit the transaction
            session.getTransaction().commit();
            System.out.println("Comparison added to database with ID: " + comparison.getId());
            return comparison;
        } catch (Exception e) {
            // If an exception occurs, perform rollback, print the error, and return null
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
            return null;
        } finally {
            // Ensure the session is closed after the operation
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }



    /**
     * Finds a Laptop entity in the database based on description and image URL.
     *
     * @param laptop The Laptop object used to search for a matching entity.
     * @return The found Laptop object or null if not found.
     */
    public Laptop findLaptop(Laptop laptop) {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.beginTransaction();

            String queryStr = "from Laptop where description='" + laptop.getDescription() + "' AND imageUrl='" + laptop.getImageUrl() + "'";
            List<Laptop> laptopList = session.createQuery(queryStr).getResultList();

            if (!laptopList.isEmpty()) {
                return laptopList.get(0); // Found the laptop with the same description
            } else {
                return null;
            }
        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
            return null;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    /**
     * Default constructor for the LaptopDao class.
     */
    public LaptopDao(){

    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void shutDown(){sessionFactory.close();
        System.out.println("Hibernate session factory closed");;}

}
