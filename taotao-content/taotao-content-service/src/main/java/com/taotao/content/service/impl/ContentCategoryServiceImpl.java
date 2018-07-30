package com.taotao.content.service.impl;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentCategoryService;
import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.pojo.TbContentCategory;
import com.taotao.pojo.TbContentCategoryExample;
import com.taotao.pojo.TbContentCategoryExample.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {
    @Autowired
    private TbContentCategoryMapper contentCategoryMapper;

    @Override
    public List<EasyUITreeNode> getContentCategoryList(long parenId) {
        TbContentCategoryExample example = new TbContentCategoryExample();
        Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parenId);
        List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
        List<EasyUITreeNode> resultList = new ArrayList<>();
        for (TbContentCategory item : list) {
            EasyUITreeNode node = new EasyUITreeNode();
            node.setId(item.getId());
            node.setText(item.getName());
            node.setState(item.getIsParent() ? "closed" : "open");
            resultList.add(node);
        }
        return resultList;
    }

    @Override
    public TaotaoResult addConCategory(Long parentId, String name) {
        TbContentCategory tbContentCategory = new TbContentCategory();

        tbContentCategory.setParentId(parentId);
        tbContentCategory.setName(name);
        // 1.正常状态
        tbContentCategory.setStatus(1);

        tbContentCategory.setSortOrder(1);
        tbContentCategory.setIsParent(false);
        tbContentCategory.setCreated(new Date());
        tbContentCategory.setUpdated(new Date());

        contentCategoryMapper.insertSelective(tbContentCategory);
        //判断父节点状态
        TbContentCategory parent = contentCategoryMapper.selectByPrimaryKey(parentId);
        if (!parent.getIsParent()) {
            parent.setIsParent(true);
            contentCategoryMapper.updateByPrimaryKey(parent);
        }

        return TaotaoResult.ok(tbContentCategory);
    }

    @Override
    public TaotaoResult updateCategory(Long id, String name) {
        TbContentCategory tbContentCategory = new TbContentCategory();
        tbContentCategory.setId(id);
        tbContentCategory.setName(name);
        tbContentCategory.setUpdated(new Date());
        contentCategoryMapper.updateByPrimaryKeySelective(tbContentCategory);
        return TaotaoResult.ok();
    }

    @Override
    public TaotaoResult deleteContentCategory(Long id) {
        //查询当前节点
        TbContentCategory currentCategory = contentCategoryMapper.selectByPrimaryKey(id);
        //查询当前节点的父节点
        TbContentCategory parentCategory = contentCategoryMapper.selectByPrimaryKey(currentCategory.getParentId());

        //判断当前节点是否是叶子节点
        if (currentCategory.getIsParent()) {
            //不是叶子节点
            //递归删除
            deleteNode(currentCategory);
        } else {
            //是叶子节点直接删除
            contentCategoryMapper.deleteByPrimaryKey(id);
        }

        //判断父节点是否还有子节点
        TbContentCategoryExample example = new TbContentCategoryExample();
        Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentCategory.getId());
        List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
        if (list.size()==0){
            parentCategory.setIsParent(false);
            contentCategoryMapper.updateByPrimaryKeySelective(parentCategory);
        }


        return TaotaoResult.ok("delete success");
    }

    private void deleteNode(TbContentCategory currentCategory) {
        /*if (!currentCategory.getIsParent()) {
            //是叶子节点直接删除
            contentCategoryMapper.deleteByPrimaryKey(currentCategory.getId());
            return;
        }*/
        //查询子节点
        TbContentCategoryExample example = new TbContentCategoryExample();
        Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(currentCategory.getId());
        List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
        for (TbContentCategory category : list) {
            //删除子节点
            deleteNode(category);
        }
        //删自己
        contentCategoryMapper.deleteByPrimaryKey(currentCategory.getId());
    }
}
