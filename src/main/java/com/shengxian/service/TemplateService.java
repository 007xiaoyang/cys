package com.shengxian.service;

import com.shengxian.entity.Template;

import java.util.List;

/**
 * Description:
 *
 * @Author: yang
 * @Date: 2019-05-09
 * @Version: 1.0
 */
public interface TemplateService {

    /**
     * 添加模板2
     * @param business_id
     * @param title
     * @param type
     * @param one
     * @param two
     * @return
     */
    Integer addTemplateTwo( Integer business_id,String title ,Integer type ,String one , String two);

    /**
     * 查询店铺模板2集合
     * @param token
     * @param role
     * @return
     */
    List<Template> selectTemplateTwoList(String token ,Integer role);

    /**
     * 根据类型查询店铺模板2
     * @param token
     * @param role
     * @param type
     * @return
     */
    Template selectTemplateTwo(String token ,Integer role ,Integer type);


    /**
     * 修改模板2
     * @param id
     * @param title
     * @param one
     * @param two
     * @return
     */
    Integer updateTemplateTwo(Integer id,String title ,String one , String two );

    /**
     * 添加模板3
     * @param business_id
     * @param title
     * @param type
     * @param one
     * @param two
     * @return
     */
    Integer addTemplateThree( Integer business_id,String title ,Integer type ,String one , String two);

    /**
     * 查询店铺模板3集合
     * @param token
     * @param role
     * @return
     */
    List<Template> selectTemplateThreeList(String token ,Integer role);

    /**
     * 根据类型查询店铺模板3
     * @param token
     * @param role
     * @param type
     * @return
     */
    Template selectTemplateThree(String token ,Integer role ,Integer type);


    /**
     * 修改模板3
     * @param id
     * @param title
     * @param one
     * @param two
     * @return
     */
    Integer updateTemplateThree(Integer id,String title ,String one , String two );

    /**
     * 查询店铺模板5集合
     * @param token
     * @param role
     * @return
     */
    List<Template> selectTemplateFiveList(String token ,Integer role);

    /**
     * 根据类型查询店铺模板5
     * @param token
     * @param role
     * @param type
     * @return
     */
    Template selectTemplateFive(String token ,Integer role ,Integer type);


    /**
     * 修改模板5
     * @param id
     * @param title
     * @param one
     * @param state
     * @return
     */
    Integer updateTemplateFive(Integer id,String title ,String one , Integer state );
}
