package bank.observer;

import bank.gui.BankierSessieController;
import fontys.util.InvalidSessionException;
import javafx.application.Platform;

import java.rmi.RemoteException;

/**
 * Created by Subhi on 4-1-2015.
 */
public class RemoteObserver implements IRemoteObserver{
    private BankierSessieController parent;
    public RemoteObserver(BankierSessieController parent) {
        this.parent = parent;
    }
    @Override
    public void update(Object observable, Object updateMessage) throws RemoteException {
        try {
            parent.refreshBalance();
        } catch (InvalidSessionException e) {
            e.printStackTrace();
        }
    }
}
