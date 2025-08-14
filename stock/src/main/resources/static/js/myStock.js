// 刷新功能
let autoRefreshInterval;
let isAutoRefreshing = true;

$(function () {
    document.getElementById('myStock').innerHTML = '';
    refreshStocks();
    // getListData();
// 初始化自动刷新
//     autoRefreshInterval = setInterval(refreshStocks, 5000);
})

function getListData() {
    $.ajax({

        type: "get",

        url: "getMyStockList",

        data: {},

        success: function (data) {
            buildListHtml(data);
        }

    });
}

function buildListHtml(data) {
    console.log(data)
    var htmlArray = '';
    data.forEach((group, groupIndex) => {
            htmlArray += `
             <tr>
                <td>
                     <div class="stock-code">${group.code}</div>
                </td>
                <td>${group.name}</td>
                <td class="price-up">${group.price}</td>
                <td>${group.maxPrice}</td>
                <td>${group.minPrice}</td>
                <td class="price-rate">${group.rate}%</td>
                 <td class="price-up">${group.pe}</td>
                 <td>${group.tradingVolume}</td>
                 <td>${group.volality}</td>
                  <td>${group.quantityRatio}</td>
                  <td>${group.turnoverRate}%</td>
                <td>${group.industry}</td>
                
                <td>
                    <div class="action-buttons">
                        <button onclick="deleteStock('${group.code}')">删除</button>
                    </div>
                </td>
            </tr>
                    `;
            console.log(data);
            document.getElementById('myStock').innerHTML = htmlArray;
        }
    );
}

function refreshData() {
    document.getElementById('main').innerHTML = '';
    getLianBanChiListData();
}

// 模态窗相关
function openAddStockModal() {
    document.getElementById('addStockModal').style.display = 'block';
}

function closeAddStockModal() {
    document.getElementById('addStockModal').style.display = 'none';
    document.getElementById('stockCodeInput').value = '';
    document.getElementById('stockNameInput').value = '';
}

// 添加股票
function addStock() {
    const code = document.getElementById('stockCodeInput').value;
    const name = document.getElementById('stockNameInput').value || '未知';

    if (!code) {
        alert('请输入股票代码');
        return;
    }

    // 这里应该是向服务器发送请求添加股票
    // 简化处理：直接在表格中添加一行
    const table = document.querySelector('.stock-table tbody');
    $.ajax({

        type: "get",

        url: "addOrEditMyStock",

        data: {
            code: code
        },

        success: function (data) {
            // buildLianBanChiListHtml(data);
            refreshStocks();
        }

    });
    // // 模拟随机价格
    // const basePrice = Math.random() * 100 + 50;
    // const changePercent = (Math.random() - 0.5) * 10;
    //
    // const row = document.createElement('tr');
    // row.innerHTML = `
    //             <td>
    //                 <div class="stock-name">${name}</div>
    //                 <div class="stock-code">${code}</div>
    //             </td>
    //             <td>${code}</td>
    //             <td class="${changePercent >= 0 ? 'price-up' : 'price-down'}">${basePrice.toFixed(2)}</td>
    //             <td class="${changePercent >= 0 ? 'price-up' : 'price-down'}">${changePercent >= 0 ? '+' : ''}${changePercent.toFixed(2)}%</td>
    //             <td class="${changePercent >= 0 ? 'price-up' : 'price-down'}">${changePercent >= 0 ? '+' : ''}${(basePrice * changePercent / 100).toFixed(2)}</td>
    //             <td>12,345</td>
    //             <td>234,567</td>
    //             <td>${(basePrice * 0.98).toFixed(2)}</td>
    //             <td>${(basePrice * 0.97).toFixed(2)}</td>
    //             <td>${(basePrice * 1.01).toFixed(2)}</td>
    //             <td>${(basePrice * 0.96).toFixed(2)}</td>
    //             <td>
    //                 <div class="action-buttons">
    //                     <button onclick="buyStock('${code}')">买入</button>
    //                     <button onclick="sellStock('${code}')">卖出</button>
    //                     <button onclick="deleteStock('${code}')">删除</button>
    //                 </div>
    //             </td>
    //         `;
    //
    // table.appendChild(row);

    closeAddStockModal();
}

// 股票操作
function buyStock(code) {
    alert(`准备买入股票：${code}`);
    // 实际应用中应该跳转到交易页面
}

function sellStock(code) {
    alert(`准备卖出股票：${code}`);
    // 实际应用中应该跳转到交易页面
}

function deleteStock(code) {
    if (confirm('确定要删除这只股票吗？')) {
        $.ajax({

            type: "get",

            url: "deleteMyStock",

            data: {
                code: code
            },

            success: function (data) {
                getListData();
            }

        });
    }
}


function refreshStocks() {
    $.ajax({

        type: "get",

        url: "sysMyStockData",

        data: {},

        success: function (data) {
            getListData();
        }

    });
}

function toggleAutoRefresh() {
    if (isAutoRefreshing) {
        clearInterval(autoRefreshInterval);
        document.querySelector('.refresh-controls button:last-child').textContent = '开启';
    } else {
        autoRefreshInterval = setInterval(refreshStocks, 5000);
        document.querySelector('.refresh-controls button:last-child').textContent = '暂停';
    }

    isAutoRefreshing = !isAutoRefreshing;
}



