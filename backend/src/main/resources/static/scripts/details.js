document.addEventListener("DOMContentLoaded", function() {
    const compareBtn = document.getElementById("compareBtn");
    const compareContainer = document.getElementById("compareContainer");

    compareBtn.addEventListener("click", function(e) {
        e.preventDefault();

        // Usamos el contenido del elemento con id "productTitle" como término de búsqueda
        const searchInput = document.getElementById("productTitle").textContent.trim();
        console.log("Search term:", searchInput);
        
        const url = `/compare?searchInput=${encodeURIComponent(searchInput)}`;

        // Indicador de carga
        compareContainer.innerHTML = "<p>Cargando comparación...</p>";
        compareContainer.style.display = "block";

        fetch(url, {
            method: "GET",
            headers: {
                "Content-Type": "text/html"
            }
        })
        .then(response => {
            if (!response.ok) throw new Error("Error al cargar la comparación");
            return response.text();
        })
        .then(html => {
            compareContainer.innerHTML = html;
        })
        .catch(error => {
            console.error("Error fetching compare table:", error);
            compareContainer.innerHTML = "<p>Error al cargar la comparación.</p>";
        });
    });
});
