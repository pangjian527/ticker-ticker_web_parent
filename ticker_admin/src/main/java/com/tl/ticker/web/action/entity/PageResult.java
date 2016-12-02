package com.tl.ticker.web.action.entity;

/**
 * Created by pangjian on 16-12-2.
 */
public class PageResult {

    //总行数
    private int totalCount;

    //每一页的数量
    private int limit;

    private int currentOffset ;

    private int nextOffset;

    public PageResult(int totalCount,int limit,int currentOffset){
        this.totalCount = totalCount;
        this.limit = limit;
    }

    /* 获取分页的总页数 */
    public int getPageCount(){
        return (int)Math.ceil(totalCount/(limit*1.0));
    }

    //显示的页数
    public int getLoopCount(){
        int pageCount = this.getPageCount();

        if(pageCount >=5){
            return 5;
        }else{
            return pageCount;
        }
    }

    public int getNextOffset(){
        return currentOffset + limit;
    }

}
