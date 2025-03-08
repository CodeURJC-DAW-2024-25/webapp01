import { fetchData } from "./services/fetchService.js"
import { createHTMLProduct } from "./services/uiService.js"

const $grid = document.querySelector('[data-product-category]')
const category = $grid.dataset.productCategory

const fetchSuggestedProducts = async () => {
    const res = await fetchData(`/products?search=${category}&limit=4&page=0`, "GET", {
        cacheData: false
    })
    
    const products = res.data
    console.log(products)
    const productsHTML = []

    for (const product of products) {
        productsHTML.push(createHTMLProduct(product))
    }

    return productsHTML.join('')
}

fetchSuggestedProducts().then(productsHTML => {
    $grid.innerHTML = productsHTML
})