

// Prducts Bar chart
const ctx1 = document.getElementById('chart-1');
const data1 = {
    type: 'bar',
    data: {
        labels: ['Mercadona', 'El Corte inglés', 'Carrefour', 'Lidl', 'Dia', 'Consum', 'BM'],
        datasets: [{
            label: '# de productos',
            //   total: 59825
            data: [0, 0, 0, 0, 0, 0, 0, 0, 0],
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
}
const chart1 = new Chart(ctx1, data1);

// Activity Line chart
const ctx2 = document.getElementById('chart-2');
const data2 = {
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
}
const chart2 = new Chart(ctx2, data2);


const supermarkets = ['mercadona', 'elcorteingles', 'Carrefour', 'lidl', 'dia', 'consum', 'bm'];
const total_items = {};

const fetchItems = async () => {
    await Promise.all(supermarkets.map(async supermarket => {
        const res = await fetch(`/api/products?supermarket=${supermarket}`);
        const data = await res.json();
        console.log(`Total items in ${supermarket}: ${data.total_items}`);
        total_items[supermarket] = data.total_items;
    }))

    console.log(total_items);
    data1.data.datasets[0].data = supermarkets.map(supermarket => total_items[supermarket]);
    chart1.update();
}
fetchItems();