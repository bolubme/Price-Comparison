let app = new Vue({
  el: "#app",
  data: {
    laptops: [],
    siteheader: "After School Club",
    showHeader: true,
    showProduct: false,
    showComparison: false,
    laptopDetails: [],
    comparison: [],
    comparisonList: [],
    searchText: "",
    currentPageNum: 0,
    numOfItemsPerPage: 4,
    numOfLaptops: 0,
  },
  methods: {
    searchByBrand(brand) {
      this.searchText = brand; 
      this.performSearch(); 
    },
    showProductPage() {
      this.showHeader = false;
      this.showProduct = true;
      this.showComparison = false;
    },

    showHomePage() {
      this.showHeader = true;
      this.showProduct = false;
      this.showComparison = false;

      this.searchText = "";
      this.currentPageNum = 0;
    },

    showComparisonPage() {
      this.showHeader = false;
      this.showProduct = false;
      this.showComparison = true;
    },

    compareLaptops: async function (id) {
      const appInstance = this;
      this.showComparisonPage();

      this.laptopDetails = (await axios.get("/comparison/" + id)).data;
      this.comparison = (await axios.get("/comparison/" + id)).data;
      let modelName = this.comparison[0].model_name;
      let storage = this.comparison[0].storage;
      let memory = this.comparison[0].memory;
      let display = this.comparison[0].display;
      let processor = this.comparison[0].processor;
      let release_year = this.comparison[0].release_year;
      let searchUrl = `/comparisonSearch?model_name=${modelName}&storage=${storage}&memory=${memory}&processor=${processor}&display=${display}&release_year=${release_year}`;
      console.log(searchUrl)
      let result = await axios.get(searchUrl);
      appInstance.comparisonList = result.data.data;
    },

    // Get all the Laptops from the web service using Axios
    // Inside the Vue app's methods:

    performSearch: async function () {
      try {
        const appInstance = this;
        const offset =
          appInstance.currentPageNum * appInstance.numOfItemsPerPage;
        const searchUrl = `/search?model_name=${appInstance.searchText}&num_items=${appInstance.numOfItemsPerPage}&offset=${offset}`;


        // Store current search parameters for later use
        const currentSearchText = appInstance.searchText;
        const currentPage = appInstance.currentPageNum;

        this.showProductPage();
        this.searchText = "";

        let searchResult = await axios.get(searchUrl);

        appInstance.laptops = searchResult.data.data;
        appInstance.numOfLaptops = searchResult.data.count;

        // Restore search parameters after updating the results
        appInstance.searchText = currentSearchText;
        appInstance.currentPageNum = currentPage;
      } catch (ex) {
        console.error(ex);
      }
    },

    nextPage() {
      this.currentPageNum++;
      console.log(this.currentPageNum);

      this.performSearch();

      if (this.laptops.length === 0) {
        console.log("NO MORE PAGES");
      }
    },

    previousPage() {
      this.currentPageNum--;
      console.log(this.currentPageNum);

      this.performSearch();

      if (this.currentPageNum < 1) {
      }
    },

    goBack(event) {
      event.preventDefault();

      if (this.showComparison) {
        this.showProductPage();
      }
    },
  },
  computed: {},
}).$mount("#app");
