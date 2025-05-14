package com.lin.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 佛祖保佑此代码没有bug，即使有也一眼看出
 *
 * @author lin
 * @Description
 * @create 2025-05-06 17:02
 */
public class RandomIPGenerator {
    private static final Random random = new Random();

    public static void main(String[] args) {
        // 生成10个示例IP（过滤私有地址）
        List<String> ips = generateRandomIPs(10, true);
        ips.forEach(System.out::println);
    }

    /**
     * 生成随机IP地址+端口
     * @param count 生成数量
     * @param excludePrivate 是否排除私有IP
     * @return IP列表
     */
    public static List<String> generateRandomIPs(int count, boolean excludePrivate) {
        List<String> result = new ArrayList<>();
        while (result.size() < count) {
            int[] segments = generateIpSegments();
            if (!excludePrivate || !isPrivateIP(segments)) {
                int port = generatePort();
                result.add(formatIP(segments, port));
            }
        }
        return result;
    }

    public static String generateRandomIP() {
        List<String> result = new ArrayList<>();
        int[] segments = generateIpSegments();
        return String.format("%d.%d.%d.%d",
                segments[0], segments[1], segments[2], segments[3]);
    }

    // 生成IP四段数字
    private static int[] generateIpSegments() {
        return new int[] {
                random.nextInt(256),
                random.nextInt(256),
                random.nextInt(256),
                random.nextInt(256)
        };
    }

    // 生成端口号（1-65535）
    public static int generatePort() {
        return 1 + random.nextInt(65535);
    }

    // 格式化输出
    private static String formatIP(int[] segments, int port) {
        return String.format("http://%d.%d.%d.%d:%d",
                segments[0], segments[1], segments[2], segments[3], port);
    }

    // 检测私有地址
    private static boolean isPrivateIP(int[] segments) {
        return (segments[0] == 10) || // 10.0.0.0/8
                (segments[0] == 172 && (segments[1] >= 16 && segments[1] <= 31)) || // 172.16.0.0/12
                (segments[0] == 192 && segments[1] == 168); // 192.168.0.0/16
    }
}