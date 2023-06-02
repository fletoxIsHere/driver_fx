package ma.fletox.javafx_drivers;


import ma.fletox.javafx_drivers.entities.Driver;
import ma.fletox.javafx_drivers.services.DriverService;

import static java.lang.Boolean.parseBoolean;
import static java.lang.Integer.parseInt;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;


public class addDriver {

    @FXML
    private TextField cin;
    @FXML
    private TextField nom;
    @FXML
    private TextField prenom;
    @FXML
    private TextField dateNaissance;
    @FXML
    private TextField status;
    private  boolean update;
    int driverID=0;

    SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");


    @FXML
    protected void onHelloButtonClick(ActionEvent event) throws IOException, ParseException {
        String cin = this.cin.getText();
        String nom = this.nom.getText();
        String prenom = this.prenom.getText();
        Date dateNaissance = formatter.parse(this.dateNaissance.getText());
        Boolean status = Boolean.parseBoolean(this.status.getText());

        DriverService driverService = new DriverService();
        Driver driver = new Driver(driverID,cin, nom, prenom,dateNaissance , status);
        if(update==false){
            driverService.save(driver);
        }
        else{
            driverService.update(driver,driverID);
        }
        onClearButtonClick();

        this.cin.clear();
        this.nom.clear();
        this.prenom.clear();
        this.dateNaissance.clear();
        this.status.clear();

        // Reload the driverTableView
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("driverTableView.fxml"));
        Parent root = fxmlLoader.load();
        TableViewController controller = fxmlLoader.getController();

        controller.setDriverService(driverService);
        controller.loadData();

        Scene newScene = new Scene(root);

        // Reapply the CSS styles to the new scene
        newScene.getStylesheets().add("styles.css");

        // Get the current stage
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        // Set the new scene on the current stage
        currentStage.setScene(newScene);
        currentStage.show();
    }



    private Stage stage;
    private Scene scene;
    private Parent root;
    public void onBackButtonClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("driverTableView.fxml"));
        Parent root = fxmlLoader.load();
        TableViewController controller = fxmlLoader.getController();
        DriverService driverService = new DriverService();
        controller.setDriverService(driverService);
        controller.loadData();

        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        scene.getStylesheets().addAll(primaryStage.getScene().getStylesheets()); // Add the current stylesheets to the new scene

        primaryStage.setScene(scene);
        primaryStage.show();
    }



    public void onClearButtonClick() {
        this.cin.clear();
        this.nom.clear();
        this.prenom.clear();
        this.dateNaissance.clear();
        this.status.clear();
    }

    public void setUpdate(boolean b) {
        this.update=b;
    }

    public void setTextField(int id, String cin, String nom, String prenom, Date dateNaissance, Boolean status) {
        this.driverID=id;
        this.cin.setText(cin);
        this.nom.setText(nom);
        this.prenom.setText(String.valueOf(prenom));
        this.dateNaissance.setText(String.valueOf(dateNaissance));
        this.status.setText(String.valueOf(status));

    }


}
