package wlong.work.forumserve.service;

import org.springframework.web.multipart.MultipartFile;

public interface UploadImageService {

    /**
     * 上传文件
     * @param file
     * @return
     */
    String uploadQNImg(MultipartFile file);

    String getPrivateFile(String fileKey);

    boolean removeFile(String bucketName, String fileKey);
}
