let sortDirections = Array(9).fill(true);
let dateIndex = 0;// 初始排序方向
$(function () {
    document.getElementById('dataTable').innerHTML = '';
    getInflowAndOutFlowRankData();
    getInflowAndOutFlowListData();

    // Tab切换功能
    const tabs = document.querySelectorAll('.nav-tab');
    tabs.forEach(tab => {
        tab.addEventListener('click', function () {
            tabs.forEach(t => t.classList.remove('active'));
            this.classList.add('active');
            // 在这里可以添加加载对应板块数据的逻辑
        });
    });

    // 下拉菜单功能
    const select = document.querySelector('.dropdown-select');
    select.addEventListener('change', function () {
        // 在这里可以添加切换资金流入/流出的逻辑
        console.log('切换到: ' + this.value);
    });


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

function getInflowAndOutFlowRankData() {
    $.ajax({

        type: "get",

        url: "getInflowAndOutFlowRankData",

        data: {
            dateIndex: dateIndex
        },

        success: function (data) {
            buildInflowAndOutFlowRankHtml(data);
        }

    });
}

function getInflowAndOutFlowListData() {
    $.ajax({

        type: "get",

        url: "getInflowAndOutFlowListData",

        data: {
            dateIndex: dateIndex
        },

        success: function (data) {
            renderChart(data);
        }

    });
}

function buildInflowAndOutFlowRankHtml(data) {
    console.log(data)
    var htmlArray = '';

    data.forEach((item, index) => {
        htmlArray += `<tr>
                    <td>${index + 1}</td>
                    <td>${item.name}</td>
                    <td>${item.symbol}</td>
                    <td>${item.lastPx}</td>
                    <td >${item.pxChangeRate}</td>
                    <td >${formatToYi(item.mainNetTurnover)}</td>
                    <td >${formatToYi(item.superNetTurnover)}</td>
                    <td >${formatToYi(item.largeNetTurnover)}</td>
                    <td >${formatToYi(item.mediumNetTurnover)}</td>
                    <td >${formatToYi(item.littleNetTurnover)}</td>
                </tr>`


    });
    document.getElementById('dataTable').innerHTML = htmlArray;
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

function renderChart(content) {
    console.log(content)
    // 资金流向柱状图
    const ctx = document.getElementById('fundsChart').getContext('2d');

    // 模拟数据
    const sectors = [];

    const netInflow = [];
    content.forEach((item,index)=>{
        sectors[index]=item.name
        netInflow[index]=item.pure_in/1000000000
    })

    const chart = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: sectors,
            datasets: [{
                label: '净流入(亿元)',
                data: netInflow,
                // backgroundColor: '#e74c3c',
                backgroundColor:(ctx) => {
                    const value = ctx.raw; // 获取当前数据值
                    return value >= 0
                        ? 'rgba(54, 162, 235, 0.8)'  // 蓝色（正数）
                        : 'rgba(75, 192, 192, 0.8)'; // 绿色（负数）
                },
                borderColor: '#e74c3c',
                borderWidth: 0,
                borderRadius: 4
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    display: false
                },
                tooltip: {
                    callbacks: {
                        label: function (context) {
                            return context.dataset.label + ': ' + context.raw + ' 亿元';
                        }
                    }
                }
            },
            scales: {
                y: {
                    beginAtZero: true,
                    title: {
                        display: true,
                        text: '净流入(亿元)'
                    }
                },
                x: {
                    title: {
                        display: true,
                        text: '行业板块'
                    }
                }
            }
        }
    });
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