package com.lin.service;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.lin.bean.MyStock;
import com.lin.bean.ShengKaoBean;
import com.lin.bean.ShengKaoDataBean;
import org.springframework.stereotype.Service;

import javax.script.ScriptException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 佛祖保佑此代码没有bug，即使有也一眼看出
 *
 * @author lin
 * @Description
 * @create 2025-10-21 17:14
 */
@Service
public interface ShengKaoService {
    void sysnAllShengKaoData();

    List<ShengKaoDataBean> getShengKaoDataList(String type, String keyword);


    boolean sysShengkaoData(List<ShengKaoDataBean> list);


}