// 模拟数据存储
let stockData = [];
const itemsPerPage = 10;
let currentPage = 1;

// 板块/概念数据（示例）
const boardOptions = ['科技', '金融', '医疗', '消费', '新能源', '半导体', '人工智能', '生物医药', '新材料', '军工', '环保', '房地产', '传媒', '教育', '旅游', '物流', '农业', '汽车', '电力', '通信'];
const conceptOptions = ['AI', '5G', '区块链', '元宇宙', '碳中和', '新能源车', '光伏', '芯片', '大数据', '云计算', '物联网', '机器人', '智能驾驶', 'VR/AR', '数字经济', '生物医药', '消费升级', '乡村振兴', '绿色能源', '量子计算'];

$(function () {
    // document.getElementById('mainData').innerHTML = '';
    // getYidongData();
    // document.getElementById('newStockBtn').addEventListener('click', showModal);
    // window.addEventListener('load', init);
    initMultiSelect();
    updateTable();
// 关闭多选组件
    document.addEventListener('click', function (e) {
        if (!e.target.closest('.search-select')) {
            document.querySelectorAll('.options').forEach(el => {
                el.style.display = 'none';
            });
        }
    });

// 表单验证
    document.getElementById('stockForm').addEventListener('submit', function (e) {
        e.preventDefault();

        // 验证金额格式
        const amount = document.getElementById('amount').value;
        if (amount && !/^\d+(\.\d{1,2})?$/.test(amount)) {
            alert('请输入有效的金额（最多两位小数）');
            return;
        }

        // 收集数据
        const stock = {
            code: document.getElementById('stockCode').value,
            name: document.getElementById('stockName').value,
            business: document.getElementById('business').value,
            board: getSelectedValues('board'),
            concepts: getSelectedValues('concept'),
            amount: amount,
            region: document.getElementById('region').value
        };

        // 添加数据
        // stockData.push(stock);
        saveMarketData(stock);
        closeModal();
        updateTable();
        this.reset();
    });





    // 页面加载初始化
    document.addEventListener('DOMContentLoaded', () => {
        initMultiSelect();
        // 可选：加载示例数据
        // stockData.push(...);
        updateTable();
    });

})

