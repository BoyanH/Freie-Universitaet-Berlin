package fu.alp3;

public class ShortestPathEntry {

    private Integer distance;
    private Integer prev;

    public ShortestPathEntry(Integer dis, Integer previous) {

        this.distance = dis;
        this.prev = previous;
    }

    public Integer getDistance() {

        return this.distance;
    }

    public Integer getPrev() {

        return this.prev;
    }
}
