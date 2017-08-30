package ut.ee382n.ds.core;

/**
 * Created by dinhvan5481 on 8/30/17.
 */
public interface ICommand<T> {
    void execute();
    T getResult();
}
