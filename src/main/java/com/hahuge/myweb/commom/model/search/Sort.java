package com.hahuge.myweb.commom.model.search;


public class Sort {
    protected String property;
    protected boolean desc = false;
    public Sort(String property, boolean desc) {
        this.property = property;
        this.desc = desc;
    }
    public Sort(String property) {
        this.property = property;
    }
    public static Sort asc(String property) {
        return new Sort(property);
    }

    public static Sort desc(String property) {
        return new Sort(property, true);
    }
    
    public String getProperty() {
        return property;
    }
    
    public boolean isDesc() {
        return desc;
    }
    
    
}
