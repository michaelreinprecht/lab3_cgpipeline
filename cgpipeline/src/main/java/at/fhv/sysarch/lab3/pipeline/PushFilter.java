package at.fhv.sysarch.lab3.pipeline;

import at.fhv.sysarch.lab3.pipeline.data.Pipe;

public interface PushFilter<T, T1> {

    void write(T data);

    void setPipeSuccessor(Pipe<T1> successor);

    T1 transform(T input);
}