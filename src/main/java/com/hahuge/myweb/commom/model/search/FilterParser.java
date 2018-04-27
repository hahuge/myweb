package com.hahuge.myweb.commom.model.search;

import java.util.Map;


public interface FilterParser {
    static final int OP_AND              = Filter.OP_AND;
    static final int OP_OR               = Filter.OP_OR;
    
    String getConditionParamXql();
    Map<String, Object> getConditionParamValueMap() ;
}
