let sortDirections = Array(9).fill(true); // 初始排序方向
let whichDay=1;

$(function () {
    document.getElementById('dataTable').innerHTML = '';
    getLimitUpTodayData();

    // 切换按钮事件
    $('#btn-today').on('click', function() {
        setActiveBtn('btn-today');
        document.getElementById('dataTable').innerHTML = '';
        whichDay=1;
        getLimitUpTodayData();
    });
    $('#btn-yesterday').on('click', function() {
        setActiveBtn('btn-yesterday');
        document.getElementById('dataTable').innerHTML = '';
        whichDay=2;
        getLimitUpYesterDayData();
    });

    function setActiveBtn(activeId) {
        $('#btn-today').removeClass('active');
        $('#btn-yesterday').removeClass('active');
        $('#' + activeId).addClass('active');
    }

    // const refreshBtn = document.getElementById('refreshBtn');
    // $(document).on('click', '#refreshBtn', function () {
    //     // 添加刷新动画
    //     refreshBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> 刷新中...';
    //     refreshBtn.disabled = true;
    //
    //     // 模拟API请求延迟
    //     setTimeout(function () {
    //         document.getElementById('dataTable').innerHTML = '';
    //         if (whichDay===1){
    //             getLimitUpTodayData();
    //         }else {
    //             getLimitUpYesterDayData();
    //         }
    //
    //         // 更新最后更新时间
    //         updateTime();
    //
    //         // 恢复按钮状态
    //         refreshBtn.innerHTML = '<i class="fas fa-sync-alt"></i> 刷新数据';
    //         refreshBtn.disabled = false;
    //
    //         // 添加视觉反馈
    //         refreshBtn.style.background = 'linear-gradient(90deg, #0d9488, #0ea5e9)';
    //         setTimeout(() => {
    //             refreshBtn.style.background = 'linear-gradient(90deg, #0ea5e9, #0d9488)';
    //         }, 500);
    //
    //         // 提示刷新成功
    //         console.log('数据已刷新');
    //     }, 500);
    // });
    // 模拟刷新功能
    // refreshBtn.addEventListener('click', function () {
    //     // 添加刷新动画
    //     refreshBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> 刷新中...';
    //     refreshBtn.disabled = true;
    //
    //     // 模拟API请求延迟
    //     setTimeout(function () {
    //         // 更新最后更新时间
    //         updateTime();
    //
    //         // 恢复按钮状态
    //         refreshBtn.innerHTML = '<i class="fas fa-sync-alt"></i> 刷新数据';
    //         refreshBtn.disabled = false;
    //
    //         // 添加视觉反馈
    //         refreshBtn.style.background = 'linear-gradient(90deg, #0d9488, #0ea5e9)';
    //         setTimeout(() => {
    //             refreshBtn.style.background = 'linear-gradient(90deg, #0ea5e9, #0d9488)';
    //         }, 500);
    //
    //         // 提示刷新成功
    //         console.log('数据已刷新');
    //     }, 1500);
    // });

    // 初始化最后更新时间
    updateTime();
    // 启动实时时钟
    startRealTimeClock();
})

// 启动实时时钟
function startRealTimeClock() {
    // 每秒更新一次时间
    setInterval(function() {
        updateTime();
    }, 1000);
}

// 更新最后更新时间
function updateTime() {
    const lastUpdateEl = document.querySelector('.last-update strong');
    const now = new Date();
    const dateStr = now.toLocaleDateString('zh-CN');
    const timeStr = now.toLocaleTimeString('zh-CN');
    if (lastUpdateEl) {
        lastUpdateEl.textContent = `${dateStr} ${timeStr}`;
    }
}

function downLoadData() {
    $.ajax({

        type: "get",

        url: "downloadLimitUpData",

        data: {},

        success: function (data) {
        }

    });
}

function getLimitUpYesterDayData() {
    $.ajax({

        type: "get",

        url: "getYesterdayLimitUpData",

        data: {},

        success: function (data) {
            console.log(data);
            buildLimitUpYesterDayDataHtml(data);
        }

    });
}

function getLimitUpTodayData() {
    $.ajax({
        type: "get",
        url: "getTodayLimitUpData",
        data: {},
        success: function (data) {
            buildLimitUpYesterDayDataHtml(data);
        }
    });
}

function buildLimitUpYesterDayDataHtml(data) {
    console.log($('#limitUpNum').val())
    let mostLimitUpNum = 0;
    let limitUpBoardNum = 0;

    const htmlArray = data.map((item, index) => {
        if (item.limitUpNum > mostLimitUpNum) {
            mostLimitUpNum = item.limitUpNum;
        }
        if (item.limitUpNum > 2) {
            limitUpBoardNum += 1;
        }
        const percent = (item.limitUpMoney / item.flowMoney * 100);
        const highlightClass = percent > 1.5 ? 'highlight' : '';
        const conceptHtml = renderConcepts(item.concepts);
        return `
       <tr>
                        <td class="stock-code">${item.code}</td>
                        <td class="stock-name">${item.name}</td>
                        <td>${item.price}</td>
                        <td class="up">${item.rate}%</td>
                        <td class="seal-amount">${item.limitUpType}</td>
                        <td class="seal-amount">${(item.flowMoney / 1e8).toFixed(2)}亿</td>
                        <td class="seal-amount">${(item.limitUpMoney / 1e8).toFixed(2)}亿</td>
                        <td class="seal-amount">${(item.limitUpHandNum / 10000).toFixed(2)}万手</td>
                        <td class="seal-amount ${highlightClass}">${percent.toFixed(2)}%</td>
                        <td class="concept">
                            <div class="concept-tags">
                                 ${conceptHtml}
                            </div>
                        </td>
                        <td class="stock-limit-time">${item.limitUpTime}</td>
                        <td class="highlight">${item.limitUpNumType}</td>
                    </tr>
  `
    });
    document.getElementById('limitUpNum').innerText = data.length;
    document.getElementById('limitUpBoardNum').innerText = limitUpBoardNum;
    document.getElementById('mostLimitUpNum').innerText = mostLimitUpNum;
    document.getElementById('dataTable').innerHTML = htmlArray.join('');
}

function renderConcepts(conceptsStr) {
    if (!conceptsStr) return '';
    return conceptsStr.split('+').map(concept =>
        `<span class="concept-tag">${concept}</span>`
    ).join('');
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
        // 添加刷新动画
        // refreshBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> 刷新中...';
        // refreshBtn.disabled = true;

        // 模拟API请求延迟
        setTimeout(function () {
            document.getElementById('dataTable').innerHTML = '';
            if (whichDay===1){
                getLimitUpTodayData();
            }else {
                getLimitUpYesterDayData();
            }

            // // 更新最后更新时间
            // updateTime();
            //
            // // 恢复按钮状态
            // refreshBtn.innerHTML = '<i class="fas fa-sync-alt"></i> 刷新数据';
            // refreshBtn.disabled = false;
            //
            // // 添加视觉反馈
            // refreshBtn.style.background = 'linear-gradient(90deg, #0d9488, #0ea5e9)';
            // setTimeout(() => {
            //     refreshBtn.style.background = 'linear-gradient(90deg, #0ea5e9, #0d9488)';
            // }, 500);

            // 提示刷新成功
            console.log('数据已刷新');
        }, 500);
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