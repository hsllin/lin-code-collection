let sortDirections = Array(9).fill(true);
let dateIndex = 0;// 初始排序方向
$(function () {
    document.getElementById("marketVolume").innerHTML = '';
    document.getElementById("yzzt").innerHTML = '';
    document.getElementById('yzdt').innerHTML = '';
    getMartketVolume();
    getMarketTimeLine();
    // getMarketYzzt();
    // loadZdtData();
})

function getMartketVolume() {
    $.ajax({

        type: "get",

        url: "getMarketVolume",

        data: {
            dateIndex: dateIndex
        },

        success: function (data) {
            buildMarketVolumeHtml(data);
        }

    });
}

function getMarketTimeLine() {
    $.ajax({

        type: "get",

        url: "getMarketTimeLine",

        data: {
            dateIndex: dateIndex
        },

        success: function (data) {
            loadMarketTimeLineData(data);
        }

    });
}
function getMarketYzzt() {
    $.ajax({

        type: "get",

        url: "getMarketYzzt",

        data: {
            dateIndex: dateIndex
        },

        success: function (data) {
            buildYzzt(data);
        }

    });
}

function buildYzzt(data){
    var yzztHtmlArray='';
    var yzdtHtmlArray='';
    console.log(data.yzzt)
    data.data.yzzt.map(item => {
        yzztHtmlArray += `
          <div className="stock-item">
    <span className="stock-name">${item.name}</span>
    <span className="stock-code">${item.code}</span>
</div>
        `
    });
    data.data.yzdt.map(item => {
        yzdtHtmlArray += `
          <div className="stock-item">
    <span className="stock-name">${item.name}</span>
    <span className="stock-code">${item.code}</span>
</div>
        `
    });
    document.getElementById('yzzt').innerHTML = yzztHtmlArray;
    document.getElementById('yzdt').innerHTML = yzdtHtmlArray;

}



