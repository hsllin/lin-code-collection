$(function () {
    // 初始化渲染
    // window.addEventListener('DOMContentLoaded', renderTable);
    document.getElementById('stock-table').innerHTML = '';
    getStockTwentyData();
// 点击模态框外部关闭
    window.onclick = function (event) {
        const modal = document.getElementById("modal");
        if (event.target == modal) {
            modal.style.display = "none";
        }
    }
})
// 模拟数据
let stockData = [];

function getStockTwentyData() {
    $.ajax({

        type: "get",

        url: "getStockTwentyData",

        data: {},

        success: function (data) {
            stockData = data;
            renderTable(data);
        }

    });
}


function formatCurrency(value) {
    return (value / 1e8).toFixed(2) + '亿';
}

function renderTable(data) {
    var htmlArray = '';
    data.forEach((item, index) => {

        htmlArray += `
        <tr>
                <td>${index + 1}</td>
                <td>${item.code}</td>
                <td>${item.name}</td>
                <td>${item.price}</td>
                <td class="price-up">${item.rate}</td>
                <td>${item.moneyInNum}</td>
                 <td>${formatToYi(item.volality)}</td>
                <td><span class="collapse-text"
                          onclick="showDetail('${item.concepts}')">${item.concepts}</span></td>
                <td>${item.industry}</td>
            </tr>
  `
    });
    console.log(htmlArray)
    document.getElementById('stock-table').innerHTML = htmlArray;
}

// 显示模态框并填充内容
function showDetail(stockName) {
    const modal = document.getElementById("modal");
    const modalTitle = document.getElementById("modal-title");
    const modalBody = document.getElementById("modal-body");

    modalTitle.textContent = `${stockName} - 涨停类别详情`;

    // 根据股票名设置不同的详细内容
    // modalBody.innerHTML = "<p>注册制次新股: 属于注册制改革后上市的股票</p><p>次股与次新股: 此类股票具有较高波动性</p><p>融资融券: 可参与融资融券交易</p>";
    modalBody.innerHTML = stockName;

    modal.style.display = "block";
}

// 关闭模态框
function closeModal() {
    document.getElementById("modal").style.display = "none";
}

function downLoadData() {
    $.ajax({

        type: "get",

        url: "downloadTrendData",

        data: {},

        success: function (data) {
        }

    });
}

function refreshData() {
    document.getElementById('stock-table').innerHTML = '';
    getStockTwentyData();
}


