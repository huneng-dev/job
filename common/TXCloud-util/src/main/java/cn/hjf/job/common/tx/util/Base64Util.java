package cn.hjf.job.common.tx.util;

import org.apache.tomcat.util.codec.binary.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Base64Util {
    public static String convertInputStreamToBase64(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;

        // 读取文件并写入到 ByteArrayOutputStream
        while ((length = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, length);
        }

        // 转换为 Base64 字符串
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeBase64String(bytes); // 使用 Apache Commons Codec 库进行 Base64 编码
    }
}
