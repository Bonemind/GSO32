package bank.test

import bank.bankieren.Bank
import bank.bankieren.IBank
import bank.bankieren.IRekening
import bank.bankieren.Money
import bank.internettoegang.Balie
import bank.internettoegang.IBalie
import bank.internettoegang.IBankiersessie
import fontys.util.InvalidSessionException
import fontys.util.NumberDoesntExistException
import groovy.util.GroovyTestCase

import java.rmi.NoSuchObjectException
import java.rmi.server.ExportException
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by Subhi on 4-1-2015.
 */
class IBankiersessieTest extends GroovyTestCase {

    private IBank bank;
    private IBalie balie;
    private IBankiersessie bankiersessie;
    private String name = "Some name";
    private String place = "Some place";
    private String username;
    private String password = "Somepass";
    void setUp() {
        super.setUp();
        bank = new Bank("TestBank");
        balie = new Balie(bank);
        username = balie.openRekening(name, place, password);
        bankiersessie = balie.logIn(username, password);
    }

    void testIsGeldig() {
        assertTrue(bankiersessie.isGeldig());
        sleep(6001);
        assertFalse(bankiersessie.isGeldig());
    }

    void testMaakOver() {
        //Invalid account
        shouldFail(NumberDoesntExistException) {
            bankiersessie.maakOver(-1, new Money(200, Money.EURO))
        };
        //Own account
        shouldFail {
            bankiersessie.maakOver(bankiersessie.getRekening().nr, new Money(200, Money.EURO));
        }
        String accName = balie.openRekening(name, place + "2", password);
        IBankiersessie othersession = balie.logIn(accName, password)

        //Negative transfer
        shouldFail {
            bankiersessie.maakOver(othersession.rekening.nr, new Money(-100, Money.EURO));
        }

        //More money than we have available
        long ToTransfer = Math.abs(bankiersessie.rekening.saldo.cents + bankiersessie.rekening.kredietLimietInCenten) * 2;
        assertFalse(bankiersessie.maakOver(othersession.rekening.nr, new Money(ToTransfer, Money.EURO)));

        //Successfull transfer
        //Transfer all available money, so we can just compare to the creditlimit
        Long totalAmount = Math.abs(bankiersessie.rekening.saldo.cents - bankiersessie.rekening.kredietLimietInCenten);
        assertTrue(bankiersessie.maakOver(othersession.rekening.nr, new Money(totalAmount, Money.EURO)));
        assertEquals(bankiersessie.rekening.saldo.cents, bankiersessie.rekening.kredietLimietInCenten);

        //Check if sessions are actually validated
        sleep(6001);
        shouldFail(InvalidSessionException) {
            othersession.maakOver(bankiersessie.rekening.nr, new Money(200, Money.EURO));
        }
    }

    void testLogUit() {
        shouldFail(ExportException) {
            UnicastRemoteObject.exportObject(bankiersessie, 0)
        }
        bankiersessie.logUit();
        shouldFail(NoSuchObjectException) {
            UnicastRemoteObject.unexportObject(bankiersessie, true);
        }
    }

    void testGetRekening() {
        //Make sure this is our account
        IRekening account = bankiersessie.getRekening();
        assertEquals(name, account.getEigenaar().naam);
        assertEquals(place, account.getEigenaar().plaats);

        //Wait for the session to expire, then try to fetch the account again
        sleep(6001);
        shouldFail(InvalidSessionException) {
            bankiersessie.getRekening();
        }

    }
}
