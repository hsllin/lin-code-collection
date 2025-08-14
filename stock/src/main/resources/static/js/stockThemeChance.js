$(function () {
    // document.getElementById('main').innerHTML = '';
    getStockThemeChance();

})

function getStockThemeChance() {
    $.ajax({

        type: "get",

        url: "getStockThemeChance",

        data: {},

        success: function (data) {
            data = unicodeToChinese(data);
            data = JSON.parse(data);
            buildStockThemeChanceHtml(data);
        }

    });
}

function buildStockThemeChanceHtml(data) {
    console.log(data)
    var htmlArray = '';
    data.List.forEach((group, groupIndex) => {
            htmlArray += `
            <div class="section">
            <div class="section-title">${group.ZSName}</div>
            <div class="section-date">${formatTime(group.TimeStamp)} ${group.Title}</div>
            <ul class="stock-list">
                    `;
        group.Stocks.forEach((stock, stockIndex) => {

            htmlArray += ` 
            <li class="stock-item">
                    <div class="stock-info">
                        <span class="stock-code">${stock.Code}</span>
                        <span class="stock-name">${stock.Name}</span>
                    </div>
                    <div class="stock-change positive">${stock.Rate}%</div>
                </li>
            `
        });
        htmlArray += `  </ul>
        </div>`;

        }
    );
    document.getElementById('main').innerHTML = htmlArray;
}

function refreshData() {
    document.getElementById('main').innerHTML = '';
    getLianBanChiListData();
}

function unicodeToChinese(str) {
    return str.replace(/\\u([\da-fA-F]{4})/g,
        (_, hex) => String.fromCharCode(parseInt(hex, 16))
    );
}

function formatTime(timestamp){
    // 转换为北京时间
    const date = new Date(timestamp * 1000);

// 获取年、月、日、时、分、秒
    const year = date.getFullYear();
    const month = date.getMonth() + 1; // 月份从0开始
    const day = date.getDate();
    const hours = date.getHours();
    const minutes = date.getMinutes();
    const seconds = date.getSeconds();

// 格式化日期和时间
    const formattedDate = `${year}-${month.toString().padStart(2, '0')}-${day.toString().padStart(2, '0')} ${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`;
    return formattedDate;
}

