package wlong.work.forumserve.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import wlong.work.forumserve.common.R;
import wlong.work.forumserve.domain.User;
import wlong.work.forumserve.vo.WhereVo;
import wlong.work.forumserve.service.UploadImageService;
import wlong.work.forumserve.service.UserService;
import wlong.work.forumserve.utils.MailServiced;
import wlong.work.forumserve.utils.ValidateCodeUtils;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author wl
 * @since 2022-11-07
 */
@Api(value = "用户Controller", tags = {"用户访问接口"})
@RestController
@RequestMapping("/user")
@CrossOrigin

@Slf4j
public class UserController {


    @Resource
    private UploadImageService uploadImageService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private MailServiced mailServiced;

    @Resource
    private UserService userService;



    //文件路径
    @Value("${forum.path}")
    private String root;

    @ApiOperation("获取当前登录用户信息")
    @GetMapping("/info/{id}")
    public R<User> getUserInfo(@PathVariable("id") Integer id) {
        User user = userService.getById(id);
        return R.success(user);
    }



    @ApiOperation("获取地方用户数量")
    @GetMapping("/getWhere")
    public R<List<WhereVo>> getWhere(){
        List<WhereVo> map= userService.getWhere();;
        return R.success(map);
    }


    @GetMapping("/set-session")
    public Object writeSession(HttpSession httpSession) {
        String sessionVal="1234";
        System.out.println("Param 'sessionVal' = " + sessionVal);
        httpSession.setAttribute("sessionVal", sessionVal);
        return sessionVal;
    }

    @GetMapping("/get-session")
    public Object readSession(HttpSession httpSession) {
        Object obj = httpSession.getAttribute("sessionVal");
        System.out.println("'sessionVal' in Session = " + obj);
        return obj;
    }

