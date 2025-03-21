import { fetchData } from "./services/fetchService.js"
import { createHTMLProduct } from "./services/uiService.js"
import levensteinDistance from "./utils/levensteinAlgorithm.js"

const $grid = document.querySelector('[data-product-name]')
// regex substring
const keywords = $grid.dataset.productKeywords.match(/\[([^\]]+)\]/)[1].split(',').filter(kw => !kw.match(/\d+[gl]/i))

const fetchSuggestedProducts = async () => {
    const res = await fetchData(`/products?keywords=${keywords}&limit=10&page=0`, "GET", {
        cacheData: false
    })

    let products = res.data.page
    products = products.filter(product => product.normalized_name !== $grid.dataset.productName)
    const prodScore = {}
    products.forEach(product => prodScore[`${product.product_id}@${product.supermarket_name}`] = levensteinDistance($grid.dataset.productName, product.normalized_name))
    products.sort((a, b) => prodScore[`${a.product_id}@${a.supermarket_name}`] - prodScore[`${b.product_id}@${b.supermarket_name}`])
    products = products.slice(0, 4)

    const productsHTML = []

    for (const product of products) {
        productsHTML.push(createHTMLProduct(product))
    }

    return productsHTML.join('')
}

fetchSuggestedProducts().then(productsHTML => {
    $grid.innerHTML = productsHTML
})


