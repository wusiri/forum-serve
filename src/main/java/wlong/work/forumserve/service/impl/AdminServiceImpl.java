package wlong.work.forumserve.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wlong.work.forumserve.dao.AdminDao;
import wlong.work.forumserve.domain.Admin;
import wlong.work.forumserve.service.AdminService;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 管理员表 服务实现类
 * </p>
 *
 * @author wl
 * @since 2022-11-07
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminDao, Admin> implements AdminService {


    @Resource
    private AdminDao adminDao;

    @Override
    public Admin login(String email, String password) {
        //条件构造器
        LambdaQueryWrapper<Admin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Admin::getEmail, email).eq(Admin::getPassword, password).eq(Admin::getEnabled,1);
        return adminDao.selectOne(queryWrapper);
    }

    @Override
    public boolean getByUsername(String username) {
        LambdaQueryWrapper<Admin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Admin::getUsername, username);
        return adminDao.selectOne(queryWrapper) == null;
    }

    @Override
    public boolean openAdmin(Admin admin) {
        LambdaUpdateWrapper<Admin> updateWrapper = new LambdaUpdateWrapper<>();
        admin.setEnabled(1);
        return adminDao.updateById(admin) > 0;
    }

    @Override
    public boolean offAdmin(Admin admin) {
        LambdaUpdateWrapper<Admin> updateWrapper = new LambdaUpdateWrapper<>();
        admin.setEnabled(0);
        return adminDao.updateById(admin) > 0;
    }

    @Override
    public Page<Admin> getPage(int page, int pageSize, String name) {
        //分页构造器
        Page<Admin> pageInfo = new Page<>();
        //过滤条件
        LambdaQueryWrapper<Admin> queryWrapper = new LambdaQueryWrapper<>();
//        .like(StringUtils.isNotEmpty(name), Admin::getUsername, name)
        queryWrapper.like(StringUtils.isNotEmpty(name), Admin::getEmail, name).eq(Admin::getIsSuper,0);
        //添加排序条件
        queryWrapper.orderByAsc(Admin::getId);
        //执行查询
        adminDao.selectPage(pageInfo, queryWrapper);
        return pageInfo;
    }
}


