// 概念标注数据
let conceptMarkers = [
    {time: '9:40', concept: '算力工程', change: '+0%', isPositive: true},
    {time: '10:00', concept: '创新药', change: '+0%', isPositive: true},
    {time: '10:20', concept: 'CPO概念', change: '+0%', isPositive: true},
    {time: '10:40', concept: '机器人', change: '-10%', isPositive: true},
    {time: '11:08', concept: '存储器', change: '+1.65%', isPositive: true}
];
// 生成时间标签
$(function () {
    const ctx = document.getElementById('chartCanvas').getContext('2d');
    document.getElementById('concept-list').innerHTML = '';
    document.getElementById('industry-list').innerHTML = '';
    getHotConceptAndIndustryData();
    getTimeLineData();
    getTimeStockList();

    // 添加控制按钮点击事件
    document.querySelectorAll('.controls button').forEach(button => {
        button.addEventListener('click', function () {
            document.querySelector('.controls button.active').classList.remove('active');
            this.classList.add('active');

            // 这里可以添加不同时间周期的数据加载逻辑
        });
    });

    // 添加概念项点击事件
    document.querySelectorAll('.concept-item').forEach(item => {
        item.addEventListener('click', function () {
            const concept = this.querySelector('.concept-name').textContent;
            alert(`已选择板块: ${concept}`);
        });
    });
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

function convertData(data) {
    return data.map(item => {
        // 提取原数据字段
        const {c_time, symbol_name} = item;
        // 处理时间格式
        const time = c_time ? c_time.split(' ')[1].substring(0, 5) : "00:00";
        // 假设浮点数字段为空时，默认值为0
        const changeValue = item.float !== null ? item.float : 0;
        const change = `+${changeValue}%`;
        const isPositive = changeValue >= 0;

        return {
            time,
            concept: symbol_name,
            change,
            isPositive
        };
    });
}

function getHotConceptAndIndustryData() {
    $.ajax({
        type: "GET",
        url: "getHotConceptAndIndustry",
        // contentType: 'application/json',
        data: {},
        success: function (data) {
            timeLineData = data;
            timeLineLoaded = true;
            renderIndustryAndConcept(data);
        }
    });
}

function renderIndustryAndConcept(data) {
    var conceptHtmlArray = '';
    var industryHtmlArray = '';
    data.concepts.forEach((item, index) => {

        conceptHtmlArray += `
        <li class="concept-item">
                            <span class="concept-name">${item.concepts}(${item.code})</span>
                            <span class="concept-change change-positive">${item.rate}%</span>
                        </li>
  `
    });
    data.industry.forEach((item, index) => {

        industryHtmlArray += `
                     <li class="concept-item">
                            <span class="concept-name">${item.concepts}(${item.code})</span>
                            <span class="concept-change change-positive">${item.rate}%</span>
                        </li>
  `
    });
    document.getElementById('concept-list').innerHTML = conceptHtmlArray;
    document.getElementById('industry-list').innerHTML = industryHtmlArray;
}

function tryRenderTimeLineChart() {
    if (timeLineLoaded && conceptLoaded) {
        renderTimeLineChart();
    }
}

function renderTimeLineChart() {
    if (!timeLineData.length) return;
    // 1. 处理分时数据
    const times = timeLineData.map(item => {
        const m = item.minute.toString().padStart(4, '0');
        return `${m.slice(0, 2)}:${m.slice(2)}`;
    });
    const prices = timeLineData.map(item => item.last_px);
    const basePrice = timeLineData[0]?.last_px || 0;
    const changes = prices.map(p => ((p - basePrice) / basePrice * 100).toFixed(2));
    // 2. 渲染 Chart
    const ctx = document.getElementById('chartCanvas').getContext('2d');
    conceptMarkers = convertData(conceptData)
    // 创建图表
    window.timeLineChart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: times,
            datasets: [{
                label: '涨跌幅',
                data: changes,
                borderColor: '#9198a1',
                backgroundColor: 'rgba(26, 107, 179, 0.1)',
                borderWidth: 2,
                fill: true,
                tension: 0.4,
                pointBackgroundColor: '#fff',
                pointBorderWidth: 2,
                pointRadius: 2,
                pointHoverRadius: 4
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
                    mode: 'index',
                    intersect: false,
                    backgroundColor: 'rgba(0, 0, 0, 0.7)',
                    callbacks: {
                        label: function (context) {
                            return `涨跌幅: ${context.parsed.y}%`;
                        }
                    }
                },
                annotation: {
                    annotations: []
                }
            },
            scales: {
                y: {
                    grid: {
                        color: 'rgba(0, 0, 0, 0.05)'
                    },
                    ticks: {
                        callback: function (value) {
                            // 关键修改：使用toFixed(2)保留两位小数
                            return parseFloat(value).toFixed(2) + '%';
                        }
                    },
                    title: {
                        display: true,
                        text: '涨跌幅'
                    }
                },
                x: {
                    grid: {
                        color: 'rgba(0, 0, 0, 0.05)'
                    }
                }
            }
        }
    });
    //  testData.forEach((marker, index) => {
    //      const timeIndex = chart.data.labels.indexOf(marker.time);
    //      if (timeIndex === -1) return;
    //
    //      const value = chart.data.datasets[0].data[timeIndex];
    //
    //      chart.options.plugins.annotation.annotations.push({
    //          type: 'label',
    //          xValue: marker.time,
    //          yValue: value,
    //          content: [marker.concept, marker.change],
    //          position: 'top',
    //          // backgroundColor: marker.isPositive ? 'rgb(68,99,145)' : 'rgba(46, 155, 86, 0.8)',
    //          // backgroundColor: 'rgba(26, 107, 179, 0.1)',
    //          backgroundColor: marker.isPositive ? 'rgba(74,111,165,0.63)' : 'rgba(46, 155, 86, 0.6)',
    //          color: 'white',
    //          font: {
    //              size: 12,
    //              weight: 'bold'
    //          },
    //          borderRadius: 4,
    //          padding: 6,
    //          xAdjust: 24,
    //          yAdjust: -40,
    //          callout: {
    //              display: true,
    //              side: 'bottom',
    //              length: 10
    //          },
    //
    //          plugins: {
    //              legend: {
    //                  display: false
    //              },
    //              // tooltip: {
    //              //     mode: 'index',
    //              //     intersect: false,
    //              //     backgroundColor: 'rgba(0, 0, 0, 0.7)',
    //              //     callbacks: {
    //              //         label: function(context) {
    //              //
    //              //             const annotations = chart.options.plugins.annotation.annotations;
    //              //             const xValue = chart.data.labels[context.dataIndex];
    //              //             const yValue = context.parsed.y;
    //              //
    //              //             const matchingAnnotations = annotations.filter(annotation => {
    //              //                 console.log('xValue:'+xValue)
    //              //                 console.log('yValue:'+yValue)
    //              //                 console.log('annotation.xValue:'+annotation.xValue)
    //              //                 console.log('annotation.yValue:'+annotation.yValue)
    //              //                 return annotation.xValue === xValue && annotation.yValue === yValue;
    //              //             });
    //              //             return matchingAnnotations.content;
    //              //             // 有匹配的标注内容时，展示标注内容和涨跌幅；否则，仅显示涨跌幅
    //              //             // return matchingAnnotations.length
    //              //             //     ? `${matchingAnnotations[0].content.join(" ")} | 涨跌幅: ${context.parsed.y}%`
    //              //             //     : `涨跌幅: ${context.parsed.y}%`;
    //              //         }
    //              //     }
    //              // },
    //              // tooltip: {
    //              //     mode: 'index',
    //              //     intersect: false,
    //              //     backgroundColor: 'rgba(0, 0, 0, 0.7)',
    //              //     callbacks: {
    //              //         label: function (context) {
    //              //             return `涨跌幅: ${context.parsed.y}%`;
    //              //         }
    //              //     }
    //              // },
    //              annotation: {
    //                  annotations: []
    //              }
    //          },
    //      });
    //  });

    window.timeLineChart.update();
    addConceptAnnotations(conceptMarkers, times, changes)
    // 初始化标注
    // setTimeout(addConceptMarkers(chart), 500);
}

