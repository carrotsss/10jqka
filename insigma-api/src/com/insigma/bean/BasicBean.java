package com.insigma.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName BasicBean
 * @Description TODO
 * @Author carrots
 * @Date 2022/6/16 10:24
 * @Version 1.0
 **/
@Data
public class BasicBean implements Serializable {
    private Long id;
    private Date createTime;
    private Date updateTime;
    private Integer inValid;
}
