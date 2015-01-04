package bank.bankieren;

import java.io.Serializable;

public interface IKlant extends Serializable,Comparable<IKlant> {
  String getNaam();
  String getPlaats();
}

