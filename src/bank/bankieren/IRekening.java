package bank.bankieren;


import java.io.Serializable;
import java.util.Observable;

public interface IRekening extends Serializable {
  int getNr();
  Money getSaldo();
  IKlant getEigenaar();
  int getKredietLimietInCenten();
}

