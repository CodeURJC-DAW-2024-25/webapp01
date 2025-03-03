document.addEventListener("DOMContentLoaded", function() {
    const compareBtn = document.getElementById("compareBtn");
    const compareContainer = document.getElementById("compareContainer");

    compareBtn.addEventListener("click", function(e) {
        e.preventDefault();

        const searchQuery = localStorage.getItem("originalSearchQuery") || "";
        const url = `/compare?searchInput=${encodeURIComponent(searchQuery)}`;

        compareContainer.innerHTML = "<p>Loading comparison...</p>";
        compareContainer.style.display = "block";

        fetch(url, {
            method: "GET",
            headers: {
                "Content-Type": "text/html"
            }
        })
        .then(response => {
            if (!response.ok) throw new Error("Error loading comparison");
            return response.text();
        })
        .then(html => {
            compareContainer.innerHTML = html;
            highlightBestAndWorstPrices(); // Call the function that highlights prices
        })
        .catch(error => {
            console.error("Error loading comparison table:", error);
            compareContainer.innerHTML = "<p>Error loading comparison.</p>";
        });
    });
});


/**
 * Highlights the cell with the best price in the comparison table.
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
