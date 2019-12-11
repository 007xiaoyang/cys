package com.shengxian.vo;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @Description : UserCategoryVO
 * @Author: yang
 * @Date: 2019-12-01
 * @Version: 1.0
 */
public class UserCategoryVO {

    /**
     * 类别id
     */
    @ApiModelProperty(value = "类别id")
    private Long id;


    /**
     * 类别名称
     */
    @ApiModelProperty(value = "类别名称")
    private String name;

    private String code;

    /**
     * 用户集合
     */
    @ApiModelProperty(value = "用户信息对象")
    private List<UserVO> children;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<UserVO> getChildren() {
        return children;
    }

    public void setChildren(List<UserVO> children) {
        this.children = children;
    }
}
