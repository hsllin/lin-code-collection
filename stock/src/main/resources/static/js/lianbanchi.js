$(function () {
    document.getElementById('lianban').innerHTML = '';
    getLianBanChiListData();

})

function getLianBanChiListData() {
    $.ajax({

        type: "get",

        url: "getLianBanChiList",

        data: {},

        success: function (data) {
            buildLianBanChiListHtml(data);
        }

    });
}

function downLoadData(dateIndex) {
    $.ajax({

        type: "get",

        url: "downloadLianBanData",

        data: {
            dateIndex: dateIndex
        },

        success: function (data) {
        }

    });
}


function buildLianBanChiListHtml(data) {

    var htmlArray = '';

    data.forEach((group, groupIndex) => {
        htmlArray += `
 
<section class="day-section">
            <h2 class="date-header">${group.date} 连板信息
            <a href="javascript:;" onclick="downLoadData(${groupIndex})" class="refresh-btn" title="导出" style="margin-left: 10px;">
                    <i class="fa fa-refresh"></i> 导出
                </a>
            </h2>
`

        group.data.forEach((lianban, lianIndex) => {
                htmlArray += `<div class="streak-level">
      <h3 class="streak-title">${lianban.height}连板 包含个股数量：${lianban.number}只</h3>
      <div class="stock-list">`;
                lianban.code_list.forEach((stock, stockIndex) => {

                    htmlArray += `             <div class="stock-item">
                                      <span class="stock-name">${stock.name}</span>
                                      <span class="stock-code">${stock.code}</span>
                                  </div>`

                });
                htmlArray += `     </div>
                </div>`;

            }
        );

        htmlArray += '</section>';
    });


    console.log(data);
    //   const htmlArray = data.map((item, index) => {
    //       return `<section class="day-section">
    //           <h2 class="date-header">2025-01-22 连板信息</h2>` +
    //           item.map(row => {
    //               return `
    //             <div class="stock-item">
    //                       <span class="stock-name">${row.name}</span>
    //                       <span class="stock-code">${row.code}</span>
    //                   </div>
    //           `;
    //           });
    //       return `
    //      <div class="streak-level">
    //               <h3 class="streak-title">${item.continue_num}连板</h3>
    //               <div class="stock-list">
    // ` +
    //           `   </div>
    //           </div>
    //       </section>`;
    //   });
    console.log(htmlArray);
    document.getElementById('lianban').innerHTML = htmlArray;
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
    document.getElementById('lianban').innerHTML = '';
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