function buildMarketVolumeHtml(data) {

    var volume = data.data.stock;
    var money=data.data.moneyBean;
    console.log(money)
    var temperatureHtml=`市场温度：${data.data.temperature} `;
    var volumeHtml = ` <div class="stat-card">
            <div>总股票数</div>
            <div class="stat-value">${volume.total}</div>
        </div>
        <div class="stat-card">
            <div>涨停/跌停</div>
            <div class="stat-value" style="color:var(--rise-color)">${volume.zt}/${volume.dt}</div>
        </div>
<!--        <div class="stat-card">-->
<!--            <div>涨跌比</div>-->
<!--            <div class="stat-value" style="color:var(&#45;&#45;rise-color)">1.14</div>-->
<!--        </div>-->
        <div class="stat-card">
            <div>涨超5%</div>
            <div class="stat-value" style="color:var(--rise-color)">${volume.up5p}</div>
        </div>
        <div class="stat-card">
            <div>跌超5%</div>
            <div class="stat-value" style="color:var(--fall-color)">${volume.down5p}</div>
        </div>
        <div class="stat-card">
            <div>上涨</div>
            <div class="stat-value" style="color:var(--fall-color)">${volume.up}</div>
        </div>
        <div class="stat-card">
            <div>下跌</div>
            <div class="stat-value" style="color:var(--fall-color)">${volume.down}</div>
        </div>
        <div class="stat-card">
            <div>上证上涨</div>
            <div class="stat-value" style="color:var(--fall-color)">${money.aup}</div>
        </div>
        <div class="stat-card">
            <div>上证下跌</div>
            <div class="stat-value" style="color:var(--fall-color)">${money.adown}</div>
        </div>
        <div class="stat-card">
            <div>深证上涨</div>
            <div class="stat-value" style="color:var(--fall-color)">${money.bup}</div>
        </div>
        <div class="stat-card">
            <div>深证下跌</div>
            <div class="stat-value" style="color:var(--fall-color)">${money.bdown}</div>
        </div>
        <div class="stat-card">
            <div>成交额</div>
            <div class="stat-value" style="color:var(--fall-color)">${money.totalMoney}</div>
        </div>
`;
    var bucketList = volume.buckets;
    // var bucketBarHtml = `<div class="bucket-bar 1color-rise" style="flex:58" title="涨停：58只">${bucketList[0].stockNum}</div>
    //     <div class="bucket-bar " style="flex:${bucketList[1].stockNum}">>8%<span class="count">${bucketList[1].stockNum}</span></div>
    //     <div class="bucket-bar " style="flex:${bucketList[2].stockNum}">5-8%<span class="count">${bucketList[2].stockNum}</div>
    //     <div class="bucket-bar " style="flex:${bucketList[3].stockNum}">2-5%<span class="count">${bucketList[3].stockNum}</div>
    //     <div class="bucket-bar " style="flex:${bucketList[4].stockNum}">0-2%<span class="count">${bucketList[4].stockNum}</div>
    //     <div class="bucket-bar " style="flex:${bucketList[5].stockNum}">0%<span class="count">${bucketList[5].stockNum}</div>
    //     <div class="bucket-bar " style="flex:${bucketList[6].stockNum}">0%-2%<span class="count">${bucketList[6].stockNum}</div>
    //     <div class="bucket-bar " style="flex:${bucketList[7].stockNum}">>-2%-5%<span class="count">${bucketList[7].stockNum}</div>
    //     <div class="bucket-bar " style="flex:${bucketList[8].stockNum}">>-5%-8%<span class="count">${bucketList[8].stockNum}</div>
    //     <div class="bucket-bar" style="flex:${bucketList[9].stockNum}">>-8%<span class="count">${bucketList[9].stockNum}</div>
    //     <div class="bucket-bar " style="flex:${bucketList[10].stockNum}">>-10%${bucketList[10].stockNum}</div>`;
    loadZdtData(bucketList);

    document.getElementById("marketVolume").innerHTML = volumeHtml;
    document.getElementById("temperature").innerHTML=temperatureHtml;
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

function loadMarketTimeLineData(data) {
    const timelineData = data.data.timeline;

    // 处理时间格式
    const formatTime = tm => {
        const str = tm.toString().padStart(4, '0');
        return `${str.slice(0, 2)}:${str.slice(2)}`;
    };

    // 初始化图表
    const chartDom = document.getElementById('timeChart');
    const myChart = echarts.init(chartDom);

    const option = {
        tooltip: {
            trigger: 'axis',
            formatter: params => {
                console.log(params)
                return params.map(item => `
                        时间: ${item.name} <br/>
                        ${item.seriesName}: ${item.value}<br/>
                    `).join('');
            }
        },
        legend: {
            data: [`涨停数`, '跌停数'],
            top: 30
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        xAxis: {
            type: 'category',
            data: timelineData.map(item => formatTime(item.tm)),
            axisLabel: {
                rotate: 45
            }
        },
        yAxis: {
            type: 'value',
            min: value => Math.max(0, value.min - 2)
        },
        series: [
            {
                name: `涨停数`,
                type: 'line',
                smooth: true,
                data: timelineData.map(item => item.zt),
                itemStyle: {color: ['#dd6b66', '#759aa0', '#e69d87', '#8dc1a9', '#ea7e53', '#eedd78', '#73a373', '#73b9bc', '#7289ab', '#91ca8c', '#f49f42']},
                lineStyle: {width: 2}
            },
            {
                name: '跌停数',
                type: 'line',
                smooth: true,
                data: timelineData.map(item => item.dt),
                itemStyle: {color: ['#dd6b66', '#759aa0', '#e69d87', '#8dc1a9', '#ea7e53', '#eedd78', '#73a373', '#73b9bc', '#7289ab', '#91ca8c', '#f49f42']},
                lineStyle: {width: 2}
            }
        ]
    };

    myChart.setOption(option);

    // 窗口resize自适应
    window.addEventListener('resize', () => myChart.resize());
}

function loadZdtData(data){
        // 数据配置
        const distributionData = [
            {
                name: '涨停',
                value: data[0].stockNum,
                itemStyle: { color: '#1981f5' }
            },
            {
                name: '>8%',
                value: data[1].stockNum,
                itemStyle: { color: '#5ea7f8' }
            },
            {
                name: '5-8%',
                value: data[2].stockNum,
                itemStyle: { color: '#72adf0' }
            },
            {
                name: '2-5%',
                value: data[3].stockNum,
                itemStyle: { color: '#aed2f7' }
            },
            {
                name: '0-2%',
                value: data[4].stockNum,
                itemStyle: { color: '#c1d5eb' }
            },
            {
                name: '0%',
                value: data[5].stockNum,
                itemStyle: { color: '#9e9e9e' }
            },
            {
                name: '0-2%',
                value: data[6].stockNum,
                itemStyle: { color: '#e0e0e0' }
            },
            {
                name: '-2-5%',
                value: data[7].stockNum,
                itemStyle: { color: '#b9b9b9' }
            },
            {
                name: '-5-8%',
                value: data[8].stockNum,
                itemStyle: { color: '#a4a5a4' }
            },
            {
                name: '>-8%',
                value: data[9].stockNum,
                itemStyle: { color: '#606560' }
            },
            {
                name: '跌停',
                value: data[10].stockNum,
                itemStyle: { color: '#3c3f3c' }
            }
        ];

        // ECharts配置
        const chart = echarts.init(document.getElementById('distributionChart'));
        const option = {
            tooltip: {
                trigger: 'item',
                formatter: '{b}<br/>数量: {c}'
            },
            grid: {
                left: '8%',
                right: '4%',
                bottom: '8%',
                containLabel: true
            },
            xAxis: {
                type: 'value',
                axisLabel: {
                    color: '#666',
                    formatter: function(value) {
                        return value > 0 ? value : '';
                    }
                },
                splitLine: {
                    show: true,
                    lineStyle: {
                        color: ['#eee'],
                        type: 'dashed'
                    }
                }
            },
            yAxis: {
                type: 'category',
                data: distributionData.map(item => item.name),
                axisTick: { show: false },
                axisLine: { show: false },
                axisLabel: {
                    color: '#444',
                    fontWeight: 500,
                    margin: 12
                },
                inverse: true  // 保持与原始布局一致的排序
            },
            series: [{
                type: 'bar',
                data: distributionData,
                barWidth: 20,
                label: {
                    show: true,
                    position: 'right',
                    color: '#666',
                    formatter: '{c}'
                },
                emphasis: {
                    itemStyle: {
                        shadowBlur: 8,
                        shadowColor: 'rgba(0, 0, 0, 0.2)'
                    }
                },
                animationDuration: 800
            }]
        };

        chart.setOption(option);

        // 响应式处理
        window.addEventListener('resize', function() {
            chart.resize();
        });
}

function refreshData(){
    document.getElementById("marketVolume").innerHTML = '';
    document.getElementById("yzzt").innerHTML = '';
    document.getElementById('yzdt').innerHTML = '';
    getMartketVolume();
    getMarketTimeLine();
}