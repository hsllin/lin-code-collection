$(function () {

    getHotSpotNews();

})

function getHotSpotNews() {
    $.ajax({

        type: "get",

        url: "getHotSpotNews",

        data: {

            type: "1"

        },

        success: function (data) {
            buildHotSpotNewsHtml(data);
        }

    });
}


function buildHotSpotNewsHtml(data) {
    const htmlArray = data.map(newItem => {
        const regex = /(\d{4}-\d{2})-(\d{2}) (\d{2}:\d{2}:\d{2})/;
        const match = newItem.showTime.match(regex);
        const yearMonth = match[1];
        const day = match[2];
        const time = match[3];
        return `
      <div class="news-item">
                <div class="news-date">
                    <div class="news-date-month">${yearMonth}</div>
                    <div class="news-date-day">${day}</div>
                    <!-- 新增的时间显示 -->
                    <div class="news-date-time">${time}</div>
                </div>
                <div class="news-content">
                    <div class="news-title" href="${newItem.url}">${newItem.title}</div>
                    <a class="news-desc" href="${newItem.url}" target="_blank">${newItem.summary}</a>
                    <div class="news-meta">
<!--                        <span class="news-category">科技</span>-->
<!--                        <span>阅读量: 2.5万</span>-->
                    </div>
                </div>
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
    getHotSpotNews();
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