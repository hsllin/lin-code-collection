/**
 * API客户端封装
 * 自动处理加密数据的请求和响应
 */
class ApiClient {
    constructor() {
        this.baseURL = '';
        this.encryptionUtil = window.encryptionUtil;
    }

    /**
     * GET请求
     * @param {string} url - 请求地址
     * @param {Object} params - 查询参数
     * @returns {Promise<any>} 响应数据
     */
    async get(url, params = {}) {
        const queryString = new URLSearchParams(params).toString();
        const fullUrl = queryString ? `${url}?${queryString}` : url;
        
        return await this.encryptionUtil.fetchDecrypted(fullUrl, {
            method: 'GET'
        });
    }

    /**
     * POST请求
     * @param {string} url - 请求地址
     * @param {Object} data - 请求数据
     * @returns {Promise<any>} 响应数据
     */
    async post(url, data = {}) {
        return await this.encryptionUtil.fetchDecrypted(url, {
            method: 'POST',
            body: JSON.stringify(data)
        });
    }

    /**
     * PUT请求
     * @param {string} url - 请求地址
     * @param {Object} data - 请求数据
     * @returns {Promise<any>} 响应数据
     */
    async put(url, data = {}) {
        return await this.encryptionUtil.fetchDecrypted(url, {
            method: 'PUT',
            body: JSON.stringify(data)
        });
    }

    /**
     * DELETE请求
     * @param {string} url - 请求地址
     * @returns {Promise<any>} 响应数据
     */
    async delete(url) {
        return await this.encryptionUtil.fetchDecrypted(url, {
            method: 'DELETE'
        });
    }

    /**
     * 股票相关API
     */
    stock = {
        /**
         * 获取涨幅数据
         */
        getIncreaseData: () => this.get('/getIncreaseData'),
        
        /**
         * 获取跌幅数据
         */
        getDecreaseData: () => this.get('/getDecreaseData'),
        
        /**
         * 获取一字涨停数据
         */
        getOneWordData: () => this.get('/getOneWordData'),
        
        /**
         * 获取T字涨停数据
         */
        getTLimitupData: () => this.get('/getTLimitupData'),
        
        /**
         * 获取时间股票列表
         */
        getTimeStockList: (current = 1, size = 10) => 
            this.get('/getTimeStockList', { current, size }),
        
        /**
         * 获取时间股票线
         */
        getTimeStockLine: (current = 1, size = 10) => 
            this.get('/getTimeStockLine', { current, size }),
        
        /**
         * 获取日内变化数据
         */
        getIntraDayChange: (type, checkedData) => 
            this.get('/intraDayChange', { type, checkedData })
    };
}

// 创建全局API客户端实例
window.apiClient = new ApiClient();

// 使用示例
/*
// 获取涨幅数据
apiClient.stock.getIncreaseData().then(data => {
    console.log('涨幅数据:', data);
}).catch(error => {
    console.error('获取数据失败:', error);
});

// 获取时间股票列表
apiClient.stock.getTimeStockList(1, 20).then(data => {
    console.log('股票列表:', data);
});
*/
