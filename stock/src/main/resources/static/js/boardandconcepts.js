var stockList = []
$(function () {
    const tabs = document.querySelectorAll('.tab');
    tabs.forEach(tab => {
        tab.addEventListener('click', () => {
            tabs.forEach(t => t.classList.remove('active'));
            tab.classList.add('active');
        });
    });
    document.getElementById('dataTable').innerHTML = '';
    document.getElementById('hotCard').innerHTML = '';
    getBoardAndConcepts();
    getBoardCardList();
})

function getBoardAndConcepts() {
    $.ajax({

        type: "get",

        url: "getBoardAndConcepts",

        data: {

            type: "1"

        },

        success: function (data) {
            buildBoardAndConceptsHtml(data);
        }

    });
}

function buildBoardAndConceptsHtml(data) {
    var htmlArray = '';
    data.forEach((item, index) => {
        htmlArray += `<tr>
                <td>${index + 1}</td>
                <td>${item.plate_name}</td>
                <td class="percentage-value">${formatPercent(item.core_avg_pcp)}</td>
                <td>${item.rise_count}/${item.fall_count}/${item.stay_count}</td>
                <td>${item.limit_up_count}</td>
                <td>`

        let stocksHTML = '';
        const hotStocks = item.top_n_stocks.items;
        const stockLength = 3;
        stockList[index] = hotStocks
        hotStocks.forEach((stock, stockIndex) => {
            if (stockIndex < stockLength) {
                // stocksHTML += `<div>${stock.stock_chi_name}(${stock.symbol}) ${formatPercent(stock.change_percent)}</div>`;
                stocksHTML += `   
              <div class="description">
             <span class="collapse-text" 
              data-index="${stockIndex}" 
              onclick="showDetail(this.dataset.index)">
             ${stock.stock_chi_name}(${stock.symbol}) ${formatPercent(stock.change_percent)}
            </span>
        </div>`;
            }

        });
        htmlArray += stocksHTML;
        htmlArray += ` </td>
                <td>${formatYi(item.fund_flow)}</td>
<!--                <td>-->
<!--                    <div class="graph" style="background: linear-gradient(to right, #3caee0, rgba(71,182,226,0.79));"></div>-->
<!--                </td>-->
            </tr>
  `
    })

    document.getElementById('dataTable').innerHTML = htmlArray;
}

function buildBoardCardHtml(data) {
    var htmlArray = '';
    console.log(data)
    data.forEach((item, index) => {
        htmlArray += `<div class="card">
                <div class="card-title">
                    <h6>${item.plate_name}</h6>
                    <span class="percentage">${formatPercent(item.plateRate)}</span>
                </div>
                <p class="card-description">${item.description}</p>
                <div class="stock-rows">`
        let stocksHTML = '';
        const hotStocks = item.stocks;
        const stockLength = 3;
        hotStocks.forEach((stock, stockIndex) => {
            if (stockIndex < stockLength) {
                stocksHTML += `<div>${stock.name}(${stock.symbol}) ${(stock.rate * 1).toFixed(2)}%</div>`;
            }

        });
        htmlArray += stocksHTML;
        htmlArray += ` </div>
            </div>`
    })

    document.getElementById('hotCard').innerHTML = htmlArray;
}

function getBoardCardList() {
    $.ajax({

        type: "get",

        url: "getBoardCardList",

        data: {

            type: "1"

        },

        success: function (data) {
            buildBoardCardHtml(data);
        }

    });
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
    document.getElementById('news-list').innerHTML = '';
    getHotSpotNews();
}


function formatPercent(value, precision = 1) {
    const percentValue = value * 100;
    // 处理极小数四舍五入后可能显示为0的情况
    if (Math.round(percentValue * Math.pow(10, precision)) === 0) {
        return '<' + (1 / Math.pow(10, precision)) + '%';
    }
    return percentValue.toFixed(precision) + '%';
}

function formatYi(number) {
    // 处理异常值
    if (!Number.isFinite(number)) return "0";

    // 获取数值和符号
    const absValue = Math.abs(number);
    const sign = number >= 0 ? '+' : '-';

    // 亿级处理 (>100,000,000)
    if (absValue >= 100000000) {
        const billionValue = Math.floor(absValue / 100000000);
        return `${sign}${billionValue}亿`;
    }

    // 万级处理 (10,000-99,999,999)
    if (absValue >= 10000) {
        const tenThousandValue = Math.floor(absValue / 10000);
        return `${sign}${tenThousandValue}万`;
    }

    // 小额处理 (<10,000)
    if (absValue === 0) return "0";

    // 保留两位小数显示
    return `${sign}${absValue.toFixed(2)}`;
}

// 显示模态框并填充内容
function showDetail(index) {
    const modal = document.getElementById("modal");
    const modalTitle = document.getElementById("modal-title");
    const modalBody = document.getElementById("modal-body");
    console.log(stockList[index]);
    var innerHtml = `<div class="tooltip-container">
<div class="stock-rows">`;
    stockList[index].forEach((stock, stockIndex) => {
        // stocksHTML += `<div>${stock.stock_chi_name}(${stock.symbol}) ${formatPercent(stock.change_percent)}</div>`;
        innerHtml += `<div>${stock.stock_chi_name}(${stock.symbol}) ${formatPercent(stock.change_percent)}</div>
`;

    });
    innerHtml += `</div></div>`;
    modalBody.innerHTML = innerHtml;


    modal.style.display = "block";
}

// 关闭模态框
function closeModal() {
    document.getElementById("modal").style.display = "none";
}