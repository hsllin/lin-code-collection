$(function () {
    document.getElementById('mainData').innerHTML = '';
    getYidongData();

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

function getYidongData() {
    $.ajax({

        type: "get",

        url: "getYidongList",

        data: {
        },

        success: function (data) {
            buildYidongHtml(data);
        }

    });
}

function buildYidongHtml(data) {
    var htmlArray = '';

    data.forEach((group, groupIndex) => {
        htmlArray += `<div class="event-card">
            <div class="event-header">
                影响事件
                <span class="stock-tag">${group.stock}</span>
            </div>
            <div class="content-section">
                <div><strong>今日走势：</strong>${group.title}</div>
                <div class="reason-list">
                    <strong>异动原因揭秘：</strong>
                    <div>${group.description} </div>
                <a href="#" class="detail-link">[详细内容]</a>
            </div>
            <div class="stock-info">
                影响个股：${group.stock}
            </div>
            </div>
            </div>
        `;

    });
    document.getElementById('mainData').innerHTML = htmlArray;
    console.log(htmlArray)
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