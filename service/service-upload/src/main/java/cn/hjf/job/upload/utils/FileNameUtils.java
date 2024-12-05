package cn.hjf.job.upload.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class FileNameUtils {

    /**
     * 生成文件路径
     *
     * @param pathPrefix 路径前缀
     * @return 文件路径(不包括文件后缀)
     */
    public static String generatorFileName(String pathPrefix) {
        String fileName = new SimpleDateFormat("yyyyMMdd")
                .format(new Date()) + "/" + UUID.randomUUID().toString().replace("-", "");

        return pathPrefix + "/" + fileName;
    }
}
