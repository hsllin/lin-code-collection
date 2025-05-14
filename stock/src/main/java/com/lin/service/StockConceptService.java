package com.lin.service;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.bean.DateFormatEnum;
import com.lin.bean.lianban.LianBanNew;
import com.lin.bean.stockknow.StockConcept;
import com.lin.bean.strongstock.StrongStockBean;
import com.lin.util.AdvancedConceptCloudUtil;
import com.lin.util.CommonUtils;
import com.lin.util.ConceptFrequencyAnalyzer;
import com.lin.util.DateUtils;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.Serializable;
import java.util.*;

/**
 * 自己维护一些概念
 */
public interface StockConceptService {

    Page<StockConcept> getStockConceptList(String keyword, Integer startIndex, Integer pageSize);

    List<StockConcept> getAllStockConceptList();

    boolean addOrEditStockConcept(StockConcept stockConcept);

    boolean deleteStockConcept(String id);

    boolean sysTonghuaShunConcept();

}
