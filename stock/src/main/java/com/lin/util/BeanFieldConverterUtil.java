package com.lin.util;

import cn.hutool.core.annotation.Link;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 佛祖保佑此代码没有bug，即使有也一眼看出
 *
 * @author lin
 * @Description
 * @create 2025-06-06 17:39
 */
public class BeanFieldConverterUtil {
    public static void main(String[] args) throws IOException, IOException {
        // 定义表格参数
        int rows = 100;       // 行数
        int cols = 4;        // 列数
        int rowHeight = 20;   // 每行高度
        int colWidth = 100;   // 每列宽度
        int margin = 50;      // 边距

        // 计算图片尺寸
        int width = cols * colWidth + 2 * margin;  // 1200 像素
        int height = rows * rowHeight + 2 * margin; // 2100 像素

        // 创建 BufferedImage
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        // 设置背景为白色
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

        // 设置字体
        Font font = new Font("微软雅黑", Font.PLAIN, 12);
//        g.setFont(new Font("微软雅黑", Font.BOLD, 30));
        g.setFont(font);
        FontMetrics fm = g.getFontMetrics();

        // 模拟表格数据（100 行 11 列）
        String[][] data = new String[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i][j] = "行 " + (i + 1) + ", 列 " + (j + 1);
            }
        }

        // 绘制表格网格线
        g.setColor(Color.BLACK);
        // 绘制水平线
        for (int i = 0; i <= rows; i++) {
            int y = margin + i * rowHeight;
            g.drawLine(margin, y, margin + cols * colWidth, y);
        }
        // 绘制垂直线
        for (int j = 0; j <= cols; j++) {
            int x = margin + j * colWidth;
            g.drawLine(x, margin, x, margin + rows * rowHeight);
        }

        // 绘制表格数据
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                String text = data[i][j];
                // 计算文本位置（居中对齐）
                int x = margin + j * colWidth + (colWidth - fm.stringWidth(text)) / 2;
                int y = margin + i * rowHeight + (rowHeight + fm.getAscent() - fm.getDescent()) / 2;
                g.drawString(text, x, y);
            }
        }

        // 释放 Graphics 资源
        g.dispose();

        // 保存图片为 PNG
        ImageIO.write(image, "png", new File("D:\\1stock\\table_output.png"));
        System.out.println("图片已生成：table_output.png");
    }


    public static List<String[]> convertBeansToFieldArrays(List<?> beanList) {
        if (beanList == null || beanList.isEmpty()) {
            return new ArrayList<>();
        }

        // 获取真实类（处理代理类）
        Class<?> targetClass = getTargetClass(beanList.get(0));

        // 获取所有字段（包括父类）
        List<Field> allFields = getAllFields(targetClass);

        List<String[]> result = new ArrayList<>();

        for (Object bean : beanList) {
            // 存储每个对象的非空字段键值对
            Map<String, String> fieldMap = new LinkedHashMap<>();

            for (Field field : allFields) {
                try {
                    field.setAccessible(true);
                    Object value = field.get(bean);

                    if (value != null&&StringUtils.isNotBlank(value.toString())) {
                        // 特殊处理集合类型和数组
                        if (Collection.class.isAssignableFrom(value.getClass())) {
                            fieldMap.put(field.getName(), collectionToString((Collection<?>) value));
                        } else if (value.getClass().isArray()) {
                            fieldMap.put(field.getName(), arrayToString(value));
                        } else {
                            // 处理嵌套对象
                            fieldMap.put(field.getName(), valueToString(value));
                        }
                    }
                } catch (IllegalAccessException e) {
                    // 忽略无法访问的字段
                }
            }

            // 转换为字段名和值的格式数组
            String[] fieldArray = fieldMap.entrySet().stream()
                    .filter(e -> e.getValue() != null && StringUtils.isNotBlank(e.getValue()))
                    .map(entry -> entry.getValue())
                    .toArray(String[]::new);

            result.add(fieldArray);
        }

        return result;
    }

    private static Class<?> getTargetClass(Object bean) {
        Class<?> clazz = bean.getClass();
        // 简单代理类检测（可根据需要扩展）
        if (clazz.getName().contains("$$") || clazz.getName().contains("$Proxy")) {
            return clazz.getSuperclass();
        }
        return clazz;
    }

    private static List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        while (clazz != null && clazz != Object.class) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        return fields;
    }

    private static String valueToString(Object value) {
        // 处理嵌套对象
        if (isCustomObject(value.getClass())) {
            return "{" + convertBeansToFieldArrays(List.of(value)).get(0)[0] + "}";
        }
        return value.toString();
    }

    private static String collectionToString(Collection<?> collection) {
        return "[" + collection.stream()
                .map(obj -> obj == null ? "null" : valueToString(obj))
                .collect(Collectors.joining(", ")) + "]";
    }

    private static String arrayToString(Object array) {
        if (array instanceof Object[]) {
            return collectionToString(Arrays.asList((Object[]) array));
        }
        // 处理基本类型数组
        return "[" + Arrays.toString((Object[]) array) + "]";
    }

    private static boolean isCustomObject(Class<?> clazz) {
        return !clazz.isPrimitive() &&
                !clazz.isArray() &&
                !Collection.class.isAssignableFrom(clazz) &&
                !Map.class.isAssignableFrom(clazz) &&
                !clazz.getName().startsWith("java.");
    }
}