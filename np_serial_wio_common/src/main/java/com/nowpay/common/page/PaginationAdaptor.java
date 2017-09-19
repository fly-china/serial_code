package com.nowpay.common.page;


import com.nowpay.common.page.plugin.domain.PageList;

/**
 * 分页适配器
 * User: 韩彦伟
 * Date: 14-2-15
 * Time: 下午7:10
 */
public class PaginationAdaptor {

    public static DataTableData convert(PageList pageList, String sEcho){
        return new DataTableData(pageList.getPaginator().getTotalCount(),pageList.getPaginator().getTotalCount(),sEcho,pageList);
    }
}
