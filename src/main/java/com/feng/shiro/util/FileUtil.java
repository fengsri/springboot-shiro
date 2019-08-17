package com.feng.shiro.util;

import com.feng.shiro.constant.FilePathConstant;
import com.feng.shiro.enums.ErrorCodeEnum;
import com.feng.shiro.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @Description 文件工具类
 * @Author chenlinghong
 * @Date 2019/4/21 21:48
 * @Version V1.0
 */
@Slf4j
public class FileUtil {

    /**
     * 批量上传文件
     *
     * @param files    文件数组
     * @param filePath 存储路径
     * @return
     */
    public static List<String> upload(MultipartFile[] files, final String filePath) {
        List<String> pictureList = new ArrayList<>();
        //判断file数组不能为空并且长度大于0
        if (files != null && files.length > 0) {
            //循环获取file数组中得文件
            for (int i = 0; i < files.length; i++) {
                MultipartFile file = files[i];
                //保存文件
                String tmp = upload(file, filePath);
                pictureList.add(tmp);
            }
        }
        return pictureList;
    }

    public static List<String> upload(List<MultipartFile> files, final String filePath) {
        List<String> pictureList = new ArrayList<>();
        for (MultipartFile file : files) {
            //保存文件
            String tmp = upload(file, filePath);
            pictureList.add(tmp);
        }
        return pictureList;
    }

    /**
     * 上传文件至服务器,需要先创建相应的文件夹
     *
     * @param file     文件
     * @param filePath 文件存储子路径
     * @return 文件存储路径
     */
    public static String upload(MultipartFile file, final String filePath) {
        File targetFile;
        // 文件存储路径
        StringBuilder result = new StringBuilder(FilePathConstant.FILE_PATH);
        // 获取文件名 带后缀
        String fileName = file.getOriginalFilename();
        if (!StringUtils.isEmpty(fileName)) {
            //文件后缀
            String fileF = fileName.substring(fileName.lastIndexOf("."), fileName.length());
            //新的文件名
            fileName = new Date().getTime() + "_" + new Random().nextInt(1000) + fileF;
            // 更新文件存储路径
            result.append(filePath);

            //先判断文件是否存在
            String fileAdd = new SimpleDateFormat("yyyyMMdd").format(new Date());
            File file1 = new File(result.toString() + "/" + fileAdd);
            //如果文件夹不存在则创建
            if (!file1.exists() && !file1.isDirectory()) {
                file1.mkdir();
            }
            targetFile = new File(file1, fileName);
            try {
                file.transferTo(targetFile);

                // 更新文件存储路径
                result.append("/").append(fileAdd).append("/").append(fileName);
            } catch (IOException e) {
                log.error("UploadUtil upload,file.transferTo fail,msg={}", e);
                throw new BusinessException(ErrorCodeEnum.FILE_HANDLE_ERROR);
            }
            return FilePathConstant.URL_PRE + result.toString();
        } else {
            throw new BusinessException(ErrorCodeEnum.PARAM_IS_NULL);
        }
    }


    /**
     * 下载文件
     *
     * @param response 响应对象
     * @param fileName 文件名称
     */
    public static void download(HttpServletResponse response, String fileName) {
        if (StringUtils.isEmpty(fileName)) {
            throw new BusinessException(ErrorCodeEnum.PARAM_IS_NULL);
        }

        File file = new File(fileName);
        if (file.exists()) {
            // 默认每一次取1024B
            byte[] buffer = new byte[1024];
            FileInputStream fileInputStream;
            BufferedInputStream bufferedInputStream;

            try {
                response.setCharacterEncoding("UTF-8");

                // String mimeType = request.getServletContext().getMimeType(fileName);
                // response.setContentType(mimeType);

                // 1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
                response.setContentType("multipart/form-data");
                // 2.设置文件头：最后一个参数是设置下载文件名
                response.setHeader("content-disposition", "attachment;filename="
                        + URLEncoder.encode(fileName, "UTF-8"));

                fileInputStream = new FileInputStream(file);
                bufferedInputStream = new BufferedInputStream(fileInputStream);
                OutputStream outputStream = response.getOutputStream();

                // 文件末尾标识
                int flag = -1;
                do {
                    flag = bufferedInputStream.read(buffer);
                    outputStream.write(buffer, 0, flag);
                } while (flag != -1);
            } catch (FileNotFoundException e) {
                log.error("UploadUtil.download FileNotFoundException,msg={}", e);
                throw new BusinessException(ErrorCodeEnum.FILE_IS_NULL);
            } catch (IOException e) {
                log.error("UploadUtil.download IOException,msg={}", e);
                throw new BusinessException(ErrorCodeEnum.FILE_STREAM_CREATE_ERROR);
            }
        } else {
            // 文件不存在
            log.error("UploadUtil.download IOException,msg={}", fileName);
            throw new BusinessException(ErrorCodeEnum.FILE_IS_NULL);
        }
    }



