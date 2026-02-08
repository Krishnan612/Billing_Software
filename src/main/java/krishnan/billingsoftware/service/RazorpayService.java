package krishnan.billingsoftware.service;

import com.razorpay.RazorpayException;
import krishnan.billingsoftware.io.RazorpayOrderResponse;

public interface RazorpayService {

    RazorpayOrderResponse createOrder(Double amount, String currency) throws RazorpayException;
}
