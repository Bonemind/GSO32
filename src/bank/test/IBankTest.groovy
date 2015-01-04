package bank.test

import bank.bankieren.Bank
import bank.bankieren.IBank
import bank.bankieren.IKlant
import bank.bankieren.IRekening
import bank.bankieren.Money
import fontys.util.NumberDoesntExistException

import javax.management.monitor.MonitorMBean

/**
 * Created by Subhi on 4-1-2015.
 */
class IBankTest extends GroovyTestCase {
    IBank bank;
    private final String bankName = "TestBank";
    void setUp() {
        super.setUp()
        bank = new Bank(bankName);
    }

    void testOpenRekening() {
        String name = "Some name";
        String place = "Some Place";

        //Make sure we get -1 back when not passing a name or place
        int returnNumber = bank.openRekening(name, "");
        assertEquals(returnNumber, -1);
        returnNumber = bank.openRekening("", place);
        assertEquals(returnNumber, -1);

        //Check for valid creation
        returnNumber = bank.openRekening(name, place);
        assertFalse(returnNumber == -1);

        //Check if the account was actually created
        IRekening account = bank.getRekening(returnNumber);
        assertEquals(account.nr, returnNumber);
        IKlant customer = account.getEigenaar();

        //Check if the customer data was saved correctly
        assertEquals(customer.naam, name);
        assertEquals(customer.plaats, place);

        //Check if the same customer is reused when he is opening a new account
        int newAccount = bank.openRekening(name, place);
        assertFalse(-1 == newAccount);
        assertSame(customer, bank.getRekening(newAccount).eigenaar);

    }

    void testMaakOver() {
        int accountNumber1 = bank.openRekening("a", "b");
        int accountNumber2 = bank.openRekening("c", "d");

        //Make sure we can't transfer to the same account
        shouldFail{ bank.maakOver(accountNumber1, accountNumber1, new Money(200, Money.EURO)) };

        //Make sure we can't transfer money to a non existing account
        shouldFail(NumberDoesntExistException) {
            bank.maakOver(accountNumber1, -1, new Money(200, Money.EURO));
        }

        //Make sure we can't transfer negative amounts
        shouldFail{ bank.maakOver(accountNumber1, accountNumber2, new Money(-200, Money.EURO))};

        //Normal transfer
        int amount = 500;
        Money before = bank.getRekening(accountNumber2).saldo
        bank.maakOver(accountNumber1, accountNumber2, new Money(amount, Money.EURO));
        Money diff = new Money(amount, Money.EURO);
        assertEquals(Money.difference(bank.getRekening(accountNumber2).saldo, before), diff);

        //Transfer more than our available funds
        long toTransfer = Math.abs(bank.getRekening(accountNumber1).saldo.cents + bank.getRekening(accountNumber1).kredietLimietInCenten * 2);
        assertFalse(bank.maakOver(accountNumber1, accountNumber2, new Money(toTransfer, Money.EURO)));
    }

    void testGetRekening() {
        int number =  bank.openRekening("A", "B");
        IRekening account = bank.getRekening(number);

        //Account exists, return should not be null
        assertNotNull(account);

        //Account doesn't exist, return should be null
        assertNull(bank.getRekening(-1));
    }

    void testGetName() {
        assertEquals(bank.getName(), bankName);
    }
}
