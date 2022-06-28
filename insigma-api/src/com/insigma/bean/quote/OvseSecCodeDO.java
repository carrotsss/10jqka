package com.insigma.bean.quote;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import net.bytebuddy.implementation.bytecode.assign.TypeCasting;

import java.util.Date;

/**
 * @ClassName OvseSecCodeDO
 * @Description
 * @Author carrots
 * @Date 2022/6/28 10:17
 * @Version 1.0
 */
@Data
@Builder
@TableName("ovse_sec_code")
public class OvseSecCodeDO {
    private String secId;
    @JsonFormat(pattern = OvseDate.DEFAULT_DATE_TIME_FORMAT, timezone = "America/New_York")
    private Date createTime = new Date();

    @JsonFormat(pattern = OvseDate.DEFAULT_DATE_TIDE_FORMAT, timezone = "America/New_York")
    private Date modifyTime = new Date();
    private String state;
    private String type;
    private String symbol;
    private String cusip;
    private String isin;
    private String market;
    private String stockname;

}