    /**
     * 删除目录（文件夹）以及目录下的文件
     * @param   path 被删除目录的文件路径
     * @return  目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String path) {
        //如果sPath不以文件分隔符结尾，自动添加文件分隔符
        if (!path.endsWith(File.separator)) {
            path = path + File.separator;
        }
        File dirFile = new File(path);
        //如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        boolean flag = true;
        //删除文件夹下的所有文件(包括子目录)
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            //删除子文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) break;
            } //删除子目录
            else {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) break;
            }
        }
        if (!flag) return false;
        //删除当前目录
        if (dirFile.delete()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 删除单个文件
     *
     * @param sPath 被删除文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }


    /**
     * 传入File的List，将多个文件进行打包下载
     *
     * @param files
     * @param response
     * @throws Exception
     */
    public static void downLoadFiles(List<File> files, HttpServletResponse response,String path) throws Exception {
        //如果文件夹不存在则创建
        File fileTag = new File(path);
        if (!fileTag.exists() && !fileTag.isDirectory()) {
            fileTag.mkdir();
        }
        // List<File> 作为参数传进来，就是把多个文件的路径放到一个list里面
        String zipFilename = path;
        File file = new File(zipFilename,"data.zip");
        file.createNewFile();
        if (!file.exists()) {
            file.createNewFile();
        }
        // response.reset();
        //将多个文件封装到zip流中
        FileOutputStream fous = new FileOutputStream(file);
        ZipOutputStream zipOut = new ZipOutputStream(fous);
        zipFile(files, zipOut);
        zipOut.close();
        fous.close();
        //将打包的zip的文件流封装到response中
        downloadZip(file, response);
    }

    /**
     * 多个文件的流，封装到zip中
     *
     * @param files
     * @param outputStream
     * @throws Exception
     */
    public static void zipFile(List files, ZipOutputStream outputStream) throws Exception {
        int size = files.size();
        for (int i = 0; i < size; i++) {
            File file = (File) files.get(i);
            zipFile(file, outputStream);
        }
    }

    /**
     * 将单个文件流封装到zip流中
     *
     * @param file
     * @param ouputStream
     * @throws Exception
     */
    public static void zipFile(File file, ZipOutputStream ouputStream) throws Exception {
        if (file.exists()) {
            if (file.isFile()) {
                FileInputStream in = new FileInputStream(file);
                BufferedInputStream bins = new BufferedInputStream(in, 512);
                ZipEntry entry = new ZipEntry(file.getName());
                ouputStream.putNextEntry(entry);
                // 向压缩文件中输出数据
                int nNumber;
                byte[] buffer = new byte[512];
                while ((nNumber = bins.read(buffer)) != -1) {
                    ouputStream.write(buffer, 0, nNumber);
                }
                // 关闭创建的流对象
                bins.close();
                in.close();
            } else {
                File[] files = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    zipFile(files[i], ouputStream);
                }
            }
        }

    }

    /**
     * 将打包的zip流封装到response中
     *
     * @param file
     * @param response
     * @throws Exception
     */
    public static void downloadZip(File file, HttpServletResponse response) throws Exception {
        if (file.exists() == false) {
            log.error("FileUtil#downloadZip: download file error");
            throw new BusinessException(ErrorCodeEnum.FILE_DOWNLOAD_ERROR);
        } else {
            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(file.getPath()));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            // response.reset();
            //response.reset();

            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());

            // String mimeType = request.getServletContext().getMimeType(fileName);
            // response.setContentType(mimeType);

            // 1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
            response.setContentType("multipart/form-data");
            // 2.设置文件头：最后一个参数是设置下载文件名
            response.setHeader("content-disposition", "attachment;filename="
                    + URLEncoder.encode(file.getName(), "UTF-8"));
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        }
    }
}
