package com.lin.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 佛祖保佑，此代码无bug，就算有也一眼看出
 * 功能描述 耗材vo
 *
 * @author: songlin
 * @date: 2023年06月01日 16:34
 */
@Data
public class RepairMaterialVo extends BaseTreeVO{
    /**
     * 排序
     */
    @ApiModelProperty(value = "排序", name = "sort", required = false)
    private String sort;
    /**
     * 分类，1是父级，2是子级
     */
    @ApiModelProperty(value = "分类，1是父级，2是子级", name = "type", required = false)
    private String type;
    /**
     * 耗材单价
     */
    @ApiModelProperty(value = "耗材单价", name = "price", required = false)
    private String price;

    /**
     * 耗材单价
     */
    @ApiModelProperty(value = "用于标识是耗材分类还是耗材", name = "kind", required = false)
    private String kind;

    /**
     * 耗材单价
     */
    @ApiModelProperty(value = "描述", name = "description", required = false)
    private String description;

    /**
     * 耗材单价
     */
    @ApiModelProperty(value = "单位", name = "unit", required = false)
    private String unit;

    /**
     * 耗材单价
     */
    @ApiModelProperty(value = "耗材图片", name = "photo", required = false)
    private String photo;

    @ApiModelProperty(value = "安全库存", name = "safetyStock", required = false)
    private String safetyStock;

    /**
     * 耗材单价
     */
    @ApiModelProperty(value = "1是新增，2是编辑", name = "option", required = false)
    private String option;
}
