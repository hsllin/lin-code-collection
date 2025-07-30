$(function () {
    // 初始化渲染
    document.getElementById('board').innerHTML = '';
    getIncreaseConcept();
})
// 模拟数据
let stockData = [];
let sort = 'asc';

function getIncreaseConcept() {
    $.ajax({

        type: "get",

        url: "getIncreaseConcept",

        data: {
            sort: sort
        },

        success: function (data) {
            console.log(data)
            render(data);

        }

    });
}

function render(sectors) {
    const board = document.getElementById('board');
    board.innerHTML = '';
    let limitCount = 0;

    // const newSectors = sectors.map(s => ({
    //     ...s,
    //     rate: s.rate
    // })).sort((a, b) => b.rate - a.rate);

    sectors.forEach(s => {
        const card = document.createElement('div');
        card.className = 'card';
        card.innerHTML = `
      <h6>${s.concepts}</h6>
      <div class="info">
        <span>${s.name}</span>
        <span class="rate" style="color:${s.rate >= 0 ? 'rgba(74,111,165,0.73)' : 'rgb(145,152,161)'}">${s.rate > 0 ? '' : ''}${s.rate.toFixed(2)}%</span>
      </div>
      <div class="dragons">${s.code} ${s.limit ? '(涨停)' : ''}</div>
    `;
        board.appendChild(card);
    });
}
function getIncreaseRank() {
    document.getElementById('board').innerHTML = '';
    sort='desc';
    getIncreaseConcept();
}
function getDecreaseRank() {
    document.getElementById('board').innerHTML = '';
    sort='asc';
    getIncreaseConcept();
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


