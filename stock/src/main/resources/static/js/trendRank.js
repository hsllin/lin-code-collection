$(function () {
    // 初始化渲染
    // window.addEventListener('DOMContentLoaded', renderTable);
    document.getElementById('stock-table').innerHTML = '';
    getDecreaseData();
// 点击模态框外部关闭
    window.onclick = function (event) {
        const modal = document.getElementById("modal");
        if (event.target == modal) {
            modal.style.display = "none";
        }
    }
})
// 模拟数据
let stockData = [];

function getDecreaseData() {
    $.ajax({

        type: "get",

        url: "getTrendData",

        data: {},

        success: function (data) {
            stockData = data;
            renderTable(data);
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
                <td>${index + 1}</td>
                <td>${item.code}</td>
                <td>${item.name}</td>
                <td class="price-up">${item.rate}</td>
                <td><span class="collapse-text"
                          onclick="showDetail('${item.concepts}')">${item.concepts}</span></td>
                <td>${item.industry}</td>
                <td>${item.minPrice}</td>
                <td>${item.mostPrice}</td>
                <td>${item.fivePrice}</td>
                <td>${item.tenPrice}</td>
                <td>${item.twentyPrice}</td>
            </tr>
  `
    });
    console.log(htmlArray)
    document.getElementById('stock-table').innerHTML = htmlArray;
}

// 显示模态框并填充内容
function showDetail(stockName) {
    const modal = document.getElementById("modal");
    const modalTitle = document.getElementById("modal-title");
    const modalBody = document.getElementById("modal-body");

    modalTitle.textContent = `${stockName} - 涨停类别详情`;

    // 根据股票名设置不同的详细内容
    // modalBody.innerHTML = "<p>注册制次新股: 属于注册制改革后上市的股票</p><p>次股与次新股: 此类股票具有较高波动性</p><p>融资融券: 可参与融资融券交易</p>";
    modalBody.innerHTML = stockName;

    modal.style.display = "block";
}

// 关闭模态框
function closeModal() {
    document.getElementById("modal").style.display = "none";
}

function downLoadData() {
    $.ajax({

        type: "get",

        url: "downloadTrendData",

        data: {},

        success: function (data) {
        }

    });
}

function refreshData(){
    document.getElementById('stock-table').innerHTML = '';
    getDecreaseData();
}



