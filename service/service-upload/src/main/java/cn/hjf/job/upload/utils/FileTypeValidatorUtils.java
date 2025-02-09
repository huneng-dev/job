package cn.hjf.job.upload.utils;

import org.apache.tika.Tika;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class FileTypeValidatorUtils {

    // 创建 Tika 实例
    private static final Tika tika = new Tika();

    private static final int SMALL_FILE_THRESHOLD = 5 * 1024 * 1024;  // 文件大小 5MB 以下算作小文件


    /**
     * 校验文件类型
     *
     * @param file  需要校验的文件
     * @param types 支持的 MIME 类型列表
     * @return 如果文件 MIME 类型在支持的类型列表中，返回 true，否则返回 false
     * @throws IOException 文件读取异常
     */
    public static boolean fileTypeValidator(MultipartFile file, List<String> types) throws IOException {
        // 如果文件为空，直接返回 false
        if (file.isEmpty()) {
            return false;
        }

        // 获取文件的大小
        long fileSize = file.getSize();

        // 判断文件大小，根据大小决定读取的字节数
        int readLength = fileSize < SMALL_FILE_THRESHOLD ? 100 : 500;  // 小文件读取 100 字节，大文件读取 500 字节
        byte[] buffer = new byte[readLength];

        try (InputStream inputStream = file.getInputStream()) {
            int bytesRead = inputStream.read(buffer);

            // 检查是否读取到了有效字节
            if (bytesRead == -1) {
                return false;
            }

            // 使用实际读取的字节数来检测 MIME 类型
            String mimeType = tika.detect(buffer);

            // 如果提前判断成功，返回 true
            if (types.contains(mimeType)) {
                return true;
            }

            // 如果没有提前判断成功，继续读取更多的文件内容进行校验
            mimeType = tika.detect(inputStream);
            return types.contains(mimeType);
        } catch (IOException e) {
            // 捕获 IOException，记录错误并重新抛出
            throw e;
        }
    }


    // 允许的文件扩展名列表
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList(
            // Microsoft Office
            ".doc", ".docx",    // Word
            ".xls", ".xlsx",    // Excel
            ".ppt", ".pptx",    // PowerPoint
            // PDF
            ".pdf",
            // 压缩文件
            ".zip", ".rar", ".7z"
    );


    /**
     * 验证文件类型
     *
     * @param file 要验证的文件
     * @return true:文件类型允许 false:文件类型不允许
     */
    public static boolean validateFileType(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return false;
        }

        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            return false;
        }

        // 获取文件扩展名并转为小写
        String extension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();

        // 1. 首先验证扩展名
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            return false;
        }

        // 2. 尝试使用 Tika 验证
        try {
            String mimeType = tika.detect(file.getInputStream());

            if ("application/octet-stream".equals(mimeType)) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }

        // 如果 Tika 验证失败，只要扩展名正确就允许
        return true;
    }


}
