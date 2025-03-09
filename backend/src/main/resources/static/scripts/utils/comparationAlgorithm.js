import levensteinDistance from "./levensteinAlgorithm.js";

export default function comparationAlgorithm(mainProduct, candidateProducts) {
    // Score
    const productsScore = {}

    // Calculate score for each product by ()
    for (const product of candidateProducts) {
        const pid = `${product.product_id}@${product.supermarket_name}`
        let brandScore = levensteinDistance(mainProduct.brand, product.brand) / mainProduct.brand.length
        let nameScore = levensteinDistance(mainProduct.productName, product.normalized_name) / mainProduct.productName.length
        let keywordsScore = keywordsDistance(mainProduct.keywords, product.keywords) / mainProduct.keywords.length

        // Calculate final score (the `*N` adds a negative weight) 
        productsScore[pid] = keywordsScore
        if (keywordsScore > .5) productsScore[pid] += nameScore  // Only add nameScore if the keywords are not close enough
        if (nameScore > .5 ) productsScore[pid] += brandScore    // Only add brandScore if the name is not close enough
        

    }

    // Get the best product by score
    candidateProducts.sort((a, b) => productsScore[`${a.product_id}@${a.supermarket_name}`] - productsScore[`${b.product_id}@${b.supermarket_name}`])

    const bestBySupermarket = {}
    candidateProducts.forEach(product => {
        if (!bestBySupermarket[product.supermarket_name] && productsScore[`${product.product_id}@${product.supermarket_name}`] < Infinity) {
            bestBySupermarket[product.supermarket_name] = product
            console.log(`New best product for ${product.supermarket_name}: ${product.normalized_name}`)
            console.log(` - ProductScore: ${productsScore[`${product.product_id}@${product.supermarket_name}`]}, keywords: ${product.keywords}`)
        } 
    })


    console.log("Products:", candidateProducts, bestBySupermarket);
    return bestBySupermarket
}


function keywordsDistance(k1, k2) {
    let missing = 0
    for (const keyword of k1) {
        if (keyword.match(/\d+(kg|g|l|cl|ml|unidades|ud|unit|units)/i)) continue  // Skip units
        if (!k2.includes(keyword)) {
            missing++
        }
    }

    return missing
}