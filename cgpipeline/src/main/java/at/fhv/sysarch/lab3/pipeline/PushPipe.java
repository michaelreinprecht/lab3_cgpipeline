package at.fhv.sysarch.lab3.pipeline;

public interface PushPipe<T> {

    void write(T input);

    void setPushSuccessor(PushFilter<T, ?> pushSuccessor);
}
