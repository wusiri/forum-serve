package wlong.work.forumserve.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 云存储配置类
 */
@Data
@Configuration
public class CloudStorageConfig {


    /**
     * 七牛域名
     */
    @Value("${OSS.path}")
    private String path;
    /**
     * 七牛ACCESS_KEY
     */
    @Value("${OSS.accessKey}")
    private String qiniuAccessKey;
    /**
     * 七牛SECRET_KEY
     */
    @Value("${OSS.secretKey}")
    private String qiniuSecretKey;
    /**
     * 七牛空间名
     */
    @Value("${OSS.bucketName}")
    private String qiniuBucketName;
}
