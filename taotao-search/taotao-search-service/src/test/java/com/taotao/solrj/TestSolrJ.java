package com.taotao.solrj;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import java.io.IOException;

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
}
