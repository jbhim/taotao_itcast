package com.taotao.search.service.impl;

import com.taotao.common.pojo.SearchItem;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.search.mapper.SearchItemMapper;
import com.taotao.search.service.SearchItemService;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchItemServiceImpl implements SearchItemService {
    @Autowired
    private SearchItemMapper searchItemMapper;
    @Autowired
    private SolrServer solrServer;

    @Override
    public TaotaoResult importItemsToIndex() {

        try {
            List<SearchItem> itemList = searchItemMapper.getItemList();
            for (SearchItem searchItem : itemList) {
                SolrInputDocument document = new SolrInputDocument();
                document.setField("id", searchItem.getId());
                document.setField("item_title", searchItem.getTitle());
                document.setField("item_sell_point", searchItem.getSell_point());
                document.setField("item_price", searchItem.getPrice());
                document.setField("item_image", searchItem.getImage());
                solrServer.add(document);

            }

            //提交
            solrServer.commit();

        } catch (Exception e) {
            return TaotaoResult.build(500, "数据导入失败");
        }

        return TaotaoResult.ok();
    }
}
