$(function () {
    // 初始化渲染
    // window.addEventListener('DOMContentLoaded', renderTable);
    document.getElementById('stockData').innerHTML = '';
    getHongkongStockData();

})
let stockData = [/* 你的JSON数据 */];

function getHongkongStockData() {
    $.ajax({

        type: "POST",

        url: "https://np-tjxg-b.eastmoney.com/api/smart-tag/hk/v3/pw/search-code",
        contentType: 'application/json',
        data: JSON.stringify({
            dxInfo: [],
            extraCondition: "",
            fingerprint: "3bcfaace7971f4f85f2f3f7288d3e22c",
            gids: [],
            keyWord: "涨幅排行，概念",
            matchWord: "",
            needCorrect: true,
            ownSelectAll: false,
            pageNo: 1,
            pageSize: 50,
            removedConditionIdList: [],
            requestId: "LYntYDnnf09lcvqSJFvfKV6OoyuizgCQ1751876137190",
            shareToGuba: false,
            sortName: "CHG",
            sortWay: "desc",
            timestamp: "1751876137190766",
            xcId: "xc0b1ff4689607015185"
        }),

        success: function (data) {
            console.log(data)
            stockData = data;
        }

    });
}


function formatCurrency(value) {
    return (value / 1e8).toFixed(2) + '亿';
}

function renderChart(ctx, data) {
    console.log(data)
    return new Chart(ctx, {
        type: 'line',
        data: {
            labels: data.map((_, i) => i),
            datasets: [{
                data: data,
                borderColor: '#1890ff',
                borderWidth: 1,
                tension: 0.4,
                pointRadius: 0
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {legend: {display: false}},
            scales: {
                x: {display: false},
                y: {display: false}
            }
        }
    });
}

function renderTable() {
    const tbody = document.getElementById('stockData');
    // let stockData = [/* 你的JSON数据 */];
    stockData.forEach(stock => {
        const row = document.createElement('tr');
        console.log(stock)
        row.innerHTML = `
            <td>${stock.code}</td>
            <td>
                <strong>${stock.name}</strong>
                <span class="market-tag">${stock.market_type}</span>
            </td>
            <td>${stock.latest}</td>
            <td class="${stock.change_rate >= 0 ? 'change-positive' : 'change-negative'}">
                ${(stock.change_rate).toFixed(2)}%
            </td>
            <td>${(stock.turnover_rate).toFixed(2)}%</td>
            <td>${formatCurrency(stock.currency_value)}</td>
            <td>${(stock.limit_up_suc_rate * 100).toFixed(2)}%</td>
            <td>
                <span class="status-tag limit-failed">${stock.change_tag === 'LIMIT_FAILED' ? '涨停失败' : ''}</span>
            </td>
            <td>
                <div class="chart-container">
                    <canvas></canvas>
                </div>
            </td>
        `;

        tbody.appendChild(row);
        const chartCtx = row.querySelector('canvas').getContext('2d');
        console.log(stock.time_preview)
        renderChart(chartCtx, stock.time_preview);
    });
}

