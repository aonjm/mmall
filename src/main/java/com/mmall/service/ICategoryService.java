package com.mmall.service;

import com.mmall.common.ServerResponse;

public interface ICategoryService {
    ServerResponse<String> addCategory(String categoryName, Integer parentId);
    ServerResponse<String> updateCategoryName(Integer categoryId,String categoryName);
}
