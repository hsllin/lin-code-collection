/**
 * 前端解密工具类
 * 使用AES-GCM算法解密后端返回的加密数据
 */
class EncryptionUtil {
    constructor() {
        this.key = 'MySecretKey123456789012345678901';
        this.algorithm = 'AES-GCM';
    }

    /**
     * 从服务器获取解密密钥
     */
    async loadKey() {
        try {
            const response = await fetch('/api/key/get');
            const keyInfo = await response.json();
            console.log(response)
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
     * 加密数据（与后端AES-GCM兼容，IV前置）
     * @param {any} data 原始数据对象或字符串
     * @returns {Promise<string>} Base64(IV||ciphertext)
     */
    async encrypt(data) {
        if (!this.key) {
            await this.loadKey();
        }
        try {
            const plainString = typeof data === 'string' ? data : JSON.stringify(data);
            const keyBuffer = this.stringToArrayBuffer(this.key);
            const cryptoKey = await crypto.subtle.importKey(
                'raw',
                keyBuffer,
                { name: 'AES-GCM' },
                false,
                ['encrypt']
            );

            // 生成随机IV(12字节)
            const iv = crypto.getRandomValues(new Uint8Array(12));
            const encoded = new TextEncoder().encode(plainString);
            const encryptedBuffer = await crypto.subtle.encrypt(
                { name: 'AES-GCM', iv: iv, tagLength: 128 },
                cryptoKey,
                encoded
            );

            // 拼接 IV + 密文
            const encryptedBytes = new Uint8Array(encryptedBuffer);
            const output = new Uint8Array(iv.length + encryptedBytes.length);
            output.set(iv, 0);
            output.set(encryptedBytes, iv.length);

            // 转 base64
            let binary = '';
            for (let i = 0; i < output.byteLength; i++) {
                binary += String.fromCharCode(output[i]);
            }
            return btoa(binary);
        } catch (error) {
            console.error('加密失败:', error);
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
        console.log(url)
        try {
            const finalOptions = { ...options, headers: { ...options.headers } };

            // 对非GET请求的body进行加密封装
            const method = (finalOptions.method || 'GET').toUpperCase();
            if (method !== 'GET' && finalOptions.body != null) {
                // 将原始body（对象或字符串）加密
                const rawBody = typeof finalOptions.body === 'string' ? finalOptions.body : JSON.stringify(finalOptions.body);
                const encryptedPayload = await this.encrypt(rawBody);
                finalOptions.body = JSON.stringify({ encrypted: true, data: encryptedPayload });
                finalOptions.headers['Content-Type'] = 'application/json';
                finalOptions.headers['X-Encrypted'] = 'true';
            }

            const response = await fetch(url, {
                ...finalOptions,
                headers: {
                    'Content-Type': 'application/json',
                    ...finalOptions.headers
                }
            });
            console.log(response)
            // if (!response.ok) {
            //     throw new Error(`HTTP error! status: ${response.status}`);
            // }
            
            const data = await response.json();
            return await this.decryptResponse(data);
        } catch (error) {
            console.log(error)
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

// 兼容 jQuery：重写 $.ajax 以走加解密通道
if (window.$ && typeof window.$.ajax === 'function') {
    const originalAjax = window.$.ajax;
    window.$.ajax = function(options) {
        try {
            const method = (options.type || options.method || 'GET').toUpperCase();
            let url = options.url || '';
            let fetchOptions = { method, headers: {} };

            // 处理 data 与 contentType
            const contentType = options.contentType || options.headers?.['Content-Type'] || options.headers?.['content-type'];
            const data = options.data || {};

            if (method === 'GET') {
                const query = typeof data === 'string' ? data : Object.keys(data).map(k => `${encodeURIComponent(k)}=${encodeURIComponent(data[k])}`).join('&');
                if (query) {
                    url += (url.includes('?') ? '&' : '?') + query;
                }
            } else {
                // POST/PUT...: 按 contentType 放入 body
                if ((contentType && contentType.toLowerCase().includes('application/json')) || typeof data === 'object') {
                    fetchOptions.headers['Content-Type'] = 'application/json';
                    fetchOptions.body = typeof data === 'string' ? data : JSON.stringify(data);
                } else {
                    fetchOptions.headers['Content-Type'] = 'application/x-www-form-urlencoded; charset=UTF-8';
                    fetchOptions.body = typeof data === 'string' ? data : Object.keys(data).map(k => `${encodeURIComponent(k)}=${encodeURIComponent(data[k])}`).join('&');
                }
            }

            // 合并自定义 headers
            if (options.headers) {
                fetchOptions.headers = { ...fetchOptions.headers, ...options.headers };
            }

            // 通过加解密通道发送
            window.encryptionUtil.fetchDecrypted(url, fetchOptions)
                .then(function(respData) {
                    if (typeof options.success === 'function') {
                        options.success(respData);
                    }
                })
                .catch(function(err) {
                    if (typeof options.error === 'function') {
                        options.error(null, 'error', err);
                    } else {
                        console.log('$.ajax 请求失败:', err);
                    }
                });
        } catch (e) {
            if (typeof options?.error === 'function') {
                options.error(null, 'error', e);
            } else {
                console.log('$.ajax 调用异常:', e);
            }
        }
    };
}
