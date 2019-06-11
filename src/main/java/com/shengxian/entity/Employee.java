package com.shengxian.entity;

import java.io.Serializable;

/**
 * Description:
 *
 * @Author: yang
 * @Date: 2018-12-26
 * @Version: 1.0
 */
public class Employee implements Cloneable ,Serializable {


    private static final  long serialVersionUID = 1L;
    private String name;

    public Employee() {
        System.out.println("Employee Constructor Called....");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int hashCode(String name){
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    public boolean equlas(Object obj){
        if(this == obj){
            return true;
        }
        if(obj == null){
            return false;
        }
        if (getClass() != obj.getClass()){
            return false;
        }
        Employee other = (Employee) obj;
        if ( name == null){
            if (other.name != null){
                return false;
            }
        }else if (!name.equals(other.name))
            return false;
        return true;

    }

    public String toString(){
        return "Employee [name+"  + name + "}";
    }

}
