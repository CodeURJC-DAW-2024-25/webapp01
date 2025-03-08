import { fetchData } from "./services/fetchService.js"
import { createHTMLProduct } from "./services/uiService.js"

const $grid = document.querySelector('[data-product-name]')
// regex substring
const keywords = $grid.dataset.productKeywords.match(/\[([^\]]+)\]/)[1].split(',').filter(kw => !kw.match(/\d+[gl]/i))

const fetchSuggestedProducts = async () => {
    const res = await fetchData(`/products?keywords=${keywords}&limit=10&page=0`, "GET", {
        cacheData: false
    })
    
    let products = res.data
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


function levensteinDistance(a, b) {
    const distance = new Array(a.length + 1).fill(null).map(() => new Array(b.length + 1).fill(null))

    for (let i = 0; i <= a.length; i++) {
        distance[i][0] = i
    }

    for (let j = 0; j <= b.length; j++) {
        distance[0][j] = j
    }

    for (let i = 1; i <= a.length; i++) {
        for (let j = 1; j <= b.length; j++) {
            const cost = a[i - 1] === b[j - 1] ? 0 : 1

            distance[i][j] = Math.min(
                distance[i - 1][j] + 1,
                distance[i][j - 1] + 1,
                distance[i - 1][j - 1] + cost
            )
        }
    }

    return distance[a.length][b.length]
}