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
    "1.看到不懂的票，**特别是涨了6-9.5%的那种票，如果不是涨停的话不能买**，因为其有可能是庄家诱多出货，要买只能等涨停了，封单慢慢增多的时候再买",
    "2.不要怕卖飞，条件单尽量设置低一点，不要抱有幻想，**强势的票不会一直在均价下面运行的**，**卖掉了就再找强势的股**。",
    "3.**业绩预增的票，如果开盘不是强势，不要买**，先观察竞价的价格，比如4.22号入了妙可蓝多，结果买入直接一直跌，亏6%，这种就是典型的借业绩出货，迫不及待了",
    "4.**如果是搞地天板模式的，只能翘跌停买，比如上涨0.01%买入，千万不要赌地天板，比如跌停开，一直拉到5%，肯定有很多获利盘忍不住砸盘的，只能等涨停封住了再挂单买入**，半路接的话性价比不高的，因为大部分第二天可能跌停开。",
    "5.开**盘跌-4%的拉升先不要急着追，看看后续走势如何，大概率是骗炮的，拉升骗你接盘，如果是强势得话为啥会开-4%，肯定是主力跑路了，懒得维护了**",
    "6.对于那种人气票，如果一直维护高开在5-7%以上，可耐心留到两点50分，最后有可能有大资金封盘的，对标2025.04.25的步步高",
    "7.**缩量涨停的票不能去追，买到了就是大面一碗**",
    "8.**跌停数量贼多的时候不要硬打板，容易大面一碗**",
    "9.翘地板的时候要等上穿开盘点才跟随买进，能避免骗炮的，自己手上有票的，应该先卖而不是先买",
    "10.严守条件单的卖出，对于开板的票，给的阈值大概就是7-7.5%左右，跌破这个区间，无条件卖出",
    "11.**加速过的票鱼尾不要搞，因为风险大于收益**",
    "12.翘跌停只能用少量资金，这种模式不熟悉，往往造成重大亏损",
    "13.跌过超3%的票拉升特别是尾盘不要追，大部分是骗炮",
    "14.打板操作的个股，一定要先分析前面的k线，是不是两三板都吃独食的，比如成交量低于8000万的，这种竞价多强都不要去接，不然吃到天地板就是大面一碗",
    "15.不管多看好的票都要设置一个条件单，跌破接受的价格就无条件清仓",
    "16.翘跌停不要搞那种缩量涨停然后天地版的标的，没人会来接盘，要搞那种前一天有资金进来接盘的票",
    "17.不在交易规划内的票不要盲目追涨，特别是那种高开有点幅度的，然后磨磨蹭蹭拉了一点又跌好多的那种，应该是庄家出货导致的",
    "18.即使多看好的票都要设置条件单，当能跌破那个价格肯定有鬼，应该保留子弹去干最强的票，而不应把希望留在这种弱势票上面。",
    "19.公告大利好，早盘涨停单一直撤的那种票千万不要买，风险大于机会，容易天地板大面。",
    "20.**周四周五是个情绪退潮期的分界线，特别是周四，观察一下短线亏钱效应，如果连人气股都亏钱的话，那么恐慌情绪就会蔓延，手上有的票该考虑止盈了而不是一股脑冲进去**",
    "21.**条件单卖飞的股，看到拉涨，先看看什么情况，不要无脑的追的，特别是是亏1个点卖，然后涨4%-5%的时候再追来，亏的慌**",
    "22.周五一般是一个退潮的点，切忌打板，一般这时候是属于题材切换，潮水退去，应该是干新的概念",
    "23.**当自己的跑路个股都绿了一大片的时候，就知道情绪很差了，这时候先想着先跑路而不是去接盘**",
    "24.早上半路的时候，挑选个股，先看拐头，如果拐头向下的，虽然大单净流入，但是拐头向下的，不要追",
    "25.早上追涨模式，如果是前期高位股，比如五六连板的时候，不要傻乎乎一股脑的跟着追，先看流入资金，**至少出现好几手万手大单**，不然就是诱多出货，比如通达电器，都6板了，首阴之后，第二天自己在涨2%的时候追，结果上套了，直接回落12%，血亏",
    "26.专注自己的模式，之前的高位股早盘涨3%以上别追了，骗炮的居多，抵住诱惑",
    "27.观**察当前的连板池板块，当最高连板低于4板，不要追高板了，基本情绪很差**",
    "28.经常遇到一些早盘出货的票，故意拉高一两个点，装的资金大幅流入的样子，然后一直砸盘，一整天都在卖卖卖的，如何避免被这种票收割呢，**就是看主动买入的，如果没有连续大手进来，而且绿单较多的话，那么宁愿放弃吧，买到骗炮的票，轻轻松松能亏5-7%**",
    "29.对于那些当天涨幅在5%以上的票，如果不好把握卖点的话，也设一个条件单，比如3.5-4.5%就卖，冲不上就是弱势的票",
    "30.早盘低开的票先不要慌，观察一下成交额，**如果低于5000万可以先观察一下，可能只是洗盘，超过1个亿就要小心了，说明出货**",
    "31.早上设置条件单的时候，不要设太近了，容易卖飞，如果是低开的，那可以设置近一点，如果是高于2%的，就设到绿盘比如-0.5%左右，如果真跌破这个价位说明卖压比较重了",
    "32.当一个板块真的很强的时候，即使早盘打板显示不强的话也不要担心，干就对了，因为板块的强导致后排也能吃肉。",
    "33.要翘板不要在高点接手，比如他跌-5%，涨到-1%的时候，千万不要接盘，这时候回落砸的很伤，要不就是红盘再买，如果强的话为啥不能红盘呢",
    "34.周五不打板",
    "35.当你真正意识到所有价格的波动都是随机的，你能控制的只有自己的止损和出入场，你就会**放弃所有对市场的主观推测、分析等等妄想的行为**。**交易的本质是用确定性的风险，来博取不确定性的收益。**失败者总在质疑和主观预测中错失良机，成功者则是用**逆向思维**、**概率思维、风险博弈思维**，**专心在找市场上风险最小的机会利用人性来大胆博弈，赚取情绪的流动性溢价。你要更多考虑的是这笔交易如果失败、它会怎么失败、他会亏损多少，要如何做到少亏，而非想着如何盈利。**",
    "36.尾盘如果要低吸的话千万不要追高，比如跌5-6%的，忽然拉到-3-2%，没有量的不要跟，低吸重在低。能跌这么多，要不就是洗盘。吸的话一定要在均线以下吸。如果没吸到宁愿不买",
    "37.当出现一个大题材前排个股买不到的时候，特别几十个一字板涨停的话，进场最好的方式就是买那种相关etf进行套利，说不定也有20左右的收益，要拿先手",
    "38.历史性的大题材强了两三天的时候，不适合再去冲那些后排的票，往往是套利的，涨了20%再去接容易被套。",
    "39.连续一字板涨停吃独食的票不要接，接到了就是大面一碗，翘板也不要翘这种，没有充分换手，全是获利盘，抛压太大了。",
    "40.大题材杀跌分歧那天如果持票跌多的话比如-5.5-7.5%开的话线不要恐慌，一般都会拉升一下的，早盘这样设置条件单就会很亏。"
];


