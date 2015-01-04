package bank.observer;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Subhi on 4-1-2015.
 */
public interface IRemoteObserver extends Remote, Serializable {
    void update(Object observable, Object updateMessage) throws RemoteException;
}
