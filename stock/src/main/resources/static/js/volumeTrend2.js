$(function () {
    // document.getElementById('mainChart').innerHTML = '';
    loadEchartsData();

})

function updateVolumeTrendData() {
    $.ajax({

        type: "get",

        url: "updateVolumeTrendData",

        data: {},

        success: function (data) {
        }

    });
}

function loadEchartsData() {
    $.ajax({

        type: "get",

        url: "getVolumeTrendData",

        data: {},

        success: function (data) {
            loadData(data);
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
    // document.getElementById('chart').innerHTML = '';
    loadEchartsData();
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

function loadData(result) {
    const rawData = result;
    console.log(result)
    // 数据预处理
    const data = rawData.map(item => ({
        date: item.date,
        volume: (item.volume / 1e8).toFixed(2),  // 转换为亿单位
        limitUp: item.limitUp,
        limitDown: item.limitDown,
        limitCount: item.limitCount,
        inputUp: item.inputUp,
        inputDown: item.inputDown,
        limit1: item.limit1,
        limit2: item.limit2,
        limit3: item.limit3,
        limit4: item.limit4,
        limit5: item.limit5
    }));  // 按日期正序排列

    // 生成表格
    const tbody = document.getElementById('order');
    data.slice(0, 20).forEach(item => {
        const row = document.createElement('tr');
        row.innerHTML = `
                <td>${item.date}</td>
                <td>${item.volume}</td>
                <td class="positive">${item.limitUp}</td>
                <td class="negative">${item.limitDown}</td>
                <td>${item.limitCount}</td>
                <td class="positive">${item.inputUp}</td>
                <td class="negative">${item.inputDown}</td>
                <td>${item.limit1}</td>
                <td>${item.limit2}</td>
                <td>${item.limit3}</td>
                <td>${item.limit4}</td>
                <td>${item.limit5}</td>
            `;
        tbody.appendChild(row);
    });

    // 初始化ECharts
    const chart = echarts.init(document.getElementById('mainChart'));

    const option = {
        title: {text: '股票市场趋势分析', left: 'center'},
        tooltip: {trigger: 'axis'},
        legend: {
            data: ['成交金额', '涨停数量', '跌停数量', '上涨个股', '下跌个股'],
            top: 30
        },
        grid: {top: 100, bottom: 80},
        xAxis: {
            type: 'category',
            data: data.reverse().map(item => item.date),
            axisLabel: {rotate: 45}
        },
        yAxis: [
            {type: 'value', name: '金额（亿）'},
            {type: 'value', name: '个股数量'}
        ],
        series: [
            {
                name: '成交金额',
                type: 'bar',
                yAxisIndex: 0,
                data: data.map(item => item.volume),
                itemStyle: {color: '#63b7ee'}
            },
            {
                name: '涨停数量',
                type: 'line',
                yAxisIndex: 1,
                data: data.map(item => item.limitUp * 10),
                itemStyle: {color: '#f14040'},
                tooltip: {
                    valueFormatter: function (value) {
                        return value/10 + "自定义内容";
                    },
                },
            },
            {
                name: '跌停数量',
                type: 'line',
                yAxisIndex: 1,
                data: data.map(item => item.limitDown * 10),
                itemStyle: {color: '#2ecc71'},

            },
            {
                name: '上涨个股',
                type: 'line',
                yAxisIndex: 1,
                data: data.map(item => item.inputUp),
                itemStyle: {color: '#e67e22'}
            },
            {
                name: '下跌个股',
                type: 'line',
                yAxisIndex: 1,
                data: data.map(item => item.inputDown),
                itemStyle: {color: '#9b59b6'}
            }
        ],
        dataZoom: [{
            type: 'inside',
            start: 50,
            end: 100
        }]
    };

    chart.setOption(option);

    // 窗口大小改变时自适应
    window.addEventListener('resize', () => chart.resize());
}