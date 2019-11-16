package com.shengxian.service.impl;

import com.shengxian.entity.Template;
import com.shengxian.mapper.ShopMapper;
import com.shengxian.service.TemplateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Description:
 *
 * @Author: yang
 * @Date: 2019-05-09
 * @Version: 1.0
 */
@Service
public class TemplateServiceImpl implements TemplateService {

    @Resource
    private ShopMapper shopMapper;


    //添加模板2
    @Override
    @Transactional
    public Integer addTemplateTwo(Integer business_id, String title, Integer type, String one, String two) {
        return shopMapper.addTemplateTwo(business_id , title , type ,one ,two);
    }

    //查询店铺模板2集合
    @Override
    public List<Template> selectTemplateTwoList(String token, Integer role) {
        Integer business_id = shopMapper.shopipByTokenAndRole(token, role);
        return shopMapper.selectTemplateTwoList(business_id);
    }

    //根据类型查询店铺模板2
    @Override
    public Template selectTemplateTwo(String token, Integer role, Integer type) {
        Integer business_id = shopMapper.shopipByTokenAndRole(token, role);
        return shopMapper.selectTemplateTwo(business_id , type);
    }

    //修改模板2
    @Override
    @Transactional
    public Integer updateTemplateTwo(Integer id, String title,  String one, String two ,int barcode) {
        return shopMapper.updateTemplateTwo(id , title  , one ,two ,barcode);
    }

    //添加模板3
    @Override
    @Transactional
    public Integer addTemplateThree(Integer business_id, String title, Integer type, String one, String two) {
        return shopMapper.addTemplateThree(business_id , title , type ,one ,two);
    }

    //查询店铺模板3集合
    @Override
    public List<Template> selectTemplateThreeList(String token, Integer role) {
        Integer business_id = shopMapper.shopipByTokenAndRole(token, role);
        return shopMapper.selectTemplateThreeList(business_id);
    }

    //根据类型查询店铺模板3
    @Override
    public Template selectTemplateThree(String token, Integer role, Integer type) {
        Integer business_id = shopMapper.shopipByTokenAndRole(token, role);
        return shopMapper.selectTemplateThree(business_id , type);
    }

    //修改模板3
    @Override
    @Transactional
    public Integer updateTemplateThree(Integer id, String title, String one, String two ,int barcode) {
        return shopMapper.updateTemplateThree(id , title  , one ,two ,barcode);
    }

    //查询店铺模板5集合
    @Override
    public List<Template> selectTemplateFiveList(String token, Integer role) {
        Integer business_id = shopMapper.shopipByTokenAndRole(token, role);
        return shopMapper.selectTemplateFiveList(business_id);
    }

    //根据类型查询店铺模板5
    @Override
    public Template selectTemplateFive(String token, Integer role, Integer type) {
        Integer business_id = shopMapper.shopipByTokenAndRole(token, role);
        return shopMapper.selectTemplateFive(business_id ,type);
    }

    //修改模板5
    @Override
    @Transactional
    public Integer updateTemplateFive(Integer id, String title, String one, Integer state ,int barcode) {
        return shopMapper.updateTemplateFive(id , title  , one ,state ,barcode);
    }

    //查询店铺模板4集合
    @Override
    public List<Template> selectTemplateFourList(String token, Integer role) {
        Integer business_id = shopMapper.shopipByTokenAndRole(token, role);
        return shopMapper.selectTemplateFourList(business_id);
    }

    //根据类型查询店铺模板4
    @Override
    public Template selectTemplateFour(String token, Integer role, Integer type) {
        Integer business_id = shopMapper.shopipByTokenAndRole(token, role);
        return shopMapper.selectTemplateFour(business_id ,type);
    }

    //修改模板4
    @Override
    @Transactional
    public Integer updateTemplateFour(Integer id, String title, String one, Integer state,int barcode) {
        return shopMapper.updateTemplateFour(id ,title , one ,state ,barcode);
    }

    //查询店铺模板6集合
    @Override
    public List<Template> selectTemplateSixList(String token, Integer role) {
        Integer business_id = shopMapper.shopipByTokenAndRole(token, role);
        return shopMapper.selectTemplateSixList(business_id);
    }

    //根据类型查询店铺模板6
    @Override
    public Template selectTemplateSix(String token, Integer role, Integer type) {
        Integer business_id = shopMapper.shopipByTokenAndRole(token, role);
        return shopMapper.selectTemplateSix(business_id ,type);
    }

    //修改模板6
    @Override
    @Transactional
    public Integer updateTemplateSix(Integer id, String title, String one, Integer state,int barcode) {
        return shopMapper.updateTemplateSix(id ,title , one ,state ,barcode);
    }
}
