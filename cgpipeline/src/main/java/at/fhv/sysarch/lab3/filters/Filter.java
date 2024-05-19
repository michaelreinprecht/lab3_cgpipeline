package at.fhv.sysarch.lab3.filters;

public interface Filter<I, O> {
    O process(I input);
}
