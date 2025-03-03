const $grid = document.querySelector('[data-product-category]')
const category = $grid.dataset.productCategory

const fetchSuggestedProducts = async () => {
    const response = await fetch(`/api/products?search=${category}?limit=20`)
    const products0 = await response.json()
    const products = products0.data.slice(0, 4)

    const productsHTML = []

    for (const product of products) {
        const encodedName = encodeURIComponent(product.display_name)
        const encodedPrice = encodeURIComponent(product.price.total)
        const encodedSrc = encodeURIComponent(product.thumbnail)
        await fetch(`/products/custom?id=${product._id}&name=${encodedName}&price=${encodedPrice}&src=${encodedSrc}`)
            .then(response => response.text())
            .then(productHTML => productsHTML.push(productHTML))
    }

    return productsHTML.join('')
}

fetchSuggestedProducts().then(productsHTML => {
    $grid.innerHTML = productsHTML
})