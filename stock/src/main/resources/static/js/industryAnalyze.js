$(function () {
    document.querySelector('#industryCard').innerHTML = '';
    getIndustryAnalyzeData();
})

function getIndustryAnalyzeData() {
    $.ajax({

        type: "get",

        url: "getIndustryAnalyzeData",

        data: {
        },

        success: function (data) {
            buildIndustryAnalyzeHtml(data);
        }

    });
}

function buildIndustryAnalyzeHtml(data) {
    document.querySelector('#industryCard').innerHTML =
        data.data.result.map(item => createCard(item)).join('');
}

function createCard(item) {
    return `
                <div class="card">
                    <h2 class="title ${item.title_red ? 'title-red' : ''} ${item.title_bold ? 'title-bold' : ''}">
                        ${item.title}
                    </h2>
                    ${item.imgs ? `
                    <div class="img-container">
                        <img src="${JSON.parse(item.imgs)[0]}" class="main-img" alt="插图">
                    </div>` : ''}
                    
                    ${item.keyword ? `
                    <div class="keywords">
                        ${item.keyword.split('\t').map(k =>
        `<span class="keyword">${k.trim()}</span>`
    ).join('')}
                    </div>` : ''}
                    
                    <div class="content">${item.content.replace(/\n/g, '<br>')}</div>
                    
                    <div class="meta-info">
                        <div>
                            ${item.create_time ? `创建时间：${item.create_time}` : ''}
                        </div>
                        <div class="stats">
                            <span>转发：${item.forward_count}</span>
                            <span>浏览：${item.browsers_count}</span>
                            ${item.update_time ? `<span>更新：${item.update_time}</span>` : ''}
                        </div>
                    </div>
                </div>
            `;
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