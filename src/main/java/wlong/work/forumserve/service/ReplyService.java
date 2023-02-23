package wlong.work.forumserve.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import wlong.work.forumserve.domain.Reply;
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
public interface ReplyService extends IService<Reply> {

    List<Reply> getByCommentID(Integer commentId);

    boolean updateGoodNum(int replyGoodNum, Integer replyId);

    Page<Reply> getPage(Integer page, Integer pageSize, String name);
}
