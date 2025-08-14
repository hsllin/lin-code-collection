// JSON data
let data = [];


$(function () {
    document.getElementById('content').innerHTML = '';
    getTradeRuleList();
})

function getTradeRuleList() {
    $.ajax({

        type: "get",

        url: "getTradeRuleList",

        data: {},

        success: function (reuslt) {
            data = reuslt;
            renderTable();
        }

    });
}

// Function to render the table
function renderTable() {
    var htmlArray = '';
    data.forEach((item, index) => {

        htmlArray += `
        <li class="my">
                <p>${item.content}</p>
            </li>
  `
    });
    console.log(htmlArray)
    document.getElementById('content').innerHTML = htmlArray;
}

