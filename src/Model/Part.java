/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author almirmuratovic
 */
public abstract class Part {

    //Instance variables
    private final StringProperty name;
    private final IntegerProperty partID;
    private final DoubleProperty price;
    private final IntegerProperty inStock;
    private final IntegerProperty min;
    private final IntegerProperty max;

    //Contructor
    public Part() {
        partID = new SimpleIntegerProperty();
        name = new SimpleStringProperty();
        price = new SimpleDoubleProperty();
        inStock = new SimpleIntegerProperty();
        min = new SimpleIntegerProperty();
        max = new SimpleIntegerProperty();
    }

    public IntegerProperty partIDProperty() {
        return partID;
    }

    public StringProperty partNameProperty() {
        return name;
    }

    public DoubleProperty partPriceProperty() {
        return price;
    }

    public IntegerProperty partInvProperty() {
        return inStock;
    }

//Getters
    public int getPartID() {
        return this.partID.get();
    }

    public String getPartName() {
        return this.name.get();
    }

    public double getPartPrice() {
        return this.price.get();
    }

    public int getPartInStock() {
        return this.inStock.get();
    }

    public int getPartMin() {
        return this.min.get();
    }

    public int getPartMax() {
        return this.max.get();
    }

    //Setters
    public void setPartID(int partID) {
        this.partID.set(partID);
    }

    public void setPartName(String name) {
        this.name.set(name);
    }

    public void setPartPrice(double price) {
        this.price.set(price);
    }

    public void setPartInStock(int inStock) {
        this.inStock.set(inStock);
    }

    public void setPartMin(int min) {
        this.min.set(min);
    }

    public void setPartMax(int max) {
        this.max.set(max);
    }

    //Part Validation Method
    public static String isPartValid(String name, int min, int max, int inv, double price, String errorMessage) {
        if (name == null) {
            errorMessage = errorMessage + ("Name field is blank.");
        }
        if (inv < 1) {
            errorMessage = errorMessage + ("The inventory must be greater than 0.");
        }
        if (price < 1) {
            errorMessage = errorMessage + ("The price must be greater than $0");
        }
        if (min > max) {
            errorMessage = errorMessage + ("The inventory MIN must be less than the MAX.");
        }
        if (inv < min || inv > max) {
            errorMessage = errorMessage + ("Part inventory must be between MIN and MAX values.");
        }
        return errorMessage;
    }
}
