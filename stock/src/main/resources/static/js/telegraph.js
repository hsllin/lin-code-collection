$(function () {
    document.getElementById('main').innerHTML = '';
    getData();

})

function getData() {
    $.ajax({

        type: "get",

        url: "getTelegraph",

        data: {},

        success: function (data) {
            buildHtml(data);
        }

    });
}

function buildHtml(data) {
    console.log(data)
    var htmlArray = '';
    data.data.roll_data.forEach((group, groupIndex) => {
            var time = formatTimestamp(group.ctime);
            var images = '';
            var stockList = ''
            group.images.forEach((image, imageIndex) => {
                images += `<img src="${image}" style="width: 400px;height: 600px" onclick="openImageModal(this.src)">`
            });
            group.stock_list.forEach((stock, stockIndex) => {
                stockList += ` <div class="stock-item">
                    <span>${stock.name}</span>
                    <span>${stock.RiseRange.toFixed(2)}%</span>
                </div>`
            });
            htmlArray += `
              <!-- 新闻一 -->
        <div class="news-item">
            <div class="news-title">${time} ${group.title}</div>
<!--            <div class="news-time">2025.10.15 星期三</div>-->
            <div class="news-content hidden-content">
                <p>${group.content}</p>
                <div class="image-container">
                ${images}
<!--                    <img src="https://via.placeholder.com/400x200?text=资金流入数据图表" alt="资金流入数据图表" onclick="openImageModal(this.src)">-->
                </div>
            </div>
            <div class="expand-btn" onclick="toggleExpand(this)">展开更多...</div>
            <div class="market-data">
            ${stockList}
<!--                <div class="stock-item">-->
<!--                    <span>三花智控</span>-->
<!--                    <span>-2.85%</span>-->
<!--                </div>-->
            </div>
            <div class="stats-section">
<!--                <div class="stats-text">阅262.31W</div>-->
<!--                <div class="stats-actions">-->
<!--                    <span>评论(0)</span>-->
<!--                    <span>分享(69)</span>-->
<!--                </div>-->
            </div>
        </div>
                    `;

        }
    );
    document.getElementById('main').innerHTML = htmlArray;
}

function refreshData() {
    document.getElementById('main').innerHTML = '';
    getData();
}

// 展开/收起内容
function toggleExpand(button) {
    const content = button.previousElementSibling;
    content.classList.toggle("expanded");
    button.textContent = content.classList.contains("expanded")
        ? "收起内容"
        : "展开更多...";
}

// 打开图片查看
function openImageModal(src) {
    const modal = document.getElementById("imageModal");
    const modalImage = document.getElementById("modalImage");
    modalImage.src = src;
    modal.style.display = "block";
}

// 关闭图片查看
function closeImageModal() {
    document.getElementById("imageModal").style.display = "none";
}

// 点击模态框外部关闭
window.onclick = function (event) {
    const modal = document.getElementById("imageModal");
    if (event.target === modal) {
        closeImageModal();
    }
}

