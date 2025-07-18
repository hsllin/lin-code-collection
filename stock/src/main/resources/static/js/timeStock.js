$(function () {
    getTimeLineData();
    getTimeStockList();
    setInterval(updateDateTime, 1000);
    updateDateTime();
    document.getElementById('concepts').innerHTML = '';
    document.getElementById('industry').innerHTML = '';
    getHotConceptAndIndustryData();
});

let timeLineData = [];
let conceptData = [];
let timeLineLoaded = false;
let conceptLoaded = false;

function getTimeLineData() {
    $.ajax({
        type: "GET",
        url: "getTimeStockLine",
        contentType: 'application/json',
        data: {},
        success: function (data) {
            timeLineData = data;
            timeLineLoaded = true;
            tryRenderTimeLineChart();
        }
    });
}

function getTimeStockList() {
    $.ajax({
        type: "GET",
        url: "getTimeStockList",
        contentType: 'application/json',
        data: {},
        success: function (data) {
            conceptData = data;
            conceptLoaded = true;
            tryRenderTimeLineChart();
        }
    });
}

function getHotConceptAndIndustryData() {
    $.ajax({
        type: "GET",
        url: "getHotConceptAndIndustry",
        contentType: 'application/json',
        data: {},
        success: function (data) {
            timeLineData = data;
            timeLineLoaded = true;
            console.log(data)
            renderIndustryAndConcept(data);
        }
    });
}
function renderIndustryAndConcept(data) {
    var conceptHtmlArray = '';
    var industryHtmlArray = '';
    data.concepts.forEach((item, index) => {

        conceptHtmlArray += `
        <div class="sector-item">
                        <div class="sector-name">${item.concepts}(${item.code})</div>
                        <div class="sector-change positive">${item.rate}%</div>
                    </div>
  `
    });
    data.industry.forEach((item, index) => {

        industryHtmlArray += `
        <div class="sector-item">
                       <div class="sector-name">${item.concepts}(${item.code})</div>
                        <div class="sector-change positive">${item.rate}%</div>
                    </div>
  `
    });
    document.getElementById('concepts').innerHTML = conceptHtmlArray;
    document.getElementById('industry').innerHTML = industryHtmlArray;
}

function tryRenderTimeLineChart() {
    if (timeLineLoaded && conceptLoaded) {
        renderTimeLineChart();
    }
}

function renderTimeLineChart() {
    console.log(timeLineData)
    if (!timeLineData.length) return;
    // 1. 处理分时数据
    const times = timeLineData.map(item => {
        const m = item.minute.toString().padStart(4, '0');
        return `${m.slice(0, 2)}:${m.slice(2)}`;
    });
    console.log(times)
    const prices = timeLineData.map(item => item.last_px);
    const basePrice = timeLineData[0]?.last_px || 0;
    console.log(prices)
    const changes = prices.map(p => ((p - basePrice) / basePrice * 100).toFixed(2));
    console.log(changes)
    // 2. 渲染 Chart
    const ctx = document.getElementById('chart').getContext('2d');
    if (window.timeLineChart) window.timeLineChart.destroy();
    window.timeLineChart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: times,
            datasets: [{
                label: '上证指数',
                data: changes,
                borderColor: 'rgba(145,152,161,0.43)',
                borderWidth: 3,
                pointRadius: 0,
                fill: true,
                tension: 0.3
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {display: false},
                tooltip: {
                    mode: 'index',
                    intersect: false,
                    callbacks: {
                        label: function (context) {
                            const index = context.dataIndex;
                            const value = prices[index];
                            const change = changes[index];
                            const changeText = change >= 0 ? `+${change}%` : `${change}%`;
                            return `上证指数: ${value.toFixed(2)} (${changeText})`;
                        }
                    }
                }
            },
            scales: {
                x: {
                    grid: {color: 'rgba(255,255,255,0.05)'},
                    ticks: {
                        color: '#94a3b8',
                        maxRotation: 0,
                        callback: function (value, index) {
                            const time = this.getLabelForValue(value);
                            return time.endsWith(':00') || time === '09:30' || time === '11:30' || time === '15:00' ? time : '';
                        }
                    }
                },
                y: {
                    position: 'right',
                    grid: {color: 'rgba(255,255,255,0.05)'},
                    ticks: {
                        color: '#94a3b8',
                        callback: value => value + '%'
                    }
                }
            },
            interaction: {mode: 'index', intersect: false}
        }
    });
    // 3. 概念标注
    addConceptAnnotations(times, changes);
}

function addConceptAnnotations(times, changes) {
    // 清除旧标注
    document.querySelectorAll('.annotation, .time-indicator, .time-label').forEach(el => el.remove());
    const chartWrapper = document.getElementById('chart-wrapper');
    const chart = window.timeLineChart;
    if (!chart) return;
    const xScale = chart.scales.x;
    const yScale = chart.scales.y;

    // 概念标注
    conceptData.forEach(point => {
        // 取 c_time 的分钟部分
        const cTime = point.c_time || point.cTime;
        const timeStr = cTime.slice(11, 16); // "09:30"
        // 找到 times 中最接近的时间点
        let index = times.indexOf(timeStr);
        console.log(index)
        if (index === -1) {
            let minDiff = 9999, minIdx = -1;
            times.forEach((t, i) => {
                const diff = Math.abs(
                    parseInt(t.replace(':', ''), 10) -
                    parseInt(timeStr.replace(':', ''), 10)
                );
                if (diff < minDiff) {
                    minDiff = diff;
                    minIdx = i;
                }
            });
            index = minIdx;
        }
        if (index !== -1) {
            const xPos = xScale.getPixelForValue(index);
            const yVal = Number(changes[index]);
            const yPos = yScale.getPixelForValue(yVal);

            // 红点
            const dot = document.createElement('div');
            dot.className = 'annotation-dot';
            dot.style.left = `${xPos + 36}px`;
            dot.style.top = `${yPos +36}px`;
            chartWrapper.appendChild(dot);

            // 竖线
            const line = document.createElement('div');
            line.className = 'annotation-line';
            line.style.left = `${xPos + 36}px`;
            line.style.top = `${yPos + 8}px`;
            line.style.height = `28px`;
            chartWrapper.appendChild(line);

            // 标签
            const annotation = document.createElement('div');
            annotation.className = 'annotation';
            annotation.textContent = point.symbol_name;
            annotation.title = point.symbol_name;
            annotation.style.left = `${xPos}px`;
            annotation.style.top = `${yPos - 20}px`;
            annotation.onclick = function () {
                window.open(point.schema, '_blank');
            };
            chartWrapper.appendChild(annotation);
        }
    });
}

// 更新日期时间
function updateDateTime() {
    const now = new Date();
    const dateStr = now.toLocaleDateString('zh-CN', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        weekday: 'long'
    });
    const timeStr = now.toLocaleTimeString('zh-CN');
    document.getElementById('current-date').textContent = dateStr;
    document.getElementById('current-time').textContent = timeStr;
    document.getElementById('update-time').textContent = now.toLocaleString('zh-CN');
}

// 下面保留原有的表格渲染和辅助函数
let stockData = [];

function formatCurrency(value) {
    return (value / 1e8).toFixed(2) + '亿';
}

