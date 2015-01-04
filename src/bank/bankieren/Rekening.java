package bank.bankieren;

import bank.observer.IRemoteObserver;

import java.rmi.RemoteException;
import java.util.*;

class Rekening implements IRekeningTbvBank {

    private static final long serialVersionUID = 7221569686169173632L;
    private static final int KREDIETLIMIET = -10000;
    private int nr;
    private IKlant eigenaar;
    private Money saldo;
    private ArrayList<IRemoteObserver> observerList;

    /**
     * creatie van een bankrekening met saldo van 0.0<br>
     * de constructor heeft package-access omdat de PersistentAccount-objecten door een
     * het PersistentBank-object worden beheerd
     * @see banking.persistence.PersistentBank
     * @param number het bankrekeningnummer
     * @param klant de eigenaar van deze rekening
     * @param currency de munteenheid waarin het saldo is uitgedrukt
     */
    Rekening(int number, IKlant klant, String currency) {
        this(number, klant, new Money(0, currency));
        observerList = new ArrayList<>();
    }

    /**
     * creatie van een bankrekening met saldo saldo<br>
     * de constructor heeft package-access omdat de PersistentAccount-objecten door een
     * het PersistentBank-object worden beheerd
     * @see banking.persistence.PersistentBank
     * @param number het bankrekeningnummer
     * @param name de naam van de eigenaar
     * @param city de woonplaats van de eigenaar
     * @param currency de munteenheid waarin het saldo is uitgedrukt
     */
    Rekening(int number, IKlant klant, Money saldo) {
        this.nr = number;
        this.eigenaar = klant;
        this.saldo = saldo;
    }

    public boolean equals(Object obj) {
        return nr == ((IRekening) obj).getNr();
    }

    public int getNr() {
        return nr;
    }

    public String toString() {
        return nr + ": " + eigenaar.toString();
    }

    boolean isTransferPossible(Money bedrag) {
        return (bedrag.getCents() + saldo.getCents() >= KREDIETLIMIET);
    }

    public IKlant getEigenaar() {
        return eigenaar;
    }

    public Money getSaldo() {
        return saldo;
    }

    public boolean muteer(Money bedrag) {
        if (bedrag.getCents() == 0) {
            throw new RuntimeException(" bedrag = 0 bij aanroep 'muteer'");
        }

        System.out.println("Muteer");
        if (isTransferPossible(bedrag)) {
            saldo = Money.sum(saldo, bedrag);
            notifyObservers();
            return true;
        }
        notifyObservers();
        return false;
    }

    @Override
    public int getKredietLimietInCenten() {
        return KREDIETLIMIET;
    }

    @Override
    public void addObserver(IRemoteObserver observer) {
        System.out.println("Observerlist before:" + observerList.size());
        if (observerList.contains(observer)) {
            return;
        }
        observerList.add(observer);
        System.out.println("Observerlist after:" + observerList.size());
    }

    @Override
    public void removeObserver(IRemoteObserver observer) {
        if (observerList.contains(observer)) {
            observerList.remove(observer);
        }
    }

    @Override
    public void notifyObservers() {
        for (IRemoteObserver ro : observerList) {
            try {
                ro.update(this, null);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
