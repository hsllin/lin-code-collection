// JSON data
let data = [];
// 当前使用的配色算法
let colorAlgorithm = 'hsl';

// 预定义的精美颜色调色板
const colorPalette = [
    '#FF6B6B', '#4ECDC4', '#45B7D1', '#FFBE0B', '#6A0572',
    '#FF9F1C', '#E71D36', '#2EC4B6', '#F46036', '#5B6C5D',
    '#0B3954', '#087E8B', '#C81D25', '#FFC857', '#2E5266'
];

$(function () {
    document.getElementById('content').innerHTML = '';
    getTradeRuleList();
})

function getTradeRuleList() {
    $.ajax({

        type: "get",

        url: "getTradeRuleList",

        data: {},

        success: function (reuslt) {
            data = reuslt;
            renderTable();
        }

    });
}

// Function to render the table
function renderTable() {
    var htmlArray = '';
    data.forEach((item, index) => {

        htmlArray += `
<!--        <li class="counter-list">-->
                <div class="counter-item">${item.content}</div>
<!--            </li>-->
  `
    });
    document.getElementById('content').innerHTML = htmlArray;
    updateCounterColors();
}



// 更新计数器颜色
function updateCounterColors() {
    const items = document.querySelectorAll('.counter-item');
    const totalItems = items.length;

    items.forEach((item, index) => {
        // 使用HSL颜色空间生成均匀分布的颜色
        const hue = (360 / totalItems) * index;
        // const color = `hsl(${hue}, 70%, 55%)`;

        // 或者使用预定义的调色板
        // const colors = ['#FF6B6B', '#4ECDC4', '#45B7D1', '#FFBE0B'];
        const colors = ['#FF6B6B', '#4ECDC4', '#45B7D1', '#FFBE0B','#FF9671',
            '#2ECCA4','#EF553B','#B8E994','#81D8D0','#FF6B6B','#45B7D1','#2D3436','#F46036','#5B6C5D'];
        const color = colors[index % colors.length];

        item.style.setProperty('--counter-color', color);
    });
    const style = document.createElement('style');
    style.textContent = `
            .counter-item::before {
                background-color: var(--counter-color);
            }
        `;
    document.head.appendChild(style);
}

