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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author almirmuratovic
 */
public class Product {

    //Instance variables
    private static ObservableList<Part> parts = FXCollections.observableArrayList();
    private final IntegerProperty productID;
    private final StringProperty name;
    private final DoubleProperty price;
    private final IntegerProperty inStock;
    private final IntegerProperty min;
    private final IntegerProperty max;

    //Contructor
    public Product() {
        productID = new SimpleIntegerProperty();
        name = new SimpleStringProperty();
        price = new SimpleDoubleProperty();
        inStock = new SimpleIntegerProperty();
        min = new SimpleIntegerProperty();
        max = new SimpleIntegerProperty();
    }

    //Getters
    public IntegerProperty productIDProperty() {
        return productID;
    }

    public StringProperty productNameProperty() {
        return name;
    }

    public DoubleProperty productPriceProperty() {
        return price;
    }

    public IntegerProperty productInvProperty() {
        return inStock;
    }

    public int getProductID() {
        return this.productID.get();
    }

    public String getProductName() {
        return this.name.get();
    }

    public double getProductPrice() {
        return this.price.get();
    }

    public int getProductInStock() {
        return this.inStock.get();
    }

    public int getProductMin() {
        return this.min.get();
    }

    public int getProductMax() {
        return this.max.get();
    }

    public ObservableList getProductParts() {
        return parts;
    }

    //Setters
    public void setProductID(int productID) {
        this.productID.set(productID);
    }

    public void setProductName(String name) {
        this.name.set(name);
    }

    public void setProductPrice(double price) {
        this.price.set(price);
    }

    public void setProductInStock(int inStock) {
        this.inStock.set(inStock);
    }

    public void setProductMin(int min) {
        this.min.set(min);
    }

    public void setProductMax(int max) {
        this.max.set(max);
    }

    public void setProductParts(ObservableList<Part> parts) {
        this.parts = parts;
    }

    //Product Validation Method
    public static String isProductValid(String name, int min, int max, int inv, double price, ObservableList<Part> parts, String message) {
        double sumParts = 0.00;
        for (int i = 0; i < parts.size(); i++) {
            sumParts = sumParts + parts.get(i).getPartPrice();
        }
        if (name.equals("")) {
            message = message + ("Name field is blank.");
        }
        if (min < 0) {
            message = message + ("The inventory must be greater than 0.");
        }
        if (price < 0) {
            message = message + ("The price must be greater than $0");
        }
        if (min > max) {
            message = message + ("The inventory MIN must be less than the MAX.");
        }
        if (inv < min || inv > max) {
            message = message + ("Part inventory must be between MIN and MAX values.");
        }
        if (parts.size() < 1) {
            message = message + ("Product must contain at least 1 part.");
        }
        if (sumParts > price) {
            message = message + ("Product price must be greater than cost of parts.");
        }
        return message;
    }
}
