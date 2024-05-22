package at.fhv.sysarch.lab3.pipeline;

import at.fhv.sysarch.lab3.pipeline.data.Pipe;

public interface PushFilter<I, O> {

    void write(I data);

    void setPipeSuccessor(Pipe<O> successor);

    O transform(I input);
}