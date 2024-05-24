package at.fhv.sysarch.lab3.pipeline.data;

public class Pair<T1, T2> {
    private T1 first;
    private T2 second;

    public Pair(T1 f, T2 s) {
        this.first = f;
        this.second = s;
    }

    public T1 first() {
        return this.first;
    }

    public T2 second() {
        return this.second;
    }
}