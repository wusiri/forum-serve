package wlong.work.forumserve.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import wlong.work.forumserve.domain.Comment;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wl
 * @since 2022-11-07
 */
public interface CommentService extends IService<Comment> {

    List<Comment> getByArticleID(Integer articeId);

    boolean updateGoodNum(int commentGoodNum, Integer commentId);

    Page<Comment> getPage(Integer page, Integer pageSize, String name);
}
