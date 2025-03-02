

// Prducts Bar chart
const ctx = document.getElementById('chart-1');
new Chart(ctx, {
    type: 'bar',
    data: {
        labels: ['Mercadona', 'El Corte inglés', 'Carrefour', 'Lidl', 'Dia', 'Consum', 'BM'],
        datasets: [{
            label: '# de productos',
            //   total: 59825
            data: [4993, 28520, 26539, 0, 1000, 6500, 4000, 3500, 3000],
            borderWidth: 1
        }]
    },
    options: {
        scales: {
            y: {
                beginAtZero: true
            }
        },
        plugins: {
            title: {
                display: true,
                text: '# de productos por supermercado'
            }
        }
    }
});

// Activity Line chart
const ctx2 = document.getElementById('chart-2');
new Chart(ctx2, {
    type: 'line',
    data: {
        labels: ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'],
        datasets: [{
            label: 'Actividad',
            data: [
                1000, 1200, 900, 1500, 2000, 1800, 2500, 1900, 1300, 1700, 2200, 2500
            ],
            fill: false,
            borderColor: 'rgb(75, 192, 192)',
            tension: .4
        }]
    },
    options: {
        scales: {
            y: {
                beginAtZero: true
            }
        },
        plugins: {
            title: {
                display: true,
                text: 'Actividad por mes'
            }
        }
    }
});