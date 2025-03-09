import { fetchData } from "./services/fetchService.js";
import { createHTMLProduct } from "./services/uiService.js";
import levensteinDistance from "./utils/levensteinAlgorithm.js";

const $productsContainer = document.querySelector(".products-container");
const $previousButton = document.querySelector("#previous-button");
const $pageNumber = document.querySelector("#page-number");
const $nextButton = document.querySelector("#next-button");
const $applyFiltersButton = document.querySelector("#apply-filters");

const $minPriceInput = document.querySelector("#minPrice");
const $maxPriceInput = document.querySelector("#maxPrice");
const $supermarketRadios = document.querySelectorAll("input[name='supermarket']")

const PRODUCTS_SIZE = 24;

let currentPage = 0;
let loading = false;
let isEnd = false;
let totalPages = Infinity;
let searchQuery = "";


// Retrieve URL parameters
let queryParams = null;
document.addEventListener("DOMContentLoaded", () => {
    queryParams = new URLSearchParams(window.location.search);
    searchQuery = queryParams.get("searchInput") || "";

    // Save the original search in localStorage to have it on other pages
    if (searchQuery) {
        localStorage.setItem("originalSearchQuery", searchQuery);
    } else {
        searchQuery = localStorage.getItem("originalSearchQuery") || "";
    }
    // Load filters from URL
    const supermarket = queryParams.get("supermarket") || "";
    const minPrice = queryParams.get("minPrice") || "";
    const maxPrice = queryParams.get("maxPrice") || "";

    // Store filters globally
    window.filterOptions = { supermarket, minPrice, maxPrice };

    // Set filters in the form
    $minPriceInput.value = minPrice;
    $maxPriceInput.value = maxPrice;
    $supermarketRadios.forEach(radio => {
        if (radio.value === supermarket) radio.checked = true;
    });

    // Load products when the page loads
    loadProducts();
});

// Events for pagination
$previousButton.addEventListener("click", () => loadProducts(currentPage - 1));
$nextButton.addEventListener("click", () => loadProducts(currentPage + 1));
$applyFiltersButton.addEventListener("click", () => applyFilters(currentPage));

async function loadProducts(page = 0) {
    if (loading) return;
    if (page < 0) return;

    loading = true;

    // Format the URL with the search query and filters
    const { supermarket, minPrice, maxPrice } = window.filterOptions || {};
    let endpoint = `/products?page=${page}&limit=${PRODUCTS_SIZE}&search=${encodeURIComponent(searchQuery)}`;
    if (supermarket) endpoint += `&supermarket=${encodeURIComponent(supermarket)}`;
    if (minPrice) endpoint += `&minPrice=${encodeURIComponent(minPrice)}`;
    if (maxPrice) endpoint += `&maxPrice=${encodeURIComponent(maxPrice)}`;
    
    const data = await fetchData(endpoint, "GET");

    currentPage = data.current_page;
    totalPages = data.total_pages < 1 ? 1 : data.total_pages + 1;
    isEnd = currentPage >= totalPages - 1;

    // Update disabled state of buttons
    $previousButton.disabled = currentPage === 0;
    $nextButton.disabled = isEnd;

    const productsHTML = data.data.map(product => createHTMLProduct(product)).join("");
    $productsContainer.innerHTML = productsHTML;
    $pageNumber.textContent = `Page ${currentPage + 1} of ${totalPages}`;

    loading = false;
}

async function applyFilters(page = 0) {
    // Retrieve filters from the form
    const minPrice = $minPriceInput.value;
    const maxPrice = $maxPriceInput.value;
    const supermarket = document.querySelector("input[name='supermarket']:checked")?.value || "";

    // Store filters globally
    window.filterOptions = { supermarket, minPrice, maxPrice };

    // Retrieve new products with the new filters
    currentPage = 0;
    isEnd = false;
    loadProducts(page);
}