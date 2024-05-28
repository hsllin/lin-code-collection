package com.lin.api;

import com.lin.bean.RepairMaterialVo;
import com.lin.service.RepairService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author JiaQIng
 * @Description 测试api
 * @ClassName TestApi
 * @Date 2023/4/23 20:16
 **/
@RestController
@RequestMapping("/test")
public class TestYdxyApi {
    @Autowired
    RepairService repairService;

    @GetMapping("/repair-tree")
    public List<RepairMaterialVo> testAnnotation() {
        return repairService.getRepairMaterialList();
    }

}
