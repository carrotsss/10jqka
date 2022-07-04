package com.insigma.config.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * @ClassName BasicDataSourceConfig
 * @Description
 * @Author carrots
 * @Date 2022/6/30 16:26
 * @Version 1.0
 */
@Configuration
@MapperScan(basePackages = "com.myhexin.basic.mapper", sqlSessionFactoryRef = "basicSqlSessionFactory")
public class BasicDataSourceConfig {
    @Bean("basicDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.basic")
    public DataSource newsDataSource() {
        return new DruidDataSource();
    }

    @Bean
    @Primary
    public SqlSessionFactory basicSqlSessionFactory(@Qualifier("basicDataSource") DataSource dataSource) throws Exception {
        MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        bean.setMapperLocations(resolver.getResources("classpath*:mapper/basic*/*.xml"));
        bean.setTypeAliasesPackage("com.myhexin.basic.dto");
        return bean.getObject();
    }

    @Bean("basicTransactionManager")
    @Primary
    public DataSourceTransactionManager basicTransactionManager(@Qualifier("basicDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean("basicSqlSessionTemplate")
    @Primary
    public SqlSessionTemplate basicSqlSessionTemplate(@Qualifier("basicSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
