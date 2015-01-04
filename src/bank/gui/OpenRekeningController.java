/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bank.gui;

import bank.internettoegang.IBalie;
import bank.internettoegang.IBankiersessie;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author frankcoenen
 */
public class OpenRekeningController  implements Initializable {

    @FXML
    private TextField tfName;
    @FXML
    private TextField tfCity;
    @FXML
    private TextField tfPassWord;
    @FXML
    private Button btRegister;

    private IBalie balie;
    private  BankierClient application;
       
    public void setApp(BankierClient application, IBalie balie) {
        this.application = application;
        this.balie = balie;
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tfName.setPromptText("Naam");
        tfCity.setPromptText("Woonplaats");
        tfPassWord.setPromptText("Wachtwoord");
    }

    @FXML
    private void registerAccount(ActionEvent event) {
        try {
            String accountNaam;
            accountNaam = balie.openRekening(tfName.getText(), tfCity.getText(), tfPassWord.getText());
            if (accountNaam == null) {
                return;
            }
            System.out.println("account:"+accountNaam);
            application.gotoLogin(balie, accountNaam);
        } catch (RemoteException ex) {
            Logger.getLogger(OpenRekeningController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
