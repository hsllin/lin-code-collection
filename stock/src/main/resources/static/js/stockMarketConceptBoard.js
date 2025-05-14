$(function () {

    getIncreaseData();
    getDecreaseData();

})

function getIncreaseData() {
    $.ajax({

        type: "get",

        url: "intraDayChange",

        data: {

            type: "1"

        },

        success: function (data) {
            buildIncreaseHtml(data);
        }

    });
}

function getDecreaseData() {
    $.ajax({

        type: "get",

        url: "intraDayChange",

        data: {

            type: "2"

        },

        success: function (data) {
            buildDecreaseHtml(data);
        }

    });
}

function buildIncreaseHtml(data) {
    const htmlArray = data.map(stock => {
        const percentValue = stock.percent;
        const count= stock.count;

        // 判断条件
        const icon = percentValue > 3.5 ? '🚀' : count > 2 ? '🔥' : "";
        return `
    <div class="stock-item">
   
        <span>${stock.time} ${stock.count}</span>
        <span>${icon} ${stock.n}(${stock.c})</span>
        <span class="positive">${stock.percent}%</span>
    </div>
  `
    });
    document.getElementById('industry-item-up').innerHTML = htmlArray.join('');
}

function buildDecreaseHtml(data) {
    const htmlArray = data.map(stock => {
        const percentValue = stock.percent;

        // 判断条件
        const icon = percentValue < -5 ? '💥' :
            '';
        return `
    <div class="stock-item">
   
        <span>${stock.time} ${stock.count}</span>
        <span>${icon}${stock.n}(${stock.c})</span>
        <span class="negative">${stock.percent}%</span>
    </div>
  `
    });
    document.getElementById('industry-item-down').innerHTML = htmlArray.join('');
}

// 简单的XSS转义函数
function escapeHtml(unsafe) {
    return unsafe.toString()
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");
}

function refreshData() {
    document.getElementById('industry-item-up').innerHTML = '';
    document.getElementById('industry-item-down').innerHTML = '';
    getIncreaseData();
    getDecreaseData();

}