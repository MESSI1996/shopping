package com.tz.service.impl;

import com.google.common.collect.Sets;
import com.tz.common.ServerResponse;
import com.tz.dao.CategoryMapper;
import com.tz.pojo.Category;
import com.tz.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Service
public class CategoryService implements ICategoryService {

    @Autowired
    CategoryMapper categoryMapper;

    @Override
    public ServerResponse get_category(Integer categoryId) {
        //1：非空校验
        if (categoryId==null){
            return  ServerResponse.serverResponseByError("类别参数不能为空");
        }

        //2：根据categoryId查询类别
        Category category=categoryMapper.selectByPrimaryKey(categoryId);
        if (category==null){
            return  ServerResponse.serverResponseByError("查询的类别不存在");
        }
        //3:查询子类别
        List<Category> categoryList=categoryMapper.findChildCategory(categoryId);


        //4：返回结果
        return ServerResponse.serverResponseBySuccess(categoryList);
    }

    @Override
    public ServerResponse add_category(Integer parentId, String categoryName) {
        //1:参数校验
        if (categoryName==null){
          return  ServerResponse.serverResponseByError("添加节点不能为空,类别名称不能为空");
    }

        //2：添加节点
        Category category=new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
      int result = categoryMapper.insert(category);
        if (result>0){
            return  ServerResponse.serverResponseBySuccess("添加成功");
        }
        //3：返回结果
        return ServerResponse.serverResponseByError("添加失败");
    }

    @Override
    public ServerResponse set_category_name(Integer categoryId, String categoryName) {
        //1:参数非空校验
        if (categoryId==null||categoryId.equals("")){
            return  ServerResponse.serverResponseByError("类别参数不能为空");
        }
        if (categoryName==null||categoryName.equals("")){
            return  ServerResponse.serverResponseByError("类别参数不能为空");
        }

        //2：根据categoryId查询
           Category category= categoryMapper.selectByPrimaryKey(categoryId);
        if (category == null) {

            return  ServerResponse.serverResponseByError("更改类别不存在");
        }
        //3:修改
            category.setName(categoryName);
            int result=categoryMapper.updateByPrimaryKey(category);
            if (result>0){
                return  ServerResponse.serverResponseBySuccess();
            }
        //4：返回结果


        return ServerResponse.serverResponseByError("添加失败");
    }

    @Override
    public ServerResponse get_deep_category(Integer categoryId) {
        //1:参数非空校验
        if (categoryId==null||categoryId.equals("")){
            return  ServerResponse.serverResponseByError("类别参数不能为空");
        }
      //2查询
        Set<Category> categorySet= Sets.newHashSet();
        categorySet=findALLChildCategory(categorySet,categoryId);

        Set<Integer> integerSet=Sets.newHashSet();
        Iterator<Category> iterator=categorySet.iterator();
        while (iterator.hasNext()){
            Category category=iterator.next();
            integerSet.add(category.getId());
        }

        return ServerResponse.serverResponseBySuccess(integerSet);
    }

    private Set<Category> findALLChildCategory(Set<Category> categorySet,Integer categoryId){
        Category category=categoryMapper.selectByPrimaryKey(categoryId);
        if (category!=null){
            categorySet.add(category);
        }
        //查找categoryId下的子节点(平级)
        List<Category> categoryList=categoryMapper.findChildCategory(categoryId);
        if (categoryList!=null&&categoryList.size()>0){
            for (Category category1:categoryList){
                findALLChildCategory(categorySet,category1.getId());
            }

        }
        return  categorySet;
    }
}
