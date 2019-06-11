package com.shengxian.entity;

/**
 * Description:
 *
 * @Author: yang
 * @Date: 2019-03-08
 * @Version: 1.0
 */
public class AppParameter {

    private Integer business_id;
    private Integer staff_id;
    private String name ;
    private String number;
    private Integer status;
    private Integer state ;
    private Integer type;
    private Integer binding_id;
    private Integer category_id;
    private Integer level;



    private String startTime ;
    private String endTime;

    private Integer startIndex ;
    private Integer pageSize ;
    private Integer mold ;

    private Integer scheme_id ;

    private byte shield  ; // 1屏蔽进价
    private byte inv  ; // 1屏蔽库存




    public AppParameter() {
    }

    public AppParameter(Integer business_id, String name, String number) {
        this.business_id = business_id;
        this.name = name;
        this.number = number;
    }


    public AppParameter(Integer business_id, String name, String number, Integer startIndex, Integer pageSize) {
        this.business_id = business_id;
        this.name = name;
        this.number = number;
        this.startIndex = startIndex;
        this.pageSize = pageSize;
    }

    public AppParameter(Integer business_id, String name, String number, Integer state , String startTime , String endTime) {
        this.business_id = business_id;
        this.name = name;
        this.number = number;
        this.state = state;
        this.startTime = startTime;
        this.endTime  = endTime;
    }

    public AppParameter(Integer business_id, String name, String number, Integer state, String startTime , String endTime , Integer startIndex, Integer pageSize) {
        this.business_id = business_id;
        this.name = name;
        this.number = number;
        this.state = state;
        this.startTime = startTime;
        this.endTime  = endTime;
        this.startIndex = startIndex;
        this.pageSize = pageSize;
    }

    //
    public AppParameter(Integer business_id, Integer staff_id, String name) {
        this.business_id = business_id;
        this.staff_id = staff_id;
        this.name = name;
    }

    public AppParameter(Integer business_id, Integer staff_id, String name, Integer startIndex, Integer pageSize) {
        this.business_id = business_id;
        this.staff_id = staff_id;
        this.name = name;
        this.startIndex = startIndex;
        this.pageSize = pageSize;
    }

    public AppParameter(Integer business_id, Integer staff_id, String name, Integer mold) {
        this.business_id = business_id;
        this.staff_id = staff_id;
        this.name = name;
        this.mold = mold;
    }

    public AppParameter(Integer business_id, Integer staff_id, String name, String startTime, String endTime) {
        this.business_id = business_id;
        this.staff_id = staff_id;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public AppParameter(Integer business_id, Integer staff_id, String name, String startTime, String endTime, Integer startIndex, Integer pageSize) {
        this.business_id = business_id;
        this.staff_id = staff_id;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startIndex = startIndex;
        this.pageSize = pageSize;
    }
    //完成mold
    public AppParameter(Integer business_id, Integer staff_id, String name, String startTime, String endTime, Integer mold) {
        this.business_id = business_id;
        this.staff_id = staff_id;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.mold = mold;
    }



    public AppParameter(Integer business_id, String name , Integer category_id) {
        this.business_id = business_id;
        this.name = name;
        this.category_id = category_id;
    }

    public AppParameter(Integer business_id, String name, Integer category_id, Integer startIndex, Integer pageSize) {
        this.business_id = business_id;
        this.name = name;
        this.category_id = category_id;
        this.startIndex = startIndex;
        this.pageSize = pageSize;
    }

    public AppParameter(Integer business_id, Integer binding_id, Integer category_id , Integer level , String name ,byte shield ,byte inv ) {
        this.business_id = business_id;
        this.binding_id = binding_id;
        this.category_id = category_id;
        this.level = level;
        this.name = name;
        this.shield = shield;
        this.inv = inv ;

    }

    public AppParameter(Integer business_id, Integer binding_id, Integer category_id  , Integer level, String name , byte shield ,byte inv , Integer startIndex, Integer pageSize) {
        this.business_id = business_id;
        this.binding_id = binding_id;
        this.category_id = category_id;
        this.level = level;
        this.name = name;
        this.shield = shield;
        this.inv = inv ;
        this.startIndex = startIndex;
        this.pageSize = pageSize;
    }


    public AppParameter(Integer business_id, Integer binding_id, Integer category_id , Integer level , String name ,Integer scheme_id , byte inv) {
        this.business_id = business_id;
        this.binding_id = binding_id;
        this.category_id = category_id;
        this.level = level;
        this.name = name;
        this.scheme_id = scheme_id ;
        this.inv = inv;
    }

    public AppParameter(Integer business_id, Integer binding_id, Integer category_id  , Integer level, String name  ,Integer scheme_id , byte inv, Integer startIndex, Integer pageSize) {
        this.business_id = business_id;
        this.binding_id = binding_id;
        this.category_id = category_id;
        this.level = level;
        this.name = name;
        this.inv = inv;
        this.scheme_id = scheme_id;
        this.startIndex = startIndex;
        this.pageSize = pageSize;
    }

    //采购


    public AppParameter(Integer business_id, String name, String number, String startTime, String endTime) {
        this.business_id = business_id;
        this.name = name;
        this.number = number;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public AppParameter(Integer business_id, String name, String number, String startTime, String endTime, Integer startIndex, Integer pageSize) {
        this.business_id = business_id;
        this.name = name;
        this.number = number;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startIndex = startIndex;
        this.pageSize = pageSize;
    }




}
