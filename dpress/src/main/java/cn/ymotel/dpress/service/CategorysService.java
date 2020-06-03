package cn.ymotel.dpress.service;

import cn.hutool.core.collection.CollUtil;
import cn.ymotel.dpress.entity.mapper.CategoriesMapper;
import cn.ymotel.dpress.entity.model.Categories;
import cn.ymotel.dpress.entity.model.PostCategories;
import com.alicp.jetcache.anno.Cached;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.BeanUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Component
public class CategorysService {
    @Autowired
    private SqlSession sqlSession;

    @Resource
    private CategoriesMapper categoriesMapper;
    @Cached
    public Long count(Object siteid){
        Map data=new HashMap<>();
        data.put("siteid",siteid);
        return sqlSession.selectOne("categories.qcount",data);
    }
    public Categories getCategorybySlug(Object siteid, String slug){
        QueryWrapper queryWrapper=new QueryWrapper();
        Map map=new HashMap();
        map.put("siteid",siteid);
        map.put("slug",slug);
        queryWrapper.allEq(map);

        return categoriesMapper.selectOne(queryWrapper);
    }
    @Autowired
    PostCategorysService postCategorysService;
    public List<Categories> listCategoriesbyPostId(Object siteid,Object postid){
        return listCategoriesByPostCategories(postCategorysService.listPostCategoriesbyId(siteid,postid),siteid);
    }
    @Autowired
    OptionsService optionsService;
    public List<Categories> listCategoriesByPostCategories(List<PostCategories> list,Object siteid){
        List collection=new ArrayList();
        list.forEach(postCategories -> collection.add(postCategories.getId()));
        List<Categories> rtnList= categoriesMapper.selectBatchIds(collection);
        rtnList.forEach(categories -> {
            String fullPath="/"+optionsService.getCategories(siteid)+"/"+categories.getSlug()+optionsService.getPathSuffix(siteid);
            categories.setFullPath(fullPath);
        });
        return rtnList;
    }
    public List<Categories> listAll(Object siteid,String sort){
        QueryWrapper queryWrapper=new QueryWrapper();
        Map map=new HashMap();
        map.put("siteid",siteid);
        queryWrapper.allEq(map);
        queryWrapper.orderByDesc(sort);
      return   categoriesMapper.selectList(queryWrapper);
    }
    public List<Map> listAsTree(Object siteid,String sort){
        List<Categories> listall=listAll(siteid,sort);
        List<Map> listMap=new ArrayList<>();
        listall.forEach(categories -> {
            Map map= BeanMap.create(categories);
            listMap.add(map);
        });
        buildTree(listMap,"id","parentId",0);
        return listMap;
    }

    public List buildTree(List<Map> list, String idKey, String parentIdKey, Object parentId){
        List rtnList=new ArrayList();
        for(Map map:list){
            Object tparentId=map.get(parentIdKey);
            Object id=map.get(idKey);
            if(tparentId==null){
                continue;
            }
            if(tparentId.toString().equals(parentId.toString())){
                rtnList.add(map);
            }else{
                continue;
            }
            List list2=buildTree(list,idKey,parentIdKey,id);
            if(list2==null||list2.size()==0){
                map.put("children", null);

            }else {
                map.put("children", list2);
            }
        }
        return rtnList;
    }

//     fullPath.append(URL_SEPARATOR)
//            .append(optionService.getCategoriesPrefix())
//            .append(URL_SEPARATOR)
//            .append(category.getSlug())
//            .append(optionService.getPathSuffix());
}
