package com.lin.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lin.annotation.DecryptRequest;
import com.lin.util.EncryptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 请求体解密处理
 */
@ControllerAdvice
@Component
public class DecryptRequestAdvice implements RequestBodyAdvice {

    @Autowired
    private EncryptionUtil encryptionUtil;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return methodParameter.hasMethodAnnotation(DecryptRequest.class);
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        try {
            String body = new String(inputMessage.getBody().readAllBytes(), StandardCharsets.UTF_8);
            if (body == null || body.isEmpty()) {
                return inputMessage;
            }
            Map<String, Object> json = objectMapper.readValue(body, Map.class);
            Object isEncrypted = json.get("encrypted");
            Object encryptedData = json.get("data");
            if (Boolean.TRUE.equals(isEncrypted) && encryptedData instanceof String) {
                String decrypted = encryptionUtil.decrypt((String) encryptedData);
                byte[] bytes = decrypted.getBytes(StandardCharsets.UTF_8);
                return new HttpInputMessage() {
                    @Override
                    public InputStream getBody() {
                        return new ByteArrayInputStream(bytes);
                    }

                    @Override
                    public HttpHeaders getHeaders() {
                        return inputMessage.getHeaders();
                    }
                };
            }
        } catch (Exception ignored) {
        }
        return inputMessage;
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }
}



