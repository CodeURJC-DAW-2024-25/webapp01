
export function createHTMLProduct(product) {
    const thumbnail = product.thumbnail || "/assets/template_image.png";
    const searchQueryParam = encodeURIComponent(localStorage.getItem("originalSearchQuery") || searchQuery);
    return `
    <div class="product-card">
        <a href="/products/${product._id}?searchInput=${searchQueryParam}" class="product-link">
            <div class="product-image-container">
                <div class="supermarket-badge">${product.supermarket_name}</div>
                <div class="product-image">
                    <img src="${thumbnail}" alt="${product.display_name}" />
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
                        ${
                            (product.price.per_reference_unit && product.price.reference_unit_name) ? 
                            `<p class="product-unit-price">${product.price.per_reference_unit} €/${product.price.reference_unit_name}</p>` : 
                            `<p class="product-unit-price">Sin datos</p>`

                        }
                    </div>
                    <i class="icon bi bi-arrows-angle-expand"></i>
                </div>
            </div>
        </a>
    </div>
    `;
}

export function createHTMLPost(post) {
    return `
    <a class="post extended" href="/posts/${post.id}">
        <i class="bi bi-box-arrow-up-right icon"></i>
        <div class="post-top">
            <h2 class="post-title">${post.title}</h2>
            <p class="post-description">Descubre cómo puedes ahorrar dinero en tus compras de supermercado con SaveX.</p>
            <div class="post-metadata">
                <div class="tags-container">
                    ${post.tags.map(tag => `<span class="tag">${tag}</span>`).join('')}
                </div>
            </div>
        </div>
        <div class="post-bottom">
            <div class="time-container">
                <span class="date">${post.date}</span>
                <span class="separator">•</span>
                <span class="read-time">${post.readingTime}</span>
            </div>
            <div class="img-container">
                <img src="/api/posts/${post.id}/banner" alt="${post.title}">
            </div>
        </div>
    </a>
    `;
}