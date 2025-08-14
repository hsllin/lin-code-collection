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
    getLianBanChiListData();
}