    @ApiOperation("上传头像,保存到本地")
    @PostMapping("/upload")
    public R<String> upload(@RequestParam("userId") Integer id, @RequestParam("file") MultipartFile file){
        //file是一个临时文件，需要转存到指定位置，否则本次请求完成后临时文件会删除
        log.info(file.toString());

        //原始文件名
        String originalFilename = file.getOriginalFilename();
        //获取文件后缀
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        //使用UUID重新生成文件名，防止文件名称重复造成文件覆盖
        //名字+后缀
        String fileName = UUID.randomUUID().toString() + suffix;
        //创建一个目录对象
        File dir = new File(root);
        //判断当前目录是否存在
        if (!dir.exists()) {
            //目录不存在，需要创建
            dir.mkdirs();
        }
        try {
            //将临时文件转存到指定位置
            file.transferTo(new File(root + fileName));
            //同步到数据库
            String pathDB = root+fileName;
            userService.updatePortrait(id, pathDB);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(fileName);
    }


    @ApiOperation("文件下载")
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
        try {
            //输入流，通过输入流读取文件内容

            FileInputStream fileInputStream = new FileInputStream(new File(root + name));
            //输出流，通过输出流将文件写回浏览器
            ServletOutputStream outputStream = response.getOutputStream();
            //设置文件格式
            response.setContentType("image/jpeg");
            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
                outputStream.flush();
            }
            //关闭资源
            outputStream.close();
            fileInputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
            userService.updatePortrait(id, paths);
            return R.success(paths);
        }
        return R.error("上传失败");
    }

    @ApiOperation(value = "修改当前用户信息")
    @PutMapping("/updateUserInfo")
    public R<String> updateUserinfo(@RequestBody User user) {
        log.info(user.toString());
        boolean updateUserinfo = userService.updateUserinfo(user);
        if (updateUserinfo) {
            return R.success("修改成功");
        } else {
            return R.error("修改失败");
        }
    }

    @ApiOperation(value = "禁用和启用")
    @PostMapping("/update")
    public R<String> update(@RequestBody User user) {
        log.info(user.toString());
        boolean updateUserinfo = userService.update(Wrappers.lambdaUpdate(User.class).set(User::getEnabled,user.getEnabled()).eq(User::getId,user.getId()));
        if (updateUserinfo) {
            return R.success("修改成功");
        } else {
            return R.error("修改失败");
        }
    }

    @ApiOperation("密码比对")
    @PostMapping("/check")
    public R<String> check(@RequestBody User user,String password,String password2){
        String md5DigestAsHex = DigestUtils.md5DigestAsHex(password.getBytes());
        String md5DigestAsHex1 = DigestUtils.md5DigestAsHex(password2.getBytes());
        User user1 = userService.getById(user.getId());
        if(md5DigestAsHex.equals(user1.getPassword())){
            return R.error("请检查原密码是否正确!!!");
        }
        if(md5DigestAsHex.equals(md5DigestAsHex1)){
            return R.error("新密码与原密码过度相似!!!");
        }else {
            return R.success("");
        }
    }


    @ApiOperation("注销登录")
    @PostMapping("/logout")
    public R<String> logout(@RequestBody User user){
        boolean delete = stringRedisTemplate.delete(user.getEmail());
        if(delete){
          return   R.success("注销成功,欢迎下次使用!!!");
        }else {
          return   R.error("注销失败，请重试");
        }

    }

    @ApiOperation("修改密码")
    @PutMapping("/updatePassword")
    public R<String> updatePassword(@RequestBody User user){
        if(user.getPassword() == null){
            return R.success("密码不符合规定");
        }
        boolean updatePassword = userService.updatePassword(user);
        if (updatePassword) {
            return R.success("修改成功");
        } else {
            return R.error("修改失败");
        }
    }

    @ApiOperation(value = "通过用户ID获取用户信息")
    @PostMapping("/getUserinfoById/{id}")
    public R<User> getUserinfoById(@PathVariable("id") Integer id) {
        User user = userService.getById(id);
        user.setPassword(null);
        return R.success(user);
    }


    @ApiOperation(value = "发送验证码")
    @PostMapping("/sendEmail")
    public R<String> sendEmail(@RequestBody User user, HttpSession session) {
        //获取邮箱
        String email = user.getEmail();
        if (StringUtils.isNotEmpty(email)) {
            //生成随机的4位验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code={}", code);
            //发送验证码
//            mailServiced.sendEmail(email, code);
            //需要将生成的验证码保存到Session
            //session.setAttribute(email, code);
            //将验证码以<key,value>形式缓存到redis
            stringRedisTemplate.opsForValue().set(email,code, 60,TimeUnit.SECONDS);

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
    public R<User> loginEmailApi(@RequestBody Map map, HttpSession session) {
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
            User user = userService.login(email, md5DigestAsHex);
            if(user!=null){
                session.setAttribute("user", user.getId());
                user.setLoginTime(LocalDateTime.now());
                userService.updateById(user);
                stringRedisTemplate.delete(email);
            }else {
                return R.error("当前用户不存在");
            }
//            // 使用UUID作为token值
//            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
//            // 在uuid后拼接管理员id组成最后的token值（添加id是为了方便后续验证）
//            String token = uuid + user.getId();
//            // 将用户的ID信息存入redis缓存，并设置两小时的过期时间
//            stringRedisTemplate.opsForValue().set("user"+user.getId(), token, 7200, TimeUnit.SECONDS);
//            session.setAttribute("user",user.getId());
            return R.success(user);
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
        //获取用户昵称
        String nickname = map.get("nickname").toString();
        //获取用户邮箱
        String email = map.get("email").toString();
        //判断邮箱是否已注册
        User one = userService.getOne(Wrappers.lambdaQuery(User.class).eq(User::getEmail, email));
        if(one!=null){
            return R.error("邮箱已被注册");
        }
        //获取用户密码
        String password = map.get("password").toString();
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
                boolean existence = userService.getByUsername(username);
                if (existence) {
                    break;
                }
            }
            //封装数据
            User user = new User();
            user.setUsername(username);
            //进行加密
            user.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));
            user.setEmail(email);
            user.setNickname(nickname);
            boolean save = userService.save(user);
            if (save) {
                return R.success("注册成功");
            } else {
                return R.success("注册失败");
            }
        }
        return R.error("注册失败!");
    }

    @ApiOperation("获取用户信息")
    @GetMapping("/getPage")
    public R<Page<User>> getPage(Integer page, Integer pageSize, String name){
        log.info("{},{},{}",page,pageSize,name);
        Page<User> page1 = userService.getPage(page, pageSize, name);
        return R.success(page1);
    }

    @ApiOperation("获取用户信息")
    @GetMapping("/getList")
    public R<List<User>> List(){
        List<User> list = userService.list();
        return R.success(list);
    }




    @ApiOperation("根据id删除用户")
    @DeleteMapping("/deleteById/{id}")
    public R<String> deleteById(@PathVariable("id") Integer id){
        boolean remove = userService.removeById(id);
        if(remove){
            return R.success("删除成功!");
        }else {
            return R.error("删除失败");
        }
    }



    @ApiOperation("删除选中内容")
    @DeleteMapping()
    public R<String> deleteByIds(@RequestBody  List<Integer> ids) {
        boolean remove = userService.removeByIds(ids);
        if (remove) {
            return R.success("删除成功");
        } else {
            return R.success("删除失败");
        }
    }


}

