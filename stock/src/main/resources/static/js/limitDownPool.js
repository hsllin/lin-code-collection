let sortDirections = Array(9).fill(true); // 初始排序方向
$(function () {
    document.getElementById('pool').innerHTML = '';
    getLimitPoolData('330324', '');

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