package bank.bankieren;


import bank.observer.IObservable;
import bank.observer.IRemoteObservable;

import java.io.Serializable;

public interface IRekening extends Serializable, IObservable {
  int getNr();
  Money getSaldo();
  IKlant getEigenaar();
  int getKredietLimietInCenten();
}

