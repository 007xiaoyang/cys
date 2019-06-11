package com.shengxian.entity.clerkApp;

/**
 * Description:
 *
 * @Author: yang
 * @Date: 2019-04-14
 * @Version: 1.0
 */
public class Clerk {

    /**
     * 员工id
     */
    private Integer id ;
    /**
     * 店铺id
     */
    private Integer business_id ;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBusiness_id() {
        return business_id;
    }

    public void setBusiness_id(Integer business_id) {
        this.business_id = business_id;
    }
}
