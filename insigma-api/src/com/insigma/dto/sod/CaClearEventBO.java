package com.insigma.dto.sod;

import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName CaClearEventBO
 * @Description
 * @Author carrots
 * @Date 2022/6/28 16:42
 * @Version 1.0
 */
@Data
public class CaClearEventBO {
    private List<StockTransDtlTmp> stockTransDtlTmps = new ArrayList<>();

    private String time;
    @Data
    public static class PendingSettleChange{
        private String serialNo;
    }

    public List<StockTransDtlTmp> getStockTransDtl() {
        if (CollectionUtils.isEmpty(this.stockTransDtlTmps)) {
            return this.stockTransDtlTmps;
        }
        if (this.time == null) {
            return null;
        } else if (this.time.compareTo((new Date()).toString()) > -1) {
            return null;
        } else {
            return null;
        }
    }

}
