package org.bolu.model;
import javax.persistence.*;


/**
 * Entity class representing a laptop comparison entity in the database.
 * This class defines the structure and mapping of the 'comparison' table.
 * This class has a foreign key relationship with {@link LaptopVariation}
 *
 * @author Bolu
 */
@Entity
@Table(name = "comparison")
public class Comparison {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "laptop_variation_id", nullable = false)
    private LaptopVariation laptopVariation;

    @Column(name = "price_url")
    private String priceUrl;

    @Column(name = "price")
    private double price;

    @Column(name ="store_name")
    private String storeName;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LaptopVariation getLaptopVariation() {
        return laptopVariation;
    }

    public void setLaptopVariation(LaptopVariation laptopVariation) {
        this.laptopVariation = laptopVariation;
    }

    public String getPriceUrl() {
        return priceUrl;
    }

    public void setPriceUrl(String priceUrl) {
        this.priceUrl = priceUrl;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    @Override
    public String toString() {
        return "Comparison{" +
                "id=" + id +
                ", laptopVariation=" + laptopVariation +
                ", priceUrl='" + priceUrl + '\'' +
                ", price=" + price +
                ", storeName='" + storeName + '\'' +
                '}';
    }
}

