package bank.internettoegang;


public interface ILoginAccount {
	  String getNaam();
	  int getReknr();
	  boolean checkWachtwoord(String wachtwoord);
	}

