$(function () {
    document.getElementById('main').innerHTML = '';
    getLianBanChiListData();

})

function getLianBanChiListData() {
    $.ajax({

        type: "get",

        url: "dealLianBanData",

        data: {},

        success: function (data) {
            buildLianBanChiListHtml(data);
        }

    });
}

function buildLianBanChiListHtml(data) {
    // console.log(data)
    console.log(22222222222222);
    var htmlArray = '';
    var tagList = '';

    data.forEach((group, groupIndex) => {
            htmlArray += `
              <h3>${group.limitNum}连板 </h3>
        <table class="stock-table">
            <thead>
            <tr>
                <th>股票名称</th>
                <th>价格</th>
                <th>概念</th>
                <th>涨停时间</th>
            </tr>
            </thead>
            <tbody>
            <!-- 数据行开始 -->
            
`
            group.stockList.forEach((stock, stockIndex) => {
                console.log(stock.concept)
                tagList = splitByPlus(stock.concept);

                htmlArray += ` <tr>
                    <td>${stock.name}(${stock.code})</td>
                    <td class="price">${stock.price}</td>
                    <td>`
                if (tagList){
                    tagList.forEach((tag, tagIndex) => {
                        htmlArray += `
                        <span class="concept-tag">${tag}</span>`
                    });
                }


                htmlArray += ` </td>
                        <td class="time">${stock.firstTime}</td>
                        </tr>
                    `

            });
            htmlArray += '  </tbody>\n' +
                '    </table>';


        }
    );
    console.log(1111111111111);
    console.log(htmlArray);
    document.getElementById('main').innerHTML = htmlArray;
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
    document.getElementById('main').innerHTML = '';
    getLianBanChiListData();
}

function sortTable(orderFiled, orderBy) {
    document.getElementById('pool').innerHTML = '';
    getLimitPoolData(orderFiled, orderBy);
    // 更新排序指示器
    // updateSortIndicator(column);
}


function updateSortIndicator(column) {
    const indicators = document.querySelectorAll(".sort-indicator");
    indicators.forEach(ind => ind.textContent = "");
    // indicators[column].textContent = sortDirections[column] ? "▼" : "▲";
}

function splitByPlus(str) {
    // 使用 split 方法按 '+' 分隔字符串
    if (null==str){
        return '';
    }
    return str.split('+');
}