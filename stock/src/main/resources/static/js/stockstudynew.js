// 模拟数据
let stocksData = {
    today: [],
    learned: [],
    notLearned: []
};
let currentPage = 1;
const pageSize = 10;
const paginationState = {
    currentPage: 1,
    pageSize: 20,
    totalItems: 100,
    totalPages: 5,
    maxVisibleButtons: 5
};
let type = 2;

$(function () {
    // 初始化渲染
    document.getElementById('stocks').innerHTML = '';
    // 标签切换功能
    getStudyStockListData();
    getStudyNumData();
    const tabs = document.querySelectorAll('.tab');
    tabs.forEach(tab => {
        tab.addEventListener('click', () => {
            tabs.forEach(t => t.classList.remove('active'));
            tab.classList.add('active');

            // 在实际应用中，这里会调用API加载对应标签的数据
            console.log(`切换到: ${tab.dataset.tab}`);
            if (tab.dataset.tab === 'today') {
                type = '2';
            } else if (tab.dataset.tab === 'learned') {
                type = '1';
            } else {
                type = '0';
            }
            getStudyStockListData();
        });
    });
    // 使用事件委托来处理动态生成的元素
    $(document).on('click', '.show-description', function () {
        const description = $(this).prev('.description');
        const lineClamp = description.css('webkitLineClamp');

        if (lineClamp === '2') {
            description.css('webkitLineClamp', 'unset');
            $(this).text('收起');
        } else {
            description.css('webkitLineClamp', '2');
            $(this).text('查看更多');
        }
    });
    // 概念标签展开/收起功能
    // document.querySelectorAll('.toggle-tags').forEach(button => {
    //     button.addEventListener('click', function () {
    //         const concepts = this.previousElementSibling;
    //         const hiddenTags = concepts.querySelectorAll('.concept-tag.hidden');
    //
    //         if (hiddenTags.length > 0) {
    //             hiddenTags.forEach(tag => tag.classList.remove('hidden'));
    //             this.textContent = '收起';
    //         } else {
    //             const allTags = concepts.querySelectorAll('.concept-tag');
    //             for (let i = 3; i < allTags.length; i++) {
    //                 allTags[i].classList.add('hidden');
    //             }
    //             this.textContent = '展开更多';
    //         }
    //     });
    // });
    // $(document).on('click', '.show-more', function() {
    //     const concepts = this.previousElementSibling;
    //     const hiddenTags = concepts.querySelectorAll('.concept-tag.hidden');
    //
    //     if (hiddenTags.length > 0) {
    //         hiddenTags.forEach(tag => tag.classList.remove('hidden'));
    //         this.textContent = '收起';
    //     } else {
    //         const allTags = concepts.querySelectorAll('.concept-tag');
    //         for (let i = 3; i < allTags.length; i++) {
    //             allTags[i].classList.add('hidden');
    //         }
    //         this.textContent = '展开更多';
    //     }
    // });
    $(document).on('click', '.show-more', function () {
        const container = this.parentElement;
        const hiddenTags = container.querySelectorAll('.concept-tag.hidden');

        hiddenTags.forEach(tag => {
            tag.classList.remove('hidden');
        });

        this.style.display = 'none';
    });
    $(document).on('click', '.action-btn', function () {
        const button = $(this);

        // 如果已经是已学习状态，则不做任何操作
        if (button.data('learned')) return;

        changeStudyStatus($(this).val());
        getStudyNumData();


        // 添加动画效果
        button.css({
            transform: 'scale(0.95)',
            opacity: 0.8
        });

        // 短暂延迟后更新状态
        setTimeout(() => {
            button.text('已学习');
            button.addClass('learned');
            button.data('learned', true);
            button.prop('disabled', true);

            // 恢复按钮样式（不带动画）
            button.css({
                transform: '',
                opacity: ''
            });

            // 这里可以添加保存到服务器的逻辑
            // saveLearnedStatus(button.closest('tr').data('stock-id'));

            // 视觉反馈 - 添加完成动画
            button.parent().append(
                $('<div class="checkmark-animation">✓</div>').fadeIn(300).delay(500).fadeOut(400, function () {
                    $(this).remove();
                })
            );
        }, 200);

    });

    // 添加股票模态框控制
    const addBtn = document.getElementById('addStockBtn');
    const modal = document.getElementById('addStockModal');
    const closeBtn = document.querySelector('.close-btn');
    const cancelBtn = document.querySelector('.cancel-btn');

    addBtn.addEventListener('click', () => {
        modal.style.display = 'flex';
    });

    closeBtn.addEventListener('click', () => {
        modal.style.display = 'none';
    });

    cancelBtn.addEventListener('click', () => {
        modal.style.display = 'none';
    });

    // 点击模态框外部关闭
    window.addEventListener('click', (e) => {
        if (e.target === modal) {
            modal.style.display = 'none';
        }
    });

    // 表单提交处理
    const stockForm = document.getElementById('stockForm');
    stockForm.addEventListener('submit', (e) => {
        e.preventDefault();

        // 这里实际应用中会发送数据到后端
        addOrEditStudyStock();
        modal.style.display = 'none';
        stockForm.reset();
    });

    // 分页按钮事件
    // document.querySelectorAll('.pagination button').forEach(button => {
    //     button.addEventListener('click', function () {
    //         if (this.classList.contains('active')) return;
    //
    //         document.querySelector('.pagination button.active').classList.remove('active');
    //         this.classList.add('active');
    //
    //         // 实际应用中会调用API加载新页面数据
    //         console.log('加载新页面数据');
    //     });
    // });

    // 搜索功能
    const searchInput = document.getElementById('searchInput');
    searchInput.addEventListener('keyup', (e) => {
        if (e.key === 'Enter') {
            // 实际应用中会调用API搜索股票
            console.log(`搜索: ${searchInput.value}`);
        }
    });
    renderPagination();

})