function addConceptAnnotations(conceptData, times, changes) {
    // 清除旧标注
    document.querySelectorAll('.annotation, .time-indicator, .time-label').forEach(el => el.remove());
    const chartWrapper = document.getElementById('chart-container');
    const chart = window.timeLineChart;
    if (!chart) return;
    const xScale = chart.scales.x;
    const yScale = chart.scales.y;
    // 概念标注
    conceptData.forEach(point => {
        // 取 c_time 的分钟部分
        const cTime = point.time;
        const timeStr = cTime
        // 找到 times 中最接近的时间点
        let index = times.indexOf(timeStr);
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
            dot.style.left = `${xPos + 17}px`;
            dot.style.top = `${yPos + 12}px`;
            chartWrapper.appendChild(dot);
            // 竖线
            const line = document.createElement('div');
            line.className = 'annotation-line';
            line.style.left = `${xPos + 20}px`;
            line.style.top = `${yPos - 20}px`;
            line.style.height = `30px`;
            chartWrapper.appendChild(line);
            // 标签
            const annotation = document.createElement('div');
            annotation.className = 'annotation';
            annotation.textContent = point.concept;
            annotation.title = point.concept;
            annotation.style.left = `${xPos}px`;
            annotation.style.top = `${yPos - 50}px`;

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


// 添加概念标注
function addConceptMarkers(chart) {
    conceptMarkers.forEach((marker, index) => {
        const timeIndex = chart.data.labels.indexOf(marker.time);
        if (timeIndex === -1) return;

        const value = chart.data.datasets[0].data[timeIndex];

        chart.options.plugins.annotation.annotations.push({
            type: 'label',
            xValue: marker.time,
            yValue: value,
            content: [marker.concept, marker.change],
            position: 'top',
            backgroundColor: marker.isPositive ? 'rgba(229, 76, 76, 0.8)' : 'rgba(46, 155, 86, 0.8)',
            color: 'white',
            font: {
                size: 12,
                weight: 'bold'
            },
            borderRadius: 4,
            padding: 6,
            xAdjust: 0,
            yAdjust: -20,
            callout: {
                display: true,
                side: 'bottom',
                length: 10
            }
        });
    });

    chart.update();
}

function refreshData() {
    document.getElementById('concept-list').innerHTML = '';
    document.getElementById('industry-list').innerHTML = '';

    const ctx = document.getElementById('chartCanvas').getContext('2d');
    // 清除整个画布内容
    ctx.clearRect(0, 0, ctx.width, ctx.height);
    getHotConceptAndIndustryData();
    getTimeLineData();
    getTimeStockList();
}