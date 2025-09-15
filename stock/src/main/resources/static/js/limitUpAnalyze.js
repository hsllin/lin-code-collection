$(function () {
    document.getElementById('mainData').innerHTML = '';
    getLimitUpAnalyzeData();

})

function getLimitUpAnalyzeData() {
    $.ajax({

        type: "get",

        url: "getLimitUpAnalyze",

        data: {},

        success: function (data) {
            data = unicodeToChinese(data);
            data = JSON.parse(data);
            console.log(data)
            buildLimitUpAnalyzeHtml(data);
        }

    });
}

function buildLimitUpAnalyzeHtml(data) {
    console.log(data.data)
    var htmlArray = '';
    data.data.forEach((group, groupIndex) => {
        console.log(group.plate_name)
        // htmlArray+=`<tr>
        //      <th colspan="7">${group.plate_name} <span class="hot-tag">${group.stocks.length}</span> 个</th>
        //       </tr>`
        htmlArray+=`<tr class="section-title">
                    <td colspan="7">${group.plate_name} (${group.stocks.length}只)</td>
                </tr>`
        group.stocks.forEach((item,itemGroup)=>{
            const marketType=item.market_type?'('+item.market_type+')':''
            // htmlArray += `
            //   <tr>
            //         <td>${item.stock_name}${marketType}</td>
            //         <td>${item.stock_code}</td>
            //         <td>${item.stock_price}</td>
            //         <td>${item.up_limit_time}</td>
            //         <td>${item.up_limit_desc}</td>
            //         <td>${formatToYi(item.fengdan_money)}</td>
            //         <td>${formatToYi(item.actualcirculation_value)}</td>
            //     </tr>
            //     <tr>
            //         <td colspan="7">
            //             <div class="stock-info">
            //                 <div>液冷服务器+水利；</div>
            //                 <div>${item.reason}</div>
            //             </div>
            //         </td>
            //     </tr>
            //         `;
            htmlArray += `
              <tr>
                    <td>
                        <div class="stock-name">${item.stock_name}</div>
                        <div class="stock-tags">
                            <span class="myTag">${marketType}</span>
                        </div>
                    </td>
                    <td><span class="stock-code">${item.stock_code}</span></td>
                    <td class="up-limit">${item.stock_price}</td>
                    <td>${item.up_limit_time}</td>
                    <td><span class="first-board">${item.up_limit_desc}</span></td>
                    <td>${formatToYi(item.fengdan_money)}</td>
                    <td>${formatToYi(item.actualcirculation_value)}</td>
                </tr>
                <tr>
                    <td colspan="7">
                        <div class="description">
                        ${item.reason}
                        </div>
                    </td>
                </tr>
                    `;
        })
            htmlArray += `
            
                    `;

        }
    );
    document.getElementById('mainData').innerHTML = htmlArray;
}

function refreshData() {
    document.getElementById('mainData').innerHTML = '';
    getLimitUpAnalyzeData();
}

function unicodeToChinese(str) {
    return str.replace(/\\u([\da-fA-F]{4})/g,
        (_, hex) => String.fromCharCode(parseInt(hex, 16))
    );
}

function formatToYi(num) {
    // 转换为数字类型并验证有效性
    const number = parseFloat(num);
    if (isNaN(number)) return '0';

    const absNum = Math.abs(number);

    // 超过1亿：转换为亿单位
    if (absNum >= 100000000) {
        const result = (number / 100000000).toFixed(2).replace(/\.?0+$/, '');
        return result + '亿';
    }
    // 超过1万但不足1亿：转换为万单位
    else if (absNum >= 10000) {
        const result = (number / 10000).toFixed(2).replace(/\.?0+$/, '');
        return result + '万';
    }
    // 不足1万：直接返回整数
    return Math.round(number).toString();
}