// 渲染分页组件
function renderPagination() {
    const paginationContainer = document.getElementById('pagination');
    paginationContainer.innerHTML = '';

    // 添加上一页按钮
    const prevButton = document.createElement('button');
    prevButton.innerHTML = '<i class="fa fa-chevron-left"></i>';
    prevButton.title = '上一页';
    prevButton.disabled = paginationState.currentPage === 1;
    if (prevButton.disabled) prevButton.classList.add('disabled');
    prevButton.addEventListener('click', () => goToPage(paginationState.currentPage - 1));
    paginationContainer.appendChild(prevButton);

    // 计算要显示的页码范围
    let startPage, endPage;
    if (paginationState.totalPages <= paginationState.maxVisibleButtons) {
        // 总页数小于等于最大可见按钮数
        startPage = 1;
        endPage = paginationState.totalPages;
    } else {
        // 计算起始页和结束页
        const maxVisibleHalf = Math.floor(paginationState.maxVisibleButtons / 2);
        if (paginationState.currentPage <= maxVisibleHalf + 1) {
            // 当前页在开头部分
            startPage = 1;
            endPage = paginationState.maxVisibleButtons;
        } else if (paginationState.currentPage >= paginationState.totalPages - maxVisibleHalf) {
            // 当前页在结尾部分
            startPage = paginationState.totalPages - paginationState.maxVisibleButtons + 1;
            endPage = paginationState.totalPages;
        } else {
            // 当前页在中间部分
            startPage = paginationState.currentPage - maxVisibleHalf;
            endPage = paginationState.currentPage + maxVisibleHalf;
        }
    }

    // 添加第一页按钮（如果需要）
    if (startPage > 1) {
        const firstButton = createPageButton(1);
        paginationContainer.appendChild(firstButton);

        if (startPage > 2) {
            const ellipsis = document.createElement('button');
            ellipsis.innerHTML = '<i class="fa fa-ellipsis-h"></i>';
            ellipsis.classList.add('ellipsis');
            ellipsis.disabled = true;
            paginationContainer.appendChild(ellipsis);
        }
    }

    // 添加页码按钮
    for (let i = startPage; i <= endPage; i++) {
        const pageButton = createPageButton(i);
        paginationContainer.appendChild(pageButton);
    }

    // 添加最后一页按钮（如果需要）
    if (endPage < paginationState.totalPages) {
        if (endPage < paginationState.totalPages - 1) {
            const ellipsis = document.createElement('button');
            ellipsis.innerHTML = '<i class="fa fa-ellipsis-h"></i>';
            ellipsis.classList.add('ellipsis');
            ellipsis.disabled = true;
            paginationContainer.appendChild(ellipsis);
        }

        const lastButton = createPageButton(paginationState.totalPages);
        paginationContainer.appendChild(lastButton);
    }

    // 添加下一页按钮
    const nextButton = document.createElement('button');
    nextButton.innerHTML = '<i class="fa fa-chevron-right"></i>';
    nextButton.title = '下一页';
    nextButton.disabled = paginationState.currentPage === paginationState.totalPages;
    if (nextButton.disabled) nextButton.classList.add('disabled');
    nextButton.addEventListener('click', () => goToPage(paginationState.currentPage + 1));
    paginationContainer.appendChild(nextButton);

    // 添加每页显示数量选择器
    const pageSizeContainer = document.createElement('div');
    pageSizeContainer.className = 'page-size';
    pageSizeContainer.innerHTML = `
                每页显示:
                <select id="pageSizeSelect">
                    <option value="10">10</option>
                    <option value="20" selected>20</option>
                    <option value="50">50</option>
                    <option value="100">100</option>
                </select>
            `;
    paginationContainer.appendChild(pageSizeContainer);

    // 设置当前选择的每页显示数量
    document.getElementById('pageSizeSelect').value = paginationState.pageSize;

    // 添加每页显示数量改变事件
    document.getElementById('pageSizeSelect').addEventListener('change', function () {
        paginationState.pageSize = parseInt(this.value);
        paginationState.totalPages = Math.ceil(paginationState.totalItems / paginationState.pageSize);

        // 如果当前页大于总页数，则跳转到最后一页
        if (paginationState.currentPage > paginationState.totalPages) {
            paginationState.currentPage = paginationState.totalPages;
        }

        // 重新渲染
        generateContent();
        renderPagination();
        // updatePageInfo();
    });
}

