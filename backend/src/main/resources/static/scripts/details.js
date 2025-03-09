import { fetchData } from "./services/fetchService.js"
import comparationAlgorithm from "./utils/comparationAlgorithm.js"

const $grid = document.querySelector('[data-product-name]')
const keywordsUnparsed = $grid.dataset.productKeywords.match(/\[([\s\S]+)\]/)[1]
const keywords = keywordsUnparsed.split(',').map(kw => kw.trim())
const brand = $grid.dataset.productBrand


document.addEventListener("DOMContentLoaded", function () {
    const compareBtn = document.getElementById("compareBtn")
    const compareContainer = document.getElementById("compareContainer")

    compareBtn.addEventListener("click", function (e) {
        e.preventDefault()

        const productName = document.getElementById("productTitle")?.textContent.trim()
        const searchQuery = productName || localStorage.getItem("originalSearchQuery") || ""

        const url = `/compare?searchInput=${encodeURIComponent(searchQuery)}`
        console.log("Fetching comparison from URL:", url)

        compareContainer.innerHTML = "<p>Cargando resultados...</p>"
        compareContainer.style.display = "block"
        compareBtn.innerHTML = "<i class='bi bi-arrow-repeat spin'></i>"

        fetchData(`/products?keywords=${keywordsUnparsed}&limit=1000&page=0`, "GET", {
            cacheData: false
        })
            .then(res => {
                let products = res.data
                const mainProduct = { productName, keywords, brand, }
                const bestBySupermarket = comparationAlgorithm(mainProduct, products)

                fetch("/get-compare-table?products=" + Object.values(bestBySupermarket).map(p => `${p.supermarket_name}@${p.display_name}@${p.price.total}@${p.product_url}`).join("_"))
                    .then(response => response.text())
                    .then(html => {
                        compareContainer.innerHTML = html
                        highlightBestAndWorstPrices()
                        // Add link to product
                        document.querySelectorAll("[data-product-url]").forEach(a => a.addEventListener("click", () => window.open(a.dataset.productUrl, "_blank")))
                    })
            })

    })
})

/**
 * Highlights the cells with the best and worst prices in the comparison table.
 */
function highlightBestAndWorstPrices() {
    const priceCells = document.querySelectorAll("#compareContainer table tbody tr td:nth-child(3)")

    let minPrice = Infinity
    let maxPrice = -Infinity
    let bestCells = []
    let worstCells = []

    priceCells.forEach(cell => {
        const priceText = cell.textContent.trim()
        const price = parseFloat(priceText.replace("€", "").replace(",", "."))

        if (!isNaN(price)) {
            // Update best price
            if (price < minPrice) {
                minPrice = price
                bestCells = [cell]
            } else if (price === minPrice) {
                bestCells.push(cell)
            }

            // Update worst price
            if (price > maxPrice) {
                maxPrice = price
                worstCells = [cell]
            } else if (price === maxPrice) {
                worstCells.push(cell)
            }
        }
    })

    // Apply styles
    bestCells.forEach(cell => {
        cell.classList.add("best-price")
    })

    worstCells.forEach(cell => {
        cell.classList.add("worst-price")
    })
}
