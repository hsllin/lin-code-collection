package com.lin.util;

import com.lin.bean.tonghuashun.IncreaseRankData;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 佛祖保佑此代码没有bug，即使有也一眼看出
 *
 * @author lin
 * @Description
 * @create 2025-06-05 11:44
 */
public class PhoUtil {
    public static List<String[]> convertBeanListToStringList(List<?> beanList) {
        List<String[]> result = new ArrayList<>();
        if (beanList == null || beanList.isEmpty()) {
            return result;
        }

        // 获取第一个对象的Class来确定字段
        Class<?> beanClass = beanList.get(0).getClass();
        if (isProxy(beanClass)) {
            beanClass = org.springframework.aop.support.AopUtils.getTargetClass(beanClass);
        }
        // 获取所有字段名（作为表头）
        Field[] fields = beanClass.getDeclaredFields();
        List<String> headers = new ArrayList<>();
        for (Field field : fields) {
            headers.add(field.getName());
        }
        // 添加表头
        result.add(headers.toArray(new String[0]));

        // 处理每个Bean对象
        for (Object bean : beanList) {
            List<String> values = new ArrayList<>();
            for (Field field : fields) {
                try {
                    // 通过getter方法获取值
                    String fieldName = field.getName();
                    String methodName = "get" +
                            fieldName.substring(0, 1).toUpperCase() +
                            fieldName.substring(1);

                    Method method = beanClass.getMethod(methodName);
                    Object value = method.invoke(bean);

                    // 处理特殊类型
                    String strValue;
                    if (value == null) {
                        strValue = "";
                    } else if (value instanceof Object[]) {
                        strValue = Arrays.toString((Object[]) value);
                    } else if (value.getClass().isArray()) {
                        strValue = arrayToString(value);
                    } else {
                        strValue = value.toString();
                    }

                    values.add(strValue);
                } catch (Exception e) {
                    values.add("");
                }
            }
            result.add(values.toArray(new String[0]));
        }

        return result;
    }

    // 处理基本类型数组
    private static String arrayToString(Object array) {
        if (array instanceof byte[])
            return Arrays.toString((byte[]) array);
        if (array instanceof short[])
            return Arrays.toString((short[]) array);
        if (array instanceof int[])
            return Arrays.toString((int[]) array);
        if (array instanceof long[])
            return Arrays.toString((long[]) array);
        if (array instanceof char[])
            return Arrays.toString((char[]) array);
        if (array instanceof float[])
            return Arrays.toString((float[]) array);
        if (array instanceof double[])
            return Arrays.toString((double[]) array);
        if (array instanceof boolean[])
            return Arrays.toString((boolean[]) array);
        return Arrays.toString((Object[]) array);
    }


    public static void main(String[] args) {
        // 示例用法
        List<IncreaseRankData> stocks = new ArrayList<>();
        IncreaseRankData bean = new IncreaseRankData();
        bean.setCode("600350");
        bean.setName("sssss");
        bean.setHeat(2224.5);
        bean.setRate(1.0);
        stocks.add(bean);

        List<String[]> result = PhoUtil.convertBeanListToStringList(stocks);

        // 打印转换结果
        for (String[] row : result) {
            System.out.println(Arrays.toString(row));
        }

//        createTableImage(data, "stock_table.png");
    }

