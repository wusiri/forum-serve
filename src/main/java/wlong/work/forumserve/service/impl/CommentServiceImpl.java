package wlong.work.forumserve.service.impl;

import wlong.work.forumserve.domain.Comment;
import wlong.work.forumserve.dao.CommentDao;
import wlong.work.forumserve.service.ICommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wl
 * @since 2022-11-07
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentDao, Comment> implements ICommentService {

}
