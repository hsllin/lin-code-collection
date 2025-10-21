package com.lin.service.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.bean.MyStock;
import com.lin.bean.ShengKaoBean;
import com.lin.bean.ShengKaoDataBean;
import com.lin.bean.stockknow.StockBoard;
import com.lin.bean.stockstudy.StockStudy;
import com.lin.mapper.ShengKaoMapper;
import com.lin.mapper.StockBoardMapper;
import com.lin.service.ShengKaoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.script.ScriptException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.baomidou.mybatisplus.extension.toolkit.Db.saveOrUpdateBatch;

/**
 * 佛祖保佑此代码没有bug，即使有也一眼看出
 *
 * @author lin
 * @Description
 * @create 2025-10-21 17:27
 */
@Service
public class ShengKaoServiceImpl extends ServiceImpl<ShengKaoMapper, ShengKaoDataBean> implements ShengKaoService {
    public static void main(String[] args) {
//        sysShengKaoData();
    }

    public void sysnAllShengKaoData() {
        List<String> zoneList = new ArrayList<>();
        zoneList.add("01");
        zoneList.add("02");
        zoneList.add("03");
        zoneList.add("04");
        zoneList.add("05");
        zoneList.add("06");
        zoneList.add("07");
        zoneList.add("08");
        zoneList.add("09");
        zoneList.add("10");
        zoneList.add("11");
        zoneList.add("12");
        zoneList.add("13");
        zoneList.add("14");
        zoneList.add("15");
        zoneList.add("16");
        zoneList.add("17");
        zoneList.add("18");
        zoneList.add("19");
        zoneList.add("20");
        zoneList.add("21");

        for (String zone : zoneList) {
            int index = 1;
            List<ShengKaoDataBean> shengKaoDataBeanList = new ArrayList<>();
            ShengKaoBean bean = getShengKaoData(zone, index);
            int total = bean.getTotal();
            int totalPage = total / 100 + 1;
            Long currentTime = 0L;
            for (int i = 1; i <= totalPage; i++) {
                try {
                    // 休眠3秒
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt(); // 恢复中断状态
                }
                bean = getShengKaoData(zone, i);
                for (ShengKaoBean.RowsDTO dto : bean.getRows()) {
                    ShengKaoDataBean dataBean = new ShengKaoDataBean();
                    dataBean.setId(dto.getId().getBfa001() + "_" + dto.getId().getBfz315());
                    dataBean.setRecruitmentUnit(dto.getAab004());
                    dataBean.setRecruitmentPosition(dto.getBfe3a4());
                    dataBean.setPositionCode(dto.getBfe301());
                    dataBean.setParent_code(dto.getBab301());
                    dataBean.setSuccessfulApplicants(dto.getAab119().toString());
                    dataBean.setNeedNum(dto.getAab019().toString());
                    if (currentTime == 0) {
                        currentTime = dto.getAae036();
                    }
                    if (currentTime == 1761040800000L) {
                        dataBean.setA10(dto.getAab119().toString());
                    }

                    shengKaoDataBeanList.add(dataBean);
                }

            }
            sysShengkaoData(shengKaoDataBeanList);
        }
    }

    public static String transferTime(Long time) {
        Instant instant = Instant.ofEpochMilli(time);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(ZoneId.of("Asia/Shanghai")); // 设置为北京时间时区

        String formattedDate = formatter.format(instant);
        return formattedDate;
    }

    public static ShengKaoBean getShengKaoData(String zone, int page) {

        String url = "https://ggfw.hrss.gd.gov.cn:8443/gwyks/exam/details/spQuery.do?bfa001=202601&bab301=" + zone + "&page=" + page + "&rows=100";
        Map<String, Object> param = new HashMap<>();
//        param.put("bfa001", "202601");
//        param.put("bab301", "21");
//        param.put("page", "1");
//        param.put("rows", "1000");
//        ?bfa001=202601&bab301=21&page=1&rows=50
        Map<String, String> header = new HashMap<>();
        String cookie = "JSESSIONID=uNgGmP3x940KKEyN0USNbebkTS4IfyrM7Jaw8TnqUaRM9thGgPTF!450060461; ../issoYH_MASS_TOKEN=184fe899-881b-446c-817b-7356311146f2; Client-ID=31229d4c2d9a4a2c8e299bbb4af86903%3Ai+anukRcGNf+M1vfUaEDcBXlOdaU+WOaAbOcfoHgHDU%3D; 公职人员管理系统IP池_gwyks=60887.57421.1041.0000; x-xun-no=A2481996D25D67F8";
        header.put("Host", "ggfw.hrss.gd.gov.cn:8443");
        header.put("origin", "https://ggfw.hrss.gd.gov.cn:8443");
        header.put("Cookie", cookie);
        header.put("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/135.0.0.0 Safari/537.36");
        header.put("referer", "https://ggfw.hrss.gd.gov.cn:8443/gwyks/center.do?nvt=1761018432307");
        header.put("content-type", "application/x-www-form-urlencoded; charset=UTF-8");
        header.put("x-requested-with", "XMLHttpRequest");
        header.put("sec-ch-ua", "Chromium\";v=\"136\", \"Google Chrome\";v=\"136\", \"Not.A/Brand\";v=\"99");
        header.put("sec-ch-ua-mobile", "?0");
        header.put("sec-ch-ua-platform", "Windows");
        header.put("sec-fetch-dest", "empty");
        header.put("sec-fetch-mode", "cors");
        header.put("sec-fetch-site", "same-origin");
        header.put("pragma", "no-cache");
        header.put("", "");


        String result = HttpUtil.createPost(url).addHeaders(header).body(JSONUtil.toJsonStr(param)).execute().body();
        ShengKaoBean shengKaoBean = JSONUtil.toBean(result, ShengKaoBean.class);
        return shengKaoBean;
    }


    @Override
    public List<ShengKaoDataBean> getShengKaoDataList(String type, String keyword) {
        QueryWrapper<ShengKaoDataBean> queryWrapper = new QueryWrapper<>();
        Page<ShengKaoDataBean> page = new Page<>(1, 60);

        if (type.equals("1")) {
            if (StringUtils.isNotBlank(keyword)) {
                queryWrapper.like("position_code", keyword)
                        .or().like("recruitment_unit", keyword)
                        .or().like("recruitment_position", keyword);
            }

        } else {
            List<String> codeList = new ArrayList<>();
            String code = "10204632657001,10200382657002,10200382657003,60200572659002,60200672659001,11200812655001,61200182659002,61200022659005,61200172659001,61200042659005,10500132655001,10500962655001,10502792657001,60500092659004,10901162657002,10903752657001,60900652659001,11302522657001,10105302657001,10109672657001,10112582657001,10113482657001,19900952641001,60100622659001,60101602659001,11600672655002,11606152655002,11703822657005,10600382655001,61100032659010";
            codeList = Arrays.asList(code.split(","));
            queryWrapper.in("position_code", codeList);
        }
        queryWrapper.orderByAsc("CAST(a10 AS SIGNED)");
        Page<ShengKaoDataBean> dataBeanPage = getBaseMapper().selectPage(page, queryWrapper);
        return dataBeanPage.getRecords();
    }

    public boolean sysShengkaoData(List<ShengKaoDataBean> list) {
        return saveOrUpdateBatch(list);
    }
}