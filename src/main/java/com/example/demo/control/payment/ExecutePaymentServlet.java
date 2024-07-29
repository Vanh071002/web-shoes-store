package com.example.demo.control.payment;


import com.example.demo.entity.User;
import com.example.demo.template.PaypalPayment;
import com.paypal.api.payments.PayerInfo;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.PayPalRESTException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/execute_payment")
public class ExecutePaymentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public ExecutePaymentServlet() {
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String paymentId = request.getParameter("paymentId");
        String payerId = request.getParameter("PayerID");

        try {

            PaypalPayment paymentServices = new PaypalPayment();
            Payment payment = paymentServices.executePayment(paymentId, payerId);

            PayerInfo payerInfo = payment.getPayer().getPayerInfo();
            Transaction transaction = payment.getTransactions().get(0);

            request.setAttribute("payer", payerInfo);
            request.setAttribute("transaction", transaction);


            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("acc");
            String address = request.getParameter("address");
            String phone = request.getParameter("phone");
            String total = request.getParameter("total");
            paymentServices.saveInvoice(user,address,phone,request, response);

            //huy cookie
            Cookie c = new Cookie("cart", "");
            c.setMaxAge(0);
            response.addCookie(c);
            //
            paymentServices.sendEmail(user, user.getUserName(),phone,address,total);
            request.getRequestDispatcher("checkout").forward(request, response);

        } catch (PayPalRESTException ex) {
            request.setAttribute("errorMessage", ex.getMessage());
            ex.printStackTrace();
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

}