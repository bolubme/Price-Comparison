/**
 * Importing necessary modules and setting up testing environment
 */
const app = require("../server");
const db = require("../client");

const chai = require("chai");
const { expect } = chai;
const should = chai.should();

const chaiHttp = require("chai-http");
chai.use(chaiHttp);

function logToTerminal(message) {
  if (process.env.NODE_ENV !== "test") {
    return;
  }
  console.log(`[Test Log]: ${message}`);
}


// Testing API Endpoints
describe("API Endpoints Test", () => {
  // Test GET / endpoint
  describe('/GET home page', () => {
    it('should return the home page', (done) => {
      chai.request(app)
        .get('/')
        .end((err, res) => {
          expect(res).to.have.status(200);
          done();
        });
    });
  });

  // Test GET /search endpoint
  describe('/GET search', () => {
    it('should search for laptops', (done) => {
      chai.request(app)
        .get('/search?model_name=apple')
        .end((err, res) => {
          expect(res).to.have.status(200);
          expect(res.body).to.be.an('object');
          expect(res.body).to.have.property('count');
          expect(res.body).to.have.property('data');
          done();
        });
    });
  });

// Test GET /comparison by id end point 
  describe('/GET comparison by ID', () => {
    it('should get comparison data for a valid ID', (done) => {
      chai.request(app)
        .get('/comparison/12')
        .end((err, res) => {
          expect(res).to.have.status(200);
          expect(res.body).to.be.an('array');
          done();
        });
    });
  
    it('should return a 404 for an invalid ID', (done) => {
      chai.request(app)
        .get('/comparison/wer') 
        .end((err, res) => {
          expect(res).to.have.status(404);
          done();
        });
    });

  });

  // Test GET /comparisonSearch endpoint
  describe('/GET comparisonSearch', () => {
    it('should search for laptops based on provided parameters', (done) => {
      chai.request(app)
        .get('/comparisonSearch?model_name=MacBook Air&storage=256&memory=8&processor=Apple M2&display=15.3 inch&release_year=2023')
        .end((err, res) => {
          expect(res).to.have.status(200);
          expect(res.body).to.be.an('object');
          expect(res.body).to.have.property('count');
          expect(res.body).to.have.property('data');
          done();
        });
    });
  });

});


// Testing Database Functions
describe("Database Functions", () => {
  // Test function to get total product count
  describe("getTotalProductCount", () => {
    it("should return total count of products as a number greater than 500", async () => {
      const count = await db.getTotalProductCount();
      expect(count).to.be.a("number");
      expect(count).to.be.greaterThan(500);
    });
  });


  // Test function to get laptops from  db 
  describe("getLaptops", () => {
    it("should retrieve all laptops as an object ", async () => {
      const laptops = await db.getLaptops();
      expect(laptops).to.be.an("array");
      expect(laptops.length).to.be.greaterThan(0);
      laptops.forEach((laptop) => {
        expect(laptop).to.have.property("id");
        expect(laptop).to.have.property("brand");
        expect(laptop).to.have.property("model_name");
        expect(laptop).to.have.property("release_year");
        expect(laptop).to.have.property("description");
        expect(laptop).to.have.property("image_url");
      });
    });

    it("should retrieve a specific number of laptops with offset", async () => {
      const numOfItems = 5;
      const offset = 0;
      const laptops = await db.getLaptops(numOfItems, offset);
      expect(laptops).to.be.an("array");
      expect(laptops.length).to.equal(numOfItems);
    });
  });
});

