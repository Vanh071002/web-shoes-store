package com.example.demo.control.cart;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

import com.example.demo.control.payment.OrderDetail;
import com.example.demo.entity.User;
import com.example.demo.loaddata.PaymentDao;
import com.example.demo.template.CodPayment;
import com.example.demo.template.PaypalPayment;
import com.paypal.base.rest.PayPalRESTException;

@WebServlet("/paymentController")
public class PaymentController extends HttpServlet {
    private PaymentDao paymentDao;

    public void init() {
        paymentDao = new PaymentDao();
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        String action = "";
        String url = "";
        if (action == null) {
            action = "join";  // default action
        }

        HttpSession session = request.getSession();
        String accode = (String) session.getAttribute("actionCode");
        String statusTrans = request.getParameter("statusTrans");

        String userName = (String) session.getAttribute("userName");


        double total = (double) session.getAttribute("totalMoney");

        String userphone = request.getParameter("customerPhone");
        String des = request.getParameter("customerName");
        String address = request.getParameter("address");
        String typePayment = request.getParameter("typePayment");

        if (userName == null) {
            userName = "Thien";
        }
        System.out.println(typePayment);

        if (typePayment != null) {
            if (typePayment.equals("paypal")) {
                PaypalPayment payment = new PaypalPayment();
                OrderDetail orderDetail = new OrderDetail(des, String.valueOf(total), String.valueOf(10), String.valueOf(10), String.valueOf(total + 10 + 10)
                        , address, userName, userphone);
                try {
                    String approvalLink = payment.authorizePayment(orderDetail);
                    response.sendRedirect(approvalLink);

                } catch (PayPalRESTException ex) {
                    request.setAttribute("errorMessage", ex.getMessage());
                    ex.printStackTrace();
                    request.getRequestDispatcher("error.jsp").forward(request, response);
                }
//            response.sendRedirect("order.jsp");
            } else {
                if (typePayment.equals("cash")){
                    CodPayment codPayment = new CodPayment();
                    if(codPayment.checklogin(request))
                    {
                        User user = (User)session.getAttribute("acc");
                        codPayment.saveInvoice(user,userphone, address, request, response);
                        codPayment.sendEmail(user,user.getUserName(),userphone, address,String.valueOf(total));
                        request.getRequestDispatcher("productload").forward(request, response);


                    }
                }
            }
        }


//        switch (accode) {
//            case "trans":
//                try {
//                    boolean mess = paymentDao.addPay(Useruser,totall,userphone,address);
//                    if(mess){
//                        System.out.println("susscess");
//                        response.sendRedirect("index.jsp");
//                    }
//                    else {
//                        System.out.println("error");
//                        response.sendRedirect("trans.jsp");
//                    }
//                } catch (SQLException e) {
//                    throw new RuntimeException(e);
//                }
//                break;
//            default:
//
//                break;
//        }

    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    public static String message = "";
}
