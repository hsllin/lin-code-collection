package com.lin.bean;

import lombok.Data;

import java.util.List;

/**
 * 佛祖保佑，此代码无bug，就算有也一眼看出
 * 功能描述 报修地址树形结构
 *
 * @author: songlin
 * @date: 2023年05月19日 14:36
 */
@Data
public class BaseTreeVO<T extends BaseTreeVO> {
    private String code;
    private String name;
    private String upperCode;
    private List<T> children;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<T> getChildren() {
        return this.children;
    }

    public void setChildren(List<T> children) {
        this.children = children;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUpperCode() {
        return upperCode;
    }

    public void setUpperCode(String upperCode) {
        this.upperCode = upperCode;
    }
}
