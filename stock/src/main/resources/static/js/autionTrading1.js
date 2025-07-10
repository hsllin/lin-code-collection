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
    document.getElementById('board').innerHTML = '';
    getAutionTradingData();
    getShortBoardData();
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

function getAutionTradingData() {
    $.ajax({

        type: "get",

        url: "getAutionTradingData",

        data: {},

        success: function (data) {
            buildAutionTradingHtml(data);
        }

    });
}

function getShortBoardData() {
    $.ajax({

        type: "get",

        url: "https://app.aigupiao.com/stock/getMarketSentimentList",

        data: {},

        success: function (data) {
            buildShortHtml(data);
        }

    });
}

function buildShortHtml(data) {
    const item = data.data;
    console.log(item)
    const htmlArray = `
       <div class="header">
            <div class="mood">短线情绪：<span>${item.market_temperature}</span></div>
            <div class="median">中位数：<span>${item.median_val}</span></div>
        </div>

        <div class="board-stats">
            <div class="board-cell">
                <h6>一板</h6>
                <p>${item.one_limit_up_stock_num}</p>
                <div class="rate">连板率</div>
            </div>
            <div class="board-cell">
                <h6>二板</h6>
                <p>${item.two_limit_up_stock_num}</p>
                <div class="rate">${item.two_limit_up_stock_rate}</div>
            </div>
            <div class="board-cell">
                <h6>三板</h6>
                <p>${item.three_limit_up_stock_num}</p>
                <div class="rate">${item.three_limit_up_stock_rate}</div>
            </div>
            <div class="board-cell">
                <h6>高度板</h6>
                <p>${item.high_limit_up_stock_num}</p>
                <div class="rate">${item.high_limit_up_stock_rate}</div>
            </div>
        </div>

        <div class="炸板">
            <div class="炸板-item">
                <h6>今日炸板率</h6>
                <p>${item.blast_break_pct}</p>
            </div>
            <div class="炸板-item">
                <h6>昨涨停表现</h6>
                <p class="negative">${item.raising_limit_theme_chg_pct}</p>
            </div>
        </div>

        <div class="炸板">
            <div class="炸板-item">
                <h6>昨炸板表现</h6>
                <p class="positive">${item.limit_up_break_chg_pct}</p>
            </div>
            <div class="炸板-item">
                <h6>昨连板表现</h6>
                <p class="positive">${item.repetition_limit_theme_chg_pct}</p>
            </div>
        </div>
`;
    document.getElementById('board').innerHTML = htmlArray;
}

function buildAutionTradingHtml(data) {
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
    document.getElementById('board').innerHTML = '';
    getAutionTradingData();
    getShortBoardData();
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