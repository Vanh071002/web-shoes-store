package com.example.demo.control.payment;

import java.io.IOException;
import java.util.List;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.example.demo.entity.Cart;
import com.example.demo.entity.LineItem;
import com.example.demo.entity.Product;
import com.example.demo.loaddata.LoadData;
import com.example.demo.template.PaypalPayment;
import com.paypal.api.payments.*;
import com.paypal.base.rest.PayPalRESTException;

@WebServlet("/review_payment")
public class ReviewPayment extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public ReviewPayment() {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String paymentId = request.getParameter("paymentId");
        String payerId = request.getParameter("PayerID");
        try {

            PaypalPayment paymentServices = new PaypalPayment();
            Payment payment = paymentServices.getPaymentDetails(paymentId);

            PayerInfo payerInfo = payment.getPayer().getPayerInfo();
            Transaction transaction = payment.getTransactions().get(0);
            ShippingAddress shippingAddress = transaction.getItemList().getShippingAddress();

            request.setAttribute("payer", payerInfo);
            request.setAttribute("transaction", transaction);
            request.setAttribute("shippingAddress", shippingAddress);
            //get cart
            List<Product> list = LoadData.getInstance().getAllProduct();
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
            List<LineItem> listItem = cart.getItems();

            int n;
            if (listItem != null) {
                n = listItem.size();
            } else {
                n = 0;
            }
            request.setAttribute("size", n);
            request.setAttribute("cart", cart);

//            request.getRequestDispatcher("reviewInvoice.jsp").forward(request, response);

            String url = "reviewInvoice.jsp?paymentId=" + paymentId + "&PayerID=" + payerId;

            request.getRequestDispatcher(url).forward(request, response);
        } catch (PayPalRESTException ex) {
            request.setAttribute("errorMessage", ex.getMessage());
            ex.printStackTrace();
            request.getRequestDispatcher("error.jsp").forward(request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

//        try {
//            PaymentServices paymentServices = new PaymentServices();
//            Payment payment = paymentServices.getPaymentDetails(paymentId);
//
//            PayerInfo payerInfo = payment.getPayer().getPayerInfo();
//            Transaction transaction = payment.getTransactions().get(0);
//            ShippingAddress shippingAddress = transaction.getItemList().getShippingAddress();
//
//            request.setAttribute("payer", payerInfo);
//            request.setAttribute("transaction", transaction);
//            request.setAttribute("shippingAddress", shippingAddress);
//
//            String url = "review.jsp?paymentId=" + paymentId + "&PayerID=" + payerId;
//
//            request.getRequestDispatcher(url).forward(request, response);
//
//        } catch (PayPalRESTException ex) {
//            request.setAttribute("errorMessage", ex.getMessage());
//            ex.printStackTrace();
//            request.getRequestDispatcher("error.jsp").forward(request, response);
//        }
    }

}
