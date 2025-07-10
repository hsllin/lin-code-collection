let sortDirections = Array(9).fill(true); // 初始排序方向

// 时钟更新函数
function updateClock() {
    const now = new Date();

    // 格式化时间 HH:MM:SS
    const hours = String(now.getHours()).padStart(2, '0');
    const minutes = String(now.getMinutes()).padStart(2, '0');
    const seconds = String(now.getSeconds()).padStart(2, '0');
    const timeString = `${hours}:${minutes}:${seconds}`;

    // 格式化日期
    const year = now.getFullYear();
    const month = String(now.getMonth() + 1).padStart(2, '0');
    const day = String(now.getDate()).padStart(2, '0');
    const weekdays = ['日', '一', '二', '三', '四', '五', '六'];
    const weekday = weekdays[now.getDay()];
    const dateString = `${year}年${month}月${day}日 星期${weekday}`;

    // 更新DOM元素
    document.getElementById('time').textContent = timeString;
    document.getElementById('date').textContent = dateString;
}

$(function () {
    // 初始化时钟
    updateClock();

    // 每秒更新时钟
    setInterval(updateClock, 1000);

    document.getElementById('pool').innerHTML = '';
    getAutionNoMatchedData();
})

function downLoadData() {
    $.ajax({

        type: "get",

        url: "downloadLimitUpData",

        data: {},

        success: function (data) {
        }

    });
}

function getAutionNoMatchedData() {
    $.ajax({

        type: "get",

        url: "getAutionNoMatchedData",

        data: {},

        success: function (data) {
            buildAutionNoMatchedHtml(data);
        }

    });
}

function buildAutionNoMatchedHtml(data) {
    const i = 1;
    console.log(data)
    const htmlArray = data.map((item, index) => {

        return `
        <tr>
                <td>${index + 1}</td>
                <td>${item.name}</td>
                <td>${item.code}</td>
                <td>${item.rate}%</td>
                <td>${item.price}</td>
                 <td>${item.industry || '-'}</td>
                 <td>${item.concepts || '-'}</td>
                <td>${formatToYi(item.buyMoney)}</td>
                <td>${(item.buyNum / 10000).toFixed(2)}万</td>
                <td>${formatToYi(item.flowMoney)}</td>
                <td>${formatToYi(item.unMatchedMoney)}</td>
                <td>${formatToYi(item.unMatchedNum)}</td>
                 <td>${item.type}</td>
                <td>${item.ratingType}</td>
            </tr>
  `
    });
    document.getElementById('pool').innerHTML = htmlArray.join('');
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
    getAutionNoMatchedData();
}

function sortTable(orderFiled, orderBy) {
    document.getElementById('pool').innerHTML = '';
    getAutionNoMatchedData();
}


function updateSortIndicator(column) {
    const indicators = document.querySelectorAll(".sort-indicator");
    indicators.forEach(ind => ind.textContent = "");
    // indicators[column].textContent = sortDirections[column] ? "▼" : "▲";
}