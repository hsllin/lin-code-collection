// JSON data
let data = [];
$(function () {
    document.getElementById('content').innerHTML = '';
    getLiveStreamingData();
})

function getLiveStreamingData() {
    $.ajax({

        type: "get",

        url: "getLiveStreamingData",

        data: {},

        success: function (reuslt) {
            reuslt = unicodeToChinese(reuslt);
            data = JSON.parse(reuslt);
            renderTable();
        }

    });
}

function unicodeToChinese(str) {
    return str.replace(/\\u([\da-fA-F]{4})/g,
        (_, hex) => String.fromCharCode(parseInt(hex, 16))
    );
}

function formatTimestamp(timestamp) {
    const date = new Date(timestamp * 1000);
    return date.toLocaleTimeString('zh-CN', {hour: '2-digit', minute: '2-digit'});
}

function renderStocks(stocks) {
    if (!stocks || stocks.length === 0) return '';
    return stocks.map(([code, name, change]) => {
        const isUp = change > 0;
        return `<span class="stock-item ${isUp ? 'up' : 'down'}">${name}(${code}): ${change > 0 ? '+' : ''}${change}%</span>`;
    }).join('');
}


// Function to format turnover value (e.g., 15114400 to 151.1亿)
function formatTurnover(value) {
    return (value / 100000).toFixed(1) + '亿';
}

// Function to format percentage change (e.g., 0.0669553 to 0.07%)
function formatPercentage(value) {
    return (value * 100).toFixed(2) + '%';
}

// Function to get RPS count class for styling
function getRPSCountClass(count) {
    if (count === 5) return 'rps-count-5';
    if (count === 3) return 'rps-count-3';
    if (count === 2) return 'rps-count-2';
    return '';
}

// Function to render the table
function renderTable() {
    console.log(data.List)
    const contentDiv = document.querySelector('.content');
    data.List.forEach(item => {
        const newsItemDiv = document.createElement('div');
        newsItemDiv.className = 'news-item';

        let plateInfoHtml = '';
        if (item.PlateName) {
            plateInfoHtml = `<div class="plate-info"><span class="theme-tag">${item.PlateName}</span> 板块涨跌幅: ${item.PlateZDF}%</div>`;
        }

        newsItemDiv.innerHTML = `
                <div class="news-header">
                    <div class="user-info">来自: <strong>${item.UserName}</strong></div>
                    <div class="timestamp">${formatTimestamp(item.Time)}</div>
                </div>
                <div class="news-content">${item.Comment.replace('（该内容由AI大模型根据行情自动生成）', '')}</div>
                ${plateInfoHtml}
                <div class="stock-list">
                    ${renderStocks(item.Stock)}
                </div>
            `;
        contentDiv.appendChild(newsItemDiv);
    });
}

function refreshData(){
    document.getElementById('content').innerHTML = '';
    getLiveStreamingData();
}




