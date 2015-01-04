package bank.internettoegang;


public class LoginAccount implements ILoginAccount {

	private String naam;
	private String wachtwoord;
	private int reknr;

	public LoginAccount(String naam, String wachtwoord, int rekening) {
		this.naam = naam;
		this.wachtwoord = wachtwoord;
		this.reknr = rekening;
	}

	public boolean checkWachtwoord(String wachtwoord) {
		return this.wachtwoord.equals(wachtwoord);
	}

	public String getNaam() {
		return naam;
	}

	public int getReknr() {
		return reknr;
	}

}
