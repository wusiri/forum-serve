package wlong.work.forumserve.controller;


import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import sun.nio.ch.IOUtil;
import wlong.work.forumserve.common.CustomException;
import wlong.work.forumserve.common.R;
import wlong.work.forumserve.domain.Article;
import wlong.work.forumserve.domain.Comment;
import wlong.work.forumserve.domain.Like;
import wlong.work.forumserve.domain.User;
import wlong.work.forumserve.service.ArticleService;
import wlong.work.forumserve.service.LikeService;
import wlong.work.forumserve.service.UploadImageService;
import wlong.work.forumserve.utils.FileUtil;

import javax.annotation.Resource;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static wlong.work.forumserve.utils.FontChangeImageUtils.createImage;
import static wlong.work.forumserve.utils.FontChangeImageUtils.inputStreamToBytes;

/**
 * <p>
 * 前端控制器 文章
 * </p>
 *
 * @author wl
 * @since 2022-11-07
 */
@Api(value = "文章Controller", tags = {"文章访问接口"})
@RestController
@RequestMapping("/article")
@Slf4j
public class ArticleController {

    @Value("${forum.path}")
    private String basePath;

    @Resource
    private ArticleService articleService;


    @Resource
    private LikeService likeService;

    @Resource
    private UploadImageService uploadImageService;

    @ApiOperation("获取当天文章数量")
    @GetMapping("/getNowTime")
    public R<Integer> getNowTime() {
        String now = String.valueOf(LocalDateTime.now()).split("T")[0];
        log.info("{}", now);
        int size = articleService.list(Wrappers.lambdaQuery(Article.class).like(Article::getCreateTime, now)).size();
        return R.success(size);
    }

    @ApiOperation("获取当天问题相关数量")
    @GetMapping("/getNowTime/{id}")
    public R<Integer> getNowTimeSate(@PathVariable("id") Integer id) {
        String now = String.valueOf(LocalDateTime.now()).split("T")[0];
        log.info("{}", now);
        int size = articleService.list(Wrappers.lambdaQuery(Article.class).eq(Article::getAuditState, id).like(Article::getCreateTime, now)).size();
        return R.success(size);
    }


    @ApiOperation("获取文章信息")
    @GetMapping("/getPage")
    public R<Page<Article>> getPage(Integer page, Integer pageSize, String name) {
        log.info("{},{},{}", page, pageSize, name);
        Page<Article> page1 = articleService.getPage(page, pageSize, name);
        return R.success(page1);
    }

    @ApiOperation("获取文章信息列表")
    @GetMapping("/getList")
    public R<List<Article>> getList() {
        List<Article> list = articleService.list();
        return R.success(list);
    }

