package com.mmall.service.Impl;

import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.service.ICategoryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 */
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    public ServerResponse<String> addCategory(String categoryName,Integer parentId){
        if (parentId==null || StringUtils.isBlank(categoryName)){
            return ServerResponse.creatByErrorMessage("添加品类参数错误");
        }

        Category category=new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(true);

        int effectCount=categoryMapper.insert(category);
        if (effectCount>0){
            return ServerResponse.creatBySuccess("添加品类成功");
        }
        return ServerResponse.creatByErrorMessage("添加品类失败");
    }

    public ServerResponse<String> updateCategoryName(Integer categoryId,String categoryName){
        if (categoryId==null || StringUtils.isBlank(categoryName)){
            return ServerResponse.creatByErrorMessage("参数错误");
        }
        Category category=new Category();
        category.setName(categoryName);
        category.setId(categoryId);

        int effectNum=categoryMapper.updateByPrimaryKeySelective(category);
        if (effectNum>0){
            return ServerResponse.creatBySuccess("更新品类成功");
        }else {
            return ServerResponse.creatByErrorMessage("更新品类名称失败");
        }
    }
}
