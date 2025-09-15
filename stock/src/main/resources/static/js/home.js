
    // 标签页管理
    let tabCount = 0;
    const tabsContainer = document.getElementById('tabs');
    const iframeContent = document.getElementById('iframeContent');

    function openTab(name, href) {
        console.log(name)
        console.log(href)
    const tabId = `tab-${href.replace(/\//g, '-')}`;

    // 检查标签页是否已存在
    let existingTab = document.getElementById(tabId);

    if (existingTab) {
    // 切换到现有标签页
    switchToTab(tabId);
    return;
}

    // 创建新标签页
    const tab = document.createElement('div');
    tab.className = 'tab';
    tab.id = tabId;
    tab.dataset.target = `frame-${tabId}`;
    tab.innerHTML = `
                ${name}
                <span class="tab-close"><i class="fa fa-times"></i></span>
            `;

    // 添加关闭按钮事件
    const closeBtn = tab.querySelector('.tab-close');
    closeBtn.addEventListener('click', (e) => {
    e.stopPropagation();
    closeTab(tabId);
});

    // 创建iframe内容
    const iframe = document.createElement('iframe');
    iframe.className = 'content-frame';
    iframe.id = `frame-${tabId}`;
    iframe.name = `frame-${tabId}`;
    iframe.src = href;

    // 添加标签页和内容
    tabsContainer.appendChild(tab);

    // // 隐藏欢迎信息
    // document.querySelector('.welcome-message').style.display = 'none';

    // 添加iframe到内容区域
    iframeContent.innerHTML = '';
    iframeContent.appendChild(iframe);

    // 切换到新标签页
    switchToTab(tabId);

    // 添加标签点击事件
    tab.addEventListener('click', () => {
    switchToTab(tabId);
});

    tabCount++;
}

    function switchToTab(tabId) {
    // 取消所有标签的活动状态
    document.querySelectorAll('.tab').forEach(t => t.classList.remove('active'));

    // 激活选中的标签
    const selectedTab = document.getElementById(tabId);
    if (selectedTab) {
    selectedTab.classList.add('active');

    // 显示对应的iframe内容
    const targetFrame = document.getElementById(selectedTab.dataset.target);
    if (targetFrame) {
    document.querySelectorAll('.content-frame').forEach(frame => {
    frame.style.display = 'none';
});
    targetFrame.style.display = 'block';
}
}
}

    function closeTab(tabId) {
    const tab = document.getElementById(tabId);
    if (!tab) return;

    const frameId = tab.dataset.target;
    const frame = document.getElementById(frameId);

    // 如果关闭的是当前活动的标签，需要切换到另一个标签
    if (tab.classList.contains('active')) {
    // 尝试找到前一个或后一个标签
    const prevTab = tab.previousElementSibling;
    const nextTab = tab.nextElementSibling;

    if (prevTab) {
    switchToTab(prevTab.id);
} else if (nextTab) {
    switchToTab(nextTab.id);
} else {
    // 没有其他标签，显示欢迎信息
    // document.querySelector('.welcome-message').style.display = 'flex';
    iframeContent.innerHTML = '';
    // iframeContent.appendChild(document.querySelector('.welcome-message'));
}
}

    // 移除标签和iframe
    tab.remove();
    if (frame) frame.remove();

    tabCount--;
}

function init(){
    // 菜单展开/折叠功能
    const menuItems = document.querySelectorAll('.child_menu > li > a');

    menuItems.forEach(item => {
        item.addEventListener('click', function(e) {
            e.preventDefault();

            const parentLi = this.parentElement;
            const isActive = parentLi.classList.contains('active');

            // 关闭所有其他菜单
            document.querySelectorAll('.side-menu > li').forEach(li => {
                li.classList.remove('active');
            });

            // 切换当前菜单状态
            if (!isActive) {
                parentLi.classList.add('active');
            }
        });
    });

    // 菜单项点击事件
    const menuLinks = document.querySelectorAll('.menuItem');

    menuLinks.forEach(link => {
        link.addEventListener('click', function(e) {
            e.preventDefault();

            const menuText = this.textContent;
            const menuHref = this.getAttribute('data-href');

            // 打开或切换到标签页
            openTab(menuText, menuHref);
        });
    });
}

$(function () {
    // document.getElementById('main').innerHTML = '';
    // getLianBanChiListData();
    init();

})

// function getLianBanChiListData() {
//     $.ajax({
//
//         type: "get",
//
//         url: "dealLianBanData",
//
//         data: {},
//
//         success: function (data) {
//             buildLianBanChiListHtml(data);
//         }
//
//     });
// }
//
// function buildLianBanChiListHtml(data) {
//     console.log(data)
//     var htmlArray = '';
//     data.forEach((group, groupIndex) => {
//             htmlArray += `
//
//                     `;
//             console.log(data);
//             document.getElementById('main').innerHTML = htmlArray;
//         }
//     );
// }

// function refreshData() {
//     document.getElementById('main').innerHTML = '';
//     getLianBanChiListData();
// }


