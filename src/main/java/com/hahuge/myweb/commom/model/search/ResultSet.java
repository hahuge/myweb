package com.hahuge.myweb.commom.model.search;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ResultSet<T> implements Serializable {

    private static final long serialVersionUID = 3839265941196824806L;
    private Pageable          pageable;
    private List<T>           data;

    private ResultSet(){ 
    	
    }
    @SuppressWarnings("unchecked")
    public ResultSet(List<T> data, Pageable pageable, long total){
        if (null == data) {
            data = Collections.EMPTY_LIST;
        }
        this.data = data;
        this.pageable = pageable;
        this.pageable.setTotal(total);
    }

    public ResultSet(Pageable pageable){
        this(null, pageable,0);
    }

    public List<T> getData() {
        return Collections.unmodifiableList(data);
    }

    public int getPageNumber() {

        return pageable == null ? 0 : pageable.getPageNumber();
    }

    public int getPageSize() {

        return pageable == null ? 0 : pageable.getPageSize();
    }


    public long getTotal() {

        return pageable == null ? 0 : pageable.getTotal();
    }

   

    public Pageable getPageable() {
		return pageable;
	}

	@Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof ResultSet)) {
            return false;
        }

        ResultSet<?> that = (ResultSet<?>) obj;

        boolean contentEqual = this.data.equals(that.data);
        boolean pageableEqual = this.pageable == null ? that.pageable == null : this.pageable.equals(that.pageable);

        return  contentEqual && pageableEqual;
    }

    @Override
    public int hashCode() {

        int result = 17;
        result = 31 * result + (pageable == null ? 0 : pageable.hashCode());
        result = 31 * result + data.hashCode();

        return result;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE, false).concat(String.format("@hashCode=%s",
                                                                                                                      this.hashCode()));
    }
    
    @SuppressWarnings("unchecked")
	public static final <T > ResultSet<T> emptyResultSet() {
		return (ResultSet<T>) EMPTY_RESULTSET;
	}

    
    private static final EmptyResultSet EMPTY_RESULTSET = new EmptyResultSet();

	private static class EmptyResultSet extends ResultSet<Object> {
		private static final long serialVersionUID = 3791930334095887707L;
		private static final List<Object> items = Collections.emptyList();
		
		private static final Pageable page = new Pageable();
		@Override
		public List<Object> getData() {
			return EmptyResultSet.items;
		}
		@Override
		public Pageable getPageable() {
			return page;
		}
		@Override
		protected Object clone() throws CloneNotSupportedException {
			return super.clone();
		}
	}


}
