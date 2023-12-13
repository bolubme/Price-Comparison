-- Laptop Model Table
CREATE TABLE laptop (
    id INT AUTO_INCREMENT PRIMARY KEY,
    brand VARCHAR(255),
    model_name VARCHAR(255),
    release_year INT,
    operating_system VARCHAR(255),    
    description VARCHAR(255),
    image_url VARCHAR(255)
);

-- Laptop Variations Table
CREATE TABLE laptop_variations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    laptop_id INT,
    display VARCHAR(255),
    processor VARCHAR(255),
    storage VARCHAR(255),
    memory VARCHAR(255),
    color VARCHAR(255),
    FOREIGN KEY (laptop_id) REFERENCES laptop(id)
); 

-- Comparison table
CREATE TABLE comparison (
    id INT AUTO_INCREMENT PRIMARY KEY,
    laptop_variation_id INT,
    price_url VARCHAR(255),
    price DECIMAL(10, 2),
    store_name VARCHAR(255),
    FOREIGN KEY (laptop_variation_id) REFERENCES laptop_variations(ID)
);
