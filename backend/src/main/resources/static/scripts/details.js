document.addEventListener("DOMContentLoaded", function() {
    const compareBtn = document.getElementById("compareBtn");
    const compareContainer = document.getElementById("compareContainer");
    
    compareBtn.addEventListener("click", function(e) {
        e.preventDefault();
    
        const productName = document.getElementById("productTitle")?.textContent.trim();
        const searchQuery = productName || localStorage.getItem("originalSearchQuery") || "";
    
        const url = `/compare?searchInput=${encodeURIComponent(searchQuery)}`;
        console.log("Fetching comparison from URL:", url);
    
        compareContainer.innerHTML = "<p>Loading comparison...</p>";
        compareContainer.style.display = "block";
        compareBtn.innerHTML = "<i class='bi bi-arrow-repeat spin'></i>";
    
        fetch(url, { method: "GET", headers: { "Content-Type": "text/html" } })
            .then(response => response.text())
            .then(html => {
                compareContainer.innerHTML = html;
                highlightBestAndWorstPrices();
                compareBtn.disabled = true;
                compareBtn.innerHTML = "<i class='bi bi-check-lg'></i>";
            })
            .catch(error => {
                console.error("Error loading comparison table:", error);
                compareContainer.innerHTML = "<p>Error loading comparison.</p>";
                compareBtn.innerHTML = "<i class='bi bi-ban'></i>";
            });
    });
});

/**
 * Highlights the cells with the best and worst prices in the comparison table.
 */
function highlightBestAndWorstPrices() {
    const priceCells = document.querySelectorAll("#compareContainer table tbody tr td:nth-child(3)");

    let minPrice = Infinity;
    let maxPrice = -Infinity;
    let bestCells = [];
    let worstCells = [];

    priceCells.forEach(cell => {
        const priceText = cell.textContent.trim();
        const price = parseFloat(priceText.replace("€", "").replace(",", "."));

        if (!isNaN(price)) {
            // Update best price
            if (price < minPrice) {
                minPrice = price;
                bestCells = [cell];
            } else if (price === minPrice) {
                bestCells.push(cell);
            }

            // Update worst price
            if (price > maxPrice) {
                maxPrice = price;
                worstCells = [cell];
            } else if (price === maxPrice) {
                worstCells.push(cell);
            }
        }
    });

    // Apply styles
    bestCells.forEach(cell => {
        cell.classList.add("best-price");
    });

    worstCells.forEach(cell => {
        cell.classList.add("worst-price");
    });
}
