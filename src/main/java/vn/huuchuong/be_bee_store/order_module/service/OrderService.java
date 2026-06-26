package vn.huuchuong.be_bee_store.order_module.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.huuchuong.be_bee_store.analys_module.payload.response.BestSellerResponse;
import vn.huuchuong.be_bee_store.analys_module.payload.response.DashboardSummaryResponse;
import vn.huuchuong.be_bee_store.analys_module.payload.response.RevenueStatsResponse;
import vn.huuchuong.be_bee_store.order_module.payload.request.CheckoutRequest;
import vn.huuchuong.be_bee_store.order_module.payload.response.OrderResponse;
import vn.huuchuong.be_bee_store.order_module.payload.response.UserOrderResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface OrderService {
    Page<OrderResponse> getAll(Pageable pageable);

    OrderResponse checkout(CheckoutRequest request);

    OrderResponse getOrderById(Integer orderId);

    Page<OrderResponse> getMyOrders(Pageable pageable);

    OrderResponse cancelOrder(Integer orderId);

    OrderResponse getDetailsAdminRole(Integer orderId);

    UserOrderResponse getUserByOrderId(Integer orderId);

    int countOrdersByStatusDeliverred();

    int countTotalOrders();

    BigDecimal getTotalRevenue();

    DashboardSummaryResponse getDashboardSummary();

    List<BestSellerResponse> getBestSellers(LocalDate from, LocalDate to, int limit);

    List<RevenueStatsResponse> getRevenueByDay(LocalDate from, LocalDate to);

    List<RevenueStatsResponse> getRevenueByMonth(LocalDate from, LocalDate to);


    List<String> getAddressFromOrder();

    boolean setStatusIsShipping(Integer orderId);

    boolean setStatusIsDelivered(Integer orderId);

    boolean updateStatus(Integer orderId, String status);
}
