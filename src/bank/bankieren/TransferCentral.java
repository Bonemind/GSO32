package bank.bankieren;

import fontys.util.NumberDoesntExistException;

import javax.naming.NameNotFoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michon on 5-1-2015.
 */
public class TransferCentral extends UnicastRemoteObject implements ITransferCentral {
    private List<IBank> banks = new ArrayList<>();

    public TransferCentral() throws RemoteException {
    }

    @Override
    public void maakOver(int bestemming, Money bedrag) throws NumberDoesntExistException, RemoteException {
        boolean success = false;
        for (IBank bank : banks) {
            try {
                bank.maakOver(bestemming, bedrag);
            } catch (Exception e) {
                continue;
            }
            success = true;
            break;
        }
        if (!success) {
            throw new NumberDoesntExistException("account " + bestemming
                    + " unknown at any of the banks at this transfercentral");
        }
    }

    @Override
    public void register(IBank bank) throws RemoteException {
        System.out.println("Registered bank: " + bank.getName());
        banks.add(bank);
    }
}
