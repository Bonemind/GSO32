package bank.bankieren;

import fontys.util.*;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

public class Bank implements IBank {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8728841131739353765L;
	private Map<Integer,IRekeningTbvBank> accounts;
	private Collection<IKlant> clients;
	private volatile int nieuwReknr;
	private AtomicIntegerFieldUpdater<Bank> integerFieldUpdater = AtomicIntegerFieldUpdater.newUpdater(Bank.class, "nieuwReknr");
	private String name;
    private ITransferCentral transferCentral;

	public Bank(String name, ITransferCentral transferCentral) {
		accounts = new HashMap<Integer,IRekeningTbvBank>();
		clients = new ArrayList<IKlant>();
		nieuwReknr = 100000000;	
		this.name = name;
        this.transferCentral = transferCentral;
	}

	public int openRekening(String name, String city) throws RemoteException {
		if (name.equals("") || city.equals(""))
			return -1;

		IKlant klant = getKlant(name, city);
		IRekeningTbvBank account = new Rekening(nieuwReknr, klant, Money.EURO);
		synchronized (account) {
			accounts.put(nieuwReknr, account);
		}

		integerFieldUpdater.getAndIncrement(this);
		return integerFieldUpdater.get(this) - 1;
	}

	private IKlant getKlant(String name, String city) throws RemoteException {
		for (IKlant k : clients) {
			if (k.getNaam().equals(name) && k.getPlaats().equals(city))
				return k;
		}
		IKlant klant = new Klant(name, city);
		synchronized (clients) {
			clients.add(klant);
		}
		return klant;
	}

	public IRekening getRekening(int nr) throws RemoteException {
		return accounts.get(nr);
	}

	public boolean maakOver(int source, int destination, Money money)
			throws RemoteException, NumberDoesntExistException {
		if (source == destination)
			throw new RuntimeException(
					"cannot transfer money to your own account");
		if (!money.isPositive())
			throw new RuntimeException("money must be positive");

		synchronized (this) {
			IRekeningTbvBank source_account = (IRekeningTbvBank) getRekening(source);
			if (source_account == null)
				throw new NumberDoesntExistException("account " + source
						+ " unknown at " + name);

            // Schrijf het bedrag af van de bron rekening.
			Money negative = Money.difference(new Money(0, money.getCurrency()),
					money);
			if (!source_account.muteer(negative))
				return false;

            // Schrijf het bedrag bij bij de bestemming.
            try {
                transferCentral.maakOver(destination, money);
            } catch (NumberDoesntExistException ex) {
                // Als het bijschrijven niet gelukt is wordt het bedrag terug gestort op de begin rekening.
                source_account.muteer(money);
                return false;
            }

			return true;
		}
	}

    @Override
    public void maakOver(int bestemming, Money bedrag) throws RemoteException, NumberDoesntExistException {
        IRekeningTbvBank dest_account = (IRekeningTbvBank) getRekening(bestemming);
        if (dest_account == null)
            throw new NumberDoesntExistException("account " + bestemming
                    + " unknown at " + name);
        dest_account.muteer(bedrag);
    }

    @Override
	public String getName() throws RemoteException {
		return name;
	}

}
