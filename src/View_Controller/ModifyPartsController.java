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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import Model.InhousePart;
import Model.Inventory;
import Model.OutsourcedPart;
import Model.Part;
import static View_Controller.MainScreenController.partToModifyIndex;
import static Model.Inventory.getPartInventory;

/**
 * FXML Controller class
 *
 * @author almirmuratovic
 */
public class ModifyPartsController implements Initializable {

    private boolean isOutsourced;
    int partIndex = partToModifyIndex();
    private String exceptionMessage = new String();
    private int partID;

    @FXML
    private TextField ModifyPartsIDField;
    @FXML
    private TextField ModifyPartsNameField;
    @FXML
    private TextField ModifyPartsInvField;
    @FXML
    private TextField ModifyPartsPriceField;
    @FXML
    private TextField ModifyPartsMinField;
    @FXML
    private TextField ModifyPartsDynField;
    @FXML
    private TextField ModifyPartsMaxField;
    @FXML
    private Label DynModifyPartLabel;
    @FXML
    private RadioButton ModifyPartsInHouseRadioButton;
    @FXML
    private RadioButton ModifyPartsOutsourcedRadioButton;

    @FXML
    void ModifyPartsOutsourcedRadio(ActionEvent event) {
        isOutsourced = true;
        DynModifyPartLabel.setText("Company Name.");
    }

    @FXML
    void ModifyPartsInHouseRadio(ActionEvent event) {
        isOutsourced = false;
        DynModifyPartLabel.setText("Machine ID");
    }

    @FXML
    void ModifyPartsCancelClicked(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirmation Needed");
        alert.setHeaderText("Confirm Part Delete");
        alert.setContentText("Are you sure you want to cancel update of part " + ModifyPartsNameField.getText() + "?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            System.out.println("Part add has been cancelled.");
            Parent partsCancel = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
            Scene scene = new Scene(partsCancel);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } else {
            System.out.println("You clicked cancel. Please complete part info.");
        }
    }

    @FXML
    void ModifyPartsSaveClicked(ActionEvent event) throws IOException {
        String partName = ModifyPartsNameField.getText();
        String partInv = ModifyPartsInvField.getText();
        String partPrice = ModifyPartsPriceField.getText();
        String partMin = ModifyPartsMinField.getText();
        String partMax = ModifyPartsMaxField.getText();
        String partDyn = ModifyPartsDynField.getText();
        try {
            exceptionMessage = Part.isPartValid(partName, Integer.parseInt(partMin), Integer.parseInt(partMax), Integer.parseInt(partInv), Double.parseDouble(partPrice), exceptionMessage);
            if (exceptionMessage.length() > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error Adding Part");
                alert.setHeaderText("Error!");
                alert.setContentText(exceptionMessage);
                alert.showAndWait();
            } else {
                if (isOutsourced == false) {
                    InhousePart inhousePart = new InhousePart();
                    inhousePart.setPartID(partID);
                    inhousePart.setPartName(partName);
                    inhousePart.setPartPrice(Double.parseDouble(partPrice));
                    inhousePart.setPartInStock(Integer.parseInt(partInv));
                    inhousePart.setPartMin(Integer.parseInt(partMin));
                    inhousePart.setPartMax(Integer.parseInt(partMax));
                    inhousePart.setPartMachineID(Integer.parseInt(partDyn));
                    Inventory.updatePart(partIndex, inhousePart);
                } else {
                    OutsourcedPart outsourcedPart = new OutsourcedPart();
                    outsourcedPart.setPartID(partID);
                    outsourcedPart.setPartName(partName);
                    outsourcedPart.setPartPrice(Double.parseDouble(partPrice));
                    outsourcedPart.setPartInStock(Integer.parseInt(partInv));
                    outsourcedPart.setPartMin(Integer.parseInt(partMin));
                    outsourcedPart.setPartMax(Integer.parseInt(partMax));
                    outsourcedPart.setPartCompanyName(partDyn);
                    Inventory.updatePart(partIndex, outsourcedPart);;
                }

                Parent modifyProductCancel = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
                Scene scene = new Scene(modifyProductCancel);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            }
        } catch (NumberFormatException e) {
            System.out.println("Blank Fields");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error Adding Part!");
            alert.setHeaderText("Error");
            alert.setContentText("Form contains blank field.");
            alert.showAndWait();
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Part part = getPartInventory().get(partIndex);
        partID = getPartInventory().get(partIndex).getPartID();
        ModifyPartsIDField.setText("Part ID autoset to: " + partID);
        ModifyPartsNameField.setText(part.getPartName());
        ModifyPartsInvField.setText(Integer.toString(part.getPartInStock()));
        ModifyPartsPriceField.setText(Double.toString(part.getPartPrice()));
        ModifyPartsMinField.setText(Integer.toString(part.getPartMin()));
        ModifyPartsMaxField.setText(Integer.toString(part.getPartMax()));
        if (part instanceof InhousePart) {
            ModifyPartsDynField.setText(Integer.toString(((InhousePart) getPartInventory().get(partIndex)).getPartMachineID()));
            DynModifyPartLabel.setText("Machine ID");
            ModifyPartsInHouseRadioButton.setSelected(true);

        } else {
            ModifyPartsDynField.setText(((OutsourcedPart) getPartInventory().get(partIndex)).getPartCompanyName());
            DynModifyPartLabel.setText("Company Name");
            ModifyPartsOutsourcedRadioButton.setSelected(true);
        }
    }
}
