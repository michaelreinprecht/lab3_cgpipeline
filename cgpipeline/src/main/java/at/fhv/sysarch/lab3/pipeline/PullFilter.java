package at.fhv.sysarch.lab3.pipeline;

import at.fhv.sysarch.lab3.pipeline.data.Pipe;

public interface PullFilter<T, T1> {

    T read();

    void setPipePredecessor(Pipe<T1> predecessor);

    T transform(T1 input);
}