    @ApiOperation("发布文章")
    @PostMapping("/publish")
    public R<String> publish(@RequestBody Article article) {
        //判断是否有封面
        if (article.getArticleImage().equals("null")) {
            try {
                createImage(article.getArticleTitle(), new Font("微软雅黑", Font.PLAIN, 48), new File(basePath + "/a.png"), 400, 300);
                File file = new File(basePath + "/a.png");
                FileItem multipartFile = FileUtil.getMultipartFile(file, "a.png");
                MultipartFile file1 = new CommonsMultipartFile(multipartFile);
                String path = uploadImageService.uploadQNImg((file1));
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
        boolean save = articleService.save(article);
        if (save) {
            return R.success("发布成功!!!");
        } else {
            return R.success("发布失败");
        }
    }

    @ApiOperation("上传封面")
    @PutMapping("/saveImg")
    public R<String> updatePhoto(@RequestParam("id") Integer id, @RequestParam("file") MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            String path = uploadImageService.uploadQNImg(file);
            log.info("七牛云返回的图片链接:{}", path);
            //链接处理
            int i = path.indexOf("?");
            String paths = "http://" + path.substring(0, i).replace("%2F", "/");
            log.info("处理后的链接:{}", paths);
            /*qiniu.wlong.work/code%2Fduck%2F20230113-850f938c035746e1ab1e112548e739fa.jpg
                ?e=1673605941&token=IaXM0rTcHF4rF__5qn9iuWhHqJgW3XaCXSb1FsAV:v7bOVgb9BsrhPXChymf7exlClck=
                http://qiniu.wlong.work/forum/v2-e0d294a91376a3527fc07a61dd111cfe_r.jpg*/

            return R.success(paths);
        }
        return R.error("上传失败");
    }


    @ApiOperation("保存文章中的图片并返回地址")
    @PostMapping("/saveImg")
    public R<String> saveImg(@RequestParam("userId") Integer id, @RequestParam("image") MultipartFile image) {
        String contentType = image.getContentType();
        contentType = contentType.substring(contentType.indexOf("/") + 1);
        File file = new File(basePath);
        String imagerUrl = "";
        if (file.getParentFile() != null || !file.getParentFile().isDirectory()) {
            //创建文件夹
            file.getParentFile().mkdirs();
        }
        try {
            //将文件存放到本地
            image.transferTo(new File(basePath));
            imagerUrl = "http://localhost:8080/" + "User/" + "id_" + id + "/article" + LocalDateTime.now() + "_." + contentType;
        } catch (IOException e) {
            throw new CustomException("存放失败");
        }
        return R.success(imagerUrl);
    }

    @ApiOperation("获取文章与问题")
    @GetMapping("/getArticle/{articleTypeId}")
    public R<List<Article>> getArticle(@PathVariable("articleTypeId") Integer articleTypeId) {
        List<Article> list = null;
        if (articleTypeId == 0) {
            //获取全部
            list = articleService.list();
        } else {
            list = articleService.getArticle(articleTypeId);

        }
        return R.success(list);
    }


    @ApiOperation("更新评论数")
    @PutMapping("/addComment")
    public R<String> addComment(@RequestParam("articleCommentNum") Integer articleCommentNum, @RequestParam("articleId") Integer articleId) {
        LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(Article::getArticleCommentNum, articleCommentNum).eq(Article::getArticleId, articleId);
        boolean update = articleService.update(updateWrapper);
        if (update) {
            return R.success("更新成功");
        } else {
            return R.success("更新失败");
        }
    }


    @ApiOperation("更新浏览数")
    @PutMapping("/addView")
    public R<String> addView(@RequestParam("articleViewNum") Integer articleViewNum, @RequestParam("articleId") Integer articleId) {
        LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(Article::getArticleViewNum, articleViewNum).eq(Article::getArticleId, articleId);
        boolean update = articleService.update(updateWrapper);
        if (update) {
            return R.success("更新成功");
        } else {
            return R.success("更新失败");
        }
    }

    @ApiOperation("更新点赞数")
    @PutMapping("/addGood")
    public R<String> addGood(@RequestParam("type") Boolean isDisplay, @RequestParam("articleId") Integer articleId, @RequestParam("userId") Integer userId) {
        //获取点赞数量
        Integer articleGoodNum = articleService.getById(articleId).getArticleGoodNum();
        log.info("点赞数:{}", articleGoodNum);
        //isDisplay true表示点赞,false表示取消
        if (isDisplay) {
            //获取状态码
            Like like = likeService.getArticleLikeState(articleId, userId);
            if (like == null) {
                Like articleLike = new Like();
                articleLike.setArticleId(articleId);
                articleLike.setArticleLikeNum(1);
                articleLike.setUserId(userId);
                likeService.save(articleLike);
            } else {
                likeService.setArticleLikeState(articleId, userId, 1);
            }
            articleService.updateGoodNum(articleGoodNum + 1, articleId);

        } else {
            likeService.setArticleLikeState(articleId, userId, 0);
            articleService.updateGoodNum(articleGoodNum - 1, articleId);
        }
        return R.success("操作成功");
    }


    @ApiOperation("设置问题的最佳答案")
    @PutMapping("/setGood")
    public R<String> setGood(Integer commentId, Integer articleId) {
        LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(Article::getCommentId, commentId).eq(Article::getArticleId, articleId);
        boolean update = articleService.update(updateWrapper);
        if (update) {
            return R.success("更新成功");
        } else {
            return R.success("更新失败");
        }
    }

    @ApiOperation("搜索")
    @GetMapping("search/{articleLabelId}")
    public R<List<Article>> search(@PathVariable("articleLabelId") Integer articleLabelId) {
        List<Article> list = articleService.list(Wrappers.lambdaQuery(Article.class).eq(Article::getArticleLabelId, articleLabelId).eq(Article::getAuditState, 1));
        return R.success(list);
    }

    @ApiOperation("获取单个文章")
    @GetMapping("/{articleId}")
    public R<Article> getById(@PathVariable("articleId") Integer articleId) {
        Article one = articleService.getById(articleId);
        return R.success(one);
    }

    @ApiOperation("获取hot榜")
    @GetMapping("/getHot")
    public R<List<Article>> getHot(@RequestParam("articleTypeId") Integer articleTypeId, @RequestParam("articleLabelId") Integer articleLabelId) {
        List<Article> hot = articleService.getHot(articleTypeId, articleLabelId);
        return R.success(hot);
    }

    @ApiOperation("获取社区hot榜")
    @GetMapping("/getHotByCommunity")
    public R<List<Article>> getHotByCommunity(@RequestParam("communityId") Integer communityId, @RequestParam("articleLabelId") Integer articleLabelId) {
        List<Article> list = null;
        if (articleLabelId == 0) {
            list = articleService.list(Wrappers.lambdaQuery(Article.class).eq(Article::getArticleCommunityId, communityId).eq(Article::getAuditState, 1).orderByDesc(Article::getArticleGoodNum));
        } else {
            list = articleService.list(Wrappers.lambdaQuery(Article.class).eq(Article::getArticleCommunityId, communityId).eq(Article::getArticleLabelId, articleLabelId).eq(Article::getAuditState, 1).orderByDesc(Article::getArticleGoodNum));
        }
        return R.success(list);
    }

    @ApiOperation("获取社区最新榜")
    @GetMapping("/getNewByCommunity")
    public R<List<Article>> getNewByCommunity(@RequestParam("communityId") Integer communityId, @RequestParam("articleLabelId") Integer articleLabelId) {
        List<Article> list = null;
        if (articleLabelId == 0) {
            list = articleService.list(Wrappers.lambdaQuery(Article.class).eq(Article::getArticleCommunityId, communityId).eq(Article::getAuditState, 1).orderByDesc(Article::getCreateTime));
        } else {
            list = articleService.list(Wrappers.lambdaQuery(Article.class).eq(Article::getArticleCommunityId, communityId).eq(Article::getAuditState, 1).eq(Article::getArticleLabelId, articleLabelId).orderByDesc(Article::getCreateTime));
        }
        return R.success(list);
    }

    @ApiOperation("获取社区推荐榜")
    @GetMapping("/getHeaderCommunity")
    public R<List<Article>> getHeaderCommunity(@RequestParam("communityId") Integer communityId, @RequestParam("articleLabelId") Integer articleLabelId) {
        List<Article> list = null;
        if (articleLabelId == 0) {
            list = articleService.list(Wrappers.lambdaQuery(Article.class).eq(Article::getArticleCommunityId, communityId).eq(Article::getAuditState, 1).eq(Article::getRecommend, 1).orderByDesc(Article::getCreateTime));
        } else {
            list = articleService.list(Wrappers.lambdaQuery(Article.class).eq(Article::getArticleCommunityId, communityId).eq(Article::getAuditState, 1).eq(Article::getArticleLabelId, articleLabelId).eq(Article::getRecommend, 1).orderByDesc(Article::getCreateTime));
        }
        return R.success(list);
    }


    @ApiOperation("获取最新")
    @GetMapping("/getNew")
    public R<List<Article>> getNew(@RequestParam("articleTypeId") Integer articleTypeId, @RequestParam("articleLabelId") Integer articleLabelId) {
        List<Article> aNew = articleService.getNew(articleTypeId, articleLabelId);
        return R.success(aNew);
    }

    @ApiOperation("获取指定类型问题")
    @GetMapping("/getIssue")
    public R<List<Article>> getIssueByState(@RequestParam("solveState") Integer solveState, @RequestParam("articleLabelId") Integer articleLabelId) {
        log.info("问题状态:{}", solveState);
        List<Article> list = null;
        if (articleLabelId == 0) {
            if (solveState == 0) {
                list = articleService.list(Wrappers.lambdaQuery(Article.class).eq(Article::getArticleTypeId, 2).eq(Article::getAuditState, 1).orderByDesc(Article::getCreateTime));
            } else {
                list = articleService.list(Wrappers.lambdaQuery(Article.class).eq(Article::getArticleTypeId, 2).eq(Article::getAuditState, 1).eq(Article::getSolveState, solveState).orderByDesc(Article::getCreateTime));
            }
        } else {
            list = articleService.list(Wrappers.lambdaQuery(Article.class).eq(Article::getArticleTypeId, 2).eq(Article::getSolveState, solveState).eq(Article::getAuditState, 1).eq(Article::getArticleLabelId, articleLabelId).orderByDesc(Article::getCreateTime));
        }
        return R.success(list);
    }

    @ApiOperation("获取顶部的推荐文章与问题")
    @GetMapping("/getHeader")
    public R<List<Article>> getHeader(@RequestParam("articleTypeId") Integer articleTypeId, @RequestParam("articleLabelId") Integer articleLabelId) {
        List<Article> header = articleService.getHeader(articleTypeId, articleLabelId);
        return R.success(header);
    }

    @ApiOperation("获取用户有关的")
    @GetMapping("/getByUser")
    public R<List<Article>> getByUser(@RequestParam("articleTypeId") Integer articleTypeId, @RequestParam("userId") Integer userId, @RequestParam("articleLabelId") Integer articleLabelId) {
        List<Article> list = articleService.getByUserAndType(articleTypeId, userId, articleLabelId);
        return R.success(list);
    }


    @ApiOperation("删除选中内容")
    @DeleteMapping()
    public R<String> deleteByIds(@RequestBody List<Integer> ids) {
        boolean remove = articleService.removeByIds(ids);
        if (remove) {
            return R.success("删除成功");
        } else {
            return R.success("删除失败");
        }
    }

    @ApiOperation("删除内容")
    @DeleteMapping("/delete/{id}")
    public R<String> deleteById(@PathVariable("id") Integer id) {
        boolean remove = articleService.removeById(id);
        if (remove) {
            return R.success("删除成功");
        } else {
            return R.success("删除失败");
        }
    }

    @ApiOperation("编辑内容")
    @PutMapping("/update")
    public R<String> update(@RequestBody Article article) {
        boolean update = articleService.update(Wrappers.lambdaUpdate(Article.class)
                .set(Article::getCommentId, article.getCommentId())
                .set(Article::getRecommend, article.getRecommend())
                .set(Article::getSolveState, article.getSolveState())
                .set(Article::getAuditState, article.getAuditState())
                .eq(Article::getArticleId, article.getArticleId()));

        if (update) {
            return R.success("修改成功");
        } else {
            return R.success("修改失败");
        }
    }


}

