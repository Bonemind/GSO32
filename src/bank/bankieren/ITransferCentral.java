package bank.bankieren;

import fontys.util.NumberDoesntExistException;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Michon on 5-1-2015.
 */
public interface ITransferCentral extends Remote {
    /**
     * er wordt bedrag overgemaakt naar de bankrekening met nummer bestemming
     *
     * @param bestemming
     * @param bedrag
     *            is groter dan 0
     * @throws NumberDoesntExistException
     *             als de bestemming niet bekend is
     */
    void maakOver(int bestemming, Money bedrag)
            throws RemoteException, NumberDoesntExistException;

    /**
     * Register a bank with this transfer central, so that it can send and receive transfers.
     *
     * @param bank The bank to add.
     */
    void register(IBank bank) throws RemoteException;
}
