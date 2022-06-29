package com.insigma.dto;

import java.awt.print.Pageable;
import java.io.Serializable;

/**
 * @ClassName BaseQuery
 * @Description
 * @Author carrots
 * @Date 2022/6/29 15:43
 * @Version 1.0
 */
public class BaseQuery implements Serializable {
    private Long createTimeStart;
    private Long createTimeEnd;
    private Integer startIndex;
    private Pageable page;

    public int getStartIndex() {
        if (null != page) {
            this.startIndex = page.getNumberOfPages() * page.getSize();
        } else {
            this.startIndex = 0;
        }
        return startIndex;
    }
}
