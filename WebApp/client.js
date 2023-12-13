const mysql = require('mysql');

/**
 * MySQL connection pool for the database.
 * @type {mysql.Pool}
 */
const connectionPool = mysql.createPool({
    connectionLimit: 1,
    host: "localhost",
    user: "root",
    password: "",
    database: "price_website",
    debug: false
});

/**
 * Gets the total count of products.
 * @returns {Promise<number>} The total count of products.
 */
module.exports.getTotalProductCount = async ()=> {
    const sqlQuery = "SELECT COUNT(*) FROM laptop"
    let numCount = await executeSQLQuery(sqlQuery);
    return numCount[0]["COUNT(*)"];
}


/**
 * Gets the total count of products based on a search query.
 * @param {string} descrip - The description to search for.
 * @returns {Promise<number>} The total count of products based on the search query.
 */
module.exports.getTotalProductCountBySearch = async (descrip) => {
    const sqlQuery = `
        SELECT COUNT(*) FROM laptop
        WHERE description LIKE "%${descrip}%"
    `;
    
    let numCount = await executeSQLQuery(sqlQuery);
    return numCount[0]["COUNT(*)"];
}


/**
 * Retrieves comparison search results based on criteria.
 * @param {string} modelName - The model name to search for.
 * @param {string} storage - The storage size.
 * @param {string} memory - The memory size.
 * @param {number} numOfItems - Number of items to retrieve.
 * @param {number} offset - Offset for results.
 * @returns {Promise<any>} Promise that resolves to the query result.
 */
module.exports.getComparisonSearch = async (modelName, storage, memory, display, processor, release_year, numOfItems, offset) => {
    let querySql = `SELECT 
    laptop.id,
    laptop.model_name,
    laptop.brand,
    laptop.release_year,
    laptop.description,
    laptop_variations.display,
    laptop_variations.processor,
    laptop_variations.storage,
    laptop_variations.memory,
    laptop_variations.color,
    comparison.price,
    comparison.store_name,
    comparison.price_url,
    laptop.image_url 
FROM 
    comparison 
INNER JOIN 
    laptop_variations ON comparison.laptop_variation_id = laptop_variations.id
INNER JOIN 
    laptop ON laptop_variations.laptop_id = laptop.id 
WHERE 
    laptop.model_name LIKE "%${modelName}%"
    AND laptop_variations.processor LIKE "%${processor}%"
    AND laptop_variations.display LIKE "%${display}%"
    AND laptop_variations.storage = ${storage}
    AND laptop_variations.memory = ${memory}
    AND laptop.release_year = ${release_year}
    `
    

    // Check if numItems or offset has been provided, if it has then run query to provide limit and offset
    if(numOfItems !== undefined && offset !== undefined ){
        sql += "ORDER BY laptop.id LIMIT " + numOfItems + " OFFSET " + offset;
    }

    // Run query
    return executeSQLQuery(querySql);
}


/**
 * Searches for laptops based on a specific description.
 * @param {string} descrip - The description to search for.
 * @param {number} numOfItems - Number of items to retrieve.
 * @param {number} offset - Offset for results.
 * @returns {Promise<any>} Promise that resolves to the query result.
 */

module.exports.searchLaptop = async (descrip, numOfItems, offset) => {
    let querySql = `
        SELECT laptop.id, laptop.brand, laptop.model_name, laptop.description, 
        laptop.release_year, laptop.image_url, comparison.price
        FROM laptop
        LEFT JOIN laptop_variations ON laptop.id = laptop_variations.laptop_id
        LEFT JOIN comparison ON laptop_variations.id = comparison.laptop_variation_id
        WHERE laptop.description LIKE "%${descrip}%"
    `;

    if (numOfItems !== undefined && offset !== undefined) {
        querySql += ` ORDER BY laptop.id LIMIT ${numOfItems} OFFSET ${offset}`;
    }

    try {
        const result = await executeSQLQuery(querySql);
        return result;
    } catch (error) {
        throw error;
    }
};


