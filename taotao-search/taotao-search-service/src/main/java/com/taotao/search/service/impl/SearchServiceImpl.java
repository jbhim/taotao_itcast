package com.taotao.search.service.impl;

import com.taotao.common.pojo.SearchResult;
import com.taotao.search.dao.SearchDao;
import com.taotao.search.service.SearchService;
import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private SearchDao searchDao;

    @Override
    public SearchResult search(String queryString, int page, int rows) throws Exception {
        SolrQuery query = new SolrQuery();
        query.setQuery(queryString);
        //设置分宜
        if (page < 1) page = 1;
        query.setStart((page - 1) * rows);
        if (rows < 10) rows = 10;
        query.setRows(rows);
        //设置默认搜索域
        query.set("df","item_title");
        //设置高亮
        query.setHighlight(true);
        query.addHighlightField("item_title");
        query.setHighlightSimplePre("<font color='red'>");
        query.setHighlightSimplePost("</font>");
        //dao
        SearchResult search = searchDao.search(query);

        long recordCount = search.getRecordCount();
        long pages = recordCount / rows;
        if (recordCount % rows > 0) {
            pages += 1;
        }
        search.setTotalPages(pages);

        return search;
    }
}
