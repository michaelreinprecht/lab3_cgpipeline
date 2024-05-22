package at.fhv.sysarch.lab3.pipeline;

import at.fhv.sysarch.lab3.pipeline.data.Pipe;

public interface PullFilter<I, O> {

    I read();

    void setPipePredecessor(Pipe<O> predecessor);

    I transform(O input);
}