/**
 * Retrieves a list of laptops.
 * @param {number} numOfItems - Number of items to retrieve.
 * @param {number} offset - Offset for results.
 * @returns {Promise<any>} Promise that resolves to the query result.
 */
module.exports.getLaptops = async (numOfItems, offset) => {
    let querySql = "SELECT * FROM laptop";

    if (numOfItems !== undefined && offset !== undefined) {
        querySql += ` ORDER BY laptop.id LIMIT ${numOfItems} OFFSET ${offset}`;

        try {
            const result = await executeSQLQuery(querySql);
            return result;
        } catch (error) {
            throw error;
        }
    } else {
        try {
            const result = await executeSQLQuery(querySql);
            return result;
        } catch (error) {
            throw error;
        }
    }
};


/**
 * Retrieves a list of comparisons.
 * @param {number} numOfItems - Number of items to retrieve.
 * @param {number} offset - Offset for results.
 * @returns {Promise<any>} Promise that resolves to the query result.
 */
module.exports.getComparisons = async (numOfItems, offset) => {
    try {
        let querySql = `SELECT laptop.id, laptop.model_name, laptop.brand, laptop_variations.display, laptop_variations.processor, laptop_variations.storage, laptop_variations.memory, laptop_variations.color, comparison.price, comparison.store_name, comparison.price_url 
        FROM ((comparison 
        INNER JOIN laptop_variations ON comparison.laptop_variation_id = laptop_variations.id) 
        INNER JOIN laptop ON laptop_variations.laptop_id = laptop.id)`

        // Check if numItems or offset has been provided to apply LIMIT and OFFSET
        if (numOfItems !== undefined && offset !== undefined) {
            querySql += ` ORDER BY comparison.id LIMIT ${numOfItems} OFFSET ${offset}`;
        }

        // Run query
        const results = await executeSQLQuery(querySql);
        return results;
    } catch (error) {
        throw new Error(`Error in getComparisons: ${error.message}`);
    }
};


/**
 * Retrieves a specific comparison for a laptop.
 * @param {number} laptopId - The ID of the laptop.
 * @returns {Promise<any>} Promise that resolves to the query result.
 */
module.exports.getComparison = async (laptopId) => {
    try {
        const querySql = `
        SELECT laptop.id, laptop.model_name, laptop.brand, laptop.release_year, laptop.image_url,laptop.description, laptop_variations.display, laptop_variations.processor, laptop_variations.storage, laptop_variations.memory, laptop_variations.color, comparison.price, comparison.store_name, comparison.price_url 
        FROM ((comparison 
        INNER JOIN laptop_variations ON comparison.laptop_variation_id = laptop_variations.id) 
        INNER JOIN laptop ON laptop_variations.laptop_id = laptop.id) 
        WHERE laptop.id = ${laptopId};
        `;

        const result = await executeSQLQuery(querySql);
        return result;
    } catch (error) {
        throw new Error(`Error in getComparison: ${error.message}`);
    }
};


/**
 * Retrieves details of a specific laptop based on ID.
 * @param {number} laptopId - The ID of the laptop.
 * @returns {Promise<any>} Promise that resolves to the query result.
 */
module.exports.getLaptop = (laptopId) => {
    const querySql = "SELECT * FROM laptop WHERE id=" + laptopId;
    return executeSQLQuery(querySql);
}


/**
 * Executes an SQL query on the database.
 * @param {string} sql - The SQL query to be executed.
 * @returns {Promise<any>} Promise that resolves to the query result.
 */
async function executeSQLQuery(sql) {
    try {
        const result = await new Promise((resolve, reject) => {
            connectionPool.query(sql, (err, result) => {
                if (err) {
                    reject(err);
                }
                resolve(result);
            });
        });
        return result;
    } catch (error) {
        throw error;
    }
}
