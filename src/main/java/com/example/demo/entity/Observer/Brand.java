package com.example.demo.entity.Observer;

import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import com.example.demo.loaddata.BrandDAO;

import java.util.ArrayList;
import java.util.List;

public class Brand implements Subject {
    private String brandName;
    private Product newProduct;
    private List<Observer> observers = new ArrayList<>();

    public void addNewProduct() {

        BrandDAO brandDAO = null;
        try {
            brandDAO = BrandDAO.getInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println(brandName);

        for(User user : brandDAO.getUsersByBrand(brandName)) {
            Observer observingUser = new User(user.getName(), user.getEmail());
            observers.add(observingUser);
        }
        notifyObservers();
    }

    @Override
    public void attach(Observer observer) {
        BrandDAO brandDAO = null;
        try {
            brandDAO = BrandDAO.getInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        String email = ((User) observer).getEmail();
        brandDAO.userFollowBrand(email, brandName);
    }

    @Override
    public void detach(Observer observer) {
        BrandDAO brandDAO = null;
        try {
            brandDAO = BrandDAO.getInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        String email = ((User) observer).getEmail();
        brandDAO.userUnfollowBrand(email, brandName);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            ((User) observer).update(newProduct);
        }
    }

    // Constructor, Getter and Setter
    public Brand() {
    }

    public Brand(String brandName, Product newProduct, List<Observer> observers) {
        this.brandName = brandName;
        this.newProduct = newProduct;
        this.observers = observers;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public List<Observer> getObservers() {
        return observers;
    }

    public void setObservers(List<Observer> observers) {
        this.observers = observers;
    }

    public Product getNewProduct() {
        return newProduct;
    }

    public void setNewProduct(Product newProduct) {
        this.newProduct = newProduct;
    }
}
