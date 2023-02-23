package wlong.work.forumserve.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import wlong.work.forumserve.domain.Admin;

import java.util.List;

/**
 * <p>
 * 管理员表 服务类
 * </p>
 *
 * @author wl
 * @since 2022-11-07
 */
public interface AdminService extends IService<Admin> {

    Admin login(String email, String password);

    boolean getByUsername(String username);

    boolean openAdmin(Admin admin);

    boolean offAdmin(Admin admin);

    Page<Admin> getPage(int page,int pageSize,String name);

}
