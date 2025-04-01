package com.lin.bean.index;

import lombok.Data;

import java.util.List;

@Data
public class ConceptAndIndex {
    private List<Concept> increaseConceptList;
    private List<Concept> decreaseConceptList;
    private List<Index> indexList;

    @Data
    public static class Concept {
        private String concept;
        private double conceptPercent;
        private String leader;
        private double leaderPercent;
        private String downConcept;
        private double downConceptPercent;
        private Integer up;
        private Integer down;
    }

    @Data
    public static class Index {
        private String name;
        private double index;
        private double current;
    }
}
