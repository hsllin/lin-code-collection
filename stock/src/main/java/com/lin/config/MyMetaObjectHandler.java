package com.lin.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.lin.util.DateUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        System.out.println("insertFill...");
        this.fillStrategy(metaObject, "createDate", LocalDateTime.now());
        this.fillStrategy(metaObject, "updateDate", LocalDateTime.now());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        System.out.println("updateFill...");
        this.fillStrategy(metaObject, "updateDate", LocalDateTime.now());
    }
}
