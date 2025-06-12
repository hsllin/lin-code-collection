let sortDirections = Array(9).fill(true); // 初始排序方向
$(function () {
    document.getElementById('pool').innerHTML = '';
    document.getElementById('pool').innerHTML = 'industry-item-up';
    getLimitPoolData('330324', '');
    getOpenLimitDownData();
})

function downLoadData() {
    $.ajax({

        type: "get",

        url: "downloadLimitDownData",

        data: {},

        success: function (data) {
        }

    });
}

function getLimitPoolData(orderFiled, orderBy) {
    $.ajax({

        type: "get",

        url: "getLimitDownPoolList",

        data: {

            orderFiled: orderFiled,
            orderBy: orderBy

        },

        success: function (data) {
            buildLimitPoolDataHtml(data);
        }

    });
}

function buildLimitPoolDataHtml(data) {
    const i = 1;
    const htmlArray = data.map((item, index) => {

        return `
        <tr>
                <td>${index+1}</td>
                <td>${item.name}</td>
                <td>${item.code}</td>
                <td>${item.percent}%</td>
                <td>${item.price}</td>
                <td>${item.firstTime}</td>
                <td>${item.changeRate}%</td>
                <td>${item.totalPrice}</td>
            </tr>
  `
    });
    document.getElementById('pool').innerHTML = htmlArray.join('');
}

function getOpenLimitDownData() {
    $.ajax({

        type: "get",

        url: "openLimitDown",

        data: {

            type: "1"

        },

        success: function (data) {
            buildOpenLimitDownHtml(data);
        }

    });
}

function buildOpenLimitDownHtml(data) {
    const htmlArray = data.map(stock => {
        const buyNum = stock.buyNum;
        const code = buyNum >= 3 ? 'hight' : buyNum >= 1 ? 'code' : '';

        // 判断条件
        const icon = buyNum > 0.8 ? '🔥' : "";
        return `
    <div class="stock-item">
   
        <span>${stock.time}</span>
        <span class="${code}">${icon} ${stock.n}(${stock.c})</span>
        <span class="positive">${stock.percent}元</span>
    </div>
  `
    });
    document.getElementById('industry-item-up').innerHTML = htmlArray.join('');
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
    document.getElementById('pool').innerHTML = '';
    getLimitPoolData('330324', '');
}

function sortTable(orderFiled, orderBy) {
    document.getElementById('pool').innerHTML = '';
    getLimitPoolData(orderFiled, orderBy);
    // 更新排序指示器
    // updateSortIndicator(column);
}


function updateSortIndicator(column) {
    const indicators = document.querySelectorAll(".sort-indicator");
    indicators.forEach(ind => ind.textContent = "");
    // indicators[column].textContent = sortDirections[column] ? "▼" : "▲";
}