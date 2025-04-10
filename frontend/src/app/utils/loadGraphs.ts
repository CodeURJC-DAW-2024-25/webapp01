
export function loadGraphs(Chart: any, products: any) {
    // Prducts Bar chart
    const ctx1 = document.getElementById('chart-1');
    const data1: {
        type: string;
        data: {
            labels: string[];
            datasets: { label: string; data: number[]; borderWidth: number }[];
        };
        options: {
            scales: { y: { beginAtZero: boolean } };
            plugins: { title: { display: boolean; text: string } };
        };
    } = {
        type: 'bar',
        data: {
            labels: [],
            datasets: [{
                label: 'nº de productos',
                data: [],
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
                    text: 'nº de productos por supermercado'
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


    for (const stat of products.stats) {
        data1.data.labels.push(stat.name);
        data1.data.datasets[0].data.push(stat.count);
    }
    chart1.update();

}
