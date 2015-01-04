package bank.observer;

/**
 * Created by Subhi on 4-1-2015.
 */
public interface IObservable {
    public void addObserver(IRemoteObserver observer);
    public void removeObserver(IRemoteObserver observer);
    public void notifyObservers();
}
