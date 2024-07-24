package com.lin.service.impl;

import com.lin.RepairCommonUtil;
import com.lin.bean.RepairMaterialVo;
import com.lin.service.RepairService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RepairServiceImpl implements RepairService {
    /**
     * 构造树形结构
     *
     * @return
     */
    @Override
    public List<RepairMaterialVo> getRepairMaterialList() {
        RepairMaterialVo parent = new RepairMaterialVo();
        parent.setCode("-1");
        parent.setName("父级");
        parent.setKind("1");
        RepairMaterialVo child = new RepairMaterialVo();
        child.setUpperCode("-1");
        child.setCode("1");
        child.setName("父级");
        child.setKind("1");
        List<RepairMaterialVo> list = new ArrayList<>(6);
        list.add(parent);
        list.add(child);
        list = RepairCommonUtil.list2Tree(list);
        return list;
    }
}
