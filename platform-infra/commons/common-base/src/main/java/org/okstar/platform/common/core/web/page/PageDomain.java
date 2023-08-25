/*
 * * Copyright (c) 2022 船山科技 chuanshantech.com
 * OkEDU-Classroom is licensed under Mulan PubL v2.
 * You can use this software according to the terms and conditions of the Mulan
 * PubL v2. You may obtain a copy of Mulan PubL v2 at:
 *          http://license.coscl.org.cn/MulanPubL-2.0
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PubL v2 for more details.
 * /
 */

package org.okstar.platform.common.core.web.page;

import org.okstar.platform.common.core.utils.OkStringUtil;

/**
 * 分页数据
 *
 *
 */
public class PageDomain {
    /**
     * 当前记录起始索引
     */
    private Integer pageNum;

    /**
     * 每页显示记录数
     */
    private Integer pageSize;

    /**
     * 排序列
     */
    private String orderByColumn;


    /**
     * 排序的方向desc或者asc
     */
    private String order;
    private String isAsc = "asc";


    public static PageDomain build(int pageNum, int pageSize) {
        PageDomain domain = new PageDomain();
        domain.setPageNum(pageNum);
        domain.setPageSize(pageSize);
        return domain;
    }

    public String getOrderBy() {
        if (OkStringUtil.isEmpty(orderByColumn)) {
            return "";
        }
        return OkStringUtil.toUnderScoreCase(orderByColumn) + " " + isAsc;
    }

    public Integer getPageNum() {
        if (pageNum == null) return 1;
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        if (pageSize == null) return 10;
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getOrderByColumn() {
        return orderByColumn;
    }

    public void setOrderByColumn(String orderByColumn) {
        this.orderByColumn = orderByColumn;
    }

    public String getIsAsc() {
        return isAsc;
    }

    public void setIsAsc(String isAsc) {
        if (OkStringUtil.isNotEmpty(isAsc)) {
            // 兼容前端排序类型
            if ("ascending".equals(isAsc)) {
                isAsc = "asc";
            } else if ("descending".equals(isAsc)) {
                isAsc = "desc";
            }
            this.isAsc = isAsc;
        }
    }

    public String getOrderByClause() {
        if(OkStringUtil.isBlank(getOrderByColumn())|| OkStringUtil.isBlank(getOrder())){
            return null;
        }

        return getOrderByColumn() + " " + getOrder();
    }

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}
}
