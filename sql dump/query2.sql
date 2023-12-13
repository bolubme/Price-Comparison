SELECT 
    lv.laptop_id AS laptop_id,
    lv.id AS variation_id,
    l.id AS laptop_model_id,
    c.id AS comparison_id,
    c.laptop_variation_id AS comparison_laptop_variation_id,
    l.description AS laptop_model_description
FROM 
    laptop_variations lv
JOIN 
    laptop l ON lv.laptop_id = l.id
JOIN 
    comparison c ON lv.id = c.laptop_variation_id;


select * from laptop where description = "Apple MacBook Pro 13.3'' MF839LL/A (2015) Intel i5 8GB RAM 128GB SSD - Good";

select * from laptop_variations;

select * from laptop_variations;

select * from comparison where store_name = "StockMustGo";

select * from laptop;

select * from comparison;

select * from laptop_variations

DELETE FROM laptop WHERE id = 1768;





-- WHERE description LIKE '%Apple MacBook Pro 13.3'' MF839LL/A (2015) Intel i5 8GB RAM 128GB SSD - Good%';



