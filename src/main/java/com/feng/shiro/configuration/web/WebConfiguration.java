package com.feng.shiro.configuration.web;

import com.feng.shiro.api.filter.AllowOriginFilter;
import com.feng.shiro.constant.FilePathConstant;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.FormContentFilter;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.IOException;

/**
 * @Description web配置类
 * @Author chenlinghong
 * @Date 2019/3/29 18:31
 **/
@Configuration
@EnableWebMvc
@ComponentScan("com.feng.shiro")
public class WebConfiguration extends WebMvcConfigurerAdapter {

    /**
     * 上传文件解析器
     *
     * @return
     * @throws IOException
     */
    @Bean
    public CommonsMultipartResolver multipartResolver() throws IOException {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setDefaultEncoding("utf-8");
        resolver.setUploadTempDir(new FileSystemResource(FilePathConstant.TEMP_DIR));
        resolver.setMaxInMemorySize(0);
        // 单个文件限制300M
        resolver.setMaxUploadSizePerFile(300 * 1024 * 1024L);
        // 限制1G
        resolver.setMaxUploadSize(1024 * 1024 * 1024L);
        return resolver;
    }

    /**
     * 解决DELETE、PUT参数获取不到
     *
     * @return
     */
    @Bean
    public FormContentFilter formContentFilter() {
        return new FormContentFilter();
    }



    /**
     * 注入RestTemplate
     *
     * @return
     */
    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    /**
     * 跨域Fileter注入
     *
     * @return 跨域注册Bean对象
     */
    @Bean
    public FilterRegistrationBean registerFilter() {
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.addUrlPatterns("/*");
        bean.setFilter(new AllowOriginFilter());
        return bean;
    }

}
