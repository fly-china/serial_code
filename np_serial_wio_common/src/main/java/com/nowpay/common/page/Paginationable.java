package com.nowpay.common.page;

import java.io.Serializable;

/**
 * 分页对象
 */
public abstract class Paginationable implements Serializable {

	private static final long serialVersionUID = 4779158739497015716L;

    /**
     * 当前页
     */
    private int currentPage;

    /**
     * 每页大小
     */
	private int pageSize;

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
