$(function () {

    getConceptAndIndex();
    getIndustry();

    updateDate();
})

function downLoadData() {
    $.ajax({

        type: "get",

        url: "downloadData",

        data: {},

        success: function (data) {
        }

    });
}

function getConceptAndIndex() {
    $.ajax({

        type: "get",

        url: "getConceptAndIndexList",

        data: {

            type: "concept"

        },

        success: function (data) {
            buildIncreaseConceptHtml(data.increaseConceptList);
            buildDecreaseConceptHtml(data.decreaseConceptList);
            buildIndexHtml(data.indexList);
            console.log(data.indexList)
        }

    });
}

function getIndustry() {
    $.ajax({

        type: "get",

        url: "getConceptAndIndexList",

        data: {

            type: "industry"

        },

        success: function (data) {
            buildIncreaseIndustrHtml(data.increaseConceptList);
            buildDecreaseIndustryHtml(data.decreaseConceptList);
        }

    });
}


function buildIncreaseConceptHtml(data) {
    const htmlArray = data.map(item => {

        const conceptPercent = item.conceptPercent > 0 ? "positive" : "negative";
        const stockPercent = item.leaderPercent > 0 ? "positive" : "negative";
        const downPercent=item.downConceptPercent> 0 ? "positive" : "negative";
        return `
     <tr>
     <td>${item.concept}</td>
     <td class="${conceptPercent}">${item.conceptPercent}%</td>
      <td>
       <div class="stock-detail">
           ${item.leader}<br>
         <span class="${stockPercent}">${item.leaderPercent}%</span>
       </div>
     </td>
      <td>
       <div class="stock-detail">
           ${item.downConcept}<br>
         <span class="${downPercent}">${item.downConceptPercent}%</span>
       </div>
     </td>
     <td>${item.up}</td>
     <td>${item.down}</td>
     </tr>
  `
    });
    document.getElementById('increaseTr').innerHTML = htmlArray.join('');
}

function buildIncreaseIndustrHtml(data) {
    const htmlArray = data.map(item => {

        const conceptPercent = item.conceptPercent > 0 ? "positive" : "negative";
        const stockPercent = item.leaderPercent > 0 ? "positive" : "negative";
        const downPercent=item.downConceptPercent> 0 ? "positive" : "negative";
        return `
     <tr>
     <td>${item.concept}</td>
     <td class="${conceptPercent}">${item.conceptPercent}%</td>
      <td>
       <div class="stock-detail">
           ${item.leader}<br>
         <span class="${stockPercent}">${item.leaderPercent}%</span>
       </div>
     </td>
      <td>
       <div class="stock-detail">
           ${item.downConcept}<br>
         <span class="${downPercent}">${item.downConceptPercent}%</span>
       </div>
     </td>
     <td>${item.up}</td>
     <td>${item.down}</td>
     </tr>
  `
    });
    document.getElementById('increaseIndustry').innerHTML = htmlArray.join('');
}

function buildDecreaseIndustryHtml(data) {
    const htmlArray = data.map(item => {

        const conceptPercent = item.conceptPercent > 0 ? "positive" : "negative";
        const stockPercent = item.leaderPercent > 0 ? "positive" : "negative";
        const downPercent=item.downConceptPercent> 0 ? "positive" : "negative";
        return `
     <tr>
     <td>${item.concept}</td>
     <td class="${conceptPercent}">${item.conceptPercent}%</td>
      <td>
       <div class="stock-detail">
           ${item.leader}<br>
         <span class="${stockPercent}">${item.leaderPercent}%</span>
       </div>
     </td>
     <td>
       <div class="stock-detail">
           ${item.downConcept}<br>
         <span class="${downPercent}">${item.downConceptPercent}%</span>
       </div>
     </td>
     <td>${item.up}</td>
     <td>${item.down}</td>
     </tr>
  `
    });
    document.getElementById('decreaseIndustry').innerHTML = htmlArray.join('');
}

function buildDecreaseConceptHtml(data) {
    const htmlArray = data.map(item => {

        const conceptPercent = item.conceptPercent > 0 ? "positive" : "negative";
        const stockPercent = item.leaderPercent > 0 ? "positive" : "negative";
        const downPercent=item.downConceptPercent> 0 ? "positive" : "negative";
        return `
     <tr>
     <td>${item.concept}</td>
     <td class="${conceptPercent}">${item.conceptPercent}%</td>
      <td>
       <div class="stock-detail">
           ${item.leader}<br>
         <span class="${stockPercent}">${item.leaderPercent}%</span>
       </div>
     </td>
     <td>
       <div class="stock-detail">
           ${item.downConcept}<br>
         <span class="${downPercent}">${item.downConceptPercent}%</span>
       </div>
     </td>
     <td>${item.up}</td>
     <td>${item.down}</td>
     </tr>
  `
    });
    document.getElementById('decreaseTr').innerHTML = htmlArray.join('');
}


function buildIndexHtml(data) {
    const htmlArray = data.map(item => {

        return `
      <div class="index-card">
               ${item.name} <br>
                <span class="neutral">${item.current} </span> 
                <span>${item.index}%</span>
            </div>
  `
    });
    document.getElementById('index-container').innerHTML = htmlArray.join('');
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
    getConceptAndIndex();
    getIndustry();
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