    public static void createTableImage(List<String[]> data, String filename) {
        // 图片尺寸
//        int width = 1200;
//        int height = 600;
        // 定义表格参数
        int rows = data.size();       // 行数
        int cols = data.get(0).length;        // 列数
        int rowHeight = 40;   // 每行高度
        int colWidth = 100;   // 每列宽度
        int margin = 50;      // 边距

        // 计算图片尺寸
        int width = cols * colWidth + 2 * margin+200;  // 1200 像素
        int height = rows * rowHeight + 2 * margin; // 2100 像素

        // 创建缓冲图片
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        applyQualityRenderingHints(g);
        // 设置渲染质量
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // 背景渐变
        GradientPaint bgGradient = new GradientPaint(0, 0, new Color(240, 245, 255),
                0, height, new Color(220, 230, 255));
        g.setPaint(bgGradient);
        g.fillRect(0, 0, width, height);

        // 设置字体
        Font headerFont = new Font("微软雅黑", Font.BOLD, 18);
        Font dataFont = new Font("微软雅黑", Font.PLAIN, 14);
        Font titleFont = new Font("微软雅黑", Font.BOLD, 24);

        // 绘制标题
        g.setFont(titleFont);
        g.setColor(new Color(30, 50, 90));
        g.drawString(DateUtils.getNowDate() + "热度表", width / 2 - 120, 50);

        // 设置列宽
        int[] colWidths = {200, 100, 80, 80, 80, 80};

        // 绘制表头
        String[] headers = {"股票代码", "现价(元)","涨跌幅(%)",
                "大单净量", "热度", };

        g.setFont(headerFont);
        int y = 90;
        int x = 50;

        // 表头背景
        g.setColor(new Color(30, 70, 120));
        g.fillRoundRect(x, y, width - 100, 40, 10, 10);

        // 表头文字
        g.setColor(Color.WHITE);
        int headerX = x;
        for (int i = 0; i < headers.length; i++) {
            g.drawString(headers[i], headerX + 10, y + 27);
            headerX += colWidths[i];
        }

        // 绘制数据行
        g.setFont(dataFont);
        y += 60;
//        int rowHeight = 35;

        for (int row = 0; row < data.size(); row++) {
            // 行背景 (交替颜色)
            Color rowColor = (row % 2 == 0) ? new Color(255, 255, 255) : new Color(245, 249, 255);
            g.setColor(rowColor);
            g.fillRect(x, y, width - 100, rowHeight);

            // 行边框
            g.setColor(new Color(220, 230, 240));
            g.drawRect(x, y, width - 100, rowHeight);

            // 绘制单元格内容
            String[] rowData = data.get(row);
            int cellX = x;

            for (int col = 0; col < rowData.length; col++) {
                // 涨跌幅特殊样式
                if (col == 4) {
                    try {
                        double change = Double.parseDouble(rowData[col]);
                        g.setColor(change >= 0 ? new Color(220, 60, 50) : new Color(20, 140, 60));
                    } catch (Exception e) {
                        g.setColor(Color.BLACK);
                    }
                } else {
                    g.setColor(Color.BLACK);
                }

                // 处理长文本换行
                String text = rowData[col];
                if (col == 5 && text.length() > 28) {
                    text = text.substring(0, 25) + "...";
                }

                // 绘制文字
                g.drawString(text, cellX + 10, y + 24);

                // 绘制分隔线
                if (col < rowData.length - 1) {
                    g.setColor(new Color(235, 240, 245));
                    g.drawLine(cellX + colWidths[col], y,
                            cellX + colWidths[col], y + rowHeight);
                }

                cellX += colWidths[col];
            }

            y += rowHeight;
        }

        // 添加装饰元素
//        g.setColor(new Color(20, 140, 60, 100));
//        g.fillRect(0, height - 20, width, 20);

        g.setColor(new Color(255, 255, 255));
//        g.drawString("数据来源: 股票市场实时数据 | 生成时间: 2023-11-15", 20, height - 5);

        // 添加Logo
        g.setColor(new Color(30, 70, 120, 150));
        g.setFont(new Font("Arial", Font.ITALIC, 80));
//        g.drawString("STOCK", width - 250, height - 50);

        // 释放资源
        g.dispose();

        // 保存图片
        try {
            ImageIO.write(image, "PNG", new File(filename));
            System.out.println("图片生成成功: " + filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 简单代理检测（无Spring时）
    private static boolean isProxy(Object obj) {
        return obj != null && (obj.getClass().getName().contains("$$")
                || java.lang.reflect.Proxy.isProxyClass(obj.getClass()));
    }
    private static void applyQualityRenderingHints(Graphics2D g2d) {
        // 设置高质量的渲染参数
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
    }
}