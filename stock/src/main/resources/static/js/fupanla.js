$(function () {
    // 初始化渲染
    // window.addEventListener('DOMContentLoaded', renderTable);
    document.getElementById('mainData').innerHTML = '';
    getFuPanLaData();

})
let stockData = [/* 你的JSON数据 */];

function getFuPanLaData() {
    $.ajax({

        type: "get",

        url: "getFuPanLaData",

        data: {},

        success: function (data) {
            console.log(data)
            renderTable(data);
            // document.getElementById("date").text = data[0].tradeDate;
        }

    });
}


function formatCurrency(value) {
    return (value / 1e8).toFixed(2) + '亿';
}

function renderChart(ctx, data) {
    console.log(data)
    return new Chart(ctx, {
        type: 'line',
        data: {
            labels: data.map((_, i) => i),
            datasets: [{
                data: data,
                borderColor: '#1890ff',
                borderWidth: 1,
                tension: 0.4,
                pointRadius: 0
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {legend: {display: false}},
            scales: {
                x: {display: false},
                y: {display: false}
            }
        }
    });
}

function renderTable(data) {
    var htmlArray = '';
    const tbody = document.getElementById('mainData');
    tbody.innerHTML = data;

}

function renderChat() {
    stockData.forEach((stock, index) => {
        const chartCtx = document.getElementById(`chart` + (index + 1)).getContext('2d');
        drawChart(`chart` + (index + 1), stock.kLineList)
    })
}

// 绘制图表
function drawChart(canvasId, data) {

    const canvas = document.getElementById(canvasId);
    const ctx = canvas.getContext('2d');
    const width = canvas.width;
    const height = canvas.height;

    // 计算最小值和最大值
    const min = Math.min(...data);
    const max = Math.max(...data);
    const range = max - min;

    // 绘制背景
    ctx.fillStyle = '#F5F5F5';
    ctx.fillRect(0, 0, width, height);

    // 绘制虚线
    ctx.strokeStyle = 'rgba(0, 0, 0, 0.05)';
    ctx.setLineDash([4, 4]);
    for (let i = 1; i <= 3; i++) {
        const y = height * i / 4;
        ctx.beginPath();
        ctx.moveTo(0, y);
        ctx.lineTo(width, y);
        ctx.stroke();
    }
    ctx.setLineDash([]);

    // 绘制曲线
    ctx.beginPath();
    ctx.moveTo(0, height - ((data[0] - min) / range) * height);

    for (let i = 1; i < data.length; i++) {
        const x = (i / (data.length - 1)) * width;
        const y = height - ((data[i] - min) / range) * height;
        ctx.lineTo(x, y);
    }

    ctx.strokeStyle = '#d2d2d2';
    ctx.lineWidth = 2;
    ctx.stroke();

    // 绘制填充区域
    ctx.beginPath();
    ctx.moveTo(0, height - ((data[0] - min) / range) * height);

    for (let i = 1; i < data.length; i++) {
        const x = (i / (data.length - 1)) * width;
        const y = height - ((data[i] - min) / range) * height;
        ctx.lineTo(x, y);
    }

    ctx.lineTo(width, height);
    ctx.lineTo(0, height);
    ctx.closePath();

    ctx.fillStyle = '#F5F5F5';
    ctx.fill();
    // console.log(ctx)
}

// document.addEventListener('DOMContentLoaded', function () {
//     // 图表1数据
//     const data1 = [1401.941, 1223.356, 1238.357, 1274.281, 1314.065, 1332.564, 1359.712, 1366.218, 1345.689, 1352.862, 1356.975, 1370.264, 1362.631, 1363.629, 1344.58, 1339.392, 1315.046, 1326.147, 1346.879, 1386.745, 1391.412, 1399.594, 1384.287, 1404.92, 1402.462, 1409.379, 1391.662, 1401.152, 1455.329, 1483.765];
//
//     // 图表2数据
//     const data2 = [1527.895, 1318.239, 1321.807, 1369.543, 1409.057, 1425.604, 1443.165, 1436.57, 1418.517, 1423.362, 1432.138, 1460.471, 1464.268, 1480.641, 1448.406, 1455.088, 1449.466, 1458.828, 1491.258, 1570.24, 1572.948, 1596.757, 1576.547, 1605.32, 1589.036, 1602.113, 1562.274, 1573.216, 1582.867, 1590.413];
//
//     // 绘制图表
//     function drawChart(canvasId, data) {
//         const canvas = document.getElementById(canvasId);
//         const ctx = canvas.getContext('2d');
//         const width = canvas.width;
//         const height = canvas.height;
//
//         // 计算最小值和最大值
//         const min = Math.min(...data);
//         const max = Math.max(...data);
//         const range = max - min;
//
//         // 绘制背景
//         ctx.fillStyle = '#fff';
//         ctx.fillRect(0, 0, width, height);
//
//         // 绘制虚线
//         ctx.strokeStyle = 'rgba(0, 0, 0, 0.05)';
//         ctx.setLineDash([4, 4]);
//         for (let i = 1; i <= 3; i++) {
//             const y = height * i / 4;
//             ctx.beginPath();
//             ctx.moveTo(0, y);
//             ctx.lineTo(width, y);
//             ctx.stroke();
//         }
//         ctx.setLineDash([]);
//
//         // 绘制曲线
//         ctx.beginPath();
//         ctx.moveTo(0, height - ((data[0] - min) / range) * height);
//
//         for (let i = 1; i < data.length; i++) {
//             const x = (i / (data.length - 1)) * width;
//             const y = height - ((data[i] - min) / range) * height;
//             ctx.lineTo(x, y);
//         }
//
//         ctx.strokeStyle = '#FF3B30';
//         ctx.lineWidth = 2;
//         ctx.stroke();
//
//         // 绘制填充区域
//         ctx.beginPath();
//         ctx.moveTo(0, height - ((data[0] - min) / range) * height);
//
//         for (let i = 1; i < data.length; i++) {
//             const x = (i / (data.length - 1)) * width;
//             const y = height - ((data[i] - min) / range) * height;
//             ctx.lineTo(x, y);
//         }
//
//         ctx.lineTo(width, height);
//         ctx.lineTo(0, height);
//         ctx.closePath();
//
//         ctx.fillStyle = 'rgba(255, 59, 48, 0.1)';
//         ctx.fill();
//         console.log(ctx)
//     }
//
//     drawChart('chart1', data1);
//     drawChart('chart2', data2);
// });

