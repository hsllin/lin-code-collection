$(function () {

    getSixtyDayData();
    // getSellChangeData();

})

function getSixtyDayData() {
    $.ajax({

        type: "get",

        url: "sixtyChange",

        data: {

            type: "1"

        },

        success: function (data) {
            buildSixtyDayHtml(data);
        }

    });
}

function getSellChangeData() {
    $.ajax({

        type: "get",

        url: "buyChange",

        data: {

            type: "2"

        },

        success: function (data) {
            buildSellChangeHtml(data);
        }

    });
}

function buildSixtyDayHtml(data) {
    const htmlArray = data.map(stock => {
        const buyNum = stock.buyNum;
        const code = buyNum >= 3 ? 'hight' : buyNum >= 1 ? 'code' : '';

        // 判断条件
        const icon = buyNum > 0.8 ? '🔥' : "";
        return `
    <div class="stock-item">
   
        <span>${stock.time}</span>
        <span class="${code}">${icon} ${stock.n}(${stock.c})</span>
        <span class="positive">${stock.percent}%</span>
    </div>
  `
    });
    document.getElementById('industry-item-up').innerHTML = htmlArray.join('');
}

function buildSellChangeHtml(data) {
    const htmlArray = data.map(stock => {
        const buyNum = stock.buyNum;
        const code = buyNum >= 3 ? 'hight' : buyNum >= 1 ? 'code' : '';

        // 判断条件
        // const icon = buyNum > 0.8 ? '🔥' : "";

        // 判断条件
        const icon = buyNum > 0.8? '💥' :
            '';
        return `
    <div class="stock-item">
   
        <span>${stock.time} </span>
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
    getBuyChangeData();
    // getDecreaseData();

}