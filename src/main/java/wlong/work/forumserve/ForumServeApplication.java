package wlong.work.forumserve;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@MapperScan("wlong.work.forumserve.dao")
@EnableWebMvc
public class ForumServeApplication {

    public static void main(String[] args) {
        SpringApplication.run(ForumServeApplication.class, args);
    }

}
