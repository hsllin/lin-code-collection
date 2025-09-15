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
                getIncreaseData();
            } else {
                getDecreaseData();
            }
        });
    });
    checkedData = getSelectedValues();
    getIncreaseData();
    // getDecreaseData();
})

function getIncreaseData() {
    console.log(checkedData)
    $.ajax({

        type: "get",

        url: "intraDayChange",

        data: {
            checkedData: checkedData.join(','),
            type: "1"

        },

        success: function (data) {
            buildIncreaseHtml(data);
        }

    });
}

function getDecreaseData() {
    $.ajax({

        type: "get",

        url: "intraDayChange",

        data: {
            checkedData: checkedData.join(','),
            type: "2"

        },

        success: function (data) {
            buildDecreaseHtml(data);
        }

    });
}

function buildIncreaseHtml(data) {
    const htmlArray = data.map(stock => {
        const percentValue = stock.percent;
        const count = stock.count;

        // 判断条件
        const icon = percentValue > 3.5 ? '' : count > 2 ? '🔥' : "";
        return `
    <div class="stock-item">
   
        <span>${stock.time} </span>
           ${stock.count >= 3 ?
            `<span><div class="index" style="background: #3498db">${stock.count}</div></span>` :
            `<span>${stock.count} </span>`
        }
        <span>${icon} ${stock.n}(${stock.c})</span>
        <span class="positive">${stock.percent}%</span>
        <span>${stock.industry}</span>
    </div>
  `
    });
    document.getElementById('industry-item-up').innerHTML = htmlArray.join('');
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

function buildDecreaseHtml(data) {
    const htmlArray = data.map(stock => {
        const percentValue = stock.percent;

        // 判断条件
        const icon = percentValue < -5 ? '💥' :
            '';
        return `
    <div class="stock-item">
   
        <span>${stock.time} ${stock.count}</span>
        <span>${icon}${stock.n}(${stock.c})</span>
        <span class="negative">${stock.percent}%</span>
    </div>
  `
    });
    document.getElementById('industry-item-up').innerHTML = htmlArray.join('');
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
        getIncreaseData();
    } else {
        getDecreaseData();
    }

}