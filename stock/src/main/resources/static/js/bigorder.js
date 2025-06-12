$(function () {
    // 初始化渲染
    // window.addEventListener('DOMContentLoaded', renderTable);
    document.getElementById('data-table').innerHTML = '';
    getBigOrderData();

})
// 模拟数据
let stockData = [];

// 页面加载完成后初始化图表
document.addEventListener('DOMContentLoaded', function () {
    // 初始化大单净量图表


    // 添加搜索功能
    document.getElementById('search-btn').addEventListener('click', function () {
        const searchTerm = document.getElementById('search-input').value.toLowerCase();
        const rows = document.querySelectorAll('#data-table tr');

        rows.forEach(row => {
            const code = row.querySelector('.code').textContent.toLowerCase();
            const name = row.querySelector('.name').textContent.toLowerCase();

            if (code.includes(searchTerm) || name.includes(searchTerm)) {
                row.style.display = '';
            } else {
                row.style.display = 'none';
            }
        });
    });

    // 分页功能
    document.getElementById('prev-page').addEventListener('click', function () {
        // 实现上一页功能
        console.log('上一页');
    });

    document.getElementById('next-page').addEventListener('click', function () {
        // 实现下一页功能
        console.log('下一页');
    });
});

function getBigOrderData() {
    $.ajax({

        type: "get",

        url: "getBigOrderData",

        data: {},

        success: function (data) {
            stockData = data;
            console.log(data)
            renderTable(data);
            renderChart(data.slice(0,50))
        }

    });
}


function formatCurrency(value) {
    return (value / 1e8).toFixed(2) + '亿';
}

function renderChart(data) {
    const ctx = document.getElementById('netFlowChart').getContext('2d');
    const netFlowChart = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: data.map(stock => stock.name),
            datasets: [{
                label: '大单净量',
                data: data.map(stock => stock.netInFlow),
                backgroundColor: [
                    'rgba(255, 99, 132, 0.7)',
                    'rgba(54, 162, 235, 0.7)',
                    'rgba(255, 206, 86, 0.7)',
                    'rgba(75, 192, 192, 0.7)',
                    'rgba(153, 102, 255, 0.7)'
                ],
                borderColor: [
                    'rgba(255, 99, 132, 1)',
                    'rgba(54, 162, 235, 1)',
                    'rgba(255, 206, 86, 1)',
                    'rgba(75, 192, 192, 1)',
                    'rgba(153, 102, 255, 1)'
                ],
                borderWidth: 1
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                title: {
                    display: true,
                    text: '大单净量对比图表',
                    font: {
                        size: 16
                    }
                },
                tooltip: {
                    callbacks: {
                        label: function (context) {
                            return `大单净量: ${context.raw}`;
                        }
                    }
                }
            },
            scales: {
                y: {
                    beginAtZero: true,
                    title: {
                        display: true,
                        text: '大单净量'
                    }
                }
            }
        }
    });
}

function renderTable(data) {
    var htmlArray = '';
    data.forEach((item, index) => {

        htmlArray += `
          <tr>
                <td><span class="code">${item.code}</span></td>
                <td class="name">${item.name}</td>
                <td class="rate up">${item.rate}</td>
                <td class="money-in">${item.moneyInNum}</td>
                <td class="up">${item.netInFlow}</td>
                <td class="money-in">${item.cicuration}</td>
                <td class="name">${item.price}</td>
            </tr>
  `
    });
    console.log(htmlArray)
    document.getElementById('data-table').innerHTML = htmlArray;
}

