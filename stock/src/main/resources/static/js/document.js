$(function () {

    getDocumentList();

    updateDate();
})




// 渲染表格
function renderList(data) {
    const tbody = document.getElementById('announcements');
    tbody.innerHTML = data.map(item => `
                <tr>
                    <td>${item.time}</td>
                    <td>${item.type}</td>
                    <td class="stock-code">${item.code}</td>
                    <td>${item.name.includes('ST') ? `<span class="st-mark">${item.name}</span>` : item.name}</td>
                    <td> <a href="${item.url}" class="title" target="_blank">${item.content}</a></td>
                </tr>
            `).join('');
}

// 过滤功能
function filterData() {
    const searchText = document.getElementById('searchInput').value.toLowerCase();
    const selectedType = document.getElementById('typeFilter').value;

    const filtered = announcements.filter(item => {
        const matchSearch = item.code.includes(searchText) ||
            item.company.toLowerCase().includes(searchText);
        const matchType = selectedType ? item.type === selectedType : true;
        return matchSearch && matchType;
    });

    renderList(filtered);
}

// 事件监听
document.getElementById('searchInput').addEventListener('input', filterData);
document.getElementById('typeFilter').addEventListener('change', filterData);

// 初始加载
renderList(announcements);

function getDocumentList() {
    $.ajax({

        type: "get",

        url: "getDocumentList",

        data: {

            type: "1"

        },

        success: function (data) {
            renderList(data);
        }

    });
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
    document.getElementById('announcements').innerHTML = '';
    getDocumentList();
}

function updateDate() {
    var time = getCurrentDateTime();
    document.getElementById('time').innerHTML = time;
}

function getCurrentDateTime() {
    const date = new Date();

    // 获取日期组件
    const year = date.getFullYear();
    const month = date.getMonth() + 1;  // 月份从0开始需要+1
    const day = date.getDate();

    // 获取时间组件并补零
    const hours = date.getHours().toString().padStart(2, '0');
    const minutes = date.getMinutes().toString().padStart(2, '0');
    const seconds = date.getSeconds().toString().padStart(2, '0');

    // 星期映射表
    const weekdays = ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六'];
    const weekday = weekdays[date.getDay()];

    // 组合成标准格式
    return `${year}年${month}月${day}日  ${weekday}`;
}