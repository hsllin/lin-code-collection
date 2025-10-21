// 模拟数据
const themeData = {
    chip: {
        name: "芯片",
        code: "880952",
        stocks: [
            { code: "301563", name: "云汉芯城", change: 332.59, price: 45.20, relevance: 5, days: 3, volume: 12.5, marketValue: 156.8 },
            { code: "300786", name: "国林科技", change: 20.00, price: 32.15, relevance: 4, days: 1, volume: 8.7, marketValue: 89.3 },
            { code: "300593", name: "新雷能", change: 20.00, price: 28.76, relevance: 4, days: 1, volume: 6.9, marketValue: 75.4 },
            { code: "301308", name: "江波龙", change: 20.00, price: 56.33, relevance: 5, days: 2, volume: 15.2, marketValue: 203.7 },
            { code: "688347", name: "华虹公司", change: 15.72, price: 42.18, relevance: 4, days: 0, volume: 20.1, marketValue: 312.5 },
            { code: "688691", name: "灿芯股份", change: 15.41, price: 38.92, relevance: 3, days: 0, volume: 7.8, marketValue: 95.6 },
            { code: "688233", name: "神工股份", change: 12.32, price: 51.27, relevance: 4, days: 0, volume: 9.3, marketValue: 124.8 },
            { code: "688416", name: "聚烯股份", change: 11.88, price: 29.45, relevance: 3, days: 0, volume: 5.6, marketValue: 68.9 },
            { code: "688766", name: "普冉股份", change: 10.29, price: 34.12, relevance: 3, days: 0, volume: 4.2, marketValue: 52.1 },
            { code: "603421", name: "鼎信通讯", change: 10.04, price: 23.78, relevance: 2, days: 0, volume: 3.8, marketValue: 41.5 }
        ]
    },
    storage: {
        name: "存储芯片",
        code: "880953",
        stocks: [
            { code: "603986", name: "兆易创新", change: 8.75, price: 89.32, relevance: 5, days: 0, volume: 18.9, marketValue: 456.2 },
            { code: "688008", name: "澜起科技", change: 7.23, price: 67.45, relevance: 4, days: 0, volume: 12.4, marketValue: 312.7 },
            { code: "688123", name: "聚辰股份", change: 6.89, price: 45.18, relevance: 4, days: 0, volume: 8.3, marketValue: 98.5 }
        ]
    },
    battery: {
        name: "固态电池",
        code: "880954",
        stocks: [
            { code: "300750", name: "宁德时代", change: 5.42, price: 198.76, relevance: 5, days: 0, volume: 45.6, marketValue: 1234.5 },
            { code: "002460", name: "赣锋锂业", change: 4.87, price: 56.34, relevance: 4, days: 0, volume: 23.1, marketValue: 567.8 }
        ]
    }
};

// 当前选中的主题
let currentTheme = 'chip';

// 表格排序状态
let sortState = {
    column: null,
    direction: 'asc' // 'asc' 或 'desc'
};
$(function () {
    // document.getElementById('main').innerHTML = '';
    // getLianBanChiListData();
    // 渲染初始数据
    renderStocks(currentTheme);

    // 主题标签点击事件
    document.querySelectorAll('.theme-tab').forEach(tab => {
        tab.addEventListener('click', function() {
            // 移除所有active类
            document.querySelectorAll('.theme-tab').forEach(t => t.classList.remove('active'));
            document.querySelectorAll('.theme-content').forEach(c => c.classList.remove('active'));

            // 添加当前active类
            this.classList.add('active');
            const tabId = this.getAttribute('data-tab');
            document.getElementById(tabId + '-content').classList.add('active');
            console.log('2222222222222')
            getHotTheme();

        });
    });

    // 主题标签点击事件
    document.querySelectorAll('.theme-tag').forEach(tag => {
        tag.addEventListener('click', function() {
            // 移除所有active类
            document.querySelectorAll('.theme-tag').forEach(t => t.classList.remove('active'));

            // 添加当前active类
            this.classList.add('active');

            const theme = this.getAttribute('data-theme');
            if (themeData[theme]) {
                currentTheme = theme;
                updateThemeInfo(theme);
                renderStocks(theme);
            }
        });
    });

    // 表头排序点击事件
    document.querySelectorAll('.stock-table th[data-sort]').forEach(th => {
        th.addEventListener('click', function() {
            const column = this.getAttribute('data-sort');
            sortTable(column);
        });
    });

    // 刷新按钮点击事件
    document.querySelectorAll('.refresh-btn').forEach(btn => {
        btn.addEventListener('click', function() {
            renderStocks(currentTheme);
            alert('行情已刷新！');
        });
    });
})

