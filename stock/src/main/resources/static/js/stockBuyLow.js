$(function () {
    // 初始化渲染
    // window.addEventListener('DOMContentLoaded', renderTable);
    document.getElementById('stock-table').innerHTML = '';
    getStockBuyLowStrong();
// 点击模态框外部关闭
    window.onclick = function (event) {
        const modal = document.getElementById("modal");
        if (event.target == modal) {
            modal.style.display = "none";
        }
    }
})
// 模拟数据
let stockData = [];
let tag='strong';

function getStockBuyLowStrong() {
    $.ajax({

        type: "get",

        url: "getStockBuyLowStrong",

        data: {},

        success: function (data) {
            stockData = data;
            renderTable(data);
        }

    });
}

function getStockBuyLowNormal() {
    $.ajax({

        type: "get",

        url: "getStockBuyLowNormal",

        data: {},

        success: function (data) {
            stockData = data;
            renderTable(data);
        }

    });
}


function formatCurrency(value) {
    return (value / 1e8).toFixed(2) + '亿';
}

function renderTable(data) {
    var htmlArray = '';
    data.forEach((item, index) => {

        htmlArray += `
        <tr>
                <td>${index + 1}</td>
                <td>${item.code}</td>
                <td>${item.name}</td>
                <td>${item.price}</td>
                <td class="price-up">${item.rate}</td>
                <td>${formatToYi(item.netInFlow)}</td>
                 <td>${formatToYi(item.volality)}</td>
                <td><span class="collapse-text"
                          onclick="showDetail('${item.concepts}')">${item.concepts}</span></td>
                <td>${item.industry}</td>
            </tr>
  `
    });
    console.log(htmlArray)
    document.getElementById('stock-table').innerHTML = htmlArray;
}

// 显示模态框并填充内容
function showDetail(stockName) {
    const modal = document.getElementById("modal");
    const modalTitle = document.getElementById("modal-title");
    const modalBody = document.getElementById("modal-body");

    modalTitle.textContent = `${stockName} - 涨停类别详情`;

    // 根据股票名设置不同的详细内容
    // modalBody.innerHTML = "<p>注册制次新股: 属于注册制改革后上市的股票</p><p>次股与次新股: 此类股票具有较高波动性</p><p>融资融券: 可参与融资融券交易</p>";
    modalBody.innerHTML = stockName;

    modal.style.display = "block";
}

// 关闭模态框
function closeModal() {
    document.getElementById("modal").style.display = "none";
}

// 切换标签状态和过滤功能
function toggleFilter(tagName) {
    // 获取所有标签元素（以 tag- 开头的 id）
    const allTags = document.querySelectorAll('[id^="tag-"]');

    // 移除所有标签的 active 状态
    allTags.forEach(tag => {
        tag.classList.remove('active');
    });

    // 为当前点击的标签添加 active 状态
    const clickedTag = document.getElementById(`tag-${tagName}`);
    clickedTag.classList.add('active');

    filterStocks(tagName);
}

// 根据标签过滤股票
function filterStocks(tagName) {
    console.log(tagName)
    document.getElementById('stock-table').innerHTML = '';
    if (tagName==='strong'){
        getStockBuyLowStrong()
    }else {
        getStockBuyLowNormal()
    }
}

function downLoadData() {
    $.ajax({

        type: "get",

        url: "downloadTrendData",

        data: {},

        success: function (data) {
        }

    });
}

/**
 * 格式化数字：超过1亿显示亿单位，不足1亿但超过1万显示万单位，不足1万显示原数字
 * @param {number|string} num - 待格式化的数字或数字字符串
 * @returns {string} 格式化后的字符串（自动去除小数点后无效的零）
 */
function formatToYi(num) {
    // 转换为数字类型并验证有效性
    const number = parseFloat(num);
    if (isNaN(number)) return '0';

    const absNum = Math.abs(number);

    // 超过1亿：转换为亿单位
    if (absNum >= 100000000) {
        const result = (number / 100000000).toFixed(2).replace(/\.?0+$/, '');
        return result + '亿';
    }
    // 超过1万但不足1亿：转换为万单位
    else if (absNum >= 10000) {
        const result = (number / 10000).toFixed(2).replace(/\.?0+$/, '');
        return result + '万';
    }
    // 不足1万：直接返回整数
    return Math.round(number).toString();
}


