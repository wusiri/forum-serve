package wlong.work.forumserve.service.impl;

import wlong.work.forumserve.domain.Admin;
import wlong.work.forumserve.dao.AdminDao;
import wlong.work.forumserve.service.IAdminService;
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
public class AdminServiceImpl extends ServiceImpl<AdminDao, Admin> implements IAdminService {

}
