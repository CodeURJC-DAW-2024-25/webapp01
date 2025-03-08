import { fetchData } from "./services/fetchService.js"
import { createHTMLProduct } from "./services/uiService.js"

const $grid = document.querySelector('[data-product-name]')
// regex substring
const keywords = $grid.dataset.productKeywords.match(/\[([^\]]+)\]/)[1].split(',')

const fetchSuggestedProducts = async () => {
    const res = await fetchData(`/products?keywords=${keywords}&limit=4&page=0`, "GET", {
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