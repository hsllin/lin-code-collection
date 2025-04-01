$(function () {

    getWallStreetNews();

    updateDate();
})

function getWallStreetNews() {
    $.ajax({

        type: "get",

        url: "getWallStreetNews",

        data: {

            type: "1"

        },

        success: function (data) {
            buildWallStreetNewsHtml(data);
        }

    });
}


function buildWallStreetNewsHtml(data) {
    const htmlArray = data.map(newItem => {

        return `
    <div class="news-item">
      <span class="time">${newItem.time} </span>
      <a href="${newItem.uri}" class="title" target="_blank">${newItem.content}</a>
    </div>
  `
    });
    document.getElementById('news-list').innerHTML = htmlArray.join('');
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
    document.getElementById('news-list').innerHTML = '';
    getWallStreetNews();
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