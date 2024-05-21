package at.fhv.sysarch.lab3.pipeline.data;

import at.fhv.sysarch.lab3.pipeline.PullFilter;
import at.fhv.sysarch.lab3.pipeline.PullPipe;
import at.fhv.sysarch.lab3.pipeline.PushFilter;
import at.fhv.sysarch.lab3.pipeline.PushPipe;

public class Pipe<T> implements PullPipe<T>, PushPipe<T> {

    private PullFilter<T, ?> pullSuccessor;
    private PullFilter<T, ?> pullPredecessor;
    private PushFilter<T, ?> pushSuccessor;
    private PushFilter<T, ?> pushPredecessor;

    @Override
    public T read() {
        return this.pullPredecessor.read();
    }

    @Override
    public void write(T input) {
        this.pushSuccessor.write(input);
    }

    @Override
    public void setPullPredecessor(PullFilter<T, ?> pullPredecessor) {
        this.pullPredecessor = pullPredecessor;
    }

    @Override
    public void setPushSuccessor(PushFilter<T, ?> pushSuccessor) {
        this.pushSuccessor = pushSuccessor;
    }
}
