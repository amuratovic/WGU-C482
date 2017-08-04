/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import Model.Inventory;
import Model.Part;
import Model.Product;
import static Model.Inventory.getPartInventory;

/**
 * FXML Controller class
 *
 * @author almirmuratovic
 */
public class AddProductsController implements Initializable {

    private ObservableList<Part> currentParts = FXCollections.observableArrayList();
    private String exceptionMessage = new String();
    private int productID;

    @FXML
    private TextField AddProductsIDField;
    @FXML
    private TextField AddProductsNameField;
    @FXML
    private TextField AddProductsPriceField;
    @FXML
    private TextField AddProductsInvField;
    @FXML
    private TextField AddProductsMinField;
    @FXML
    private TextField AddProductsMaxField;
    @FXML
    private TextField AddProductDeletePartSearchField;
    @FXML
    private TextField AddProductAddPartSearchField;
    @FXML
    private TableView<Part> AddProductsAddTableView;
    @FXML
    private TableColumn<Part, Integer> AddProductPartIDCol;
    @FXML
    private TableColumn<Part, String> AddProductPartNameCol;
    @FXML
    private TableColumn<Part, Integer> AddProductInvLevelCol;
    @FXML
    private TableColumn<Part, Double> AddProductPriceCol;
    @FXML
    private TableView<Part> AddProductsDeleteTableView;
    @FXML
    private TableColumn<Part, Integer> AddProductCurrentPartIDCol;
    @FXML
    private TableColumn<Part, String> AddProductCurrentPartNameCol;
    @FXML
    private TableColumn<Part, Integer> AddProductCurrentInvCol;
    @FXML
    private TableColumn<Part, Double> AddProductCurrentPriceCol;

    public AddProductsController() {
    }

        @FXML void ClearSearchAdd(ActionEvent event) {
        updatePartTableView();
        AddProductAddPartSearchField.setText("");
    }
        
       @FXML void ClearSearchRemove(ActionEvent event) {
        updateCurrentPartTableView();
        AddProductDeletePartSearchField.setText("");
    }
    
    @FXML
    void AddProductsSearchPartAddBtn(ActionEvent event) {

        String searchPart = AddProductAddPartSearchField.getText();
        int partIndex = -1;

        if (Inventory.lookupPart(searchPart) == -1) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Search");
            alert.setHeaderText("Part not found");
            alert.setContentText("The part has not been found.");
            alert.showAndWait();
        } else {
            partIndex = Inventory.lookupPart(searchPart);
            Part tempPart = Inventory.getPartInventory().get(partIndex);

            ObservableList<Part> tempProdList = FXCollections.observableArrayList();
            tempProdList.add(tempPart);
            AddProductsAddTableView.setItems(tempProdList);
        }
    }


    @FXML
    void AddProductsAddPartBtn(ActionEvent event) {
        Part part = AddProductsAddTableView.getSelectionModel().getSelectedItem();
        currentParts.add(part);
        updateCurrentPartTableView();
    }

    @FXML
    void AddProductsSearchPartDeleteBtn(ActionEvent event) {

        String searchPart = AddProductDeletePartSearchField.getText();
        int partIndex = -1;

        if (Inventory.lookupPart(searchPart) == -1) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Search");
            alert.setHeaderText("Part not found");
            alert.setContentText("The part has not been found.");
            alert.showAndWait();
        } else {
            partIndex = Inventory.lookupPart(searchPart);
            Part tempPart = Inventory.getPartInventory().get(partIndex);

            ObservableList<Part> tempProdList = FXCollections.observableArrayList();
            tempProdList.add(tempPart);
            AddProductsDeleteTableView.setItems(tempProdList);
        }
    }

    

    @FXML
    void AddProductsDeletePartBtn(ActionEvent event) {

        Part part = AddProductsDeleteTableView.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Confirm Current Part Delete!");
        alert.setContentText("Are you sure you want to delete part " + part.getPartName() + " from parts?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            System.out.println("Part deleted.");
            currentParts.remove(part);
        } else {
            System.out.println("Cancel clicked.");
        }
    }

    @FXML
    void AddProductsSaveButtonClicked(ActionEvent event) throws IOException {

        String productName = AddProductsNameField.getText();
        String productInv = AddProductsInvField.getText();
        String productPrice = AddProductsPriceField.getText();
        String productMin = AddProductsMinField.getText();
        String productMax = AddProductsMaxField.getText();

        try {

            exceptionMessage = Product.isProductValid(productName, Integer.parseInt(productMin), Integer.parseInt(productMax), Integer.parseInt(productInv),
                    Double.parseDouble(productPrice), currentParts, exceptionMessage);

            if (exceptionMessage.length() > 0) {

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error Adding Product");
                alert.setHeaderText("Error");
                alert.setContentText(exceptionMessage);
                alert.showAndWait();
                exceptionMessage = "";
            } else {
                System.out.println("Product name: " + productName);

                Product newProduct = new Product();

                newProduct.setProductID(productID);
                newProduct.setProductName(productName);
                newProduct.setProductPrice(Double.parseDouble(productPrice));
                newProduct.setProductInStock(Integer.parseInt(productInv));
                newProduct.setProductMin(Integer.parseInt(productMin));
                newProduct.setProductMax(Integer.parseInt(productMax));
                newProduct.setProductParts(currentParts);
                Inventory.addProduct(newProduct);

                Parent productsSave = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
                Scene scene = new Scene(productsSave);

                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

                window.setScene(scene);
                window.show();
            }
        } catch (NumberFormatException e) {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error Adding Part!");
            alert.setHeaderText("Error!");
            alert.setContentText("Fields cannot be left blank!");
            alert.showAndWait();
        }
    }

    @FXML
    void AddProductsCancelClicked(ActionEvent event) throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirmation Needed!");
        alert.setHeaderText("Confirm Product Delete!");
        alert.setContentText("Are you sure you want to delete product " + AddProductsNameField.getText() + "?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {

            Parent partsCancel = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
            Scene scene = new Scene(partsCancel);

            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

            window.setScene(scene);
            window.show();
        } else {
            System.out.println("You clicked cancel. Please complete part info.");
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        AddProductPartIDCol.setCellValueFactory(cellData -> cellData.getValue().partIDProperty().asObject());
        AddProductPartNameCol.setCellValueFactory(cellData -> cellData.getValue().partNameProperty());
        AddProductInvLevelCol.setCellValueFactory(cellData -> cellData.getValue().partInvProperty().asObject());
        AddProductPriceCol.setCellValueFactory(cellData -> cellData.getValue().partPriceProperty().asObject());

        AddProductCurrentPartIDCol.setCellValueFactory(cellData -> cellData.getValue().partIDProperty().asObject());
        AddProductCurrentPartNameCol.setCellValueFactory(cellData -> cellData.getValue().partNameProperty());
        AddProductCurrentInvCol.setCellValueFactory(cellData -> cellData.getValue().partInvProperty().asObject());
        AddProductCurrentPriceCol.setCellValueFactory(cellData -> cellData.getValue().partPriceProperty().asObject());

        updatePartTableView();
        updateCurrentPartTableView();

        productID = Inventory.getProductIDCount();
        AddProductsIDField.setText("AUTO GEN: " + productID);
    }

    public void updatePartTableView() {
        AddProductsAddTableView.setItems(getPartInventory());
    }

    public void updateCurrentPartTableView() {
        AddProductsDeleteTableView.setItems(currentParts);
    }
}
