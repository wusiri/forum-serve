package wlong.work.forumserve.utils;



import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

public class FileUtil {


    //将File类转化为MultipartFile的方法
    public static FileItem getMultipartFile(File file, String fieldName){
        FileItemFactory factory = new DiskFileItemFactory(16, null);
        FileItem item = factory.createItem(fieldName, "text/plain", true, file.getName());
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        try {
            FileInputStream fis = new FileInputStream(file);
            OutputStream os = item.getOutputStream();
            while ((bytesRead = fis.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return item;
    }


    //将MultipartFile转化为File类的方法
    public static File MultipartFileToFile(MultipartFile multipartFile) throws IOException {
        File toFile = null;
        if (!multipartFile.equals("") && !(multipartFile.getSize() <= 0)) {
            InputStream ins = multipartFile.getInputStream();
            toFile = new File(multipartFile.getOriginalFilename());
            inputStreamToFile(ins, toFile);
            ins.close();
        }
        return toFile;
    }

    private static void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //删除产生的临时文件的方法
    public static void deleteTempFile(File file){
        if(file!=null){
            File delFile = new File(file.toURI());
            delFile.delete();
        }
    }
}
