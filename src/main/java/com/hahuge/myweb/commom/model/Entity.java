package com.hahuge.myweb.commom.model;

import java.io.Serializable;


public interface  Entity<ID extends Serializable> extends Serializable{
    
    ID getId();
    
    void setId(ID id);

}
