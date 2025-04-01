package com.lin.util;

import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.WordCloud;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.bg.RectangleBackground;
import com.kennycason.kumo.font.KumoFont;
import com.kennycason.kumo.font.scale.LinearFontScalar;
import com.kennycason.kumo.palette.ColorPalette;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AdvancedConceptCloudUtil {
    public static void main(String[] args) throws IOException {

    }

    public static void generate(String input) {

        // 1. 数据预处理
        List<String> concepts = processInput(input);

        // 2. 生成词频数据
        List<WordFrequency> wordFrequencies = buildWordFrequencies(concepts);

        // 3. 创建词云
        try {
            generateWordCloud(wordFrequencies, "D:\\1stock\\最强概念.png");
        } catch (Exception e) {

        }
    }

    private static List<String> processInput(String input) {
        return Arrays.stream(input.split("\n"))
                .flatMap(line -> Arrays.stream(line.split("\\+")))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

    private static List<WordFrequency> buildWordFrequencies(List<String> concepts) {
        Map<String, Long> frequencyMap = concepts.stream()
                .collect(Collectors.groupingBy(k -> k, Collectors.counting()));

        return frequencyMap.entrySet().stream()
                .map(entry -> new WordFrequency(entry.getKey(), entry.getValue().intValue()))
                .collect(Collectors.toList());
    }

    private static void generateWordCloud(List<WordFrequency> wordFrequencies,
                                          String outputFile) throws IOException {
        // 创建词云对象
        Dimension dimension = new Dimension(1200, 800);
        WordCloud wordCloud = new WordCloud(dimension, CollisionMode.RECTANGLE);

        // 设置配置参数
        wordCloud.setPadding(2);
        wordCloud.setBackground(new RectangleBackground(dimension));
        wordCloud.setBackgroundColor(Color.WHITE);

        // 设置字体缩放 (最小12px - 最大50px)
        wordCloud.setFontScalar(new LinearFontScalar(12, 50));
        //此处不设置会出现中文乱码
        java.awt.Font font = new java.awt.Font("STSong-Light", 2, 18);
        // 不设置font的话，中文会乱码
        wordCloud.setKumoFont(new KumoFont(font));

        // 设置调色板 (可自定义颜色组合)
        wordCloud.setColorPalette(new ColorPalette(
                new Color(0xF14040),
                new Color(0xF16F40),
                new Color(0x58F268),
                new Color(0x40C5F1),
                new Color(0x737070),
                new Color(0xFFFFFF)
        ));

        // 加载数据并生成
        wordCloud.build(wordFrequencies);
        wordCloud.writeToFile(outputFile);
    }
}


