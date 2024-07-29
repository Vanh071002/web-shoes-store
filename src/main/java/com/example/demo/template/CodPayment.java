package com.example.demo.template;

import com.example.demo.control.email.SendEmail;
import com.example.demo.entity.Cart;
import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import com.example.demo.loaddata.LoadData;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

public class CodPayment extends AbstractPayment{
    @Override
    public boolean checklogin(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User a = (User) session.getAttribute("acc");
        if(a!=null){
            return true;
        }
        else{
            return false;
        }
    }

    public void saveInvoice(User u, String phoneNumber, String address, HttpServletRequest request, HttpServletResponse response) {
        //them request de lay cart trong cookie roi add vao cart
        //cart tu lay trong request nen khong can truyen vao nua.
        //get cart
        List<Product> list = null;
        try {
            list = LoadData.getInstance().getAllProduct();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Cookie[] arr = request.getCookies();
        String txt = "";
        if (arr != null) {
            for (Cookie o : arr) {
                if (o.getName().equals("cart")) {
                    txt += o.getValue();
//                    o.setMaxAge(0); // xem lai cho nay 8 9p
//                    response.addCookie(o);
                }
            }
        }
        Cart cart = new Cart(txt, list);
        //save
        try {
            LoadData.getInstance().addInvoice(u, cart,phoneNumber, address);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //save xog thi nho lay add cookie =""; (trong payment)

        Cookie c = new Cookie("cart", "");
        c.setMaxAge(0);
        response.addCookie(c);
    }

    @Override
    public void sendEmail(User user, String fullName, String phoneNumber, String address, String total) {
        SendEmail sm = new SendEmail();
        boolean test = sm.sendBill(user, fullName, phoneNumber, address, total);
        if (test){System.out.println("SEND MAIL CASH SUCESS");}
        else {
            System.out.println("SEND MAIL FAILED");
        }
    }
}
