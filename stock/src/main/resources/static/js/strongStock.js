let sortDirections = Array(9).fill(true);
let dateIndex = 0;// 初始排序方向
$(function () {
    document.getElementById('app').innerHTML = '';
    getStrongStockData();

})

function downLoadData() {
    $.ajax({

        type: "get",

        url: "downloadStrongStockData",

        data: {
            dateIndex: dateIndex
        },

        success: function (data) {
        }

    });
}

function getStrongStockData() {
    $.ajax({

        type: "get",

        url: "getStrongStockList",

        data: {
            dateIndex: dateIndex
        },

        success: function (data) {
            buildStrongStockHtml(data);
        }

    });
}

function buildStrongStockHtml(data) {
    var htmlArray = '';

    data.forEach((group, groupIndex) => {
        htmlArray += `<div class="concept-card">
                <div class="concept-header">
                    <div class="concept-title">
                        ${group.name}(${group.code})
                        <span class="change-rate">(${group.change}%)</span>
                    </div>
                    <div>
                        <span class="tag">涨停股: ${group.limit_up_num}</span>
                        <span class="tag">连板数: ${group.continuous_plate_num}</span>
                    </div>
                </div>

                <table class="stock-table">
                    <thead>
                    <tr>
                        <th>名称</th>
                        <th>代码</th>
                        <th>连板</th>
                        <th>最新价</th>
                        <th>概念标签</th>
                        <th>涨停原因</th>
                    </tr>
                    </thead>
                    <tbody>`

        group.stock_list.forEach((stock, stockIndex) => {
                htmlArray += `  
            <tr>
            <td class="limit-up">${stock.name}</td>
            <td>${stock.code}</td>
            <td>${stock.continue_num}连板</td>
            <td>${stock.latest}</td>
            <td>
                <div class="tag">${stock.reason_type}
                </div>
            </td>
           <td class="reason-cell" 
            @click="showFullReason(stock.reason_info)"
            @mouseleave="activeTooltip = null">
            <div class="reason-preview">
                ${stock.reason_info}
            </div>
            <div class="reason-tooltip" v-if="activeTooltip === stock.code">
                ${stock.reason_info}
            </div>
        </td>
        </tr>`;
            }
        );
        htmlArray += `</tbody></table></div>`;

    });
    document.getElementById('app').innerHTML = htmlArray;
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