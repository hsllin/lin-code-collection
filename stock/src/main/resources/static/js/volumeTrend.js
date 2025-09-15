$(function () {
    document.getElementById('chart').innerHTML = '';
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

        data: {
        },

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
    document.getElementById('order').innerHTML='';
    document.getElementById('chart').resize()
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

function loadData(data){
    // 原始数据（已按日期升序排列）
    const rawData = data; // 反转数组使日期升序

    // 准备数据
    const dates = rawData.map(item => item.date);
    const volumes = rawData.map(item => item.volume);
    const limitUp = rawData.map(item => item.limitUp);
    const limitDown = rawData.map(item => item.limitDown);
    const limitCount = rawData.map(item => item.limitCount);
    const inputUp = rawData.map(item => item.inputUp);
    const inputDown = rawData.map(item => item.inputDown);

    // 初始化图表
    const chart = echarts.init(document.getElementById('chart'));

    const option = {
        tooltip: {
            trigger: 'axis',
            axisPointer: { type: 'cross' },
            formatter: params => {
                let res = `${params[0].axisValue}<br>`;
                params.forEach(p => {
                    let value = p.value;
                    if(p.seriesName.includes('volume')) {
                        value = (value/1e12).toFixed(2) + '万亿';
                    }
                    res += `${p.marker} ${p.seriesName}: ${value}<br>`;
                });
                return res;
            }
        },
        legend: { top: 20 },
        xAxis: {
            type: 'category',
            data: dates,
            axisLabel: { rotate: 45 }
        },
        yAxis: [
            { // 左侧Y轴（涨停组）
                type: 'value',
                name: '涨停组数量',
                position: 'left'
            },
            { // 右侧Y轴（涨跌数量）
                type: 'value',
                name: '涨跌数量',
                position: 'right',
                offset: 40
            },
            { // 右侧Y轴（成交量）
                type: 'value',
                name: '成交额(万亿)',
                position: 'right',
                axisLabel: {
                    formatter: value => (value/1e12).toFixed(1)
                }
            }
        ],
        series: [
            { // 成交量背景层
                name: '成交量',
                type: 'bar',
                yAxisIndex: 2,
                itemStyle: { color: 'rgba(100,149,237,0.3)' },
                data: volumes
            },
            // 涨停组数据
            {
                name: '涨停数量',
                type: 'line',
                smooth: true,
                symbol: 'circle',
                itemStyle: { color: '#e74c3c' },
                data: limitUp
            },
            {
                name: '跌停数量',
                type: 'line',
                smooth: true,
                symbol: 'triangle',
                itemStyle: { color: '#2ecc71' },
                data: limitDown
            },
            {
                name: '连板高度',
                type: 'line',
                smooth: true,
                symbol: 'rect',
                itemStyle: { color: '#f39c12' },
                data: limitCount
            },
            // 涨跌数量组
            {
                name: '上涨数量',
                type: 'line',
                yAxisIndex: 1,
                smooth: true,
                symbol: 'none',
                lineStyle: { color: '#a2d9a6', width: 2 },
                areaStyle: { color: 'rgba(162,217,166,0.3)' },
                data: inputUp
            },
            {
                name: '下跌数量',
                type: 'line',
                yAxisIndex: 1,
                smooth: true,
                symbol: 'none',
                lineStyle: { color: '#f5b7b1', width: 2 },
                areaStyle: { color: 'rgba(245,183,177,0.3)' },
                data: inputDown
            }
        ]
    };

    chart.setOption(option);
    window.addEventListener('resize', () => chart.resize());
}