// 生成内容卡片
function generateContent() {
    getStudyStockListData()
}

// 创建页码按钮
function createPageButton(pageNumber) {
    const button = document.createElement('button');
    button.textContent = pageNumber;
    button.title = `转到第 ${pageNumber} 页`;

    if (pageNumber === paginationState.currentPage) {
        button.classList.add('active');
    }

    button.addEventListener('click', () => goToPage(pageNumber));
    return button;
}

// 跳转到指定页面
function goToPage(page) {
    if (page < 1 || page > paginationState.totalPages || page === paginationState.currentPage) {
        return;
    }

    paginationState.currentPage = page;
    generateContent();
    renderPagination();
    updatePageInfo();

    // 滚动到顶部
    window.scrollTo({top: 0, behavior: 'smooth'});
}

// 更新页面信息
function updatePageInfo() {
    // document.getElementById('currentPage').textContent = paginationState.currentPage;
    // document.getElementById('totalPages').textContent = paginationState.totalPages;
    // document.getElementById('pageSizeValue').textContent = paginationState.pageSize;
}

function getStudyStockListData() {
    const searchText = document.getElementById('searchInput').value.toLowerCase();
    $.ajax({

        type: "get",

        url: "getStudyStockList",

        data: {
            keyword: searchText,
            current: paginationState.currentPage,
            size: paginationState.pageSize,
            type: type
        },

        success: function (data) {
            console.log(data)
            paginationState.totalPages = Math.round(data.total / data.size);
            paginationState.maxVisibleButtons = Math.round(data.total / data.size);
            console.log(Math.round(data.total / data.size))
            renderStocks(data);
        }

    });
}

function addOrEditStudyStock() {
    const code = document.getElementById('stockCode').value;
    $.ajax({
        type: "get",
        url: "addOrEditStudyStock",

        data: {
            code: code,
        },

        success: function (data) {
        }

    });
}

