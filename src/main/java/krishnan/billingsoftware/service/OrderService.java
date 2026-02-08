package krishnan.billingsoftware.service;

import krishnan.billingsoftware.io.OrderRequest;
import krishnan.billingsoftware.io.OrderResponse;

import java.util.List;

public interface OrderService {

    OrderResponse createOrder(OrderRequest request);

    void deleteOrder(String orderId);

    List<OrderResponse> getLatestOrder();
}