function getHotTheme() {
    $.ajax({

        type: "get",

        url: "getHotTheme",

        data: {

        },

        success: function (data) {
            data = unicodeToChinese(data);
            data = JSON.parse(data);
            buildHotThemeHtml(data);
        }

    });
}

function buildLianBanChiListHtml(data) {
    console.log(data)
    var htmlArray = '';
    data.forEach((group, groupIndex) => {
            htmlArray += `
            
                    `;
            console.log(data);
            document.getElementById('main').innerHTML = htmlArray;
        }
    );
}

function refreshData() {
    document.getElementById('main').innerHTML = '';
    getLianBanChiListData();
}

// 更新主题信息
function updateThemeInfo(theme) {
    const data = themeData[theme];
    if (!data) return;

    document.querySelector('.theme-title').textContent = data.name;
    document.querySelector('.theme-code').textContent = data.code;
}

// 渲染股票表格
function renderStocks(theme) {
    const data = themeData[theme];
    if (!data) return;

    const tbody = document.getElementById('stock-table-body');
    tbody.innerHTML = '';

    data.stocks.forEach(stock => {
        const row = document.createElement('tr');

        // 关联度星级
        const stars = '★'.repeat(stock.relevance) + '☆'.repeat(5 - stock.relevance);

        // 涨幅颜色
        const changeClass = stock.change >= 0 ? 'positive' : 'negative';
        const changeText = stock.change >= 0 ? `+${stock.change.toFixed(2)}%` : `${stock.change.toFixed(2)}%`;

        row.innerHTML = `
                    <td><span class="relevance-stars">${stars}</span></td>
                    <td><span class="stock-code">${stock.code}</span></td>
                    <td><span class="stock-name">${stock.name}</span></td>
                    <td class="${changeClass}">${changeText}</td>
                    <td>${stock.price.toFixed(2)}</td>
                    <td>${stock.days}</td>
                    <td>${stock.volume}</td>
                    <td>${stock.marketValue}</td>
                `;

        tbody.appendChild(row);
    });
}

// 表格排序函数
function sortTable(column) {
    // 更新排序状态
    if (sortState.column === column) {
        sortState.direction = sortState.direction === 'asc' ? 'desc' : 'asc';
    } else {
        sortState.column = column;
        sortState.direction = 'asc';
    }

    // 更新表头样式
    document.querySelectorAll('.stock-table th').forEach(th => {
        th.classList.remove('sort-asc', 'sort-desc');
    });

    const currentTh = document.querySelector(`.stock-table th[data-sort="${column}"]`);
    currentTh.classList.add(`sort-${sortState.direction}`);

    // 获取当前主题的数据
    const data = themeData[currentTheme];
    if (!data) return;

    // 排序数据
    data.stocks.sort((a, b) => {
        let aValue = a[column];
        let bValue = b[column];

        // 处理百分比值
        if (column === 'change') {
            aValue = parseFloat(aValue);
            bValue = parseFloat(bValue);
        }

        if (aValue < bValue) {
            return sortState.direction === 'asc' ? -1 : 1;
        }
        if (aValue > bValue) {
            return sortState.direction === 'asc' ? 1 : -1;
        }
        return 0;
    });

    // 重新渲染表格
    renderStocks(currentTheme);
}

function buildHotThemeHtml(data) {
        var htmlArray = '';
        console.log(data.ResultSets[0].Content)
    data.ResultSets[0].Content.forEach((group, groupIndex) => {
            htmlArray+=`
            <div className="theme-tag active" data-theme="chip" id="${group[4]}">${group[1]}</div>
        `;

        });
        document.getElementById('hot-theme').innerHTML = htmlArray;
        console.log(htmlArray)

}