function getStudyNumData() {
    $.ajax({

        type: "get",

        url: "getStudyNum",

        data: {},

        success: function (data) {
            console.log(data)
            $("#todayNum").text(data.today_learned + "/" + data.totay_total)
            $("#learned").text(data.learned)
            $("#not_learned").text(data.not_learned)
            $("#total").text(data.total)
        }

    });
}

function changeStudyStatus(code) {
    $.ajax({

        type: "get",

        url: "changeStudyStatus",

        data: {
            code: code,
        },

        success: function (data) {
            // getStudyStockListData();
        }

    });
}

function sysTodayStudyData() {
    $.ajax({

        type: "get",

        url: "sysTodayStudyData",

        data: {},

        success: function (data) {
        }
    });
}

// 渲染股票数据
function renderStocks(stocks) {
    var innerHTML = '';
    console.log(stocks.records)
    stocks.records.forEach((stock, index) => {
        // 处理概念标签逻辑
        let conceptsHTML = '';
        const concepts = stock.concepts ? stock.concepts.split(';') : [];
        const visibleConcepts = 3; // 默认显示的概念数量
        const remainingCount = concepts.length > visibleConcepts ? concepts.length - visibleConcepts : 0;

        concepts.forEach((concept, conceptIndex) => {
            // 添加隐藏类（只显示前几个，其他的需要隐藏）
            const isHidden = conceptIndex >= visibleConcepts;
            conceptsHTML += `<span class="concept-tag ${isHidden ? 'hidden' : ''}">${concept}</span>`;
        });

        // 添加更多按钮（如果有隐藏的概念）
        let moreButton = '';
        if (remainingCount > 0) {
            moreButton = `<button class="show-more">+${remainingCount}</button>`;
        }

        innerHTML += `
    <tr>
        
        <td>${stock.code}</td>
        <td>${stock.name}</td>
        <td>${formatToYi(stock.marketValue)}</td>
        <td>${stock.rate}%</td>
        <td>${stock.price}</td>
        <td>${stock.industry}</td>
        <td>
            <div class="concepts">
                ${conceptsHTML}
                ${moreButton}
            </div>
        </td>
        <td>
            <div class="description">
             <span class="collapse-text" 
              data-detail="${stock.mainBusiness}" 
              onclick="showDetail(this.getAttribute('data-detail'))">
             ${stock.mainBusiness}
            </span>
        </div>
        </td>
        <td>
        <div class="description">
             <span class="collapse-text" 
              data-detail="${stock.companyIntroduce}" 
              onclick="showDetail(this.getAttribute('data-detail'))">
             ${stock.companyIntroduce}
            </span>
        </div>
            
<!--            <button class="show-description">查看更多</button>-->
        </td>
        <td>
            <button class="action-btn ${stock.studyStatus === '1' ? 'learned' : ''} " value="${stock.code}" ${stock.studyStatus === '1' ? 'style disabled' : ''} >${stock.studyStatus === '0' ? '标记已学' : '已学习'}</button>
        </td>
    </tr>
    `;
    });
    document.getElementById('stocks').innerHTML = innerHTML;
}

// 显示模态框并填充内容
function showDetail(stockName) {
    const modal = document.getElementById("modal");
    const modalTitle = document.getElementById("modal-title");
    const modalBody = document.getElementById("modal-body");

    modalBody.innerHTML = `<div class="tooltip-container">
    <p class="tooltip-title">简介详情</p>
    <p class="tooltip-content">${stockName}</p>
</div>`;


    modal.style.display = "block";
}

// 关闭模态框
function closeModal() {
    document.getElementById("modal").style.display = "none";
}

// 点击模态框外部关闭
window.onclick = function (event) {
    const modal = document.getElementById("modal");
    if (event.target == modal) {
        modal.style.display = "none";
    }
}

function formatToYi(marketValue) {
    // 将科学计数法转换为数值
    const value = parseFloat(marketValue);

    // 如果数值小于1亿，直接返回
    if (value < 100000000) {
        return value.toString();
    }

    // 转换为亿为单位
    const yiValue = value / 100000000;

    // 保留两位小数
    return yiValue.toFixed(2) + '亿';
}
