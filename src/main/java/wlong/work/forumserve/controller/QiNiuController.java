package wlong.work.forumserve.controller;


import com.alibaba.fastjson.JSON;
import com.qiniu.storage.model.DefaultPutRet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import wlong.work.forumserve.common.R;
import wlong.work.forumserve.utils.DateUtils;
import wlong.work.forumserve.utils.QiNiuUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wl
 * @since 2022-11-07
 */
@Slf4j
@RestController
@RequestMapping("/qiniu")
public class QiNiuController {

    @Resource
    private QiNiuUtils qiNiuUtils;

    /**
     * 七牛云文件上传
     *
     * @param file 文件
     * @return
     */
    @RequestMapping("/upload")
    public R upload(MultipartFile[] file) {
        if (file.length <= 0) {
            return R.error("上传文件不能为空");
        }
        if (file[0].isEmpty()) {
            return R.error("上传文件不能为空");
        }
        try {
            BufferedInputStream fileInputStream = (BufferedInputStream) file[0].getInputStream();
            String originalFilename = file[0].getOriginalFilename();
            String fileExtend = originalFilename.substring(originalFilename.lastIndexOf("."));
            String yyyyMMddHHmmss = DateUtils.getTodayDate();
            //默认不指定key的情况下，以文件内容的hash值作为文件名
            String fileKey = UUID.randomUUID().toString().replace("-", "") + "-" + yyyyMMddHHmmss + fileExtend;
            Map<String, Object> map = new HashMap<>();
            DefaultPutRet uploadInfo = qiNiuUtils.upload(fileInputStream, fileKey);
            map.put("fileName", uploadInfo.key);
            map.put("originName", originalFilename);
            map.put("size", file[0].getSize());
            //七牛云文件私有下载地址（看自己七牛云公开还是私有配置）
//            map.put("url", "/linfen/qiniu/private/file/" + uploadInfo.key);
            map.put("url", "/qiniu/file/" + uploadInfo.key);//七牛云公开下载地址
            log.info("文件：" + JSON.toJSONString(map));
            return R.success(map);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("上传失败");
        }
    }

    /**
     * 七牛云私有文件下载
     *
     * @param filename 文件名
     * @return
     */
    @RequestMapping("/private/file/{filename}")
    public void privateDownload(@PathVariable("filename") String filename, HttpServletResponse response) {
        if (filename.isEmpty()) {
            return;
        }

        try {
            String privateFile = qiNiuUtils.getPrivateFile(filename);
            log.info("文件下载地址：" + privateFile);
            response.sendRedirect(privateFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 七牛云文件下载
     *
     * @param filename 文件名
     * @return
     */
    @RequestMapping("/file/{filename}")
    public void download(@PathVariable("filename") String filename, HttpServletResponse response) {
        if (filename.isEmpty()) {
            return;
        }

        try {
            String privateFile = qiNiuUtils.getFile(filename);
            log.info("文件下载地址：" + privateFile);
            response.sendRedirect(privateFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 七牛云删除文件
     *
     * @param filename 文件名
     * @return
     */
    @RequestMapping("/delete/file/{filename}")
    public R deleteFile(@PathVariable("filename") String filename, HttpServletResponse response) {
        if (filename.isEmpty()) {
            return R.error("未知文件");
        }

        try {
            boolean result = qiNiuUtils.delete(filename);
            if (result)
                return R.success("文件删除成功");
            else
                return R.error("文件删除失败");
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("文件删除失败");
        }
    }
}
