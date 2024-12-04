package cn.hjf.job.user.utils;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class UsernameGenerator {

    // 字母表，包含大写字母和小写字母，共52个字符
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    // 用于生成随机字母的随机数生成器
    private static final Random random = new Random();

    /**
     * 将时间戳转换为字母（基于52个字母的循环）
     *
     * @param timestamp 时间戳
     * @return 转换后的字母字符串
     */
    public static String timestampToLetters(long timestamp) {
        StringBuilder sb = new StringBuilder();

        // 取时间戳的最后4位数字并进行字母转换
        for (int i = 0; i < 4; i++) {
            sb.append(ALPHABET.charAt((int) (timestamp % 52)));  // 使用52个字符
            timestamp /= 52;
        }

        // 生成的字母是倒序的，需要反转
        return sb.reverse().toString();
    }

    /**
     * 生成随机字母
     *
     * @param length 字母的长度
     * @return 随机生成的字母字符串
     */
    public static String generateRandomLetters(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));  // 从字母表中随机选择字符
        }
        return sb.toString();
    }

    /**
     * 生成默认用户名，格式：userXXXXXX，其中 XXXXXX 是基于时间戳转换后的字母 + 随机字母
     *
     * @return 默认用户名
     */
    public static String generateDefaultUsername() {
        long timestamp = System.currentTimeMillis();  // 获取当前时间戳
        String timestampAsLetters = timestampToLetters(timestamp);  // 转换为字母
        String randomLetters = generateRandomLetters(4);  // 生成4个随机字母
        return "用户名" + timestampAsLetters + randomLetters;  // 拼接“用户名”和字母
    }
}

