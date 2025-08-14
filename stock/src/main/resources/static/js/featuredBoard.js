let data = [];
let stockData=[];
let boardId='';


/* 3. 渲染右侧个股 */
function renderPlate(plate) {
    // 切换高亮
    [...plateList.children].forEach(li => li.classList.toggle('active', li.id === plate));

    const list = stockData.list || [];
    const title = document.getElementById('plateTitle');
    const table = document.getElementById('stockTable');
    const tbody = document.getElementById('stockBody');
    tbody.innerHTML = '';

    if (!list.length) {
        title.textContent = plate + '（暂无个股）';
        table.style.display = 'none';
        return;
    }

    title.textContent = plate;
    table.style.display = 'table';

    list.forEach(item => {
        const tr = document.createElement('tr');
        const tag=item[23]?'tag tag-short':'';
        tr.innerHTML = `
            <td>${item[0]}</td>
            <td>${item[1]}</td>
            <td class="limit-up">${item[6].toFixed(2)}</td>
            <td>${formatToYi(item[13])}</td>
            <td>${item[40]}</td>
            
            <td>${item[24]}</td>
            <td>${item[2]}</td>
            <td><span class="${tag}">${item[23]}</span></td>
            
            <td>${item[4]}</td>
        `;
        tbody.appendChild(tr);
    });
}


$(function () {
    document.querySelector('#plateList').innerHTML = '';
    getFeaturedBoards();
    getFeaturedBoardsData();
})

function getFeaturedBoards() {
    $.ajax({

        type: "get",

        url: "getFeaturedBoards",

        data: {

        },

        success: function (reuslt) {
            reuslt = unicodeToChinese(reuslt);
            data = JSON.parse(reuslt);
            // console.log(data)
            renderTable();
        }

    });
}

function getFeaturedBoardsData(boardId) {
    $.ajax({

        type: "get",

        url: "getFeaturedBoardsData",

        data: {
            boardId: boardId
        },

        success: function (reuslt) {
            reuslt = unicodeToChinese(reuslt);
            stockData = JSON.parse(reuslt);
            console.log(stockData)
            renderPlate(boardId);
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
    // console.log(data.List)
    const contentDiv = document.querySelector('#plateList');
    let htmlArray = '';
    boardId=data.list[0][0];
    console.log(boardId)
    data.list.forEach(item => {
        htmlArray += `
               <li id="${item[0]}" onclick="getFeaturedBoardsData('${item[0]}')">${item[1]} 强度：${item[2]} 涨幅：${item[3].toFixed(2)}%</li>
            `;

    });
    contentDiv.innerHTML = htmlArray;
    getFeaturedBoardsData(boardId);
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




