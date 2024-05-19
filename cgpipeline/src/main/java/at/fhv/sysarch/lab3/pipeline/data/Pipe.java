package at.fhv.sysarch.lab3.pipeline.data;

import at.fhv.sysarch.lab3.pipeline.PullFilter;
import at.fhv.sysarch.lab3.pipeline.PullPipe;
import at.fhv.sysarch.lab3.pipeline.PushFilter;
import at.fhv.sysarch.lab3.pipeline.PushPipe;

public class Pipe<I> implements PullPipe<I>, PushPipe<I> {

    private PullFilter<I, ?> predecessor;
    private PushFilter<I, ?> successor;

    @Override
    public I read() {
        return this.predecessor.read();
    }

    @Override
    public void write(I input) {
        this.successor.write(input);
    }

    @Override
    public void setPredecessor(PullFilter<I, ?> predecessor) {
        this.predecessor = predecessor;
    }

    @Override
    public void setSuccessor(PushFilter<I, ?> successor) {
        this.successor = successor;
    }

}
