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
import static View_Controller.MainScreenController.productToModifyIndex;
import static Model.Inventory.getPartInventory;
import static Model.Inventory.getProductInventory;

/**
 * FXML Controller class
 *
 * @author almirmuratovic
 */
public class ModifyProductsController implements Initializable {

    private ObservableList<Part> currentParts = FXCollections.observableArrayList();
    private int productIndex = productToModifyIndex();
    private String exceptionMessage = new String();
    private int productID;

    @FXML
    private TextField ModifyProductsIDField;
    @FXML
    private TextField ModifyProductsMinField;
    @FXML
    private TextField ModifyProductsMaxField;
    @FXML
    private TextField ModifyProductsInvField;
    @FXML
    private TextField ModifyProductsNameField;
    @FXML
    private TextField ModifyProductsPriceField;
    @FXML
    private TextField ModifyProductAddPartSearchField;
    @FXML
    private TextField ModifyProductDeletePartSearchField;
    @FXML
    private TableView<Part> ModifyProductAddTableView;
    @FXML
    private TableColumn<Part, Integer> ModifyProductPartIDCol;
    @FXML
    private TableColumn<Part, String> ModifyProductPartNameCol;
    @FXML
    private TableColumn<Part, Integer> ModifyProductPartInvCol;
    @FXML
    private TableColumn<Part, Double> ModifyProductPartPriceCol;
    @FXML
    private TableView<Part> ModifyProductDeleteTableView;
    @FXML
    private TableColumn<Part, Integer> ModifyProductCurrentPartIDCol;
    @FXML
    private TableColumn<Part, String> ModifyProductCurrentPartNameCol;
    @FXML
    private TableColumn<Part, Integer> ModifyProductCurrentPartInvCol;
    @FXML
    private TableColumn<Part, Double> ModifyProductCurrentPartPriceCol;

    @FXML
    void ClearSearchAdd(ActionEvent event) {
        updatePartTableView();
        ModifyProductAddPartSearchField.setText("");
    }

    @FXML
    void ClearSearchRemove(ActionEvent event) {
        updateCurrentPartTableView();
        ModifyProductDeletePartSearchField.setText("");
    }

