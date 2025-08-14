$(function () {
    // 初始化渲染
    // window.addEventListener('DOMContentLoaded', renderTable);
    document.getElementById('stock-table').innerHTML = '';
    getStrongMorningData();
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

function getStrongMorningData() {
    $.ajax({

        type: "get",

        url: "getStrongMorningData",

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
                <td>${item.price}</td>
                <td class="price-up">${item.rate}</td>
                <td class="price-up">${item.morningRate}</td>
                <td>${formatToYi(item.netInFlow)}</td>
                <td>${formatToYi(item.tradeMoney)}</td>
                 <td>${formatToYi(item.volality)}</td>
                <td><span class="collapse-text"
                          onclick="showDetail('${item.concepts}')">${item.concepts}</span></td>
                <td>${item.industry}</td>
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

/**
 * 格式化数字：超过1亿显示亿单位，不足1亿但超过1万显示万单位，不足1万显示原数字
 * @param {number|string} num - 待格式化的数字或数字字符串
 * @returns {string} 格式化后的字符串（自动去除小数点后无效的零）
 */
function formatToYi(num) {
    // 转换为数字类型并验证有效性
    const number = parseFloat(num);
    if (isNaN(number)) return '0';

    const absNum = Math.abs(number);

    // 超过1亿：转换为亿单位
    if (absNum >= 100000000) {
        const result = (number / 100000000).toFixed(2).replace(/\.?0+$/, '');
        return result + '亿';
    }
    // 超过1万但不足1亿：转换为万单位
    else if (absNum >= 10000) {
        const result = (number / 10000).toFixed(2).replace(/\.?0+$/, '');
        return result + '万';
    }
    // 不足1万：直接返回整数
    return Math.round(number).toString();
}


