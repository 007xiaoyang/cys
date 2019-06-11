package com.shengxian.common.util;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

/**
 * 分页工具类
 * @author 陆晶晶
 */
public class Page {
    private List records;
    private int pageNum;//当前页码
    private int totalPageNum;//总页码
    private int prePageNum;//上一页
    private int nextPageNum;//下一页


    private int pageSize=10;//每页显示的记录条数
    private int totalRecordsNum;//总记录条数

    private int startIndex;//每页开始记录的索引

    private String url;//查询分页的请求的地址

    private BigDecimal price; //总金额
    private BigDecimal money; //每页金额
    private HashMap hashMap;
    private HashMap hashMap1;

    public Page(int pageNum) {
        this.pageNum = pageNum;
    }

    public Page(int pageNum, int totalRecordsNum){
        this.pageNum=pageNum;
        this.totalRecordsNum=totalRecordsNum;

        //计算总页码
        totalPageNum=totalRecordsNum%pageSize==0?totalRecordsNum/pageSize:totalRecordsNum/pageSize+1;
        //计算开始记录的索引
        startIndex=(pageNum-1)*pageSize;
    }

    public Page(int pageNum, int totalRecordsNum, int pageSize){
        this.pageNum=pageNum;
        this.totalRecordsNum=totalRecordsNum;
        this.pageSize=pageSize;
        //计算总页码
        totalPageNum=totalRecordsNum%pageSize==0?totalRecordsNum/pageSize:totalRecordsNum/pageSize+1;
        //计算开始记录的索引
        startIndex=(pageNum-1)*pageSize;
    }

    public List getRecords() {
        return records;
    }

    public void setRecords(List records) {
        this.records = records;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getTotalPageNum() {
        return totalPageNum;
    }

    public void setTotalPageNum(int totalPageNum) {
        this.totalPageNum = totalPageNum;
    }

    public int getPrePageNum() {
        prePageNum=pageNum-1;
        if(prePageNum<1){
            prePageNum=1;
        }
        return prePageNum;
    }

    public void setPrePageNum(int prePageNum) {
        this.prePageNum = prePageNum;
    }

    public int getNextPageNum() {
        nextPageNum=pageNum+1;
        if(nextPageNum>totalPageNum){
            nextPageNum=totalPageNum;
        }
        return nextPageNum;
    }

    public void setNextPageNum(int nextPageNum) {
        this.nextPageNum = nextPageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalRecordsNum() {
        return totalRecordsNum;
    }

    public void setTotalRecordsNum(int totalRecordsNum) {
        this.totalRecordsNum = totalRecordsNum;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public HashMap getHashMap() {
        return hashMap;
    }

    public void setHashMap(HashMap hashMap) {
        this.hashMap = hashMap;
    }

    public HashMap getHashMap1() {
        return hashMap1;
    }

    public void setHashMap1(HashMap hashMap1) {
        this.hashMap1 = hashMap1;
    }
}
