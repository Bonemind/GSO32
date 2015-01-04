package bank.observer;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Subhi on 4-1-2015.
 */
public interface IRemoteObservable extends Remote {
    public void addObserver(IRemoteObserver observer) throws RemoteException;
    public void removeObserver(IRemoteObserver observer) throws RemoteException;
    public void notifyObservers() throws RemoteException;
}
