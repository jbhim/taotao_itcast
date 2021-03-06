package com.taotao.search.controller;

import com.taotao.common.pojo.SearchResult;
import com.taotao.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SearchController {
    @Autowired
    private SearchService searchService;

    @RequestMapping("/search")
    public String search(@RequestParam("q") String queryString,
                         @RequestParam(defaultValue = "1") Integer page,
                         Model model) {
        try {
            //转码
            queryString = new String(queryString.getBytes("iso8859-1"), "utf-8");

            SearchResult search = searchService.search(queryString, page, 50);
            model.addAttribute("query", queryString);
            model.addAttribute("totalPages", search.getTotalPages());
            model.addAttribute("itemList", search.getItemList());
            model.addAttribute("page", page);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "search";
    }

}
