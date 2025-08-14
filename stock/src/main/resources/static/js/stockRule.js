// JSON data
let data = [];


$(function () {
    document.getElementById('content').innerHTML = '';
    getStockRuleData();
})

function getStockRuleData() {
    $.ajax({

        type: "get",

        url: "getStockRuleList",

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

