package com.example.demo.control.brand;

import com.example.demo.entity.Observer.Brand;
import com.example.demo.entity.Observer.Observer;
import com.example.demo.entity.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(urlPatterns = {"/followBrand"})
public class followBrand extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        User user = (User) session.getAttribute("acc");
        if(user != null) {
            Brand brand = new Brand();
            brand.setBrandName(req.getParameter("brandName"));
            Observer obUser = new User(user.getName(), user.getEmail());

            brand.attach(obUser);
        }
    }
}
