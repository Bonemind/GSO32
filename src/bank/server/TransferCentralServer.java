package bank.server;

import bank.bankieren.Bank;
import bank.bankieren.ITransferCentral;
import bank.bankieren.TransferCentral;
import bank.internettoegang.Balie;
import bank.internettoegang.IBalie;

import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.Naming;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Michon on 5-1-2015.
 */
public class TransferCentralServer {
    public static void main(String[] argv) {
        try {
            String address = java.net.InetAddress.getLocalHost().getHostAddress();
            short port = 1098;
            String rmiAddress = "rmi://" + address + ":" + port + "/central";
            java.rmi.registry.LocateRegistry.createRegistry(port);
            TransferCentral transferCentral = new TransferCentral();
            Naming.rebind(rmiAddress, transferCentral);
        } catch (IOException ex) {
            Logger.getLogger(TransferCentralServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
