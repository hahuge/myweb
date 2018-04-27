package com.hahuge.myweb.commom.model.search;

import java.io.Serializable;

public class Pageable implements Serializable {

    private static final long serialVersionUID  = 1620221614733050774L;

    public static final int   DEFAULT_PAGE_SIZE = 20;
    
    public static final int   DEFAULT_PAGE_SIZE_TEN = 8;

    public static final int   FIRST_PAGE_NUMBER = 1;

    public static final int   UN_PAGE_SIZE      = -1;                  // 不分页

    private int               pageNumber        = FIRST_PAGE_NUMBER;   // 从1开始
    private int               pageSize          = DEFAULT_PAGE_SIZE;   // 默认20条 -1表示不分页

    private long              total             = 0;

    public Pageable(){

    }

    public Pageable(int pageNumber, int pageSize){

        this.pageNumber = pageNumber < 1 ? FIRST_PAGE_NUMBER : pageNumber;
        this.pageSize = pageSize;
    }

    public Pageable(int pageSize){
        this.pageSize = pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {

        return pageSize;
    }

    public int getPageNumber() {

        return pageNumber;
    }

    public int getOffset() {

        return (pageNumber - 1) * pageSize;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public int getTotalPages() {

        return getPageSize() == 0 ? 0 : (int) Math.ceil((double) getTotal() / (double) getPageSize());
    }

    public boolean isHasPreviousPage() {

        return getPageNumber() > 1;
    }

    public boolean isHasFirstPage() {

        return !isHasPreviousPage();
    }

    public boolean isHasNextPage() {

        return ((getPageNumber() + 1) * getPageSize()) < getTotal();
    }

    public boolean isLastPage() {

        return !isHasNextPage();
    }

    @Override
    public boolean equals(final Object obj) {

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Pageable)) {
            return false;
        }

        Pageable that = (Pageable) obj;

        boolean pageEqual = this.pageNumber == that.getPageNumber();
        boolean sizeEqual = this.pageSize == that.getPageSize();
        boolean totalEqual = this.total == that.getTotal();
        return pageEqual && sizeEqual && totalEqual;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + pageNumber;
        result = 31 * result + pageSize;
        result = 31 * result + (int) total;

        return result;
    }

    @Override
    public String toString() {
        return String.format("Pageable [pageNumber=%s, pageSize=%s,total=%s]", pageNumber, pageSize, total);
    }

}
