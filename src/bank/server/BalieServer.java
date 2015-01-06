/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bank.server;

import bank.bankieren.Bank;
import bank.bankieren.IBank;
import bank.bankieren.ITransferCentral;
import bank.gui.BankierClient;
import bank.internettoegang.Balie;
import bank.internettoegang.IBalie;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author frankcoenen
 */
public class BalieServer extends Application {

    private Stage stage;
    private final double MINIMUM_WINDOW_WIDTH = 600.0;
    private final double MINIMUM_WINDOW_HEIGHT = 200.0;
    private String nameBank;

    @Override
    public void start(Stage primaryStage) throws IOException {

        try {
            stage = primaryStage;
            stage.setTitle("Bankieren");
            stage.setMinWidth(MINIMUM_WINDOW_WIDTH);
            stage.setMinHeight(MINIMUM_WINDOW_HEIGHT);
            gotoBankSelect();

            primaryStage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean startBalie(String nameBank) {
            
            FileOutputStream out = null;
            try {
                this.nameBank = nameBank;
                String address = java.net.InetAddress.getLocalHost().getHostAddress();
                int port = 1100;

                // Get the remote transfer central.
                ITransferCentral transferCentral = (ITransferCentral) Naming.lookup("rmi://" + address + ":1098" + "/central");

                // Setup this bank.
                Properties props = new Properties();
                String rmiBalie = "rmi://" + address + ":" + port + "/" + nameBank;
                props.setProperty("balie", rmiBalie);
                out = new FileOutputStream(nameBank + ".props");
                props.store(out, null);
                out.close();
                java.rmi.registry.LocateRegistry.createRegistry(port);
                IBank bank = new Bank(nameBank, transferCentral);
                IBalie balie = new Balie(bank);
                System.out.println(nameBank);
                Naming.rebind(rmiBalie, balie);

                // Register the bank with the transfer central.
                IBank stub = (IBank) UnicastRemoteObject.exportObject(bank, 0);
                transferCentral.register(stub);
               
                return true;

            } catch (IOException ex) {
                Logger.getLogger(BalieServer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NotBoundException ex) {
                Logger.getLogger(BalieServer.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    out.close();
                } catch (IOException ex) {
                    Logger.getLogger(BalieServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            return false;
    }

    public void gotoBankSelect() {
        try {
            BalieController bankSelect = (BalieController) replaceSceneContent("Balie.fxml");
            bankSelect.setApp(this);
        } catch (Exception ex) {
            Logger.getLogger(BankierClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Initializable replaceSceneContent(String fxml) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        InputStream in = BalieServer.class.getResourceAsStream(fxml);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(BalieServer.class.getResource(fxml));
        AnchorPane page;
        try {
            page = (AnchorPane) loader.load(in);
        } finally {
            in.close();
        }
        Scene scene = new Scene(page, 800, 600);
        stage.setScene(scene);
        stage.sizeToScene();
        return (Initializable) loader.getController();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
