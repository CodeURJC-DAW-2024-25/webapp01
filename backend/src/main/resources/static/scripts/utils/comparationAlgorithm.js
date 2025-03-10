import levensteinDistance from "./levensteinAlgorithm.js";

// Adjust this threshold based on tests
export default function comparationAlgorithm(mainProduct, candidateProducts) {
    // Score
    const productsScore = {}

    // Calculate score for each product by ()
    for (const product of candidateProducts) {
        const productBrandFallback = product.brand || product.normalized_name.includes(mainProduct.brand) ? mainProduct.brand : ""
        const pid = `${product.product_id}@${product.supermarket_name}`
        let brandScore = levensteinDistance(mainProduct.brand, productBrandFallback) / mainProduct.brand.length
        let nameScore = levensteinDistance(mainProduct.productName, product.normalized_name) / mainProduct.productName.length
        let keywordsScore = keywordsDistance(mainProduct.keywords, product.keywords) / mainProduct.keywords.length

        // Calculate final score with weights
        productsScore[pid] = keywordsScore ** 4 + nameScore + brandScore
        if (keywordsScore > .8 || nameScore > .8 || brandScore > .8) productsScore[pid] = Infinity
    }

    // Get the best product by score
    candidateProducts.sort((a, b) => productsScore[`${a.product_id}@${a.supermarket_name}`] - productsScore[`${b.product_id}@${b.supermarket_name}`])

    const bestBySupermarket = {}
    candidateProducts.forEach(product => {
        const score = productsScore[`${product.product_id}@${product.supermarket_name}`]
        if (!bestBySupermarket[product.supermarket_name] && score < Infinity) {
            bestBySupermarket[product.supermarket_name] = product
        }
    })

    return bestBySupermarket
}

export function keywordsDistance(k1, k2) {
    let missing = 0
    for (const keyword of k1) {
        if (!k2.join(",").includes(keyword)) {
            missing++
        }
    }

    return missing
}