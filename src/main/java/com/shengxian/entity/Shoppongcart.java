package com.shengxian.entity;

import java.util.Date;

/**
 * Description:
 *
 * @Author: yang
 * @Date: 2019-01-20
 * @Version: 1.0
 */
public class Shoppongcart {

    private Integer id ;
    private String tic;
    private Date create_time;

    public Shoppongcart() {
    }

    public Shoppongcart(String tic, Date create_time) {
        this.tic = tic;
        this.create_time = create_time;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTic() {
        return tic;
    }

    public void setTic(String tic) {
        this.tic = tic;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }
}
