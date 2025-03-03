document.addEventListener("DOMContentLoaded", function() {
    const compareBtn = document.getElementById("compareBtn");
    const compareContainer = document.getElementById("compareContainer");

    compareBtn.addEventListener("click", function(e) {
        e.preventDefault();

        const searchQuery = localStorage.getItem("originalSearchQuery") || "";
                
        const url = `/compare?searchInput=${encodeURIComponent(searchQuery)}`;
  
        // Loading indicator
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
        })
        .catch(error => {
            console.error("Error fetching compare table:", error);
            compareContainer.innerHTML = "<p>Error loading comparison.</p>";
        });
    });
});

document.getElementById('compareBtn').addEventListener('click', () => {
    const originalSearchQuery = localStorage.getItem("originalSearchQuery") || "";

    fetch(`/compare?searchInput=${encodeURIComponent(originalSearchQuery)}`)
        .then(response => response.text())
        .then(html => {
            document.getElementById('compareContainer').innerHTML = html;
            document.getElementById('compareContainer').style.display = 'block';
        })
        .catch(error => console.error('Error loading comparison:', error));
});
