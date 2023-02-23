package wlong.work.forumserve.controller;


import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import wlong.work.forumserve.common.R;
import wlong.work.forumserve.domain.Admin;
import wlong.work.forumserve.domain.Article;
import wlong.work.forumserve.domain.User;
import wlong.work.forumserve.service.AdminService;
import wlong.work.forumserve.service.UploadImageService;
import wlong.work.forumserve.utils.MailServiced;
import wlong.work.forumserve.utils.ValidateCodeUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 管理员表 前端控制器
 * </p>
 *
 * @author wl
 * @since 2022-11-07
 */
@Api(value = "管理员Controller",tags = {"管理员访问接口"})
@RestController
@RequestMapping("/admin")
@CrossOrigin
@Slf4j
public class AdminController {

    @Resource
    private MailServiced mailServiced;

    @Resource
    private AdminService adminService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private UploadImageService uploadImageService;

    @ApiOperation("获取当前登录用户信息")
    @GetMapping("/info/{id}")
    public R<Admin> getUserInfo(@PathVariable("id") Integer id) {
        Admin admin = adminService.getById(id);
        return R.success(admin);
    }

    @ApiOperation("修改用户头像，上传云端")
    @PutMapping("/updatePhoto")
    public R<String> updatePhoto(@RequestParam("id") Integer id, @RequestParam("file") MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            log.info("图片{}",file);
            String path = uploadImageService.uploadQNImg(file);
            log.info("七牛云返回的图片链接:{}" , path);
            //链接处理
            int i = path.indexOf("?");
            String paths="http://"+path.substring(0,i).replace("%2F","/");
            log.info("处理后的链接:{}",paths);
            /*qiniu.wlong.work/code%2Fduck%2F20230113-850f938c035746e1ab1e112548e739fa.jpg
                ?e=1673605941&token=IaXM0rTcHF4rF__5qn9iuWhHqJgW3XaCXSb1FsAV:v7bOVgb9BsrhPXChymf7exlClck=
                http://qiniu.wlong.work/forum/v2-e0d294a91376a3527fc07a61dd111cfe_r.jpg*/
            //存储到数据库
            adminService.update(Wrappers.lambdaUpdate(Admin.class).set(Admin::getPortrait, paths).eq(Admin::getId, id));
            return R.success(paths);
        }
        return R.error("上传失败");
    }




    @ApiOperation(value = "发送验证码")
    @PostMapping("/sendEmail")
    public R<String> sendEmail(@RequestBody Admin admin, HttpSession session) {
        //获取邮箱
        String email = admin.getEmail();
        if (StringUtils.isNotEmpty(email)) {
            //生成随机的4位验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code={}", code);
            //发送验证码
//            mailServiced.sendEmail(email, code);
            //需要将生成的验证码保存到Session
            //session.setAttribute(email, code);
            //将验证码以<key,value>形式缓存到redis
            stringRedisTemplate.opsForValue().set(email,code, 60, TimeUnit.SECONDS);

            return R.success("邮箱验证发送成功");
        }

        return R.error("邮箱验证发送失败");
    }

    /**
     * 邮箱登录
     *
     * @param map
     * @param session
     * @return
     */
    @ApiOperation(value = "邮箱登录")
    @PostMapping("/emailLogin")
    public R<Admin> loginEmailApi(@RequestBody Map map, HttpSession session) {
        log.info(map.toString());

        //获取邮箱
        String email = map.get("email").toString();
        //获取密码
        String password = map.get("password").toString();
        //加密
        String md5DigestAsHex = DigestUtils.md5DigestAsHex(password.getBytes());

        //获取验证码
        String code = map.get("code").toString();

        //从Session中获取保存的验证码
        //Object codeInSession = session.getAttribute(email);
        String codeInSession=stringRedisTemplate.opsForValue().get(email);

        //进行验证码的比对（页面提交的验证码和Session中保存的验证码比对）
        if (codeInSession != null && codeInSession.equals(code)) {
            //如果能够比对成功，说明登录成功
             Admin admin= adminService.login(email, md5DigestAsHex);
            if(admin!=null){
                session.setAttribute("admin", admin.getId());
                admin.setLoginTime(LocalDateTime.now());
                adminService.updateById(admin);
                stringRedisTemplate.delete(email);
            }else {
                return R.error("邮箱或密码错误");
            }
//            // 使用UUID作为token值
//            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
//            // 在uuid后拼接管理员id组成最后的token值（添加id是为了方便后续验证）
//            String token = uuid + user.getId();
//            // 将用户的ID信息存入redis缓存，并设置两小时的过期时间
//            stringRedisTemplate.opsForValue().set("user"+user.getId(), token, 7200, TimeUnit.SECONDS);
//            session.setAttribute("admin",admin.getId());
            return R.success(admin);
        }
        return R.error("登录失败");
    }


    /**
     * 邮箱注册,register
     */
    @ApiOperation("邮箱注册")
    @PostMapping("/register")
    public R<String> register(@RequestBody Map map, HttpSession session) {
        log.info(map.toString());
        //获取用户邮箱
        String email = map.get("email").toString();
        //获取用户密码
        String password = map.get("password").toString();
        //判断邮箱是否已注册
        Admin one = adminService.getOne(Wrappers.lambdaQuery(Admin.class).eq(Admin::getEmail, email));
        log.info("{}",one);
        if(one!=null){
            return R.error("邮箱已被注册");
        }
        //获取邮箱校验码
        String code = map.get("code").toString();
        //获取session中的验证码
        //Object codeInSession = session.getAttribute(email);
        String codeInSession=stringRedisTemplate.opsForValue().get(email);
        //进行验证码的比对（页面提交的验证码和Session中保存的验证码比对）
        if (codeInSession != null && codeInSession.equals(code)) {
            //如果能够比对成功
            String username;
            while (true) {
                //生成随机用户名
                username = ValidateCodeUtils.getStringRandom();
                log.info(username);
                //检验是否存在
                boolean existence = adminService.getByUsername(username);
                if (existence) {
                    break;
                }
            }
            //封装数据
            Admin admin = new Admin();
            admin.setUsername(username);
            //进行加密
            admin.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));
            admin.setEmail(email);
            boolean save = adminService.save(admin);
            if (save) {
                return R.success("注册成功");
            } else {
                return R.success("注册失败");
            }
        }
        return R.error("注册失败!");
    }




    @ApiOperation("启用管理员")
    @PutMapping("/setOpenAdmin")
    public R<String> setOpenAdmin(@RequestBody Admin admin){
        boolean openAdmin = adminService.openAdmin(admin);
        if(openAdmin){
            return R.success("启动成功");
        }else {
            return R.error("启动失败");
        }
    }

    @ApiOperation("禁用管理员")
    @PutMapping("/setOffAdmin")
    public R<String> setOffAdmin(@RequestBody Admin admin){
        boolean offAdmin = adminService.offAdmin(admin);
        if(offAdmin){
            return R.success("启动成功");
        }else {
            return R.error("启动失败");
        }
    }

    @ApiOperation("启用和禁用")
    @PostMapping("/status/{enabled}")
    public R<String> updateStatus(@RequestParam List<Integer> ids,@PathVariable Integer enabled){
        LambdaUpdateWrapper<Admin> updateWrapper=new LambdaUpdateWrapper<>();
        updateWrapper.in(Admin::getId,ids);
        if(enabled==0){
            updateWrapper.eq(Admin::getEnabled,1).set(Admin::getEnabled,0);
        }else {
            updateWrapper.eq(Admin::getEnabled,0).set(Admin::getEnabled,1);
        }
        boolean update=adminService.update(updateWrapper);
        if(update){
            return R.success("修改成功");
        }else {
            return R.error("修改失败");
        }
    }

    @ApiOperation("获取全部管理员")
    @GetMapping("/getAll")
    public R<List<Admin>> getAllList(){
        List<Admin> list = adminService.list();
        return R.success(list);
    }

    @ApiOperation("获取单个管理员")
    @GetMapping("/getById/{id}")
    public R<Admin> getById(@PathVariable("id") Integer id){
        Admin admin = adminService.getById(id);
        return R.success(admin);
    }

    @ApiOperation("分页获取全部")
    @GetMapping("/page")
    public R<Page<Admin>> getPage(int page,int pageSize,String name){
        Page<Admin> page1 = adminService.getPage(page, pageSize, name);
        return R.success(page1);
    }


    @ApiOperation("添加管理员")
    @PostMapping("/save")
    public R<String> save(@RequestBody Admin admin){
        boolean save = adminService.save(admin);
        if(save){
            return R.success("添加成功");
        }else {
            return R.error("添加失败");
        }
    }


    @ApiOperation("编辑管理员")
    @PostMapping("/update")
    public R<String> update(@RequestBody Admin admin){
        boolean update = adminService.updateById(admin);
        if(update){
            return R.success("添加成功");
        }else {
            return R.error("添加失败");
        }
    }

    @ApiOperation("根据id删除管理员")
    @DeleteMapping("/deleteById/{id}")
    public R<String> deleteById(@PathVariable("id") Integer id){
        boolean remove = adminService.removeById(id);
        if(remove){
            return R.success("删除成功!");
        }else {
            return R.error("删除失败");
        }
    }

    @ApiOperation("删除选中内容")
    @DeleteMapping()
    public R<String> deleteByIds(@RequestBody  List<Integer> ids) {
        boolean remove = adminService.removeByIds(ids);
        if (remove) {
            return R.success("删除成功");
        } else {
            return R.success("删除失败");
        }
    }







}

