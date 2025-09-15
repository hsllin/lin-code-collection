// JSON data
let data = [];


$(function () {
    document.getElementById('content').innerHTML = '';
    getStockRuleData();
})

function getStockRuleData() {
    $.ajax({

        type: "get",

        url: "getStockRuleList",

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
          <div class="counter-item">${item.content}</div>
  `
    });
    console.log(htmlArray)
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
            '#2ECCA4','#45B7D1','#B8E994','#81D8D0',];
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


