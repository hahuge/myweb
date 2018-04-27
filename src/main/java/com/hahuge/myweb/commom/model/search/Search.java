package com.hahuge.myweb.commom.model.search;
import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;

/**
 * 
 * @project iapp_student
 * @date    2014年3月7日-下午7:53:05
 * @author  songsp
 * 
 * @see SetchTest
 */
public class Search {
    
    @SuppressWarnings("unchecked")
    protected List<Filter> filters = Collections.EMPTY_LIST;
    
    @SuppressWarnings("unchecked")
    protected List<Sort> sorts = Collections.EMPTY_LIST;
    
    @SuppressWarnings("unchecked")
    protected List<String> fields = Collections.EMPTY_LIST;
    
    protected boolean distinct;
    
    public static final Search EMPTY = new Search();
    
    
    public  Search() {
        
    }
    public static Search  newInstance(){
        return new Search();
    }
    
    public Search addEqualFilter(String property,Object value){
        return addFilter(Filter.equal(property, value));
    }
    public Search addFilter(Filter filter){
        
        if(filters.isEmpty()){
            filters = Lists.newArrayList();
        }
        filters.add(filter);
        return this;
    }
    
    public Search addSort(Sort sort){
        
        if(sorts.isEmpty()){
            sorts = Lists.newArrayList();
        }
        sorts.add(sort);
        
        
        return this;
    }
    
    public Search addField(String field){
      if(fields.isEmpty()){  
          fields = Lists.newArrayList();
      }
        
      fields.add(field);
      
      return this;
        
    }


    
    public boolean isDistinct() {
        return distinct;
    }


    
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }


    
    public List<Filter> getFilters() {
        return Collections.unmodifiableList(filters);
    }


    
    public List<Sort> getSorts() {
        return Collections.unmodifiableList(sorts);
    }


    
    public List<String> getFields() {
        return Collections.unmodifiableList(fields);
    }


    

}