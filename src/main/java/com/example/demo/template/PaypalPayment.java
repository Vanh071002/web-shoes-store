package com.example.demo.template;

import com.example.demo.control.email.SendEmail;

import com.example.demo.control.payment.OrderDetail;
import com.example.demo.entity.Cart;
import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import com.example.demo.loaddata.LoadData;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

public class PaypalPayment extends AbstractPayment {
    private static final String CLIENT_ID = "AROQ9oKu38wogzn4XbsPXoulEY40o6UlYUgCdapG3d8a_8hSUfp3p7RA_6b9EYRZMVMyfPIatEOX66Kh";
    private static final String CLIENT_SECRET = "EAWnUyORUMaxFKbDDaj5zpqY-lJnhTwtkHITMxNGJyZBJ0IDT-5h4JGp9csHdL8F_1ViJtNMdJbudDOg";
    private static final String MODE = "sandbox";

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

    @Override
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
    }

    @Override
    public void sendEmail(User user, String fullName, String phoneNumber, String address, String money) {
        SendEmail sm = new SendEmail();
        boolean test = sm.sendBill(user, fullName, phoneNumber, address, money);
    }
    //support function

    public String authorizePayment(OrderDetail orderDetail)
            throws PayPalRESTException {

        Payer payer = getPayerInformation();
        RedirectUrls redirectUrls = getRedirectURLs();
        List<Transaction> listTransaction = getTransactionInformation(orderDetail);

        Payment requestPayment = new Payment();
        requestPayment.setTransactions(listTransaction);
        requestPayment.setRedirectUrls(redirectUrls);
        requestPayment.setPayer(payer);
        requestPayment.setIntent("authorize");

        APIContext apiContext = new APIContext(CLIENT_ID, CLIENT_SECRET, MODE);

        Payment approvedPayment = requestPayment.create(apiContext);

        return getApprovalLink(approvedPayment);

    }

    public Payment executePayment(String paymentId, String payerId)
            throws PayPalRESTException {
        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);

        Payment payment = new Payment().setId(paymentId);

        APIContext apiContext = new APIContext(CLIENT_ID, CLIENT_SECRET, MODE);

        return payment.execute(apiContext, paymentExecution);
    }
    private Payer getPayerInformation() {
        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        PayerInfo payerInfo = new PayerInfo();
        payerInfo.setFirstName("William")
                .setLastName("Peterson")
                .setEmail("thienper@gm.com");

        payer.setPayerInfo(payerInfo);

        return payer;
    }

    private RedirectUrls getRedirectURLs() {
        RedirectUrls redirectUrls = new RedirectUrls();

        redirectUrls.setCancelUrl("http://localhost:8080/PaypalTest/checkout");
        redirectUrls.setReturnUrl("http://localhost:8080/demo_war_exploded/review_payment");

        return redirectUrls;
    }

    private List<Transaction> getTransactionInformation(OrderDetail orderDetail) {
        Details details = new Details();
        details.setShipping(orderDetail.getShipping());
        details.setSubtotal(orderDetail.getSubtotal());
        details.setTax(orderDetail.getTax());


        Amount amount = new Amount();
        amount.setCurrency("USD");
        amount.setTotal(orderDetail.getTotal());
        amount.setDetails(details);

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setDescription(orderDetail.getProductName());

        ItemList itemList = new ItemList();
        List<Item> items = new ArrayList<>();

        Item item = new Item();
        item.setCurrency("USD");
        item.setName(orderDetail.getProductName());
        item.setPrice(orderDetail.getSubtotal());
        item.setQuantity("1");

        items.add(item);

        ShippingAddress address=new ShippingAddress();
        //co the tat no di cug dc :v
        address.setRecipientName(orderDetail.getUsername());
        address.setLine1(orderDetail.getAddress());
        address.setPhone(orderDetail.getPhone());
        address.setCountryCode("VN");
        address.setCity("Ho Chi Minh");

        itemList.setShippingAddress(address);

        itemList.setItems(items);




        transaction.setItemList(itemList);

        List<Transaction> listTransaction = new ArrayList<>();
        listTransaction.add(transaction);

        return listTransaction;
    }

    private String getApprovalLink(Payment approvedPayment) {
        List<Links> links = approvedPayment.getLinks();
        String approvalLink = null;

        for (Links link : links) {
            if (link.getRel().equalsIgnoreCase("approval_url")) {
                approvalLink = link.getHref();
                break;
            }
        }

        return approvalLink;
    }
    public Payment getPaymentDetails(String paymentId) throws PayPalRESTException {
        APIContext apiContext = new APIContext(CLIENT_ID, CLIENT_SECRET, MODE);
        return Payment.get(apiContext, paymentId);
    }

}
