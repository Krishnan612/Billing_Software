package krishnan.billingsoftware.controller;

import com.razorpay.RazorpayException;
import krishnan.billingsoftware.io.PaymentRequest;
import krishnan.billingsoftware.io.RazorpayOrderResponse;
import krishnan.billingsoftware.service.OrderService;
import krishnan.billingsoftware.service.RazorpayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final RazorpayService razorpayService;
    private final OrderService orderService;

    @PostMapping("/create-order")
    @ResponseStatus(HttpStatus.CREATED)
    public RazorpayOrderResponse createRazorpayOrder(@RequestBody PaymentRequest request) throws RazorpayException {
        return razorpayService.createOrder(request.getAmount(), request.getCurrency());
    }
}
