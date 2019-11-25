package com.andy.core.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.andy.core.util.SpringUtil;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author andy_lai
 * @version 1.0
 * @date 2019/11/19 15:39
 */
@Configuration
public class CoreConfig {
    /**
     * FastJson 对象序列化工具
     * @return HttpMessageConverters
     */
    @Bean
    public HttpMessageConverters fastJsonHttpMessageConverts() {
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();

        // Spring Security 禁止MediaType.ALL带有通配符的配置
        List<MediaType> mediaTypes = Arrays.asList(
                MediaType.APPLICATION_JSON,
                MediaType.APPLICATION_JSON_UTF8,
                MediaType.APPLICATION_ATOM_XML,
                MediaType.APPLICATION_FORM_URLENCODED,
                MediaType.APPLICATION_OCTET_STREAM,
                MediaType.APPLICATION_PDF,
                MediaType.APPLICATION_RSS_XML,
                MediaType.APPLICATION_XHTML_XML,
                MediaType.APPLICATION_XML,
                MediaType.IMAGE_GIF,
                MediaType.IMAGE_JPEG,
                MediaType.IMAGE_PNG,
                MediaType.TEXT_EVENT_STREAM,
                MediaType.TEXT_HTML,
                MediaType.TEXT_MARKDOWN,
                MediaType.TEXT_PLAIN,
                MediaType.TEXT_XML
        );
        converter.setSupportedMediaTypes(mediaTypes);
        FastJsonConfig config = new FastJsonConfig();

        //todo 允许为null
        config.setSerializerFeatures(SerializerFeature.WriteEnumUsingToString);
        config.setSerializeFilters((ValueFilter) (o, s, source) -> {
            if (source == null) {
                return "";
            }
            return source;
        });
        converter.setFastJsonConfig(config);
        return new HttpMessageConverters(converter);
    }

    /**
     * Orika 对象拷贝工具
     * @return MapperFacade
     */
    @Bean
    public MapperFacade mapperFacade() {
        return new DefaultMapperFactory.Builder()
                .mapNulls(false)
                .build().getMapperFacade();
    }

    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder()
                // 连接超时
                .connectTimeout(30, TimeUnit.SECONDS)
                // 响应超时
                .readTimeout(30, TimeUnit.SECONDS)
                // 写超时
                .writeTimeout(30, TimeUnit.SECONDS)
                // 是否自动重连
                .retryOnConnectionFailure(true)
                // 连接池
                .connectionPool(new ConnectionPool())
                .build();
    }

    /**
     * SpringUtil 获取SpringApplicationContext
     * @return SpringUtil
     */
    @Bean
    public SpringUtil springUtil() {
        return new SpringUtil();
    }
}
