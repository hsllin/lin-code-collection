package com.lin.controller;

import com.lin.bean.document.DocumentResultBean;
import com.lin.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class DocumentController {
    @Autowired
    DocumentService documentService;

    @RequestMapping("/getDocumentList")
    public ResponseEntity<List<DocumentResultBean>> list(HttpServletRequest request, Model model) {
        String type = request.getParameter("type");
        List<DocumentResultBean> list = documentService.getNewDocumentInfo();

//        CollectionUtil.reverse(list);
        return ResponseEntity.ok(list);
    }
}
