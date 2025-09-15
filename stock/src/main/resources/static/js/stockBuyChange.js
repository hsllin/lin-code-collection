let checkedData = []
let currentTab = 'up';
$(function () {
    document.querySelector('.filter-options')
        .addEventListener('change', () => updateData());
    document.querySelectorAll('.tab').forEach(tab => {
        tab.addEventListener('click', () => {
            document.querySelectorAll('.tab').forEach(t => t.classList.remove('active'));
            tab.classList.add('active');
            currentTab = tab.dataset.tab;
            if (currentTab === 'up') {
                getBuyChangeData();
            } else {
                getSellChangeData();
            }
        });
    });
    checkedData = getSelectedValues();
    getBuyChangeData();
    // getSellChangeData();

})

function getBuyChangeData() {
    $.ajax({

        type: "get",

        url: "buyChange",

        data: {
            checkedData: checkedData.join(','),
            type: "1"

        },

        success: function (data) {
            buildBuyChangeHtml(data);
        }

    });
}

function getSellChangeData() {
    $.ajax({

        type: "get",

        url: "buyChange",

        data: {
            checkedData: checkedData.join(','),
            type: "2"

        },

        success: function (data) {
            buildSellChangeHtml(data);
        }

    });
}

function buildBuyChangeHtml(data) {
    const htmlArray = data.map(stock => {
        const buyNum = stock.buyNum;
        const code = buyNum >= 3 ? 'hight' : buyNum >= 1 ? 'code' : '';

        // 判断条件
        const icon = buyNum > 0.8 ? '🔥' : "";
        return `
    <div class="stock-item">
   
        <span>${stock.time}</span>
        <span class="${code}">${icon} ${stock.n}(${stock.c})</span>
        <span class="positive">${stock.buyNum}万手</span>
        <span class="positive">${stock.percent}%</span>
    </div>
  `
    });
    document.getElementById('industry-item-up').innerHTML = htmlArray.join('');
}

function buildSellChangeHtml(data) {
    const htmlArray = data.map(stock => {
        const buyNum = stock.buyNum;
        const code = buyNum >= 3 ? 'hight' : buyNum >= 1 ? 'code' : '';

        // 判断条件
        // const icon = buyNum > 0.8 ? '🔥' : "";

        // 判断条件
        const icon = buyNum > 0.8 ? '💥' :
            '';
        return `
    <div class="stock-item">
   
        <span>${stock.time} </span>
        <span>${icon}${stock.n}(${stock.c})</span>
        <span class="negative">${stock.percent}%</span>
    </div>
  `
    });
    document.getElementById('industry-item-down').innerHTML = htmlArray.join('');
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
    document.getElementById('industry-item-up').innerHTML = '';
    // document.getElementById('industry-item-down').innerHTML = '';
    if (currentTab === 'up') {
        getBuyChangeData();
    } else {
        getDecreaseData();
    }

}

function getSelectedValues(wrapperSelector = '.filter-options') {
    const wrapper = document.querySelector(wrapperSelector);
    if (!wrapper) return [];

    return [...wrapper.querySelectorAll('input[type="checkbox"]:checked')]  // 所有已勾选的 checkbox
        .map(cb => cb.nextElementSibling.textContent.trim());           // 对应 label 的文字
}

function updateData() {
    checkedData = getSelectedValues();
}