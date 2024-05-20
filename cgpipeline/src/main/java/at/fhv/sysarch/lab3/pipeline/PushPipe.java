package at.fhv.sysarch.lab3.pipeline;

public interface PushPipe<T> {

    void write(T input);

    void setSuccessor(PushFilter<T, ?> successor);
}
