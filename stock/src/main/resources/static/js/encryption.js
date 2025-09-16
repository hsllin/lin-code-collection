/**
 * 前端解密工具类
 * 使用AES-GCM算法解密后端返回的加密数据
 */
class EncryptionUtil {
    constructor() {
        this.key = null;
        this.algorithm = 'AES-GCM';
    }

    /**
     * 从服务器获取解密密钥
     */
    async loadKey() {
        try {
            const response = await fetch('/api/key/get');
            const keyInfo = await response.json();
            this.key = keyInfo.key;
            return true;
        } catch (error) {
            console.error('获取密钥失败:', error);
            return false;
        }
    }

    /**
     * 解密数据
     * @param {string} encryptedData - Base64编码的加密数据
     * @returns {Promise<any>} 解密后的数据
     */
    async decrypt(encryptedData) {
        if (!this.key) {
            await this.loadKey();
        }

        try {
            // 解码Base64数据
            const encryptedBuffer = this.base64ToArrayBuffer(encryptedData);
            
            // 分离IV和加密数据
            const iv = encryptedBuffer.slice(0, 12);
            const encrypted = encryptedBuffer.slice(12);
            
            // 导入密钥
            const keyBuffer = this.stringToArrayBuffer(this.key);
            const cryptoKey = await crypto.subtle.importKey(
                'raw',
                keyBuffer,
                { name: 'AES-GCM' },
                false,
                ['decrypt']
            );
            
            // 解密数据
            const decryptedBuffer = await crypto.subtle.decrypt(
                {
                    name: 'AES-GCM',
                    iv: iv,
                    tagLength: 128
                },
                cryptoKey,
                encrypted
            );
            
            // 转换为字符串
            const decryptedString = new TextDecoder().decode(decryptedBuffer);
            return JSON.parse(decryptedString);
        } catch (error) {
            console.error('解密失败:', error);
            throw error;
        }
    }

    /**
     * 解密API响应
     * @param {Object} response - API响应对象
     * @returns {Promise<any>} 解密后的数据
     */
    async decryptResponse(response) {
        if (response.encrypted) {
            if (response.data) {
                // 整个响应体被加密
                return await this.decrypt(response.data);
            } else if (response.encryptedFields) {
                // 部分字段被加密
                const result = { ...response.plainFields };
                for (const [field, encryptedValue] of Object.entries(response.encryptedFields)) {
                    result[field] = await this.decrypt(encryptedValue);
                }
                return result;
            }
        }
        return response;
    }

    /**
     * 处理加密的API调用
     * @param {string} url - API地址
     * @param {Object} options - fetch选项
     * @returns {Promise<any>} 解密后的响应数据
     */
    async fetchDecrypted(url, options = {}) {
        try {
            const response = await fetch(url, {
                ...options,
                headers: {
                    'Content-Type': 'application/json',
                    ...options.headers
                }
            });
            
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            
            const data = await response.json();
            return await this.decryptResponse(data);
        } catch (error) {
            console.error('API调用失败:', error);
            throw error;
        }
    }

    /**
     * Base64字符串转ArrayBuffer
     */
    base64ToArrayBuffer(base64) {
        const binaryString = atob(base64);
        const bytes = new Uint8Array(binaryString.length);
        for (let i = 0; i < binaryString.length; i++) {
            bytes[i] = binaryString.charCodeAt(i);
        }
        return bytes.buffer;
    }

    /**
     * 字符串转ArrayBuffer
     */
    stringToArrayBuffer(str) {
        const bytes = new Uint8Array(str.length);
        for (let i = 0; i < str.length; i++) {
            bytes[i] = str.charCodeAt(i);
        }
        return bytes.buffer;
    }
}

// 创建全局实例
window.encryptionUtil = new EncryptionUtil();

// 扩展fetch方法，自动处理加密响应
const originalFetch = window.fetch;
window.fetch = async function(url, options = {}) {
    const response = await originalFetch(url, options);
    
    // 检查是否是加密响应
    if (response.headers.get('content-type')?.includes('application/json')) {
        const data = await response.json();
        if (data.encrypted) {
            // 返回解密后的数据
            const decryptedData = await window.encryptionUtil.decryptResponse(data);
            return {
                ...response,
                json: () => Promise.resolve(decryptedData)
            };
        }
    }
    
    return response;
};
