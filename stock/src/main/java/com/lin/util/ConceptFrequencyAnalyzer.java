package com.lin.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ConceptFrequencyAnalyzer {
    public static void main(String[] args) {

    }

    public static void getWord(String input) {

        // 1. 拆分并清洗概念
        List<String> concepts = Arrays.stream(input.split("\\+"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());

        // 2. 统计词频
        Map<String, Integer> frequencyMap = new HashMap<>();
        for (String concept : concepts) {
            frequencyMap.put(concept, frequencyMap.getOrDefault(concept, 0) + 1);
        }

        // 3. 排序（按频率降序，同频按字母顺序）
        List<Map.Entry<String, Integer>> sorted = frequencyMap.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed()
                        .thenComparing(Map.Entry.comparingByKey()))
                .collect(Collectors.toList());

        // 4. 输出结果
        System.out.println("概念出现频率排序：");
        System.out.println("| 概念内容                    | 出现次数 |");
        System.out.println("|---------------------------|----------|");
        for (Map.Entry<String, Integer> entry : sorted) {
            System.out.printf("| %-25s | %-8d |\n", entry.getKey(), entry.getValue());
        }
    }
}
