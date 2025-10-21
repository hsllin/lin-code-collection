$(function () {
    document.getElementById('main').innerHTML = '';
    getData();

})

function getData() {
    $.ajax({

        type: "get",

        url: "dealLianBanData",

        data: {},

        success: function (data) {
            buildHtml(data);
        }

    });
}

function buildHtml(data) {
    console.log(data)
    var htmlArray = '';
    data.forEach((group, groupIndex) => {
            htmlArray += `
            
                    `;
            console.log(data);
            document.getElementById('main').innerHTML = htmlArray;
        }
    );
}

function refreshData() {
    document.getElementById('main').innerHTML = '';
    getData();
}


