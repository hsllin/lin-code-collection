$(function () {
    document.getElementById('main').innerHTML = '';

    getData();

})

function getData() {
    $.ajax({

        type: "get",

        url: "getCaiLianSheLimitUp",

        data: {},

        success: function (data) {
            buildHtml(data);
        }

    });
}

function buildHtml(data) {
    console.log(data)
    var htmlArray = '';
    var tagArray = '<div class="tabs">';
    console.log(document.getElementById('main'));
    data.data.plate_stock.forEach((group, groupIndex) => {
            if (group.secu_name === 'ST股') {
                return;
            }
            tagArray += `<div class="tab">${group.secu_name}(${group.plate_stock_up_num})</div>`;
            var stockList = '';
            group.stock_list.forEach((stock, stockIndex) => {
                stockList += `
                 <tr>
                <td>
                    <div class="stock-name">${stock.secu_name}</div>
                    <div class="stock-code">${stock.secu_code}</div>
                </td>
                <td>${stock.last_px}</td>
                <td class="stock-change positive">${(stock.change * 100).toFixed(2)}%</td>
                <td>${stock.time}</td>
                <td>${formatToYi(stock.cmc)}亿</td>
            </tr>
            <tr>
                <td colspan="5">
                    <div class="stock-highlight">${stock.up_num}</div>
                    <div class="stock-description">
                    ${stock.up_reason}
                    </div>
                    <div class="stock-description">
                    ${stock.up_tags}
                    </div>
                </td>
            </tr>
            `;
            });
            htmlArray += `
             <div class="sector-title">
            <div class="sector-name">${group.secu_name}</div>
            <div class="sector-change negative">${(group.change * 100).toFixed(2)}%</div>
        </div>

        <div class="sector-info">
            <div class="sector-driver">驱动因素</div>
            <div>${group.up_reason}</div>
        </div>
                  <table class="stock-table">
            <thead>
            <tr>
                <th>简称</th>
                <th>现价</th>
                <th>涨幅</th>
                <th>涨停时间</th>
                <th>流通市值</th>
            </tr>
            </thead>
            <tbody>  ${stockList}
            </tbody>
        </table>`;

        }
    );
    tagArray += '</div>';
    // console.log(document.getElementById('stockTab'));
    // document.getElementById('stockTab').innerHTML = tagArray;
    document.getElementById('main').innerHTML = tagArray + htmlArray;
    document.querySelectorAll('.tab').forEach(tab => {
        tab.addEventListener('click', function () {
            document.querySelectorAll('.tab').forEach(t => t.classList.remove('active'));
            this.classList.add('active');
        });
    });
}

function refreshData() {
    document.getElementById('main').innerHTML = '';
    getData();
}


