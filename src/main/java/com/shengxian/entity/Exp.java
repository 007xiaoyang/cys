package com.shengxian.entity;

/**
 * Description:
 *
 * @Author: yang
 * @Date: 2018-11-25
 * @Version: 1.0
 */
public class Exp {

    private Expense[] expenses;

    private Give[] gives;//赠送类

    private Recommend[] recommends;//推荐产品类

    private Limited[] limiteds; //限时类

    private Menu[] ontMenus; //一级菜单

    private Menu[] twoMenus; //二级菜单

    private Menu[] threeMenus; //三级菜单

    public Expense[] getExpenses() {
        return expenses;
    }

    public void setExpenses(Expense[] expenses) {
        this.expenses = expenses;
    }

    public Give[] getGives() {
        return gives;
    }

    public void setGives(Give[] gives) {
        this.gives = gives;
    }

    public Recommend[] getRecommends() {
        return recommends;
    }

    public void setRecommends(Recommend[] recommends) {
        this.recommends = recommends;
    }

    public Limited[] getLimiteds() {
        return limiteds;
    }

    public void setLimiteds(Limited[] limiteds) {
        this.limiteds = limiteds;
    }

    public Menu[] getOntMenus() {
        return ontMenus;
    }

    public void setOntMenus(Menu[] ontMenus) {
        this.ontMenus = ontMenus;
    }

    public Menu[] getTwoMenus() {
        return twoMenus;
    }

    public void setTwoMenus(Menu[] twoMenus) {
        this.twoMenus = twoMenus;
    }

    public Menu[] getThreeMenus() {
        return threeMenus;
    }

    public void setThreeMenus(Menu[] threeMenus) {
        this.threeMenus = threeMenus;
    }
}
