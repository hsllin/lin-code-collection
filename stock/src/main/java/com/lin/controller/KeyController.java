package com.lin.controller;

import com.lin.util.EncryptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 密钥管理Controller
 * 提供前端获取解密密钥的接口
 */
@Controller
@RequestMapping("/api/key")
public class KeyController {
    
    @Autowired
    private EncryptionUtil encryptionUtil;
    
    /**
     * 获取解密密钥
     * 注意：实际生产环境中应该通过更安全的方式传递密钥
     */
    @GetMapping("/get")
    public ResponseEntity<Map<String, String>> getKey(HttpServletRequest request) {
        // 可以添加权限验证逻辑
        // 例如：验证用户身份、IP白名单等
        
        Map<String, String> keyInfo = encryptionUtil.generateKeyInfo();
        return ResponseEntity.ok(keyInfo);
    }
}
