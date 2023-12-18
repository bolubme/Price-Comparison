Price Comparison Website

Introduction
In today's digital era, the abundance of laptop options across retailers overwhelms consumers, hindering informed decisions. To simplify this process, I've developed a concise laptop price comparison website. Scraping data from Amazon, Ebuyer, Laptops Direct, Box, and StockMustGo. I focused on Apple, HP, and Lenovo laptops.
The website's core features are:
Homepage: Featuring highlighted laptops and current deals, along with a user-friendly search function.
Product Listings: A clear display of searched products.
Price Comparison: Real-time comparison of the same laptop across the five retailers

Project Description
Technologies Used
Spring: Employed for dependency injection and managing dependencies within the project.
Maven: Facilitated dependency management for the project.
Selenium: Utilized for web scraping data from Amazon, Ebuyer, LaptopsDirect, Box, and StockMustGo.
Hibernate: Employed to store scraped laptop data efficiently into a MySQL database.
Node.js: Leveraged with RESTful APIs for retrieving laptop data stored in the MySQL database.
Ajax: Integrated the backend with the frontend website for seamless data communication.
Vue.js: Implemented most of the frontend functionalities, providing an interactive user interface.

The scraping process involved extracting data from five different website utilizing threads to retrieve data asynchronously. By doing this I was able to accumulate over 500 laptops.
The collected data was structured and stored efficiently in a MySQL database using Hibernate. The database architecture employed normalization and was structured to store information on laptop details, Laptop variations and Comparison details. Node.js was employed to create a backend server that communicates with the MySQL database through RESTful APIs. This helped to retrieve data seamlessly for the frontend, where I implemented Vue.js.

![Alt text](<Screen Shot 2023-12-13 at 3.28.37 AM.png>) ![Alt text](<Screen Shot 2023-12-13 at 3.28.28 AM.png>) ![Alt text](<Screen Shot 2023-12-13 at 3.28.45 AM.png>) ![Alt text](<Screen Shot 2023-12-13 at 3.28.16 AM.png>)
