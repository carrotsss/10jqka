package com.insigma.dto.ainvestdb.common;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName IncrSyncOffset
 * @Description
 * @Author carrots
 * @Date 2022/6/20 14:48
 * @Version 1.0
 */
@Data
public class IncrSyncOffset {
    @TableId
    private Long id;

    private Date createTime;
    private Date updateTime;
    private String typeName;
    private String offset;
}
