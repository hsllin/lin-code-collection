let data = [];
let stockData = [];
let boardData = [];
let boardId = '';
let tagType = 'stock';
let days = '5';
let sort = '1';
// DOM元素
let rulesList;
let ruleForm;
let ruleContent;
let searchInput;
let searchBtn;
$(function () {
    // document.querySelector('#stockBody').innerHTML = '';
    // document.querySelector('#boardBody').innerHTML = '';
    rulesList = document.getElementById('rulesList');
    ruleForm = document.getElementById('ruleForm');
    ruleContent = document.getElementById('ruleContent');
    searchInput = document.getElementById('searchInput');
    searchBtn = document.getElementById('searchBtn');
    ruleContent = document.getElementById('ruleContent');

    searchInput.addEventListener('input', handleSearch);
    searchBtn.addEventListener('click', handleSearch);
    loadRules();
    ruleContent.focus();
    // 处理表单提交
    ruleForm.addEventListener('submit', (e) => {
        e.preventDefault();
        const content = ruleContent.value.trim();

        if (!content) return;

        if (editingIndex === -1) {
            addRule(content);
        } else {
            updateRule(editingIndex, content);
        }
    });
})

// 初始祖训数据
const initialRules = [
];


// 当前编辑状态
let editingIndex = -1;
let rules = [];

// 从接口加载规则
function loadRules() {
    const searchTerm = searchInput ? searchInput.value : '';
    
    $.ajax({
        type: "GET",
        url: "/getTradeRuleList",
        data: {
            keyword: searchTerm,
        },
        success: function(result) {
            // 保存原始数据，包含ID信息
            window.tradeRuleData = result;
            
            // 将接口返回的StockRule对象数组转换为规则数组
            rules = result.map((rule, index) => {
                // 如果content已经包含编号，直接返回；否则添加编号
                const content = rule.content;
                if (content.match(/^\d+\.\s*/)) {
                    return content;
                } else {
                    return `${index + 1}. ${content}`;
                }
            });
            
            // 如果数据库中没有数据，使用初始规则
            if (rules.length === 0) {
                rules = [...initialRules];
            }
            
            renderRules();
        },
        error: function(xhr, status, error) {
            console.error('加载规则失败:', error);
            // 如果接口调用失败，使用初始规则
            rules = [...initialRules];
            renderRules();
        }
    });
}

// 保存规则到接口
function saveRules() {
    // 这个方法现在不需要了，因为每个操作都会直接调用接口
}

// 渲染规则列表
function renderRules() {
    const searchTerm = searchInput.value.toLowerCase();

    rulesList.innerHTML = '';

    if (rules.length === 0) {
        rulesList.innerHTML = `<div class="tip">暂无交易规则，请添加第一条规则</div>`;
        return;
    }

    rules.forEach((rule, index) => {
        // 筛选规则
        if (searchTerm && !rule.toLowerCase().includes(searchTerm)) {
            return;
        }

        // 提取规则编号
        const ruleNumber = rule.match(/^\d+/)?.[0] || (index + 1);

        // 格式化内容
        const formattedContent = rule
            .replace(/^\d+\.\s*/, '')
            .replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>');

        const ruleElement = document.createElement('div');
        ruleElement.className = 'rule-item';
        ruleElement.innerHTML = `
                    <div class="rule-header">
                        <div class="rule-number">${ruleNumber}</div>
                        <div class="rule-actions">
                            <button class="btn btn-edit" data-index="${index}">编辑</button>
                            <button class="btn btn-delete" data-index="${index}">删除</button>
                        </div>
                    </div>
                    <div class="rule-content">${formattedContent}</div>
                `;

        rulesList.appendChild(ruleElement);
    });

    // 添加事件监听
    document.querySelectorAll('.btn-edit').forEach(btn => {
        btn.addEventListener('click', (e) => {
            const index = parseInt(e.target.getAttribute('data-index'));
            editRule(index);
        });
    });

    document.querySelectorAll('.btn-delete').forEach(btn => {
        btn.addEventListener('click', (e) => {
            const index = parseInt(e.target.getAttribute('data-index'));
            deleteRule(index);
        });
    });
}

// 添加新规则
function addRule(content) {
    if (!content.trim()) return;

    const stockRule = {
        content: content,
        sortOrder: rules.length + 1
    };

    $.ajax({
        type: "POST",
        url: "/addOrEditTradeRuleData",
        contentType: "application/json",
        data: JSON.stringify(stockRule),
        success: function(result) {
            if (result) {
                loadRules(); // 重新加载规则列表
                resetForm();
            } else {
                alert('添加规则失败');
            }
        },
        error: function(xhr, status, error) {
            console.error('添加规则失败:', error);
            alert('添加规则失败');
        }
    });
}

// 编辑规则
function editRule(index) {
    if (index < 0 || index >= rules.length) return;

    // 准备表单编辑状态
    editingIndex = index;

    // 移除已有编号
    const rule = rules[index].replace(/^\d+\.\s*/, '');
    ruleContent.value = rule;

    // 滚动到表单
    ruleContent.focus();
}

// 更新规则
function updateRule(index, content) {
    if (index < 0 || index >= rules.length || !content.trim()) return;

    // 获取当前规则的数据
    const currentRule = rules[index];
    const ruleNumber = currentRule.match(/^\d+/)?.[0] || (index + 1);
    
    // 从保存的原始数据中获取ID
    let stockRule = {
        content: content,
        sortOrder: parseInt(ruleNumber)
    };
    
    // 如果有原始数据且索引有效，则添加ID
    if (window.tradeRuleData && window.tradeRuleData[index]) {
        stockRule.id = window.tradeRuleData[index].id;
    }

    $.ajax({
        type: "POST",
        url: "/addOrEditTradeRuleData",
        contentType: "application/json",
        data: JSON.stringify(stockRule),
        success: function(result) {
            if (result) {
                loadRules(); // 重新加载规则列表
                resetForm();
            } else {
                alert('更新规则失败');
            }
        },
        error: function(xhr, status, error) {
            console.error('更新规则失败:', error);
            alert('更新规则失败');
        }
    });
}

// 删除规则
function deleteRule(index) {
    if (index < 0 || index >= rules.length) return;
    
    if (confirm('确定要删除这条规则吗？')) {
        // 优先使用ID删除，如果没有ID则使用内容删除
        let deleteUrl = "/deleteTradeRule";
        let deleteData = {};
        
        if (window.tradeRuleData && window.tradeRuleData[index] && window.tradeRuleData[index].id) {
            deleteData.id = parseInt(window.tradeRuleData[index].id);
        } else {
            deleteUrl = "/deleteTradeRuleByContent";
            deleteData.content = rules[index].replace(/^\d+\.\s*/, '');
        }
        
        $.ajax({
            type: "GET",
            url: deleteUrl,
            data: deleteData,
            success: function(result) {
                if (result) {
                    loadRules(); // 重新加载规则列表
                    resetForm();
                } else {
                    alert('删除规则失败');
                }
            },
            error: function(xhr, status, error) {
                console.error('删除规则失败:', error);
                alert('删除规则失败');
            }
        });
    }
}

// 重置表单
function resetForm() {
    editingIndex = -1;
    ruleContent.value = '';
    ruleContent.focus();
}



// 搜索功能
function handleSearch() {
    loadRules(); // 重新从接口加载数据，包含搜索条件
}





