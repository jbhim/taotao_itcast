package com.taotao.controller;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentService;
import com.taotao.pojo.TbContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ContentController {
    @Autowired
    private ContentService contentService;

    @RequestMapping("/content/save")
    @ResponseBody
    public TaotaoResult addContent(TbContent content) {
        return contentService.addContent(content);
    }


    @RequestMapping("/content/query/list")
    @ResponseBody
    public EasyUIDataGridResult getContent(Long categoryId, Integer page, Integer rows) {
        return contentService.getContent(categoryId, page, rows);
    }


    @RequestMapping("/rest/content/edit")
    @ResponseBody
    public TaotaoResult updateContent(TbContent content) {
        return contentService.updateContent(content);
    }


    @RequestMapping("/content/delete")
    @ResponseBody
    public TaotaoResult deleteContent(Long[] ids) {
        for (Long id : ids) {
            System.out.println(id);
        }
        return contentService.deleteContent(ids);
    }
}
