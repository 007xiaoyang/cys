package com.shengxian.entity;

/**
 * Description: 参数类
 *
 * @Author: yang
 * @Date: 2019-01-09
 * @Version: 1.0
 */
public class Search {

    private Integer business_id; //店铺id
    private Integer category_id; //类别id
    private String name; //
    private Integer level;


    private Integer startIndex; //开始页数
    private Integer pageSize; // 每页显示条数

    public Search() {
    }

    public Search(Integer business_id, Integer category_id, String name , Integer level) {
        this.business_id = business_id;
        this.category_id = category_id;
        this.name = name;
        this.level = level;
    }

    public Search(Integer business_id, Integer category_id,  String name, Integer level, Integer startIndex, Integer pageSize) {
        this.business_id = business_id;
        this.category_id = category_id;
        this.name = name;
        this.level = level;
        this.startIndex = startIndex;
        this.pageSize = pageSize;
    }
}
