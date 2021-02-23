package com.xiaowei.entity;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class PageResponse<T> {
    private int pageNum;
    private int pageSize;
    private int totalPage;
    private List<T> data;

    public static void main(String[] args) {
        Integer[] arr={1,2,3};
        Arrays.sort(arr,new Comparator<Integer>(){
            public int compare(Integer o1, Integer o2) {
                return o2-o1;
            }
        });
        Arrays.stream(arr).forEach(e-> System.out.println(e));
    }

    public PageResponse() {

    }

    public static<T>PageResponse<T> pageData(int pageNum,int pageSize,int totalPage,List<T> data){
        PageResponse<T> result=new PageResponse<>();
        result.setPageNum(pageNum);
        result.setPageSize(pageSize);
        result.setTotalPage(totalPage);
        result.setData(data);
        return result;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
