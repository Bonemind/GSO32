package bank.test

import bank.bankieren.Bank
import bank.bankieren.IBank
import bank.internettoegang.Balie
import bank.internettoegang.IBalie
import bank.internettoegang.IBankiersessie

/**
 * Created by Subhi on 4-1-2015.
 */
class IBalieTest extends GroovyTestCase {
    IBank bank = new Bank("TestBank");
    IBalie balie = new Balie(bank);

    void testOpenRekening() {
        String name = "Some name";
        String place = "Some place";
        String password = "Somepass";

        //Make sure that all fields are validated
        assertNull(balie.openRekening("", place, password));
        assertNull(balie.openRekening(name, "", password));
        assertNull(balie.openRekening(name, place, ""));
        assertNull(balie.openRekening(name, place, "morethaneight"));
        assertNull(balie.openRekening(name, place, "aaa"));

        //Normal account creation
        String username = balie.openRekening(name, place, password);
        assertTrue(username.length() == 8);

        //Test login
        //Username invalid
        assertNull(balie.logIn("aassswwqq", "somepass"));
        //Password invalid
        assertNull(balie.logIn(username, "somepass"));
        IBankiersessie session = balie.logIn(username, password);
        assertNotNull(session);
    }
}
