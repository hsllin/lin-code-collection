$(function () {
    // document.getElementById('dataTable').innerHTML = '';
    // getRpsData();
    var blockName = $("#blockName").val()
    getRpsData(blockName);


})

// $(document).on('contentLoaded', function() {
//     console.log("动态内容已加载");
// });
document.addEventListener('DOMContentLoaded', function () {
    const table = document.querySelector("table");
    console.log(table)
    const headers = table.querySelectorAll('th');
    let sortedColumn = '';
    let ascending = true;

    // 为所有列添加排序功能
    headers.forEach(header => {
        header.addEventListener('click', () => {
            const currentColumn = Array.from(headers).indexOf(header);
            const tbody = table.querySelector('tbody');
            const rows = Array.from(tbody.querySelectorAll('tr'));

            // 移除所有列的排序样式
            headers.forEach(h => h.classList.remove('active', 'desc'));

            // 如果点击的是当前已经排序的列，则切换排序方向
            if (currentColumn === sortedColumn) {
                ascending = !ascending;
            } else {
                sortedColumn = currentColumn;
                ascending = true;
            }

            // 为当前排序的列添加样式
            header.classList.add('active');
            if (ascending) {
                header.classList.remove('desc');
            } else {
                header.classList.add('desc');
            }

            // 根据不同列类型进行排序
            rows.sort((a, b) => {
                const aValue = a.children[currentColumn].textContent;
                const bValue = b.children[currentColumn].textContent;

                // 处理百分比
                if (currentColumn === 4) { // 涨跌幅列
                    const aRate = parseFloat(aValue.replace('%', ''));
                    const bRate = parseFloat(bValue.replace('%', ''));
                    return ascending ? aRate - bRate : bRate - aRate;
                }

                // 处理数值型
                if (currentColumn >= 5 && currentColumn <= 7) { // 成交额、总市值、RPS 列
                    const aNum = parseFloat(aValue) || 0;
                    const bNum = parseFloat(bValue) || 0;
                    return ascending ? aNum - bNum : bNum - aNum;
                }

                // 处理序号和代码
                if (currentColumn === 0 || currentColumn === 1) {
                    const aCode = parseInt(aValue.replace(/\D/g, ''));
                    const bCode = parseInt(bValue.replace(/\D/g, ''));
                    return ascending ? aCode - bCode : bCode - aCode;
                }

                // 默认按字母排序
                return ascending ? aValue.localeCompare(bValue) : bValue.localeCompare(aValue);
            });

            // 重新插入排序后的行
            rows.forEach(row => {
                tbody.appendChild(row);
            });
        });
    });
});

function getRpsData(blockName) {
    $.ajax({

        type: "get",

        url: "getRpsDataDetail",

        data: {
            blockName: blockName
        },

        success: function (reuslt) {
            renderTable(reuslt);
            // $(document).trigger('contentLoaded');
        }

    });
}
function formatTurnover(value) {
    return (value / 100000).toFixed(1) + '亿';
}
function renderTable(data) {
    const tableBody = document.getElementById('dataTable');
    tableBody.innerHTML = ''; // Clear existing rows

    // Populate the table
    data.forEach((item, index) => {
        const row = document.createElement('tr');
        if (item.block_name === '优刻') {
            row.classList.add('highlight');
        }
        row.innerHTML = `
                       <tr>
                            <td>${index+1}</td>
                            <td>${item.symbol}</td>
                            <td>${item.name}</td>
                            <td>${item.close}</td>
                            <td class="positive">${item.day_rate}%</td>
                            <td>${formatTurnover(item.turnover_value)}</td>
                            <td>${formatTurnover(item.t_market_cap)}</td>
                            <td>${item.industry}</td>
                            <td>${item.rps10}</td>
                            <td>${item.rps20}</td>
                            <td>${item.rps50}</td>
                            <td>${item.rps120}</td>
                            <td>${item.rps250}</td>
<!--                            <td>-->
<!--                                <button class="operation-btn">加自选</button>-->
<!--                            </td>-->
                        </tr>
                `;
        tableBody.appendChild(row);
    });
}





