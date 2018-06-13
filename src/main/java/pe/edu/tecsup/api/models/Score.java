package pe.edu.tecsup.api.models;

import java.util.List;

/**
 * Created by ebenites on 15/05/2017.
 */
public class Score {

    private String course;
    private String term;
    private Integer theoId;
    private String theoTitle;
    private Double theoScore;
    private Double theoWeight;
    private Integer labId;
    private String labTitle;
    private Double labScore;
    private Double labWeight;
    private Integer workId;
    private String workTitle;
    private Double workScore;
    private Double workWeight;
    private Integer partialId;
    private String partialTitle;
    private Double partialScore;
    private Double partialWeight;
    private Integer finalId;
    private String finalTitle;
    private Double finalScore;
    private Double finalWeight;
    private Double score;
    private String formula;
    private List<Item> theos;
    private List<Item> labs;
    private List<Item> works;

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public Integer getTheoId() {
        return theoId;
    }

    public void setTheoId(Integer theoId) {
        this.theoId = theoId;
    }

    public String getTheoTitle() {
        return theoTitle;
    }

    public void setTheoTitle(String theoTitle) {
        this.theoTitle = theoTitle;
    }

    public Double getTheoScore() {
        return theoScore;
    }

    public void setTheoScore(Double theoScore) {
        this.theoScore = theoScore;
    }

    public Double getTheoWeight() {
        return theoWeight;
    }

    public void setTheoWeight(Double theoWeight) {
        this.theoWeight = theoWeight;
    }

    public Integer getLabId() {
        return labId;
    }

    public void setLabId(Integer labId) {
        this.labId = labId;
    }

    public String getLabTitle() {
        return labTitle;
    }

    public void setLabTitle(String labTitle) {
        this.labTitle = labTitle;
    }

    public Double getLabScore() {
        return labScore;
    }

    public void setLabScore(Double labScore) {
        this.labScore = labScore;
    }

    public Double getLabWeight() {
        return labWeight;
    }

    public void setLabWeight(Double labWeight) {
        this.labWeight = labWeight;
    }

    public Integer getWorkId() {
        return workId;
    }

    public void setWorkId(Integer workId) {
        this.workId = workId;
    }

    public String getWorkTitle() {
        return workTitle;
    }

    public void setWorkTitle(String workTitle) {
        this.workTitle = workTitle;
    }

    public Double getWorkScore() {
        return workScore;
    }

    public void setWorkScore(Double workScore) {
        this.workScore = workScore;
    }

    public Double getWorkWeight() {
        return workWeight;
    }

    public void setWorkWeight(Double workWeight) {
        this.workWeight = workWeight;
    }

    public Integer getPartialId() {
        return partialId;
    }

    public void setPartialId(Integer partialId) {
        this.partialId = partialId;
    }

    public String getPartialTitle() {
        return partialTitle;
    }

    public void setPartialTitle(String partialTitle) {
        this.partialTitle = partialTitle;
    }

    public Double getPartialScore() {
        return partialScore;
    }

    public void setPartialScore(Double partialScore) {
        this.partialScore = partialScore;
    }

    public Double getPartialWeight() {
        return partialWeight;
    }

    public void setPartialWeight(Double partialWeight) {
        this.partialWeight = partialWeight;
    }

    public Integer getFinalId() {
        return finalId;
    }

    public void setFinalId(Integer finalId) {
        this.finalId = finalId;
    }

    public String getFinalTitle() {
        return finalTitle;
    }

    public void setFinalTitle(String finalTitle) {
        this.finalTitle = finalTitle;
    }

    public Double getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(Double finalScore) {
        this.finalScore = finalScore;
    }

    public Double getFinalWeight() {
        return finalWeight;
    }

    public void setFinalWeight(Double finalWeight) {
        this.finalWeight = finalWeight;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public List<Item> getTheos() {
        return theos;
    }

    public void setTheos(List<Item> theos) {
        this.theos = theos;
    }

    public List<Item> getLabs() {
        return labs;
    }

    public void setLabs(List<Item> labs) {
        this.labs = labs;
    }

    public List<Item> getWorks() {
        return works;
    }

    public void setWorks(List<Item> works) {
        this.works = works;
    }

    public static class Item {
        private String title;
        private Double score;
        private Integer weight;
        private String executed;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Double getScore() {
            return score;
        }

        public void setScore(Double score) {
            this.score = score;
        }

        public Integer getWeight() {
            return weight;
        }

        public void setWeight(Integer weight) {
            this.weight = weight;
        }

        public String getExecuted() {
            return executed;
        }

        public void setExecuted(String executed) {
            this.executed = executed;
        }

        @Override
        public String toString() {
            return "Item{" +
                    "title='" + title + '\'' +
                    ", score=" + score +
                    ", weight=" + weight +
                    ", executed=" + executed +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Score{" +
                "course='" + course + '\'' +
                ", term='" + term + '\'' +
                ", theoId=" + theoId +
                ", theoTitle='" + theoTitle + '\'' +
                ", theoScore=" + theoScore +
                ", theoWeight=" + theoWeight +
                ", labId=" + labId +
                ", labTitle='" + labTitle + '\'' +
                ", labScore=" + labScore +
                ", labWeight=" + labWeight +
                ", workId=" + workId +
                ", workTitle='" + workTitle + '\'' +
                ", workScore=" + workScore +
                ", workWeight=" + workWeight +
                ", partialId=" + partialId +
                ", partialTitle='" + partialTitle + '\'' +
                ", partialScore=" + partialScore +
                ", partialWeight=" + partialWeight +
                ", finalId=" + finalId +
                ", finalTitle='" + finalTitle + '\'' +
                ", finalScore=" + finalScore +
                ", finalWeight=" + finalWeight +
                ", score=" + score +
                ", formula='" + formula + '\'' +
                ", theos=" + theos +
                ", labs=" + labs +
                ", works=" + works +
                '}';
    }
}