// 当前编辑状态
let editingIndex = -1;
let rules = [];

// 从接口加载规则
function loadRules() {
    const searchTerm = searchInput ? searchInput.value : '';
    window.encryptionUtil.fetchDecrypted(`/getStockRuleList?keyword=${encodeURIComponent(searchTerm)}`, { method: 'GET' })
        .then(function (result) {
            window.stockRuleData = result;
            rules = result.map((rule, index) => {
                const content = rule.content;
                if (content.match(/^\d+\.\s*/)) {
                    return content;
                } else {
                    return `${index + 1}. ${content}`;
                }
            });
            if (rules.length === 0) {
                rules = [...initialRules];
            }
            renderRules();
        })
        .catch(function (error) {
            console.log('加载规则失败:', error);
            rules = [...initialRules];
            renderRules();
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
        // sortOrder: rules.length + 1
    };

    window.encryptionUtil.fetchDecrypted(`/addOrEditStockRuleData`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(stockRule)
    }).then(function (result) {
        if (result) {
            loadRules();
            resetForm();
        } else {
            alert('添加规则失败');
        }
    }).catch(function (error) {
        console.log('添加规则失败:', error);
        alert('添加规则失败');
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
        // sortOrder: parseInt(ruleNumber)
    };
    
    // 如果有原始数据且索引有效，则添加ID
    if (window.stockRuleData && window.stockRuleData[index]) {
        stockRule.id = window.stockRuleData[index].id;
    }

    window.encryptionUtil.fetchDecrypted(`/addOrEditStockRuleData`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(stockRule)
    }).then(function (result) {
        if (result) {
            loadRules();
            resetForm();
        } else {
            alert('更新规则失败');
        }
    }).catch(function (error) {
        console.log('更新规则失败:', error);
        alert('更新规则失败');
    });
}

// 删除规则
function deleteRule(index) {
    if (index < 0 || index >= rules.length) return;
    
    if (confirm('确定要删除这条规则吗？')) {
        // 优先使用ID删除，如果没有ID则使用内容删除
        let deleteUrl = "/deleteStockRule";
        let deleteData = {};
        
        if (window.stockRuleData && window.stockRuleData[index] && window.stockRuleData[index].id) {
            deleteData.id = parseInt(window.stockRuleData[index].id);
        } else {
            deleteUrl = "/deleteStockRuleByContent";
            deleteData.content = rules[index].replace(/^\d+\.\s*/, '');
        }
        
        const query = Object.keys(deleteData).map(k => `${encodeURIComponent(k)}=${encodeURIComponent(deleteData[k])}`).join('&');
        window.encryptionUtil.fetchDecrypted(`${deleteUrl}${query ? ('?' + query) : ''}`, { method: 'GET' })
            .then(function (result) {
                if (result) {
                    loadRules();
                    resetForm();
                } else {
                    alert('删除规则失败');
                }
            })
            .catch(function (error) {
                console.log('删除规则失败:', error);
                alert('删除规则失败');
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
// 页面加载时初始化
// window.addEventListener('DOMContentLoaded', () => {
//     loadRules();
//     ruleContent.focus();
// });








