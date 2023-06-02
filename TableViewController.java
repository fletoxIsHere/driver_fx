package ma.fletox.javafx_drivers;

import ma.fletox.javafx_drivers.entities.Driver;
import ma.fletox.javafx_drivers.services.DriverService;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


public class TableViewController implements Initializable {

    @FXML
    private TableColumn<Driver, Integer> id;
    @FXML
    private TableColumn<Driver, String> cin;
    @FXML
    private TableColumn<Driver, String> nom;
    @FXML
    private TableColumn<Driver, Integer> prenom;
    @FXML
    private TableColumn<Driver, Double> dateNaissance;
    @FXML
    private TableColumn<Driver, Integer> status;

    @FXML
    private TableView<Driver> drivertable;

    @FXML
    private TextField rowCountLabel;
    @FXML
    private Button ajouter;
    @FXML
    private Label driverCountText;
    private DriverService driverService;

    private IntegerProperty rowCount;
    @FXML
    private Label driverCountLabel;

    ObservableList<Driver> driverList = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configureTableColumns();
        addDeleteAndUpdateButtons();
        driverService = new DriverService();
        updateDriverCountLabel();
    }

    private void updateDriverCountLabel() {
        int driverCount = driverService.getNumberOfDrivers();
        driverCountLabel.setText(" (" + driverCount + " drivers)");
    }

    private void configureTableColumns() {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        cin.setCellValueFactory(new PropertyValueFactory<>("cin"));
        nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        dateNaissance.setCellValueFactory(new PropertyValueFactory<>("dateNaissance"));
        status.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    void setDriverService(DriverService driverService) {
        this.driverService = driverService;
    }

    void loadData() {
        driverList.addAll(driverService.findAll());
        drivertable.setItems(driverList);
    }

    @FXML
    private void handleAjouterButtonAction(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("addDriverForms.fxml"));
            Parent afficherPage = loader.load();

            // Create the scene to display the loaded FXML file
            Scene afficherScene = new Scene(afficherPage);

            // Load the styles.css file
            afficherScene.getStylesheets().add("styles.css");

            // Get the primary stage and update its scene
            Stage primaryStage = (Stage) ajouter.getScene().getWindow();
            primaryStage.setScene(afficherScene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public  void exporter()
    {
        driverService.exporterVersExcel("src/main/resources/fileExcel.xlsx");
    }
    private Stage stage;
    private Scene scene;
    @FXML
    public  void importer(ActionEvent event) throws IOException {
        driverService.importerDepuisExcel("src/main/resources/fileExcel2.xlsx");
        reload(event);


    }
    private void addDeleteAndUpdateButtons(){
        // Create the "Supprimer" column
        TableColumn<Driver, Void> deleteColumn = new TableColumn<>("Supprimer");
        deleteColumn.setCellFactory(new Callback<TableColumn<Driver, Void>, TableCell<Driver, Void>>() {
            @Override
            public TableCell<Driver, Void> call(TableColumn<Driver, Void> param) {
                return new TableCell<Driver, Void>() {
                    private final Button deleteButton = new Button("Delete");

                    {
                        deleteButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
                        deleteButton.setOnAction(event -> {
                            Driver driver = getTableView().getItems().get(getIndex());
                            driverService.delete(driver);
                            try {
                                reload(event);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    }


                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(deleteButton);
                        }
                    }
                };
            }
        });

        // Create the "Modifier" column
        TableColumn<Driver, Void> updateColumn = new TableColumn<>("Modifier");
        updateColumn.setCellFactory(new Callback<TableColumn<Driver, Void>, TableCell<Driver, Void>>() {
            @Override
            public TableCell<Driver, Void> call(TableColumn<Driver, Void> param) {
                return new TableCell<Driver, Void>() {
                    private final Button updateButton = new Button("Update");

                    {
                        updateButton.setOnAction(event -> {
                            Driver driver = getTableView().getItems().get(getIndex());

                            FXMLLoader loader = new FXMLLoader(getClass().getResource("updateDriverForm.fxml"));
                            try {
                                Parent root = loader.load();
                                addDriver addDriverController = loader.getController();
                                addDriverController.setUpdate(true);
                                addDriverController.setTextField(driver.getId(), driver.getCin(), driver.getNom(), driver.getPrenom(), driver.getDateNaissance(), driver.getStatus());

                                Scene scene = new Scene(root);
                                scene.getStylesheets().add("styles.css");

                                Stage stage = new Stage();
                                stage.setScene(scene);
                                stage.initStyle(StageStyle.UTILITY);
                                stage.show();
                            } catch (IOException e) {
                                Logger.getLogger(TableViewController.class.getName()).log(Level.SEVERE, null, e);
                            }
                        });

                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(updateButton);
                        }
                    }
                };
            }
        });

        // Add the delete and update columns to the TableView
        drivertable.getColumns().add(deleteColumn);
        drivertable.getColumns().add(updateColumn);
    }


}