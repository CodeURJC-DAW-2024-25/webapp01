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
    <div class="product-card">
        <a href="/products/${product._id}" class="product-link">
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