    @FXML
    void ModifyProductsSearchPartAddBtn(ActionEvent event) {
        String searchPart = ModifyProductAddPartSearchField.getText();
        int partIndex = -1;
        if (Inventory.lookupPart(searchPart) == -1) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Search");
            alert.setHeaderText("Part not found");
            alert.setContentText("Search term not found.");
            alert.showAndWait();
        } else {
            partIndex = Inventory.lookupPart(searchPart);
            Part tempPart = Inventory.getPartInventory().get(partIndex);
            ObservableList<Part> tempProdList = FXCollections.observableArrayList();
            tempProdList.add(tempPart);
            ModifyProductAddTableView.setItems(tempProdList);
        }
    }

    @FXML
    void ModifyProductsAddButton(ActionEvent event) {
        Part part = ModifyProductAddTableView.getSelectionModel().getSelectedItem();
        currentParts.add(part);
        updateCurrentPartTableView();
    }

    @FXML
    void ModifyProductsSearchPartDeleteBtn(ActionEvent event) {
        String searchPart = ModifyProductDeletePartSearchField.getText();
        int partIndex = -1;

        if (Inventory.lookupPart(searchPart) == -1) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Search");
            alert.setHeaderText("Part not found");
            alert.setContentText("Search term not found.");
            alert.showAndWait();
        } else {
            partIndex = Inventory.lookupPart(searchPart);
            Part tempPart = Inventory.getPartInventory().get(partIndex);

            ObservableList<Part> tempProdList = FXCollections.observableArrayList();
            tempProdList.add(tempPart);
            ModifyProductDeleteTableView.setItems(tempProdList);
        }
    }

    @FXML
    void ModifyProductsDeleteButton(ActionEvent event) {
        Part part = ModifyProductDeleteTableView.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Confirm Part Delete");
        alert.setContentText("Are you sure you want to delete " + part.getPartName() + " ?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            currentParts.remove(part);
        } else {
            System.out.println("Cancel was clicked.");
        }
    }

    @FXML
    void ModifyProductsSaveButtonClicked(ActionEvent event) throws IOException {
        String productName = ModifyProductsNameField.getText();
        String productInv = ModifyProductsInvField.getText();
        String productPrice = ModifyProductsPriceField.getText();
        String productMin = ModifyProductsMinField.getText();
        String productMax = ModifyProductsMaxField.getText();
        try {
            exceptionMessage = Product.isProductValid(productName, Integer.parseInt(productMin), Integer.parseInt(productMax), Integer.parseInt(productInv),
                    Double.parseDouble(productPrice), currentParts, exceptionMessage);
            if (exceptionMessage.length() > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error Adding Product!");
                alert.setHeaderText("Error!");
                alert.setContentText(exceptionMessage);
                alert.showAndWait();
            } else {
                Product newProduct = new Product();
                newProduct.setProductID(productID);
                newProduct.setProductName(productName);
                newProduct.setProductPrice(Double.parseDouble(productPrice));
                newProduct.setProductInStock(Integer.parseInt(productInv));
                newProduct.setProductMin(Integer.parseInt(productMin));
                newProduct.setProductMax(Integer.parseInt(productMax));
                newProduct.setProductParts(currentParts);
                Inventory.updateProduct(productIndex, newProduct);

                Parent productsSave = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
                Scene scene = new Scene(productsSave);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            }
        } catch (NumberFormatException e) {
            System.out.println("Blank Field");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error Adding Part");
            alert.setHeaderText("Error!");
            alert.setContentText("Fields cannot be left blank!");
            alert.showAndWait();
        }
    }

    @FXML
    void ModifyProductsCancelClicked(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Confirm Delete!");
        alert.setContentText("Are you sure you want to cancel update of product " + ModifyProductsNameField.getText() + "?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            Parent partsCancel = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
            Scene scene = new Scene(partsCancel);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } else {
            System.out.println("You clicked cancel. Please complete form.");
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Product product = getProductInventory().get(productIndex);
        productID = getProductInventory().get(productIndex).getProductID();
        System.out.println("Product ID " + productID + " is available.");
        ModifyProductsIDField.setText("AUTO GEN: " + productID);
        ModifyProductsNameField.setText(product.getProductName());
        ModifyProductsInvField.setText(Integer.toString(product.getProductInStock()));
        ModifyProductsPriceField.setText(Double.toString(product.getProductPrice()));
        ModifyProductsMinField.setText(Integer.toString(product.getProductMin()));
        ModifyProductsMaxField.setText(Integer.toString(product.getProductMax()));
        currentParts = product.getProductParts();
        ModifyProductPartIDCol.setCellValueFactory(cellData -> cellData.getValue().partIDProperty().asObject());
        ModifyProductPartNameCol.setCellValueFactory(cellData -> cellData.getValue().partNameProperty());
        ModifyProductPartInvCol.setCellValueFactory(cellData -> cellData.getValue().partInvProperty().asObject());
        ModifyProductPartPriceCol.setCellValueFactory(cellData -> cellData.getValue().partPriceProperty().asObject());
        ModifyProductCurrentPartIDCol.setCellValueFactory(cellData -> cellData.getValue().partIDProperty().asObject());
        ModifyProductCurrentPartNameCol.setCellValueFactory(cellData -> cellData.getValue().partNameProperty());
        ModifyProductCurrentPartInvCol.setCellValueFactory(cellData -> cellData.getValue().partInvProperty().asObject());
        ModifyProductCurrentPartPriceCol.setCellValueFactory(cellData -> cellData.getValue().partPriceProperty().asObject());
        updatePartTableView();
        updateCurrentPartTableView();
    }

    public void updatePartTableView() {
        ModifyProductAddTableView.setItems(getPartInventory());
    }

    public void updateCurrentPartTableView() {
        ModifyProductDeleteTableView.setItems(currentParts);
    }

}
