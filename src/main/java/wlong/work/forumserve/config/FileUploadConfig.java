package wlong.work.forumserve.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class FileUploadConfig implements WebMvcConfigurer {

    @Value("${forum.path}")
    private String path;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html", "doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/META_INF/resources/")
                .addResourceLocations(path);

    }
}
