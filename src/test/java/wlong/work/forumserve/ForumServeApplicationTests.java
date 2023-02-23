package wlong.work.forumserve;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import wlong.work.forumserve.common.CustomException;
import wlong.work.forumserve.controller.UserController;
import wlong.work.forumserve.dao.CommentDao;
import wlong.work.forumserve.domain.Article;
import wlong.work.forumserve.domain.User;
import wlong.work.forumserve.service.*;
import wlong.work.forumserve.utils.FileUtil;

import java.awt.*;
import java.io.File;
import java.util.Arrays;
import java.util.List;

import static wlong.work.forumserve.utils.FontChangeImageUtils.createImage;

@SpringBootTest
@Slf4j
class ForumServeApplicationTests {

    @Value("${forum.path}")
    private String basePath;

    @Autowired
    private UploadImageService uploadImageService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private FansService fansService;

    @Autowired
    private CommunityService communityService;

    @Test
    void contextLoads() {

    }

    @Test
    public void delete(){}

    @Test
    public void updateUser(){
        User user=new User();
        user.setId(9);
        user.setNickname("hhh");
        user.setCity("陕西省-西安市");
        user.setGender("女");
        user.setIntroduce("铁憨憨");
        userService.updateUserinfo(user);
    }

    @Test
    void updateInfo(){
        User user=new User();
        user.setCity("陕西省-西安市");
        user.setId(3);
        userService.updateUserinfo(user);
    }

    @Test
    public void testRedis(){
        // 设置字符串序列化器，使 key 在 Redis 中以字符串形式显示，否则会出现十六进制代码，不方便查看
        this.redisTemplate.setKeySerializer(new StringRedisSerializer());

        List<String> list = Arrays.asList(new String[]{"zcf1", "zcf2", "zcf3", "zcf4"});
        this.redisTemplate.opsForValue().set("test-list", list);

        List<String> result = (List<String>) this.redisTemplate.opsForValue().get("test-list");
        System.out.println(result);
    }

    @Test
    public void testFans(){
        fansService.getUserId(1);
    }


    @Test
    public void Font(){
        Article article=new Article();
        article.setArticleImage(null);
        article.setArticleSummary("Java实现文字转图片");
        //判断是否有封面
        if (article.getArticleImage() == null) {
            try {
                createImage(article.getArticleSummary(), new Font("微软雅黑", Font.PLAIN, 64), new File(basePath + "/a.png"), 400, 300);
                File file = new File(basePath + "/a.png");
                FileItem multipartFile = FileUtil.getMultipartFile(file, "a.png");
                MultipartFile file1 = new CommonsMultipartFile(multipartFile);
                String path = uploadImageService.uploadQNImg(file1);
                log.info("七牛云返回的图片链接:{}", path);
                //链接处理
                int i = path.indexOf("?");
                String paths = "http://" + path.substring(0, i).replace("%2F", "/");
                log.info("处理后的链接:{}", paths);
                article.setArticleImage(paths);
            } catch (Exception e) {
                throw new CustomException("失败");
            }
        }
    }

}
