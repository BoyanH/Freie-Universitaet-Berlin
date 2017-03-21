package fu.alp3;

public class RateEntry {

    public RateEntry(int idx, float grd) {

        this.index = idx;
        this.grade = grd;
    }

    private int index;
    private float grade;

    public int getIndex() {

        return this.index;
    }

    public float getGrade() {

        return this.grade;
    }

}
