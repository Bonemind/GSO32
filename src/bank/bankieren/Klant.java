package bank.bankieren;

class Klant implements IKlant {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6216851042931199453L;

	private String naam;

	private String plaats;

	public Klant(String naam, String plaats) {
		this.naam = naam;
		this.plaats = plaats;
	}

	public String getNaam() {
		return naam;
	}

	public String getPlaats() {
		return plaats;
	}

	public int compareTo(IKlant arg0) {
		IKlant klant = (IKlant) arg0;
		int comp = naam.compareTo(klant.getNaam());
		if (comp!=0) return comp;
		return plaats.compareTo(klant.getPlaats());
	}
	
	public boolean equals(IKlant o) {
		return this.compareTo(o)==0;
	}
	
	public String toString() {
		return naam + " te " + "plaats";
	}

}
