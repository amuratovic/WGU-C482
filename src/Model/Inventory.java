/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author almirmuratovic
 */
public class Inventory {

    private static ObservableList<Product> productInventory = FXCollections.observableArrayList();
    private static ObservableList<Part> partInventory = FXCollections.observableArrayList();
    private static int partIDCount = 0;
    private static int productIDCount = 0;

    public Inventory() {
    }

    public static ObservableList<Part> getPartInventory() {
        return partInventory;
    }

    public static void addPart(Part part) {
        partInventory.add(part);
    }

    public static void removePart(Part part) {
        partInventory.remove(part);
    }

    public static void updatePart(int index, Part part) {
        partInventory.set(index, part);
    }

    public static int getPartIDCount() {
        partIDCount++;
        return partIDCount;
    }

    public static boolean validatePartDelete(Part part) {
        boolean isFound = false;

        for (int i = 0; i < productInventory.size(); i++) {
            if (productInventory.get(i).getProductParts().contains(part)) {
                isFound = true;
            }
        }
        return isFound;
    }

    public static int lookupPart(String searchTerm) {
        boolean isFound = false;
        int index = 0;
        if (isInteger(searchTerm)) {
            for (int i = 0; i < partInventory.size(); i++) {
                if (Integer.parseInt(searchTerm) == partInventory.get(i).getPartID()) {
                    index = i;
                    isFound = true;
                }
            }
        } else {
            for (int i = 0; i < partInventory.size(); i++) {
                if (searchTerm.equals(partInventory.get(i).getPartName())) {
                    index = i;
                    isFound = true;
                }
            }
        }
        if (isFound = true) {
            return index;
        } else {
            System.out.println("No parts found.");
            return -1;
        }
    }


    public static ObservableList<Product> getProductInventory() {
        return productInventory;
    }

    public static void addProduct(Product product) {
        productInventory.add(product);
    }

    public static void removeProduct(Product product) {
        productInventory.remove(product);
    }

    public static int getProductIDCount() {
        productIDCount++;
        return productIDCount;
    }

    public static int lookupProduct(String searchTerm) {
        boolean isFound = false;
        int index = 0;

        if (isInteger(searchTerm)) {
            for (int i = 0; i < productInventory.size(); i++) {
                if (Integer.parseInt(searchTerm) == productInventory.get(i).getProductID()) {
                    index = i;
                    isFound = true;
                }
            }
        } else {
            for (int i = 0; i < productInventory.size(); i++) {
                if (searchTerm.equals(productInventory.get(i).getProductName())) {
                    index = i;
                    isFound = true;
                }
            }
        }
        if (isFound = true) {
            return index;
        } else {
            System.out.println("No products found.");
            return -1;
        }
    }

    public static void updateProduct(int index, Product product) {
        productInventory.set(index, product);
    }

    public static boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
