package vn.huuchuong.be_bee_store.order_module.Enum;

public enum OrderStatus {
    CREATED,          // đã tạo đơn và chờ thanh toán/xác nhận
    CONFIRMED,        // đã thanh toán hoặc shop đã xác nhận COD
    SHIPPING,         // đang giao
    DELIVERED,        // giao thành công
    CANCELED          // hủy đơn
}