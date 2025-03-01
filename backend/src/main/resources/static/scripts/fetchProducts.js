const $productsContainer = document.querySelector(".products-container");

const $previousButton = document.querySelector("#previous-button");
const $pageNumber = document.querySelector("#page-number");
const $nextButton = document.querySelector("#next-button");

const CSRF_TOKEN = document.querySelector('meta[name="_csrf"]').content;
const CSRF_HEADER = document.querySelector('meta[name="_csrf_header"]').content;

const PRODUCTS_SIZE = 24;

let currentPage = 0;
let loading = false;
let isEnd = false;
let totalPages = Infinity;
let searchQuery = "";

// Retrieve query parameters from the URL
let queryParams = null;
document.addEventListener("DOMContentLoaded", () => {
    queryParams = new URLSearchParams(window.location.search);
    searchQuery = queryParams.get("searchInput") || "";

    // Load products when the page is loaded
    loadProducts();
});

// Add event listener
$previousButton.addEventListener("click", () => loadProducts(currentPage - 1));
$nextButton.addEventListener("click", () => loadProducts(currentPage + 1));

async function loadProducts(page = 0) {
    if (loading || isEnd) return;
    if (page < 0) return;

    loading = true;

    // Fetch for comments from the server
    const response = await fetch(`/api/products?page=${page}&limit=${PRODUCTS_SIZE}&search=${searchQuery}`, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            [CSRF_HEADER]: CSRF_TOKEN
        }
    })

    if (!response.ok) throw new Error("Failed to load products");

    // Parse the response
    const data = await response.json();

    // Update control variables
    currentPage = data.current_page;
    isEnd = data.is_last_page;
    totalPages = data.total_pages;
    loading = false;

    // Create HTML for each product
    const productsHTML = data.data.map(createHTMLProduct).join("");
    $productsContainer.innerHTML = productsHTML

    // Update page number
    $pageNumber.textContent = `Página ${currentPage + 1} de ${totalPages}`;
}

function createHTMLProduct(product) {
    return `
        <div class="grid-item">
            <h1>${product._id}</h1>
            <div class="image-container">
                <img src="${product.thumbnail}" alt="${product.display_name}">
            </div>
            <div class="info-container">
                <h1 class="product-title">${product.display_name}</h1>
                <p class="product-description">${product.product_type}</p>
                <p class="product-price">${product.price.total}&euro;</p>
                <a class="clickable clickable-primary" href="products/${product._id}">
                    Ver Producto
                </a>
            </div>
        </div>
    `;
}