package bank.bankieren;

import fontys.util.*;

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

	public Bank(String name) {
		accounts = new HashMap<Integer,IRekeningTbvBank>();
		clients = new ArrayList<IKlant>();
		nieuwReknr = 100000000;	
		this.name = name;	
	}

	public int openRekening(String name, String city) {
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

	private IKlant getKlant(String name, String city) {
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

	public IRekening getRekening(int nr) {
		return accounts.get(nr);
	}

	public boolean maakOver(int source, int destination, Money money)
			throws NumberDoesntExistException {
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

			Money negative = Money.difference(new Money(0, money.getCurrency()),
					money);
			boolean success = source_account.muteer(negative);
			if (!success)
				return false;

			IRekeningTbvBank dest_account = (IRekeningTbvBank) getRekening(destination);
			if (dest_account == null)
				throw new NumberDoesntExistException("account " + destination
						+ " unknown at " + name);
			success = dest_account.muteer(money);

			if (!success) // rollback
				source_account.muteer(money);
			return success;
		}
	}

	@Override
	public String getName() {
		return name;
	}

}
