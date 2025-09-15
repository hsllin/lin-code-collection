document.getElementById('refreshBtn').addEventListener('click', function() {
    // 禁用按钮并添加禁用样式
    this.disabled = true;
    const originalContent = this.innerHTML;
    // 添加刷新动画
    this.innerHTML = '<i class="fas fa-spinner fa-spin"></i> 刷新中...';
    this.classList.add('btn--disabled');
    refreshData();

    // 2秒后重新启用按钮
    setTimeout(() => {
        this.disabled = false;
        this.classList.remove('btn--disabled');
        this.innerHTML=originalContent
    }, 2000);
});

const weekMap = ['星期日','星期一','星期二','星期三','星期四','星期五','星期六'];
function tick(){
    const now = new Date(new Date().toLocaleString('zh-CN',{timeZone:'Asia/Shanghai'}));

    /* 时间 */
    const h = String(now.getHours()).padStart(2,'0');
    const m = String(now.getMinutes()).padStart(2,'0');
    const s = String(now.getSeconds()).padStart(2,'0');
    document.getElementById('time').textContent = `${h}:${m}:${s}`;

    /* 日期 & 星期 */
    const MM = String(now.getMonth()+1).padStart(2,'0');
    const DD = String(now.getDate()).padStart(2,'0');
    document.getElementById('date').textContent = `${now.getFullYear()}年${MM}月${DD}日`;
    document.getElementById('week').textContent = weekMap[now.getDay()];
}

// 星期中文名称
const weekdays = ["星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"];

// function updateClock() {
//     const now = new Date();
//
//     // 获取中国时区时间 (UTC+8)
//     const utc = now.getTime() + (now.getTimezoneOffset() * 60000);
//     const chinaTime = new Date(utc + (3600000 * 8));
//
//     // 格式化时间
//     const hours = chinaTime.getHours().toString().padStart(2, '0');
//     const minutes = chinaTime.getMinutes().toString().padStart(2, '0');
//     const seconds = chinaTime.getSeconds().toString().padStart(2, '0');
//
//     // 格式化日期
//     const year = chinaTime.getFullYear();
//     const month = chinaTime.getMonth() + 1;
//     const day = chinaTime.getDate();
//
//     // 获取星期
//     const weekday = weekdays[chinaTime.getDay()];
//
//     // 更新页面元素
//     document.getElementById('time').textContent = `${hours}:${minutes}:${seconds}`;
//     document.getElementById('date').textContent = `${year}年${month}月${day}日`;
//     document.getElementById('weekday').textContent = weekday;
// }

// 更新时钟函数
function updateClock() {
    const now = new Date();

    // 格式化时间
    const hours = String(now.getHours()).padStart(2, '0');
    const minutes = String(now.getMinutes()).padStart(2, '0');
    const seconds = String(now.getSeconds()).padStart(2, '0');
    document.getElementById('time').textContent = `${hours}:${minutes}:${seconds}`;

    // 格式化日期
    const year = now.getFullYear();
    const month = now.getMonth() + 1;
    const day = now.getDate();
    const weekdays = ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六'];
    const weekday = weekdays[now.getDay()];
    document.getElementById('date').textContent = `${year}年${month}月${day}日 ${weekday}`;
}

/**
 * 格式化数字：超过1亿显示亿单位，不足1亿但超过1万显示万单位，不足1万显示原数字
 * @param {number|string} num - 待格式化的数字或数字字符串
 * @returns {string} 格式化后的字符串（自动去除小数点后无效的零）
 */
function formatToYi(num) {
    // 转换为数字类型并验证有效性
    const number = parseFloat(num);
    if (isNaN(number)) return '0';

    const absNum = Math.abs(number);

    // 超过1亿：转换为亿单位
    if (absNum >= 100000000) {
        const result = (number / 100000000).toFixed(2).replace(/\.?0+$/, '');
        return result + '亿';
    }
    // 超过1万但不足1亿：转换为万单位
    else if (absNum >= 10000) {
        const result = (number / 10000).toFixed(2).replace(/\.?0+$/, '');
        return result + '万';
    }
    // 不足1万：直接返回整数
    return Math.round(number).toString();
}
$(function(){
    updateClock();
    setInterval(updateClock,1000);
})