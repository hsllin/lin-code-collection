let data = [];
let stockData = [];
let boardData = [];
let boardId = '';
let tagType = 'stock';
let days = '5';
let sort = '1';
$(function () {
    // 区间滑块
    const rangeSlider = document.getElementById('dayRange');
    const dateRange = document.getElementById('dateRange');
    if (rangeSlider) {
        rangeSlider.addEventListener('input', function () {
            const dateRange = document.querySelector('.date-range');
            if (!dateRange) return;

            days = this.value;
            const endDate = new Date('2025-08-01');
            const startDate = new Date(endDate);
            startDate.setDate(startDate.getDate() - days + 1);

            const formattedStartDate = `${startDate.getFullYear()}-${String(startDate.getMonth() + 1).padStart(2, '0')}-${String(startDate.getDate()).padStart(2, '0')}`;
            const formattedEndDate = '2025-08-01';

            dateRange.textContent = `${days}日 ${formattedStartDate}--${formattedEndDate}`;
        });
        rangeSlider.addEventListener('change', function () {
            const dateRange = document.querySelector('.date-range');
            if (!dateRange) return;

            days = this.value;
            const endDate = new Date('2025-08-01');
            const startDate = new Date(endDate);
            startDate.setDate(startDate.getDate() - days + 1);

            const formattedStartDate = `${startDate.getFullYear()}-${String(startDate.getMonth() + 1).padStart(2, '0')}-${String(startDate.getDate()).padStart(2, '0')}`;
            const formattedEndDate = '2025-08-01';

            dateRange.textContent = `${days}日 ${formattedStartDate}--${formattedEndDate}`;
            console.log(days)
            console.log(tagType)
            if (tagType === 'stock') {
                getRangeIncreaseStockData(days);
            } else {
                getRangeIncreaseBoardData(days);
            }
        });
    }
    document.querySelector('#stockBody').innerHTML = '';
    document.querySelector('#boardBody').innerHTML = '';

    getRangeIncreaseStockData();
    // getFeaturedBoardsData();
})


// 切换标签
function switchTab(type) {
    const tabs = document.querySelectorAll('.tab');
    const stockTable = document.getElementById('stockTable');
    const sectorTable = document.getElementById('sectorTable');
    tagType = type;
    if (type === 'stock') {
        tabs[0].classList.add('active');
        tabs[1].classList.remove('active');
        stockTable.classList.remove('hidden');
        sectorTable.classList.add('hidden');
        getRangeIncreaseStockData();
    } else {
        tabs[1].classList.add('active');
        tabs[0].classList.remove('active');
        sectorTable.classList.remove('hidden');
        stockTable.classList.add('hidden');
        getRangeIncreaseBoardData();
    }
}

function downloadRankIncreaseData(){
    $.ajax({

        type: "get",

        url: "downloadRankIncreaseData",

        data: {
            days: days,
            sort: sort
        },

        success: function (reuslt) {
            reuslt = unicodeToChinese(reuslt);
            boardData = JSON.parse(reuslt);
            console.log(boardData)
            renderBoard();
        }

    });
}

/* 3. 渲染右侧个股 */
function renderBoardData(plate) {
}


function getRangeIncreaseBoardData() {
    $.ajax({

        type: "get",

        url: "getRangeIncreaseBoardData",

        data: {
            days: days,
            sort: sort
        },

        success: function (reuslt) {
            reuslt = unicodeToChinese(reuslt);
            boardData = JSON.parse(reuslt);
            console.log(boardData)
            renderBoard();
        }

    });
}

function getRangeIncreaseStockData() {
    $.ajax({

        type: "get",

        url: "getRangeIncreaseStockData",

        data: {
            days: days,
            sort: sort
        },

        success: function (reuslt) {
            reuslt = unicodeToChinese(reuslt);
            stockData = JSON.parse(reuslt);
            // console.log(stockData)
            renderStock();
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
function renderStock() {
    let htmlArray = '';
    stockData.List.forEach(item => {
        htmlArray += `
                <tr>
                <td>${item[0]}</td>
                <td>${item[1]}</td>
                <td class="up">${item[3]}</td>
                <td>${formatToYi(item[6])}</td>
                <td>${item[7].toFixed(2)}%</td>
                <td>${item[13]}</td>
                <td class="myTag">${item[10]}</td>
            </tr>
            `;

    });
    document.getElementById("stockBody").innerHTML = htmlArray;
}

// Function to render the table
function renderBoard() {
    let htmlArray = '';
    boardData.List.forEach(item => {
        htmlArray += `
                <tr>
                <td>${item[1]}</td>
                <td class="up">${item[2]}</td>
                <td>${formatToYi(item[3])}</td>
                <td>${item[8]}</td>
            </tr>
            `;

    });
    document.getElementById("boardBody").innerHTML = htmlArray;
}

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

function changeSort() {
    if (sort == '1') {
        sort = '0'
    } else {
        sort = '1'
    }
    if (tagType == 'stock') {
        getRangeIncreaseStockData()
    } else {
        getRangeIncreaseBoardData();
    }
}

function refreshData(){
    document.querySelector('#stockBody').innerHTML = '';
    document.querySelector('#boardBody').innerHTML = '';

    getRangeIncreaseStockData();
}




