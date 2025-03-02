const $productsContainer = document.querySelector(".products-container");
const $previousButton = document.querySelector("#previous-button");
const $pageNumber = document.querySelector("#page-number");
const $nextButton = document.querySelector("#next-button");

const CSRF_TOKEN = document.querySelector('meta[name="_csrf"]').content;
const CSRF_HEADER = document.querySelector('meta[name="_csrf_header"]').content;

const PRODUCTS_SIZE = 16;

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
    // Load filters from URL
    const supermarket = queryParams.get("supermarket") || "";
    const minPrice = queryParams.get("minPrice") || "";
    const maxPrice = queryParams.get("maxPrice") || "";
    const productType = queryParams.get("productType") || "";
    // Store filters globally
    window.filterOptions = { supermarket, minPrice, maxPrice, productType };
    // Load products when the page loads
    loadProducts();
});

// Events for pagination
$previousButton.addEventListener("click", () => loadProducts(currentPage - 1));
$nextButton.addEventListener("click", () => loadProducts(currentPage + 1));

async function loadProducts(page = 0) {
    if (loading || isEnd) return;
    if (page < 0) return;

    loading = true;
    const { supermarket, minPrice, maxPrice, productType } = window.filterOptions || {};
    let url = `/api/products?page=${page}&limit=${PRODUCTS_SIZE}&search=${encodeURIComponent(searchQuery)}`;
    if (supermarket) url += `&supermarket=${encodeURIComponent(supermarket)}`;
    if (minPrice) url += `&minprice=${encodeURIComponent(minPrice)}`;
    if (maxPrice) url += `&maxprice=${encodeURIComponent(maxPrice)}`;
    if (productType) url += `&type=${encodeURIComponent(productType)}`;

    console.log("Fetching products from URL:", url);
    
    const response = await fetch(url, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            [CSRF_HEADER]: CSRF_TOKEN
        }
    });
    
    if (!response.ok) throw new Error("Failed to load products");

    const data = await response.json();
    currentPage = data.current_page;
    isEnd = data.is_last_page;
    totalPages = data.total_pages;
    loading = false;

    const productsHTML = data.data.map(createHTMLProduct).join("");
    $productsContainer.innerHTML = productsHTML;
    $pageNumber.textContent = `Page ${currentPage + 1} of ${totalPages}`;
}

function createHTMLProduct(product) {
    return `
    <div class="product-card">
        <a href="/products/${product._id}?searchInput=${encodeURIComponent(searchQuery)}" class="product-link">
            <div class="product-image-container">
                <div class="supermarket-badge">${product.supermarket_name}</div>
                <div class="product-image">
                    <img src="${product.thumbnail}" alt="${product.display_name}" />
                </div>
            </div>
            <div class="product-info">
                <div class="product-meta">
                    <span class="product-brand">${product.brand || '?'}</span>
                    <span class="product-category">• ${product.product_categories ? product.product_categories[0] : ''}</span>
                </div>
                <h3 class="product-name">${product.display_name}</h3>
                <div class="product-price-container">
                    <div class="price-info">
                        <p class="product-price">${product.price.total} €</p>
                        <p class="product-unit-price">${product.price.per_reference_unit} €/${product.price.reference_unit_name}</p>
                    </div>
                    <button class="action-button">
                        <i class="bi bi-plus"></i>
                    </button>
                </div>
            </div>
        </a>
    </div>
    `;
}
