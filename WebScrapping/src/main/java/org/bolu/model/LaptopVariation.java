package org.bolu.model;

import javax.persistence.*;


/**
 * Entity class representing variations of a laptop in the database.
 * This class defines the structure and mapping of the 'laptop_variations' table.
 */
@Entity
@Table(name = "laptop_variations")
public class LaptopVariation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name="laptop_id", nullable = false)
    Laptop laptop;

    @Column(name = "display")
    private String display;

    @Column(name = "processor")
    private String processor;

    @Column(name = "storage")
    private int storage;

    @Column(name = "memory")
    private int memory;

    @Column(name = "color")
    private String color;



    public LaptopVariation(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Laptop getLaptop() {
        return laptop;
    }

    public void setLaptop(Laptop laptop) {
        this.laptop = laptop;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getProcessor() {
        return processor;
    }

    public void setProcessor(String processor) {
        this.processor = processor;
    }

    public int getStorage() {
        return storage;
    }

    public void setStorage(int storage) {
        this.storage = storage;
    }

    public int getMemory() {
        return memory;
    }

    public void setMemory(int memory) {
        this.memory = memory;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "LaptopVariation{" +
                "id=" + id +
                ", laptop=" + laptop +
                ", display='" + display + '\'' +
                ", processor='" + processor + '\'' +
                ", storage='" + storage + '\'' +
                ", memory='" + memory + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}
