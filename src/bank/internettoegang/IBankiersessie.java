package bank.internettoegang;

import java.rmi.Remote;
import java.rmi.RemoteException;

import bank.bankieren.IRekening;
import bank.bankieren.Money;
import bank.observer.IRemoteObservable;
import bank.observer.IRemoteObserver;
import fontys.util.InvalidSessionException;
import fontys.util.NumberDoesntExistException;

public interface IBankiersessie extends Remote, IRemoteObservable, IRemoteObserver {
	
	long GELDIGHEIDSDUUR = 600000;
	/**
	 * @returns true als de laatste aanroep van getRekening of maakOver voor deze
	 *          sessie minder dan GELDIGHEIDSDUUR geleden is
	 *          en er geen communicatiestoornis in de tussentijd is opgetreden, 
	 *          anders false
	 */
	boolean isGeldig() throws RemoteException; 

	/**
	 * er wordt bedrag overgemaakt van de bankrekening met het nummer bron naar
	 * de bankrekening met nummer bestemming
	 * 
	 * @param bron
	 * @param bestemming
	 *            is ongelijk aan rekeningnummer van deze bankiersessie
	 * @param bedrag
	 *            is groter dan 0
	 * @return <b>true</b> als de overmaking is gelukt, anders <b>false</b>
	 * @throws NumberDoesntExistException
	 *             als bestemming onbekend is
	 * @throws InvalidSessionException
	 *             als sessie niet meer geldig is 
	 */
	boolean maakOver(int bestemming, Money bedrag)
			throws NumberDoesntExistException, InvalidSessionException,
			RemoteException;

	/**
	 * sessie wordt beeindigd
	 */
	void logUit() throws RemoteException;

	/**
	 * @return de rekeninggegevens die horen bij deze sessie
	 * @throws InvalidSessionException
	 *             als de sessieId niet geldig of verlopen is
	 * @throws RemoteException
	 */
	IRekening getRekening() throws InvalidSessionException, RemoteException;
}
