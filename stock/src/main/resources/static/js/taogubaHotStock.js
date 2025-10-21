var type = 1;
var stocks=[];
var stockData;
$(function () {
    document.getElementById('stock-list').innerHTML = '';
    // 主标签页切换
    const mainTabs = document.querySelectorAll('.tab');
    mainTabs.forEach(tab => {
        tab.addEventListener('click', function () {
            // 移除所有active类
            mainTabs.forEach(t => t.classList.remove('active'));
            // 给当前点击的标签添加active类
            this.classList.add('active');

            // 这里可以添加切换内容的逻辑
            // 根据data-tab属性值显示不同的内容
        });
    });

    // 子标签页切换
    const subTabs = document.querySelectorAll('.sub-tab');
    subTabs.forEach(tab => {
        tab.addEventListener('click', function () {
            // 移除所有active类
            subTabs.forEach(t => t.classList.remove('active'));
            // 给当前点击的标签添加active类
            this.classList.add('active');
            document.getElementById('stock-list').innerHTML = '';
            type=this.getAttribute("data-subtab")
            getData();
            // 这里可以添加切换内容的逻辑
            // 根据data-subtab属性值显示不同的内容
        });
    });
    getData();

})

function getData() {
    $.ajax({

        type: "get",

        url: "getTaoGuBaHotStock",

        data: {
            type: type
        },

        success: function (data) {
            stockData=data;
            stocks=[];
            data.dto.forEach((group, groupIndex) => {
                stocks.push(group.fullCode)
            });
            getTaoGuBaStockPrice();
        }

    });
}

function buildDataHtml(data) {
    console.log(data)
    var htmlArray = '';
    var tagList;
    var continuous;

    console.log(stocks)
    stockData.dto.forEach((group, groupIndex) => {
            continuous = group.continuenum > 0 ? '' : 'hidden';
            console.log(continuous)
            tagList = group.gnList
            htmlArray += `
                <div class="stock-item">
                <div class="stock-rank">${group.ranking}</div>
                <div class="stock-info">
                    <div class="stock-name">${group.stockName} (${group.fullCode})</div>
                    <div class="stock-tags">
                        <div class="tag continuous" "${continuous}">持续上榜</div>`
            if (tagList) {
                tagList.forEach((tag, tagIndex) => {
                    htmlArray += `
                        <div class="tag normal">${tag.gnName}</div>
                        `
                });
            }
            htmlArray += `
                    </div>
                    <div class="stock-concepts">${group.reason}</div>
                </div>
                <div class="stock-change positive">${data.dto[groupIndex].pxChangeRate}%</div>
            </div>
                    `;
            console.log(data);
            document.getElementById('stock-list').innerHTML = htmlArray;
        }
    );
}
function getTaoGuBaStockPrice() {
    $.ajax({

        type: "get",

        url: "getTaoGuBaStockPrice",

        data: {
            stocks: stocks
        },

        success: function (data) {
            console.log(data)
            buildDataHtml(data)
        }

    });
}


function refreshData() {
    document.getElementById('stock-list').innerHTML = '';
    getData();
}


