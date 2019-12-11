package com.shengxian.service.impl;

import com.google.common.collect.Lists;
import com.shengxian.common.util.ExcelUtil;
import com.shengxian.common.util.GroupNumber;
import com.shengxian.common.util.IntegerUtils;
import com.shengxian.common.util.Page;
import com.shengxian.entity.Parameter;
import com.shengxian.entity.Suppliers;
import com.shengxian.mapper.ShopMapper;
import com.shengxian.mapper.SuppliersMapper;
import com.shengxian.service.SuppliersService;
import com.shengxian.vo.SuppliersCategoryVO;
import com.shengxian.vo.SuppliersVO;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Description:
 *
 * @Author: yang
 * @Date: 2018/8/22
 * @Version: 1.0
 */
@Service
public class SuppliersServiceImpl implements SuppliersService {

    @Resource
    private SuppliersMapper suppliersMapper;

    @Resource
    private ShopMapper shopMapper;

    //添加供应商类别信息
    @Override
    @Transactional
    public Integer addSupplierCategory(String token ,Integer role , String name) {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);

        String code  = suppliersMapper.findCodeBySid(bid);
        if (code == null){
            code="1";
        }else {
            code = GroupNumber.getNumber(Integer.parseInt(code));
        }
        return suppliersMapper.addSupplierCategory(bid,name,code);
    }

    //查询供应商类别信息集合
    @Override
    public List<HashMap> findSuppliersCategoryInfoList(String token ,Integer role)throws NullPointerException {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);

        return suppliersMapper.findSuppliersCategoryInfoListBySid(bid);
    }

    //修改供应商类别信息
    @Override
    @Transactional
    public Integer updateSuppliersCategory(Integer id, String name) {
        return suppliersMapper.updateSuppliersCategory(id,name);
    }

    //删除供应商类别
    @Override
    @Transactional
    public Integer deleSuppliersCategory(Integer id)throws NullPointerException {

        //通过类别id查询该类别下是否有供应商
        List<HashMap> suppliers = suppliersMapper.findSuppliersList(id);

        if (suppliers.size() > 0){
            throw new NullPointerException("该类别中还有供应商存在，不能删除该类别");
        }
        return suppliersMapper.deleSuppliersCategory(id);
    }

    @Override
    public String selectNumberByCategoryId(Integer id) throws Exception{
        String num = suppliersMapper.findNumberByCategoryId(id);
        if (num == null){
            num = suppliersMapper.findCodeByid(id) +"001";
        }else {
            num = GroupNumber.getNumber(Integer.parseInt(num));
        }
        return num;
    }

    //添加供应商信息
    @Override
    @Transactional
    public Integer addSuppliersInfo(String token ,Integer role , Suppliers suppliers)throws NullPointerException {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);

        //查询该类别id是否存在
        String categoryid = suppliersMapper.findCodeByid(suppliers.getCategory_id());
        if (categoryid == null){
            throw new NullPointerException("该类别id不存在");
        }
        Integer number = suppliersMapper.findIdByCidAndNumber(suppliers.getCategory_id(), suppliers.getNumber());
        if (number != null){
            throw new NullPointerException("该编号已存在");
        }
        suppliers.setBusiness_id(bid);
        suppliers.setCreate_time(new Date());

        //添加供应商信息
        return suppliersMapper.addSuppliersInfo(suppliers);
    }

    //查询类别下的供应商信息集合
    @Override
    public Page findSuppliersInfoList(String token  ,Integer role, Integer pageNo, Integer id, String number, String name)throws NullPointerException {
        Integer bid = shopMapper.businessIdByToken(token);
        if (bid == null) {
            throw new NullPointerException("您的账号登录失效或在另一台设备登录");
        }
        int pageNum = 1;
        if (IntegerUtils.isNotEmpty(pageNo)){
            pageNum=pageNo;
        }
        //条件查询类别下的供应商总数
        Integer totalCount = suppliersMapper.findSuppliersInfoListCount(new Parameter(bid,id,number,name));
        Page page = new Page(pageNum,totalCount);

        //条件查询类别下的供应商
        List<HashMap> suppliers = suppliersMapper.findSuppliersInfoList(new Parameter(bid,page.getStartIndex(),page.getPageSize(),id,number,name));
        page.setRecords(suppliers);
        return page;
    }

    //查询供应商信息
    @Override
    public HashMap findSuppliersInfoById(Integer id) {
        return suppliersMapper.findSuppliersInfoById(id);
    }

    //修改供应商信息
    @Override
    @Transactional
    public Integer updateSuppliersInfoById(Suppliers suppliers)throws NullPointerException ,Exception {
        //通过供应商id，查询供应商信息
        HashMap hashMap = suppliersMapper.findSuppliersInfoById(suppliers.getId());

       //判断编号是否变动
        if (!hashMap.get("number").toString().equals(suppliers.getNumber()) ){
            //根据类别id和编号查询是否存在
            Integer count = suppliersMapper.findIdByCidAndNumber(suppliers.getCategory_id(), suppliers.getNumber());
            if (count != null){
               throw new NullPointerException("当前类别下的编号已存在");
            }
        }
        return suppliersMapper.updateSuppliersInfo(suppliers);
    }

    //删除供应商
    @Override
    @Transactional
    public Integer deleSuppliers(Integer id) throws NullPointerException ,Exception{
        //删除供应商前判断是否有未付款或欠款的订单
        List<Integer> pid = suppliersMapper.selectPurchaseOrder(id);
        if (pid.size() > 0){
            throw new NullPointerException("还有未支付或欠款的采购订单,故不能删除");
        }
        return suppliersMapper.deleSuppliers(id);
    }

    //供应商导出
    @Override
    public HSSFWorkbook excelDownload(String token ,Integer role, Parameter parameter)throws NullPointerException {

        Integer bid = shopMapper.businessIdByToken(token);
        if (bid == null) {
            throw new NullPointerException("您的账号登录失效或在另一台设备登录");
        }
        String sheetName = "供应商资料";//sheet名
        String[] title = new String[]{"供应商类别", "供应商编号", "供应商名称","创建时间"};//标题
        parameter.setBusiness_id(bid);
        List<HashMap> hashMaps = suppliersMapper.excelDownload(parameter);
        String[][] values = new String[hashMaps.size()][];
        for (int i = 0; i < hashMaps.size(); i++) {
            //标签长度和数据长度要一致
            values[i] = new String[title.length];
            HashMap hashMap = hashMaps.get(i);
            values[i][0] = hashMap.get("categoryName").toString();
            values[i][1] = hashMap.get("number").toString();
            values[i][2] = hashMap.get("name").toString();
            values[i][3] = hashMap.get("create_time").toString();
        }
        HSSFWorkbook workbook = ExcelUtil.getHSSWorkbook(sheetName, title, values, null);
        return workbook;
    }


    //通过类别id查询类别下的供应商集合
    @Override
    public List<HashMap> findSuppliersList(Integer id) {
        return suppliersMapper.findSuppliersList(id);
    }

    @Override
    public List<SuppliersCategoryVO> getSuppliersList(String token, Integer role) throws Exception {
        //获取店铺id
        Integer businessId = shopMapper.shopipByTokenAndRole(token, role);

        //获取店铺所有的供应商类别
        List<SuppliersCategoryVO> categoryList = suppliersMapper.getSuppliersCategoryList((long) businessId);
        //获取店铺所有的供应商列表
        List<SuppliersVO> suppliersList = suppliersMapper.getSuppliersList(((long) businessId));

        categoryList.forEach(categor ->{

            List<SuppliersVO> suppliersVO  = Lists.newArrayList();

            suppliersList.forEach(suppliers -> {
                if(categor.getId().equals(suppliers.getCategoryId())){
                    suppliersVO.add(suppliers);
                }
            });
            suppliersList.removeAll(suppliersVO);
            categor.setChildren(suppliersVO);
        });

        return categoryList;
    }
}
