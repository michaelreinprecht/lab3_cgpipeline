package at.fhv.sysarch.lab3.pipeline;

public interface PullPipe<T> {

    T read();

    void setPullPredecessor(PullFilter<T, ?> pullPredecessor);
}
