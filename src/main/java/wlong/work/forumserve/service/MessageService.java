package wlong.work.forumserve.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import wlong.work.forumserve.domain.ArticleLabel;
import wlong.work.forumserve.domain.Message;
import wlong.work.forumserve.dto.MessageUserDto;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wl
 * @since 2022-11-07
 */
public interface MessageService extends IService<Message> {

    Page<Message> getPage(Integer page, Integer pageSize, String name);


    List<Message> getList(Integer type, Integer userId, Integer state);
}
