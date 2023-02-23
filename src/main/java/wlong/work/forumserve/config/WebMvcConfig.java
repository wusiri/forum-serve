package wlong.work.forumserve.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import wlong.work.forumserve.common.JacksonObjectMapper;
import wlong.work.forumserve.handler.MyInterceptor;


import java.util.List;

@Slf4j
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

//    @Autowired
//    private MyInterceptor myInterceptor;

    /**
     * 设置拦截器
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("开始拦截");
//        registry.addInterceptor(myInterceptor).addPathPatterns("/admin/**").excludePathPatterns("/admin/login");
//        registry.addInterceptor(myInterceptor).addPathPatterns("/user/**").excludePathPatterns("/user/login");
    }

    /**
     * 设置静态资源映射
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("开始映射");
        /*registry.addResourceHandler("/**").addResourceLocations(
                "classpath:/static/");
        registry.addResourceHandler("swagger-ui.html").addResourceLocations(
                "classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations(
                "classpath:/META-INF/resources/webjars/");
        WebMvcConfigurer.super.addResourceHandlers(registry);*/
    }

    /**
     * 全局跨域处理
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {

        //设置允许跨越的路径
        registry.addMapping("/**")
                //设置允许跨域请求的域名
                .allowedOriginPatterns("*")
//                .allowedOrigins("*")
                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
                .allowCredentials(true)
                .allowedHeaders("*")
                .maxAge(3600);


    }

    /**
     * 扩展mvc框架的消息转换器
     *
     * @param converters
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("扩展消息转换器...");
        //创建消息转换器对象
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        //设置对象转换器，底层使用Jackson将Java对象转为json
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        //将上面的消息转换器对象追加到Mvc框架的转换器集合中,放到最前面位置，优先使用自己的
        converters.add(0, messageConverter);
    }


}
