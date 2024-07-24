package com.lin;

import cn.hutool.core.util.IdUtil;
import com.lin.bean.BaseTreeVO;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * 佛祖保佑，此代码无bug，就算有也一眼看出
 * 功能描述
 *
 * @author: songlin
 * @date: 2023年05月18日 17:53
 */
public class RepairCommonUtil {

    public static <T extends BaseTreeVO> List<T>  list2Tree(List<T> sourceList) {
        if (CollectionUtils.isEmpty(sourceList)) {
            return Collections.emptyList();
        }
        List<T> resultList = new ArrayList<>();
        //将数据封装成树形结构
        Map<String, T> map = new HashMap<>();
        for (T data : sourceList) {
            map.put(data.getCode(), data);
        }
        for (T data : sourceList) {
            T obj = map.get(data.getUpperCode());
            if (obj != null) {
                List<T> children = obj.getChildren();
                if (children == null || children.isEmpty()) {
                    children = new ArrayList<>();
                }
                children.add(data);
                obj.setChildren(children);

            } else {
                resultList.add(data);
            }

        }
        return resultList;
    }

    /**
     * 将list转为父子结构
     *
     * @param sourceList
     * @return
     */
    public static List<Map<String, Object>> list2TreeMap(List<Map<String, Object>> sourceList) {
        if (CollectionUtils.isEmpty(sourceList)) {
            return Collections.emptyList();
        }
        List<Map<String, Object>> resultList = new ArrayList<>();
        //将数据封装成树形结构
        Map<String, Map<String, Object>> map = new HashMap<>();
        for (Map<String, Object> data : sourceList) {
            map.put(data.get("value") + "", data);
        }
        for (Map<String, Object> data : sourceList) {
            Map<String, Object> obj = map.get(data.get("uppercode") + "");
            if (obj != null) {
                List<Map<String, Object>> children = (List<Map<String, Object>>) obj.get("children");
                if (children == null || children.isEmpty()) {
                    children = new ArrayList<>();
                }
                children.add(data);
                obj.put("children", children);

            } else {
                resultList.add(data);
            }

        }
        return resultList;
    }

    public static <T extends BaseTreeVO> T filterTreeByKeyWord(T t, String keyword) {
        List<BaseTreeVO> children = t.getChildren();
        if (CollectionUtils.isEmpty(children)) {
            return t;
        }
        filter(children, keyword);
        return t;
    }

    /**
     * 递归方法
     *
     * @param list    任意层级的节点
     * @param keyword 关键字
     */
    public static <T extends BaseTreeVO> void   filter( List<T> list, String keyword) {
        Iterator<T> parent = list.iterator();
        while (parent.hasNext()) {
            //当前节点
            T t = parent.next();
            if ((!t.getName().contains(keyword) && !t.getName().toLowerCase().contains(keyword))) {
                //当前节点不包含关键字，继续遍历下一级
                // 取出下一级节点
                List<T> children = t.getChildren();
                // 递归
                if (!CollectionUtils.isEmpty(children)) {
                    filter(children, keyword);
                }
                //下一级节点都被移除了，那么父节点也移除，因为父节点也不包含关键字
                if (CollectionUtils.isEmpty(t.getChildren())) {
                    parent.remove();
                }
            } else {
                //当前节点包含关键字，继续递归遍历
                //子节点递归如果不包含关键字则会进入if分支被删除
                List<T> children = t.getChildren();
                // 递归
                if (!CollectionUtils.isEmpty(children)) {
//                    filter(children, keyword);
                }
            }
        }
    }

//    /**
//     * 递归方法
//     *
//     * @param list    任意层级的节点
//     * @param keyword 关键字
//     */
//    private static void filter(List<BaseTreeVO> list, String keyword) {
//        Iterator<BaseTreeVO> parent = list.iterator();
//        while (parent.hasNext()) {
//            //当前节点
//            BaseTreeVO t = parent.next();
//            if (!t.getName().contains(keyword)) {
//                //当前节点不包含关键字，继续遍历下一级
//                // 取出下一级节点
//                List<BaseTreeVO> children = t.getChildren();
//                // 递归
//                if (!CollectionUtils.isEmpty(children)) {
//                    filter(children, keyword);
//                }
//                //下一级节点都被移除了，那么父节点也移除，因为父节点也不包含关键字
//                if (CollectionUtils.isEmpty(t.getChildren())) {
//                    parent.remove();
//                }
//            } else {
//                //当前节点包含关键字，继续递归遍历
//                //子节点递归如果不包含关键字则会进入if分支被删除
//                List<BaseTreeVO> children = t.getChildren();
//                // 递归
//                if (!CollectionUtils.isEmpty(children)) {
//                    filter(children, keyword);
//                }
//            }
//        }
//    }

    /**
     * 将List<Map<String,Object>>转换为List<T>
     *
     * @param maps
     * @param clazz
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public static <T> List<T> mapsToBeans(List<Map<String, Object>> maps, Class<T> clazz) {

        List<T> list = Collections.emptyList();
        if (maps != null && maps.size() > 0) {
            list = new ArrayList<>(maps.size());
            Map<String, Object> map;
            T bean;
            try {
                for (int i = 0, size = maps.size(); i < size; i++) {
                    map = maps.get(i);
                    bean = mapToBean(map, clazz.getDeclaredConstructor().newInstance());
                    list.add(bean);
                }
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            } catch (NoSuchMethodException ex) {
                ex.printStackTrace();
            } catch (InvocationTargetException ex) {
                ex.printStackTrace();
            } catch (InstantiationException ex) {
                ex.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 将map装换为javabean对象
     *
     * @param map
     * @param bean
     * @return
     */
    public static <T> T mapToBean(Map<String, Object> map, T bean) {
        BeanMap beanMap = BeanMap.create(bean);
        beanMap.putAll(map);
        return bean;
    }

    /**
     * 将List<T>转换为List<Map<String, Object>>
     *
     * @param objList
     * @return
     */
    public static <T> List<Map<String, Object>> beansToMaps(List<T> objList) {
        List<Map<String, Object>> list = Collections.emptyList();
        if (objList != null && objList.size() > 0) {
            list = new ArrayList<>(objList.size());
            Map<String, Object> map;
            T bean;
            for (int i = 0, size = objList.size(); i < size; i++) {
                bean = objList.get(i);
                map = beanToMap(bean);
                list.add(map);
            }
        }
        return list;
    }
    /**
     * 将对象装换为map
     *
     * @param bean
     * @return
     */
    public static <T> Map<String, Object> beanToMap(T bean) {
        Map<String, Object> map = new HashMap<>();
        if (bean != null) {
            BeanMap beanMap = BeanMap.create(bean);
            for (Object key : beanMap.keySet()) {
                map.put(String.valueOf(key), beanMap.get(key));
            }
        }
        return map;
    }

}
