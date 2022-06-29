package com.insigma.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @ClassName PageQuery
 * @Description
 * @Author carrots
 * @Date 2022/6/29 15:33
 * @Version 1.0
 */
@Data
@AllArgsConstructor
public class PageQuery {
    Integer page;
    Integer size;
    Integer offset;

    public PageQuery(int page, int size) {
        this.page = page;
        this.size = size;
    }

    public boolean isValid() {
        return page == null || page <= 0 || size == null || size <= 0;
    }

    public void setOffset() {
        this.offset = (this.page - 1) * this.size;
    }
}
