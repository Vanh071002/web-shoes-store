package com.example.demo.template;

import com.example.demo.entity.Cart;
import com.example.demo.entity.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AbstractPayment {

   protected abstract boolean checklogin(HttpServletRequest request);
   protected abstract void saveInvoice(User u, String phoneNumber, String address, HttpServletRequest request, HttpServletResponse response);
   protected abstract void sendEmail(User user, String fullName, String phoneNumber, String address, String total);

}
