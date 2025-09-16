package com.lin.controller;

import com.lin.annotation.EncryptResponse;
import com.lin.bean.index.ConceptAndIndex;
import com.lin.service.ConceptBoardService;
import com.lin.service.GlobalIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ConceptAndIndexController {
    @Autowired
    ConceptBoardService conceptBoardService;
    @Autowired
    GlobalIndexService globalIndexService;

    @RequestMapping("/getConceptAndIndexList")
    @EncryptResponse(encryptAll = true)
    public ResponseEntity<ConceptAndIndex> list(HttpServletRequest request, Model model) {
        String type = request.getParameter("type");
        List<ConceptAndIndex.Concept> increaseIndustryList = conceptBoardService.logLianBan("1", type);
        List<ConceptAndIndex.Concept> decreaseIndustryList = conceptBoardService.logLianBan("0", type);
        List<ConceptAndIndex.Index> indexList = globalIndexService.getIndexInfo();
//        CollectionUtil.reverse(list);
        ConceptAndIndex conceptAndIndex = new ConceptAndIndex();
        conceptAndIndex.setIncreaseConceptList(increaseIndustryList);
        conceptAndIndex.setDecreaseConceptList(decreaseIndustryList);
        conceptAndIndex.setIndexList(indexList);
        return ResponseEntity.ok(conceptAndIndex);
    }
}
