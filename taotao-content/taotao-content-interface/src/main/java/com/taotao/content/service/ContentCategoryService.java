package com.taotao.content.service;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;

import java.util.List;

public interface ContentCategoryService {
    List<EasyUITreeNode> getContentCategoryList(long parenId);

    TaotaoResult addConCategory(Long parentId, String name);

    TaotaoResult updateCategory(Long id, String name);

    TaotaoResult deleteContentCategory(Long id);
}
