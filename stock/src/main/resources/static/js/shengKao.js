var type = '1';
var keyword = '';
let currentTimeFilter = 'all';
$(function () {
    document.getElementById('tableBody').innerHTML = '';

    const tableBody = document.getElementById('tableBody');
    const searchBtn = document.getElementById('searchBtn');
    const resetBtn = document.getElementById('resetBtn');
    const jobCodeInput = document.getElementById('jobCode');
    const resultInfo = document.getElementById('resultInfo');
    const timeBtns = document.querySelectorAll('.time-btn');
    const showChartBtn = document.getElementById('showChartBtn');
    const chartContainer = document.getElementById('chartContainer');


    // let currentData = [...jobData];
    let trendChart = null;

// 查询功能
    searchBtn.addEventListener('click', function () {
        const searchCode = jobCodeInput.value.trim();
        keyword = searchCode;
        getData();
    });

// 重置功能
    resetBtn.addEventListener('click', function () {
    });

// 支持按Enter键查询
    jobCodeInput.addEventListener('keyup', function (event) {
        if (event.key === 'Enter') {
            searchBtn.click();
        }
    });

// 时间筛选功能
    timeBtns.forEach(btn => {
        btn.addEventListener('click', function () {
            timeBtns.forEach(b => b.classList.remove('active'));
            this.classList.add('active');
            currentTimeFilter = this.getAttribute('data-time');
            type=currentTimeFilter;
            getData();
        });
    });
    // 初始化表格
    // renderTable(currentData);
    getData();
})

function getData() {
    $.ajax({

        type: "get",

        url: "getShengKaoDataList",

        data: {
            type: type,
            keyword: keyword
        },

        success: function (data) {
            buildHtml(data);
            keyword='';
        }

    });
}

function sysnData() {
    $.ajax({

        type: "get",

        url: "getShengKaoDataList",

        data: {
            type: type,
            keyword: keyword
        },

        success: function (data) {
            buildHtml(data);
            keyword='';
        }

    });
}

function buildHtml(data) {
    console.log(data)
    var htmlArray = '';
    data.forEach((group, groupIndex) => {
            htmlArray += `
            <tr>
                        <td>${groupIndex + 1}</td>
                        <td>${group.recruitmentUnit}</td>
                        <td>${group.recruitmentPosition}</td>
                        <td>${group.positionCode}</td>
                        <td>${group.needNum}</td>
                        <td>${group.a10}</td>
                        <td>${group.a20}</td>
                        <td>${group.a30}</td>
                        <td>${group.a40}</td>
                    </tr>
                    `;

        }
    );
    console.log(data);
    document.getElementById('tableBody').innerHTML = htmlArray;
}

function refreshData() {
    document.getElementById('tableBody').innerHTML = '';
    getData();
}

// 计算趋势和变化量
function calculateTrend(data) {
    const times = Object.keys(data);
    const firstCount = data[times[0]];
    const lastCount = data[times[times.length - 1]];
    const change = lastCount - firstCount;

    if (change > 0) {
        return {trend: 'up', change: `+${change}`};
    } else if (change < 0) {
        return {trend: 'down', change: change.toString()};
    } else {
        return {trend: 'stable', change: '0'};
    }
}

// 渲染表格函数
function renderTable(data) {
    tableBody.innerHTML = '';

    if (data.length === 0) {
        tableBody.innerHTML = '<tr><td colspan="10" class="no-results">未找到匹配的岗位信息</td></tr>';
        resultInfo.textContent = '未找到匹配的岗位信息';
        return;
    }

    data.forEach(job => {
        const row = document.createElement('tr');
        const trendInfo = calculateTrend(job.data);

        row.innerHTML = `
                        <td>${job.id}</td>
                        <td>${job.unit}</td>
                        <td>${job.position}</td>
                        <td>${job.code}</td>
                        <td>${job.data["2025-10-21 12:00"]}</td>
                        <td>${job.data["2025-10-21 18:00"]}</td>
                        <td>${job.data["2025-10-22 12:00"]}</td>
                        <td>${job.data["2025-10-22 18:00"]}</td>
                        <td class="trend-${trendInfo.trend}">
                            ${trendInfo.trend === 'up' ? '↑' : trendInfo.trend === 'down' ? '↓' : '→'}
                        </td>
                        <td class="trend-${trendInfo.trend}">${trendInfo.change}</td>
                    `;
        tableBody.appendChild(row);
    });

    resultInfo.textContent = `显示 ${data.length} 条记录`;
}


// 应用时间筛选
function applyTimeFilter() {
    if (currentTimeFilter === 'all') {
        renderTable(currentData);
    } else {
        // 这里可以添加更复杂的时间筛选逻辑
        // 目前简单显示所有数据，但可以高亮特定时间列
        renderTable(currentData);
    }
}

// 显示图表功能
showChartBtn.addEventListener('click', function () {
    if (chartContainer.style.display === 'block') {
        chartContainer.style.display = 'none';
        showChartBtn.textContent = '显示图表';
    } else {
        chartContainer.style.display = 'block';
        showChartBtn.textContent = '隐藏图表';
        renderChart();
    }
});

// 渲染趋势图表
function renderChart() {
    const ctx = document.getElementById('trendChart').getContext('2d');

    if (trendChart) {
        trendChart.destroy();
    }

    // 准备图表数据
    const times = ["2025-10-21 12:00", "2025-10-21 18:00", "2025-10-22 12:00", "2025-10-22 18:00"];
    const datasets = currentData.map(job => {
        const color = getRandomColor();
        return {
            label: `${job.unit} - ${job.position}`,
            data: times.map(time => job.data[time]),
            borderColor: color,
            backgroundColor: color + '20', // 添加透明度
            tension: 0.1,
            pointRadius: 4
        };
    });

    trendChart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: times.map(t => t.split(' ')[1]), // 只显示时间部分
            datasets: datasets
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                y: {
                    beginAtZero: true,
                    title: {
                        display: true,
                        text: '报名人数'
                    }
                },
                x: {
                    title: {
                        display: true,
                        text: '统计时间'
                    }
                }
            },
            plugins: {
                legend: {
                    position: 'top',
                },
                title: {
                    display: true,
                    text: '岗位报名人数变化趋势'
                }
            }
        }
    });
}

// 生成随机颜色
function getRandomColor() {
    const letters = '0123456789ABCDEF';
    let color = '#';
    for (let i = 0; i < 6; i++) {
        color += letters[Math.floor(Math.random() * 16)];
    }
    return color;
}


