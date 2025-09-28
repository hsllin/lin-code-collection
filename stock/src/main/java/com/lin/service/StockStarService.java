package com.lin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.bean.stockstudy.StockStudy;

import javax.script.ScriptException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 个股学习
 */
public interface StockStarService {


    Integer getStarCount();

    boolean updateStarCount();
}
