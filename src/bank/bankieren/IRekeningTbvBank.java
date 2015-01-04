package bank.bankieren;

interface IRekeningTbvBank extends IRekening {
	/**
	 * het saldo van deze bankrekening wordt met bedrag aangepast, tenzij het
	 * saldotekort groter wordt dan het maximale krediet, dan verandert er niets
	 * 
	 * @param bedrag
	 *            is ongelijk aan 0
	 * @return (saldo + bedrag) >= -(maximaal krediet)
	 */
	boolean muteer(Money bedrag);
	

}
