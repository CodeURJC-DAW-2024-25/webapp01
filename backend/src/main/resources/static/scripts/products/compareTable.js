document.addEventListener('DOMContentLoaded', function () {
    // Initialize the table with sorting by price (ascending)
    let currentSortColumn = 'price';
    let currentSortDirection = 'asc';

    // Process prices for comparison
    processPrices();

    // Add event listeners to column headers
    document.querySelectorAll('th.sortable').forEach(header => {
        header.addEventListener('click', function () {
            const column = this.getAttribute('data-column');

            // Change direction if it's the same column
            if (column === currentSortColumn) {
                currentSortDirection = currentSortDirection === 'asc' ? 'desc' : 'asc';
            } else {
                currentSortColumn = column;
                currentSortDirection = 'asc';
            }

            // Update sorting icons
            updateSortIcons(column, currentSortDirection);

            // Sort the table
            sortTable(column, currentSortDirection);

            // Update best/worst price classes after sorting
            markBestAndWorstPrices();
        });
    });

    // Function to process prices
    function processPrices() {
        const rows = document.querySelectorAll('.compare-table tbody tr');

        rows.forEach(row => {
            const priceCell = row.querySelector('.price-cell');
            const priceText = priceCell.textContent.trim();

            // If it doesn't have a data-price attribute, extract the numeric value from the price
            if (!row.hasAttribute('data-price')) {
                // Extract only numbers and dots from the price (e.g., from "€10.99" to "10.99")
                const priceValue = parseFloat(priceText.replace(/[^0-9.]/g, ''));
                row.setAttribute('data-price', priceValue);
            }
        });

        // Mark best and worst prices
        markBestAndWorstPrices();
    }

    // Function to sort the table
    function sortTable(column, direction) {
        const tbody = document.querySelector('.compare-table tbody');
        const rows = Array.from(tbody.querySelectorAll('tr'));

        // Clear and rebuild the table
        while (tbody.firstChild) {
            tbody.removeChild(tbody.firstChild);
        }

        rows.forEach(row => {
            tbody.appendChild(row);
        });
    }

    // Function to update sorting icons
    function updateSortIcons(column, direction) {
        // Hide all icons
        document.querySelectorAll('.sort-icon').forEach(icon => {
            icon.classList.add('hidden');
        });

        // Show the correct icon
        const activeIcon = document.querySelector(`th[data-column="${column}"] .sort-icon-${direction}`);
        if (activeIcon) {
            activeIcon.classList.remove('hidden');
        }
    }

    // Function to mark the best and worst prices
    function markBestAndWorstPrices() {
        const rows = document.querySelectorAll('.compare-table tbody tr');
        if (rows.length === 0) return;

        // Clear existing classes

        // Get all prices
        const prices = Array.from(rows).map(row => {
            return parseFloat(row.getAttribute('data-price'));
        });

        // Find min and max
        const minPrice = Math.min(...prices);
        const maxPrice = Math.max(...prices);

        // Mark rows
        rows.forEach(row => {
            const price = parseFloat(row.getAttribute('data-price'));
        });
    }
});