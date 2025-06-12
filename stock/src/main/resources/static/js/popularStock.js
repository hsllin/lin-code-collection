let sortDirections = Array(9).fill(true);
let dateIndex = 0;// 初始排序方向
$(function () {
    document.getElementById('hot_content').innerHTML = '';
    document.getElementById('rise_content').innerHTML = '';
    getPopularHotStockData('normal');
    getPopularRiseStockData('skyrocket');
})

function getPopularHotStockData() {
    $.ajax({

        type: "get",
        url: "getPopularStockList",
        data: {
            type: 'normal',
        },

        success: function (data) {
            console.log(data)
            buildPopularHotStockHtml(data);
        }

    });
}

function getPopularRiseStockData() {
    $.ajax({

        type: "get",
        url: "getPopularStockList",
        data: {
            type: 'skyrocket',
        },

        success: function (data) {
            console.log(data)
            buildPopularRiseStockHtml(data);
        }

    });
}

function buildPopularHotStockHtml(data) {
    var htmlArray = '';
    var tagList = '';
    htmlArray += `<div class="section-header">
                    <h2 class="section-title">👀 大家在看</h2>
                    <div class="time-filter">
                        <span class="time-tab active">1小时</span>
                        <span class="time-tab">24小时</span>
                    </div>
                </div>`

    data.forEach((stock, stockIndex) => {
            const showAnalyze = stock.analyse == null ? 'hidden' : '';
            const showAnalyseTitle = stock.analyse_title == null ? 'hidden' : '';
            htmlArray += `  
           <!-- 股票条目 -->
                <div class="stock-item">
                    <div class="stock-header">
                        <div>
                            <span class="stock-rank">${stock.order}</span>
                            <span class="stock-name">${stock.name}-${stock.code}</span>
                            <span class="stock-change change-up">${safeToFixed(stock.rise_and_fall)}%</span>
                        </div>
                        <span class="heat">${safeToFixed(stock.rate / 10000)}万热度</span>
                    </div>
                    <div class="tag-group">
                        <span class="populartag">${stock.tag.popularity_tag}</span>`;
            tagList = stock.tag.concept_tag;
            tagList.forEach((tag, tagIndex) => {
                htmlArray += `
                                <span class="populartag">${tag}</span>`
            });
            htmlArray += `   </div>
                    <div class="hot-reason ${showAnalyze}">
                        ${stock.analyse}
                    </div>
                    <div class="hot-reason">
                    <span class="populartag ${showAnalyseTitle}"> ${stock.analyse_title}</span>
                    </div>
                </div>`;
        }
    );

    document.getElementById('hot_content').innerHTML = htmlArray;

}

function buildPopularRiseStockHtml(data) {
    var htmlArray = '';
    var tagList = '';
    htmlArray += `<div class="section-header">
                    <h2 class="section-title">🚀 快速飙升</h2>
                    <div class="time-filter">
                        <span class="time-tab active">1小时</span>
                        <span class="time-tab">24小时</span>
                    </div>
                </div>`

    data.forEach((stock, stockIndex) => {
            const showPopularTag = stock.popularity_tag == null ? 'hidden' : '';
            htmlArray += `  
           <!-- 股票条目 -->
                <div class="stock-item">
                    <div class="stock-header">
                        <div>
                            <span class="stock-rank">${stock.order}</span>
                            <span class="stock-name">${stock.name}-${stock.code}</span>
                            <span class="stock-change change-up">${safeToFixed(stock.rise_and_fall)}%</span>
                        </div>
                        <span class="heat">${safeToFixed(stock.rate / 10000)}万热度</span>
                    </div>
                    <div class="tag-group">
                        <span class="populartag ${showPopularTag}">${stock.tag == null ? '' : stock.tag.popularity_tag}</span>`;
            if (stock.tag != null && stock.tag.concept_tag != null) {
                stock.tag.concept_tag.forEach((tag, tagIndex) => {
                    htmlArray += `
                                <span class="populartag">${tag}</span>`
                });
            }

            htmlArray += `   </div>
                </div>`;
        }
    );
    document.getElementById('rise_content').innerHTML = htmlArray;

}

document.addEventListener('DOMContentLoaded', function () {
    // 2. 获取所有导航标签元素
    const tabs = document.querySelectorAll('.nav-tab');

    // 3. 遍历绑定点击事件
    tabs.forEach(tab => {
        tab.addEventListener('click', function () {
            // 4. 移除所有标签的激活状态
            tabs.forEach(t => t.classList.remove('active'));

            // 5. 添加当前标签激活状态
            this.classList.add('active');

            // 6. 根据标签类型执行对应操作
            const tabType = this.textContent.trim();
            document.getElementById('hot_content').innerHTML = '';
            document.getElementById('rise_content').innerHTML = '';
            if (tabType === '热股') {
                getPopularHotStockData();
                getPopularRiseStockData()
            } else if (tabType === '板块') {
                console.log('板块')
                getPopularHotConceptData()
                getPopularHotIndustryData()
            } else if (tabType === '东方财富') {
                getPopularHotDongCaiData();
            }
        });
    });
});

