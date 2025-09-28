$(function () {
    // 点赞功能实现
    const likeBtn = document.getElementById('likeBtn');
    const likeCount = document.getElementById('likeCount');
    const likeMessage = document.getElementById('likeMessage');

    // 从localStorage获取点赞数据
    let likes = localStorage.getItem('systemLikes');
    likes = likes ? parseInt(likes) : 0;
    likeCount.textContent = likes;

    // 检查用户是否已经点过赞
    const hasLiked = localStorage.getItem('hasLiked') === 'true';
    // if (hasLiked) {
    //     likeBtn.classList.add('active');
    //     likeBtn.innerHTML = '<svg class="heart-icon" viewBox="0 0 24 24"><path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"/></svg> 已点赞';
    //     likeBtn.disabled = true;
    // }

    // 点赞按钮点击事件
    likeBtn.addEventListener('click', function () {
        // if (hasLiked) return;

        likes++;
        likeCount.textContent = likes;
        likeBtn.classList.add('active');
        likeBtn.innerHTML = '<svg class="heart-icon" viewBox="0 0 24 24"><path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"/></svg> 已点赞';
        likeBtn.disabled = true;

        // 添加点赞动画效果
        likeCount.classList.add('animate');
        setTimeout(() => {
            likeCount.classList.remove('animate');
        }, 500);

        // 显示感谢消息
        likeMessage.classList.add('show');
        setTimeout(() => {
            likeMessage.classList.remove('show');
        }, 3000);

        // 保存到localStorage
        localStorage.setItem('systemLikes', likes);
        localStorage.setItem('hasLiked', 'true');

        // 添加按钮动画效果
        likeBtn.style.transform = 'scale(1.1)';
        setTimeout(() => {
            likeBtn.style.transform = 'scale(1)';
        }, 300);
    });

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

function updateClock() {

}

