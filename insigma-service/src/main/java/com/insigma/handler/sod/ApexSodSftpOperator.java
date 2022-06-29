package com.insigma.handler.sod;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.security.PrivateKey;

/**
 * @ClassName ApexSodSftpOperator
 * @Description
 * @Author carrots
 * @Date 2022/6/28 16:28
 * @Version 1.0
 */
@Component
public class ApexSodSftpOperator implements InitializingBean {

    private SftpHolder sftpHolder = null;

    @Value("${apex.sftp.host:}")
    private String host;

    @Override
    public void afterPropertiesSet() throws Exception {

        if (StringUtils.isNotBlank(PrivateKey)) {
            this.sftpHolder = new SftpHolder(this.username, this.host, this.port, this.privateKey);
            return;
        }
        this.sftpHolder = new sftpHolder(this.username, this.password, this.host, this.port);
    }
}