function getPopularHotIndustryData() {
    $.ajax({

        type: "get",
        url: "getPopularStockList",
        data: {
            type: 'normal',
        },

        success: function (data) {
            console.log(data)
            getPopularHotIndustryData(data);
        }

    });
}

function getPopularHotConceptData() {
    $.ajax({

        type: "get",
        url: "getPopularConceptList",
        data: {
            type: 'concept',
        },

        success: function (data) {
            console.log(data)
            buildPopularHotConceptHtml(data);
        }

    });
}

function getPopularHotIndustryData() {
    $.ajax({

        type: "get",
        url: "getPopularConceptList",
        data: {
            type: 'industry',
        },

        success: function (data) {
            console.log(data)
            buildPopularIndustryHtml(data);
        }

    });
}

function buildPopularIndustryHtml(data) {
    var htmlArray = '';
    var tagList = '';
    htmlArray += `<div class="section-header">
                    <h2 class="section-title">热门板块</h2>
<!--                    <div class="time-filter">-->
<!--                        <span class="time-tab active">1小时</span>-->
<!--                        <span class="time-tab">24小时</span>-->
<!--                    </div>-->
                </div>`

    data.forEach((stock, stockIndex) => {
            const showHotTag = stock.hot_tag == null ? 'hidden' : '';
            const showPopularTag = stock.tag == null ? 'hidden' : '';
            htmlArray += `  
           <!-- 股票条目 -->
                <div class="stock-item">
                    <div class="stock-header">
                        <div>
                            <span class="stock-rank">${stock.order}</span>
                            <span class="stock-name">${stock.name}-${stock.code}</span>
                            <span class="stock-change change-up">${safeToFixed(stock.rise_and_fall)}%</span>
                        </div>
                        <span class="heat">${safeToFixed(stock.rate / 10000)}万热度</span>
                    </div>
                    <div class="tag-group">
                        <span class="populartag ${showHotTag}">${stock.hot_tag}</span>
                        <span class="populartag ${showPopularTag}">${stock.tag}</span>
                    </div>
                </div>`;
        }
    );

    document.getElementById('rise_content').innerHTML = htmlArray;

}

function buildPopularHotConceptHtml(data) {
    var htmlArray = '';
    var tagList = '';
    htmlArray += `<div class="section-header">
                    <h2 class="section-title">热门概念</h2>
<!--                    <div class="time-filter">-->
<!--                        <span class="time-tab active">1小时</span>-->
<!--                        <span class="time-tab">24小时</span>-->
<!--                    </div>-->
                </div>`

    data.forEach((stock, stockIndex) => {
            const showHotTag = stock.hot_tag == null ? 'hidden' : '';
            const showPopularTag = stock.tag == null ? 'hidden' : '';
            htmlArray += `  
           <!-- 股票条目 -->
                <div class="stock-item">
                    <div class="stock-header">
                        <div>
                            <span class="stock-rank">${stock.order}</span>
                            <span class="stock-name">${stock.name}-${stock.code}</span>
                            <span class="stock-change change-up">${safeToFixed(stock.rise_and_fall)}%</span>
                        </div>
                        <span class="heat">${safeToFixed((stock.rate / 10000))}万热度</span>
                    </div>
                    <div class="tag-group">
                        <span class="populartag ${showHotTag}">${stock.hot_tag}</span>
                        <span class="populartag ${showPopularTag}">${stock.tag}</span>
                    </div>
                </div>`;
        }
    );

    document.getElementById('hot_content').innerHTML = htmlArray;

}

function getPopularHotDongCaiData() {
    $.ajax({

        type: "get",
        url: "getDongCaiPopularStocktList",
        data: {},

        success: function (data) {
            console.log(data)
            buildPopularDongCaiHtml(data);
        }

    });
}

function buildPopularDongCaiHtml(data) {
    var htmlArray = '';
    var tagList = '';
    htmlArray += `<div class="section-header">
                    <h2 class="section-title">热门个股</h2>
<!--                    <div class="time-filter">-->
<!--                        <span class="time-tab active">1小时</span>-->
<!--                        <span class="time-tab">24小时</span>-->
<!--                    </div>-->
                </div>`

    data.forEach((stock, stockIndex) => {
            htmlArray += `  
           <!-- 股票条目 -->
                <div class="stock-item">
                    <div class="stock-header">
                        <div>
                            <span class="stock-rank">${stockIndex + 1}</span>
                            <span class="stock-name">${stock.f14}-${stock.f12}</span>
                            <span class="stock-change change-up">${safeToFixed(stock.f3)}%</span>
                        </div>
                    </div>
                </div>`;
        }
    );

    document.getElementById('hot_content').innerHTML = htmlArray;

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

function safeToFixed(value, decimals = 2) {
    return (Number(value) || 0).toFixed(decimals);
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