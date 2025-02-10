document.addEventListener('DOMContentLoaded', function () {
    const searchInput = document.querySelector('.search-bar input');
    const gridLayout = document.querySelector('.grid-layout');

    const products = [
        { title: 'Leche', description: 'Template para el producto 1', price: '100€', image: 'https://img.freepik.com/vector-premium/vector-diseno-logotipo-minimalista-creativo-elegante-cualquier-empresa-negocios-marca_1287271-9313.jpg?semt=ais_hybrid' },
        { title: 'Leche condensada', description: 'Template para el producto 2', price: '150€', image: 'https://img.freepik.com/vector-premium/vector-diseno-logotipo-minimalista-creativo-elegante-cualquier-empresa-negocios-marca_1287271-9313.jpg?semt=ais_hybrid' },
        { title: 'Manteca', description: 'Template para el producto 3', price: '120€', image: 'https://img.freepik.com/vector-premium/vector-diseno-logotipo-minimalista-creativo-elegante-cualquier-empresa-negocios-marca_1287271-9313.jpg?semt=ais_hybrid' },
        { title: 'Chocolate', description: 'Template para el producto 4', price: '200€', image: 'https://img.freepik.com/vector-premium/vector-diseno-logotipo-minimalista-creativo-elegante-cualquier-empresa-negocios-marca_1287271-9313.jpg?semt=ais_hybrid' },
    ];

    
    function renderProducts(items) {
        const prevScrollY = window.scrollY; 

        gridLayout.innerHTML = '';

        if (items.length === 0) {
            gridLayout.innerHTML = '<p class="loader">No se encontraron resultados.</p>';
        } else {
            items.forEach(product => {
                const gridItem = document.createElement('div');
                gridItem.className = 'grid-item';
                gridItem.innerHTML = `
                    <div class="image-container">
                        <img src="${product.image}" alt="${product.title}">
                    </div>
                    <div class="info-container">
                        <h1 class="product-title">${product.title}</h1>
                        <p class="product-description">${product.description}</p>
                        <p class="product-price">${product.price}</p>
                        <button class="clickable clickable-primary">Más Detalles</button>
                    </div>
                `;
                gridLayout.appendChild(gridItem);
            });
        }

        
        window.scrollTo(0, prevScrollY);
    }

    
    renderProducts(products);

    
    searchInput.addEventListener('input', function (e) {
        const query = e.target.value.toLowerCase();

        
        gridLayout.innerHTML = '<p class="loader">Cargando...</p>';

        
        setTimeout(() => {
            
            const filteredProducts = products.filter(product =>
                product.title.toLowerCase().includes(query)
            );

            
            renderProducts(filteredProducts);
        }, 1000);
    });
});
