$(function () {
    document.getElementById("ratioRanking").innerHTML = '';
    getRatioRankingData();

})

function getRatioRankingData() {
    $.ajax({

        type: "get",

        url: "getRatioRanking",

        data: {

            type: "1"

        },

        success: function (data) {
            buildRatioRankingHtml(data);
        }

    });
}


function buildRatioRankingHtml(data) {
    const htmlArray = data.dataBeanList.map(stock => {
        console.log( stock.changeRate)
        const code = stock.increaseAndDecrease > 5 ? 'code' : '';
        const upOrDown = stock.increaseAndDecrease > 0 ? 'up' : 'down';
        const fudu = stock.amplitude > 10 ? 'up' : 'down';
        const changeRate = stock.changeRate > 15 ? '' : 'code';
        // 判断条件
        return `
            <tr>
                <td class="stock-code">${stock.code}</td>
                <td class="stock-name ${code}">${stock.name}</td>
                <td class="volume-ratio">${stock.ratio}</td>
                 <td class="volume-ratio ${changeRate}">${stock.changeRate}%</td>
                <td class="change">${stock.increaseAndDecrease}%</td>
                <td class="latest-price">${stock.current}</td>
                <td class="amount">${stock.sellMoney}</td>
                <th class="amount">${stock.max}</th>
                <th class="amount">${stock.min}</th>
                <th class="amount ${fudu}">${stock.amplitude}%</th>
            </tr>
  `
    });
    document.getElementById('ratioRanking').innerHTML = htmlArray.join('');
}

function buildDecreaseHtml(data) {
    const htmlArray = data.map(stock => {
        const percentValue = stock.percent;

        // 判断条件
        const icon = percentValue < -5 ? '💥' :
            '';
        return `
    <div class="stock-item">
   
        <span>${stock.time} ${stock.count}</span>
        <span>${icon}${stock.n}(${stock.c})</span>
        <span class="negative">${stock.percent}%</span>
    </div>
  `
    });
    document.getElementById('industry-item-down').innerHTML = htmlArray.join('');
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
    document.getElementById('ratioRanking').innerHTML = '';
    getRatioRankingData();

}