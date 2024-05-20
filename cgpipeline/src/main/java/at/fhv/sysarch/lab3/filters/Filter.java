package at.fhv.sysarch.lab3.filters;

public interface Filter<I, O> {
    O transform(I input);
}
