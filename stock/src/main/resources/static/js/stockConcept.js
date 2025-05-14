// 全局变量
// let concepts = JSON.parse(localStorage.getItem('stockConcepts')) || [];
let concepts=null;
let currentPage = 1;
const pageSize = 10;
let currentEditId = null;
let isEditMode = false;
$(function () {
    // 初始化页面
    getStockConceptList();
    setupSearch();
})

function getStockConceptList() {
    const searchText = document.getElementById('searchInput').value.toLowerCase();
    $.ajax({

        type: "get",

        url: "getStockConceptList",

        data: {

            keyword: searchText,
            current: currentPage,
            size: pageSize

        },

        success: function (data) {
            concepts=data.records;
            console.log(data)
            renderTable(data);
        }

    });
}


// 渲染表格数据
function renderTable(data) {
    const start = (currentPage - 1) * pageSize;
    const end = start + pageSize;
    const tbody = document.getElementById('tableBody');
    console.log(data)
    tbody.innerHTML = data.records.map(item => `
                <tr>
                    <td>${item.id}</td>
                    <td>${item.name}</td>
                    <td>${item.description || '-'}</td>
                    <td>
                        <button class="action-btn edit-btn" onclick="editConcept('${item.id}')">编辑</button>
                        <button class="action-btn delete-btn" onclick="deleteConcept('${item.id}')">删除</button>
                    </td>
                </tr>
            `).join('');
    renderPagination(data.total);
}

// 渲染分页
function renderPagination(total) {
    const pageCount = Math.ceil(total / pageSize);
    const pagination = document.getElementById('pagination');

    let html = '';

    // 上一页按钮
    if (currentPage > 1) {
        html += `<div class="page-item" onclick="changePage(${currentPage - 1})">上一页</div>`;
    }

    // 页码按钮
    const maxVisiblePages = 5;
    let startPage = Math.max(1, currentPage - Math.floor(maxVisiblePages / 2));
    let endPage = Math.min(pageCount, startPage + maxVisiblePages - 1);

    if (endPage - startPage + 1 < maxVisiblePages) {
        startPage = Math.max(1, endPage - maxVisiblePages + 1);
    }

    if (startPage > 1) {
        html += `<div class="page-item" onclick="changePage(1)">1</div>`;
        if (startPage > 2) {
            html += `<div class="page-item disabled">...</div>`;
        }
    }

    for (let i = startPage; i <= endPage; i++) {
        html += `<div class="page-item ${i === currentPage ? 'active' : ''}" onclick="changePage(${i})">${i}</div>`;
    }

    if (endPage < pageCount) {
        if (endPage < pageCount - 1) {
            html += `<div class="page-item disabled">...</div>`;
        }
        html += `<div class="page-item" onclick="changePage(${pageCount})">${pageCount}</div>`;
    }

    // 下一页按钮
    if (currentPage < pageCount) {
        html += `<div class="page-item" onclick="changePage(${currentPage + 1})">下一页</div>`;
    }

    pagination.innerHTML = html;
}

// 切换页码
function changePage(page) {
    currentPage = page;
    getStockConceptList();
}

// 设置搜索功能
function setupSearch() {
    const searchInput = document.getElementById('searchInput');
    let searchTimer;

    searchInput.addEventListener('input', function () {
        clearTimeout(searchTimer);
        searchTimer = setTimeout(() => {
            currentPage = 1;
            renderTable();
        }, 300);
    });
}

// 打开添加模态框
function openAddModal() {
    isEditMode = false;
    currentEditId = null;
    console.log(document.getElementById('modalTitle'))
    document.getElementById('modalTitle').textContent = '添加概念';
    document.getElementById('conceptName').value = '';
    document.getElementById('conceptDesc').value = '';
    document.getElementById('conceptModal').style.display = 'flex';
}
function sysTonghuaShunConcept() {
    $.ajax({
        type: "get",
        url: "sysTonghuaShunConcept",
        data: {
        },
        success: function (data) {
        }

    });
}
function sysnConceptData() {
    $.ajax({
        type: "get",
        url: "sysnConceptData",
        data: {
        },
        success: function (data) {
        }

    });
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

// 关闭模态框
function closeModal() {
    document.getElementById('conceptModal').style.display = 'none';
}

function saveConceptData() {
    const name = document.getElementById('conceptName').value.trim();
    const desc = document.getElementById('conceptDesc').value.trim();
    console.log("currentEditId"+currentEditId)
    if (!name) {
        alert('概念名称不能为空');
        return;
    }
    $.ajax({
        contentType: 'application/json;charset=UTF-8',
        type: "POST",
        dataType: "json",
        url: "addOrEditStockConceptData",
        data: JSON.stringify({
            "id":currentEditId,
            "name": name,
            "description": desc,
        }),
        success: function (data) {
            refreshConcept();
        }
    });
}

// 保存概念
function refreshConcept() {
    const name = document.getElementById('conceptName').value.trim();
    const desc = document.getElementById('conceptDesc').value.trim();

    if (isEditMode) {
        // 编辑模式
        concepts = concepts.map(item => {
            if (item.id === currentEditId) {
                return {...item, name, desc};
            }
            return item;
        });
    } else {
        // 添加模式
        const newConcept = {
            id: Date.now(),
            name,
            desc
        };
        concepts.unshift(newConcept);
    }

    // localStorage.setItem('stockConcepts', JSON.stringify(concepts));

    closeModal();
    getStockConceptList();
}

// 删除概念
function deleteConcept(id) {
    console.log(id)
    if (confirm('确定要删除这个概念吗？')) {
        $.ajax({

            type: "get",

            url: "deleteStockConcept",

            data: {

                id: id,

            },

            success: function (data) {

                getStockConceptList();
            }

        });
        // localStorage.setItem('stockConcepts', JSON.stringify(concepts));

        // 如果当前页没有数据了，且不是第一页，则返回上一页
        const start = (currentPage - 1) * pageSize;
        if (start >= concepts.length && currentPage > 1) {
            currentPage--;
        }

        // renderTable();
    }
}
