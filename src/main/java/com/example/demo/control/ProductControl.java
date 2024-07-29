package com.example.demo.control;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

import com.example.demo.entity.*;
import com.example.demo.entity.Observer.Brand;
import com.example.demo.loaddata.BrandDAO;
import com.example.demo.loaddata.LoadData;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author Asus
 */
@WebServlet(urlPatterns = {"/productload"})
public class ProductControl extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");


        List<Product> list = null;
        try {
            list = LoadData.getInstance().getAllProduct();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        System.out.print("SIZE: " + list.size());
        // for search
        List<Type> listT = null;
        try {
            listT = LoadData.getInstance().getAllType();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        List<Product> last3P = null;
        try {
            last3P = LoadData.getInstance().get3LastProduct();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //Page indexing
        int count = 0;
        try {
            count = LoadData.getInstance().getAmountProduct();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        int endPage = count / 9;
        if (count % 9 != 0) {
            endPage++;
        }
        System.out.println("Endpage at: " + endPage);
        System.out.println("count at: " + count);
        String indexPage = request.getParameter("index");
        if (indexPage == null) {
            indexPage = "1";
        }
        int index = Integer.parseInt(indexPage);

        List<Product> listProductIndex = null;
        try {
            listProductIndex = LoadData.getInstance().getProductByPage(index);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("acc");
        if(user != null) {
            BrandDAO brandDAO = null;
            try {
                brandDAO = BrandDAO.getInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            List<Brand> followBrands = brandDAO.getUserBrands(user.getEmail());
            request.setAttribute("followBrands", followBrands);
        }        request.setAttribute("activeIndex", index);



        request.setAttribute("endP", endPage);
//        request.setAttribute("ListP", list);
        request.setAttribute("ListP", listProductIndex);

        //vi khi nguoi dung quay lai mua tiep se can cookie nen can lay cookie o day
        //cart
        Cookie[] arr = request.getCookies();
        String txt = "";
        if (arr != null) {
            for (Cookie o : arr) {
                if (o.getName().equals("cart")) {
                    txt += o.getValue();
                }
            }
        }
        Cart cart = new Cart(txt, list);
        List<LineItem> listItem = cart.getItems();
        //nếu có sẵn 1 giỏ hàng, thì sẽ cần có số lượng tổng để hiện thị
        int n;
        if (listItem != null) {
            n = listItem.size();
        } else {
            n = 0;
        }



        request.setAttribute("size", n);
        //for search
        request.setAttribute("ListT", listT);
        request.setAttribute("Last3P", last3P);

        request.getRequestDispatcher("shop.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
}
