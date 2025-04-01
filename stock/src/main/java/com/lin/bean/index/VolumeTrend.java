package com.lin.bean.index;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 成交量走势
 */
@Data
@TableName("stock_volume_trend")
public class VolumeTrend {
    private String volume;
    private Integer limitUp;
    private Integer limitDown;
    private Integer limitCount;
    private Integer inputUp;
    private Integer inputDown;
    private String id;
    private String date;
    private Integer limit1;
    private Integer limit2;
    private Integer limit3;
    private Integer limit4;
    private Integer limit5;
}
