package com.taotao.solrj;

import com.taotao.common.pojo.SearchItem;
import com.taotao.common.pojo.SearchResult;
import com.taotao.search.service.SearchService;
import com.taotao.search.service.impl.SearchServiceImpl;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class TestSolrJ {
    @Test
    public void testAddDocument() throws IOException, SolrServerException {
        //new httpSolrServer对象
        //指定url
        SolrServer solrServer = new HttpSolrServer("http://192.168.1.133:8080/solr/collection1");
        //创建文档对象
        SolrInputDocument document = new SolrInputDocument();
        //向文档加到域中
        document.setField("id", "test001");
        document.setField("item_title", "测试商品1");
        document.setField("item_price", 1000);
        //把文档对象写入索引库
        solrServer.add(document);
        //提交
        solrServer.commit();
    }

    @Test
    public void delete() throws IOException, SolrServerException {
        SolrServer solrServer = new HttpSolrServer("http://192.168.1.133:8080/solr/collection1");
        solrServer.deleteById("test001");
        solrServer.commit();
    }

    @Test
    public void searchDoc() throws Exception {
        //
        SolrServer solrServer = new HttpSolrServer("http://192.168.1.133:8080/solr/collection1");
        SolrQuery query = new SolrQuery();
        //设置查询条件
        // query.set("q", "*:*");
        query.setQuery("手机");
        //分页
        query.setStart(30);
        query.setRows(20);
        //设置默认搜索域
        query.set("df", "item_keywords");
        //设置高亮
        query.setHighlight(true);
        query.setHighlightSimplePre("<div>");
        query.setHighlightSimplePost("</div>");

        QueryResponse response = solrServer.query(query);

        SolrDocumentList results = response.getResults();
        for (SolrDocument result : results) {
            System.out.println(result.get("id"));
            //取高亮
            Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
            List<String> list = highlighting.get(result.get("id")).get("item_title");
            String itemTitle = "";
            if (list != null && list.size() > 0) {
                itemTitle = list.get(0);
            } else {
                itemTitle = (String) result.get("item_title");
            }
            System.out.println(itemTitle);
            System.out.println(result.get("item_sell_point"));
            System.out.println(result.get("item_price"));
            System.out.println(result.get("item_image"));
            System.out.println(result.get("item_category_name"));
            System.out.println("=========================");
        }
        System.out.println(results.getNumFound());

    }


    @Test
    public void c() throws Exception {
        SearchService s = new SearchServiceImpl();
        SearchResult search = s.search("手机", 1, 30);
        List<SearchItem> itemList = search.getItemList();
        for (SearchItem searchItem : itemList) {
            System.out.println(searchItem.getTitle());
        }
    }
}
