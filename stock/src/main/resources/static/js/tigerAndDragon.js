let sortDirections = Array(9).fill(true);
let dateIndex = 0;// 初始排序方向
let dataStorage = null;
$(function () {
    dataStorage = null;
    getTigerAndDragonData();


})


function getTigerAndDragonData() {
    $.ajax({

        type: "get",

        url: "getTigerAndDragonData",

        data: {},

        success: function (data) {
            dataStorage = data.data.lhbStocks;
            buildTigerAndDragonHtml(dataStorage);
        }

    });
}

function buildTigerAndDragonHtml(data) {
    document.getElementById('stockList').innerHTML = '';
    var htmlArray = '';
    console.log(data)
    data.forEach((stock, groupIndex) => {
        htmlArray += `
         <tr >
          <td onclick="loadBuyAndSellData(${groupIndex})"><a href="javascript:;">${stock.stockName}</a></td>
          <td>${stock.stockCode}</td>
          <td>${stock.chg}</td>
          <td>${stock.netValueTotal}万</td>
          <td>${stock.buyValueTotal}</td>
            <td>${stock.sellValueTotal}万</td>
            <td>${stock.infoClassName}</td>
         </tr>
        `
    });
    document.getElementById('stockList').innerHTML = htmlArray;
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
    dateIndex = 0;
    document.getElementById('app').innerHTML = '';
    getStrongStockData();
}

function preDayData() {
    dateIndex++;
    getStrongStockData();
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


function showTooltip(event) {
    // 获取当前单元格内的提示框
    const tooltip = event.target.querySelector('.reason-tooltip');
    if (tooltip) {
        // 设置提示框的显示状态
        tooltip.style.display = 'block';
        // 计算提示框的位置，避免超出屏幕边界
        const rect = event.target.getBoundingClientRect();
        tooltip.style.left = `${rect.left + window.scrollX}px`;
        tooltip.style.top = `${rect.bottom + window.scrollY}px`;
    }
}

function hideTooltip(event) {
    // 获取当前单元格内的提示框
    const tooltip = event.target.querySelector('.reason-tooltip');
    if (tooltip) {
        // 隐藏提示框
        tooltip.style.display = 'none';
    }
}

function showFullReason(content) {
    this.fullReasonContent = content;
    // 创建详情弹窗
    const div = document.createElement('div');
    div.className = 'reason-full active';
    div.innerHTML = `
                <div class="reason-content">${content}</div>
                <span class="close-btn" onclick="this.parentElement.remove()">×</span>
            `;
    document.body.appendChild(div);
}

function loadBuyAndSellData(index) {
    document.getElementById('buy').innerHTML = '';
    document.getElementById('sell').innerHTML = '';
    var buyBranch = dataStorage[index].lhbBranch.buyBranches;
    var sellBranch = dataStorage[index].lhbBranch.sellBranches;
    var buyHtmlArray='';
    var sellHtmlArray='';
    buyBranch.forEach((branch, branchIndex) => {
        buyHtmlArray += `
          <tr >
           <td class="branch-name">${branch.branchName}</td>
          <td>${branch.buyValue}万</td>
          <td>${branch.buyRatio}%</td>
          <td>${branch.sellRatio}%</td>
          <td :class="${branch.netValue} >= 0 ? 'positive' : 'negative'">
          ${branch.netValue}}万
           </td>
      </tr>
        `
    });
    sellBranch.forEach((branch, branchIndex) => {
        sellHtmlArray += `
          <tr >
           <td class="branch-name">${branch.branchName}</td>
          <td>${branch.buyValue}万</td>
          <td>${branch.buyRatio}%</td>
          <td>${branch.sellRatio}%</td>
          <td :class="${branch.netValue} >= 0 ? 'positive' : 'negative'">
          ${branch.netValue}万
           </td>
      </tr>
        `
    });
    document.getElementById('buy').innerHTML = buyHtmlArray;
    document.getElementById('sell').innerHTML = sellHtmlArray;
}