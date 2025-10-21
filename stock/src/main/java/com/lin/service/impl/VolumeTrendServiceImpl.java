package com.lin.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.bean.index.VolumeTrend;
import com.lin.bean.lianban.LianBanBean;
import com.lin.bean.lianban.LianBanNew;
import com.lin.bean.market.MarketBean;
import com.lin.mapper.VolumeTrendMapper;
import com.lin.service.LianBanChiService;
import com.lin.service.MarketVolumeTemperatureService;
import com.lin.service.StrongStockService;
import com.lin.service.VolumeTrendService;
import com.lin.util.CommonUtils;
import com.lin.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VolumeTrendServiceImpl extends ServiceImpl<VolumeTrendMapper, VolumeTrend> implements VolumeTrendService {
    @Autowired
    MarketVolumeTemperatureService marketVolumeTemperatureService;
    @Autowired
    LianBanChiService lianBanChiService;
    @Autowired
    StrongStockService strongStockService;

    @Override
    public List<VolumeTrend> getVolumeTrendData() {
        QueryWrapper<VolumeTrend> wrapper = new QueryWrapper();
        wrapper.orderByDesc("date");
        List<VolumeTrend> list = getBaseMapper().selectList(wrapper);
        return list;
    }

    @Override
    public boolean saveOrUpdateData() {
        MarketBean marketBean = marketVolumeTemperatureService.getMarketList();
        MarketBean.DataBean.MoneyBean moneyBean = marketBean.getData().getMoneyBean();
        MarketBean.DataBean.StockBean stockBean = marketBean.getData().getStock();
        VolumeTrend bean = new VolumeTrend();
        Integer aUp = Integer.valueOf(moneyBean.getAUp());
        Integer bUp = Integer.valueOf(moneyBean.getBDown());
        String today = CommonUtils.getTradeDay(0);
//        today="20250523";
        List<LianBanBean.DataBean> lianbanList = lianBanChiService.lianBan(today);

        bean.setVolume(moneyBean.getTotalOriginalMoney());
        bean.setLimitUp(stockBean.getZt());
        bean.setLimitDown(stockBean.getDt());
        bean.setLimitCount(lianbanList.get(0).getHeight());
        bean.setInputUp(stockBean.getUp());
        bean.setInputDown(stockBean.getDown());
        bean.setId(IdUtil.fastSimpleUUID());
        bean.setDate(DateUtils.getNowDate());
        String date = CommonUtils.getTradeDay(0);
        List<LianBanNew> lianBanNewList = StrongStockService.dealLianBanData(date);
        for (LianBanNew lianban : lianBanNewList) {
            if (lianban.getLimitNum() == 1) {
                bean.setLimit1(lianban.getStockList().size());
            }
            if (lianban.getLimitNum() == 2) {
                bean.setLimit2(lianban.getStockList().size());
            }
            if (lianban.getLimitNum() == 3) {
                bean.setLimit3(lianban.getStockList().size());
            }
            if (lianban.getLimitNum() == 4) {
                bean.setLimit4(lianban.getStockList().size());
            }
            if (lianban.getLimitNum() >= 5) {
                bean.setLimit5(bean.getLimit5() == null ? 0 : bean.getLimit5() + lianban.getStockList().size());
            }
        }


        UpdateWrapper<VolumeTrend> updateWrapper = new UpdateWrapper<VolumeTrend>()
                .eq("date", bean.getDate());
        return saveOrUpdate(bean, updateWrapper);
    }
}