function saveMarketData(stock) {
    console.log(stock)
    window.encryptionUtil.fetchDecrypted("addOrEditStockMarketData", {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
            code: stock.code,
            name: stock.name,
            description: stock.business,
            mainBusiness: stock.business,
            location: stock.region,
            profitLoss: stock.business,
            volume: stock.amount,
        })
    }).then(function (data) {
        // refreshConcept();
    }).catch(function (error) {
        console.log('saveMarketData 请求失败:', error);
    });
}
function getMarketDataList() {
    console.log(stock)
    window.encryptionUtil.fetchDecrypted("getMarketDataList", {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
            code: stock.code,
            name: stock.name,
            description: stock.business,
            mainBusiness: stock.business,
            location: stock.region,
            profitLoss: stock.business,
            volume: stock.amount,
        })
    }).then(function (data) {
        stockData=data;
    }).catch(function (error) {
        console.log('getMarketDataList 请求失败:', error);
    });
}
// 删除概念
function deleteConcept(id) {
    console.log(id)
    if (confirm('确定要删除这个概念吗？')) {
        window.encryptionUtil.fetchDecrypted(`deleteStockConcept?id=${encodeURIComponent(id)}`, { method: 'GET' })
            .then(function (data) {
                getStockConceptList();
            })
            .catch(function (error) {
                console.log('deleteStockConcept 请求失败:', error);
            });
        // localStorage.setItem('stockConcepts', JSON.stringify(concepts));

        // 如果当前页没有数据了，且不是第一页，则返回上一页
        const start = (currentPage - 1) * pageSize;
        if (start >= concepts.length && currentPage > 1) {
            currentPage--;
        }

        // renderTable();
    }
    // 打开编辑模态框
    function editConcept(id) {
        console.log(id)
        isEditMode = true;
        currentEditId = id;
        const concept = concepts.find(item => item.id === id);
        console.log(concept)

        document.getElementById('modalTitle').textContent = '编辑概念';
        document.getElementById('conceptName').value = concept.name;
        document.getElementById('conceptDesc').value = concept.description || '';
        document.getElementById('conceptModal').style.display = 'flex';
    }

function downLoadData() {
    window.encryptionUtil.fetchDecrypted(`downloadStrongStockData?dateIndex=${encodeURIComponent(dateIndex)}`, { method: 'GET' })
        .then(function (data) {
            // Success callback
        })
        .catch(function (error) {
            console.log('downloadStrongStockData 请求失败:', error);
        });
}

// 初始化多选组件
function initMultiSelect() {
    const boardContainer = document.getElementById('boardOptions');
    const conceptContainer = document.getElementById('conceptOptions');
    boardOptions.forEach(option => {
        const div = document.createElement('div');
        div.className = 'option-item';
        div.innerHTML = `
                    <label>
                        <input type="checkbox" value="${option}">
                        ${option}
                    </label>
                `;
        boardContainer.appendChild(div);
    });

    conceptOptions.forEach(option => {
        const div = document.createElement('div');
        div.className = 'option-item';
        div.innerHTML = `
                    <label>
                        <input type="checkbox" value="${option}">
                        ${option}
                    </label>
                `;
        conceptContainer.appendChild(div);
    });
}

// 多选组件控制
function toggleOptions(type) {
    document.querySelectorAll('.options').forEach(el => {
        if (el.id !== type + 'Options') el.style.display = 'none';
    });
    document.getElementById(type + 'Options').style.display =
        document.getElementById(type + 'Options').style.display === 'block' ? 'none' : 'block';
}

function filterOptions(type, query) {
    const container = document.getElementById(type + 'Options');
    const items = container.getElementsByClassName('option-item');

    Array.from(items).forEach(item => {
        const text = item.textContent.toLowerCase();
        item.style.display = text.includes(query.toLowerCase()) ? 'block' : 'none';
    });
}


function getSelectedValues(type) {
    const container = document.getElementById(type + 'Options');
    console.log(document.getElementById(type + 'Options'))
    const checked = container.querySelectorAll('input[type="checkbox"]:checked');
    return Array.from(checked).map(cb => cb.value);
}

// 更新表格
function updateTable() {
    const tbody = document.querySelector('#dataTable tbody');
    tbody.innerHTML = '';

    const start = (currentPage - 1) * itemsPerPage;
    const end = start + itemsPerPage;
    const pageData = stockData.slice(start, end);

    pageData.forEach((stock, index) => {
        const row = document.createElement('tr');
        row.innerHTML = `
                    <td>${stock.code}</td>
                    <td>${stock.name}</td>
                    <td title="${stock.business}">${stock.business.substring(0, 30)}${stock.business.length > 30 ? '...' : ''}</td>
                    <td>${stock.board.join(', ')}</td>
                    <td>${stock.concepts.join(', ')}</td>
                    <td>${stock.amount}</td>
                    <td>${stock.region}</td>
<!--                    <td><span class="delete-btn" onclick="deleteItem(${start + index})">🗑️</span></td>-->
                    <td>
                        <div class="action-buttons">
                            <span class="btn-action edit-btn" onclick="editItem(${start + index})">✏️ 编辑</span>
                            <span class="btn-action delete-btn" onclick="deleteItem(${start + index})">🗑️ 删除</span>
                        </div>
                    </td>
                `;
        tbody.appendChild(row);
    });

    renderPagination();
}

function deleteItem(index) {
    if (confirm('确定要删除这条记录吗？')) {
        stockData.splice(index, 1);
        updateTable();
    }
}

// 分页渲染
function renderPagination() {
    const pagination = document.getElementById('pagination');
    pagination.innerHTML = '';
    const totalPages = Math.ceil(stockData.length / itemsPerPage);

    // 上一页
    const prev = document.createElement('div');
    prev.className = 'page-item' + (currentPage === 1 ? ' disabled' : '');
    prev.innerHTML = '«';
    prev.onclick = () => {
        if (currentPage > 1) {
            currentPage--;
            updateTable();
        }
    };
    pagination.appendChild(prev);

    // 页码
    for (let i = 1; i <= totalPages; i++) {
        const btn = document.createElement('div');
        btn.className = 'page-item' + (currentPage === i ? ' active' : '');
        btn.textContent = i;
        btn.onclick = () => {
            currentPage = i;
            updateTable();
        };
        pagination.appendChild(btn);
    }

    // 下一页
    const next = document.createElement('div');
    next.className = 'page-item' + (currentPage === totalPages || totalPages === 0 ? ' disabled' : '');
    next.innerHTML = '»';
    next.onclick = () => {
        if (currentPage < totalPages) {
            currentPage++;
            updateTable();
        }
    };
    pagination.appendChild(next);
}

// 模态框控制
function openModal() {
    document.getElementById('stockModal').style.display = 'block';
}

function closeModal() {
    document.getElementById('stockModal').style.display = 'none';
    document.getElementById('stockForm').reset();
    document.querySelectorAll('.options').forEach(el => el.style.display = 'none');
}

