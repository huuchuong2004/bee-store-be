package vn.huuchuong.be_bee_store.coupon_module.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import vn.huuchuong.be_bee_store.base.BaseResponse;
import vn.huuchuong.be_bee_store.coupon_module.payload.request.CreateCouponRequest;
import vn.huuchuong.be_bee_store.coupon_module.payload.response.CouponResponse;
import vn.huuchuong.be_bee_store.coupon_module.service.CouponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;


@Tag(
        name = "Quản lý mã giảm giá",
        description = "Nhóm API quản lý coupon/mã giảm giá, bao gồm lấy danh sách, tạo mới và xóa coupon."
)
@RestController
@RequestMapping("/api/v1/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @Operation(
            summary = "Lấy danh sách coupon",
            description = """
                    API lấy danh sách coupon/mã giảm giá trong hệ thống theo dạng phân trang.

                    Hỗ trợ các tham số:
                    - page: số trang, bắt đầu từ 0
                    - size: số lượng bản ghi mỗi trang
                    - sort: trường cần sắp xếp, ví dụ couponId,desc hoặc couponCode,asc

                    Dữ liệu trả về bao gồm mã coupon, giá trị giảm, ngày bắt đầu, ngày kết thúc,
                    giá trị đơn hàng tối thiểu, số lượt sử dụng tối đa, số lượt sử dụng tối đa trên mỗi người dùng
                    và số lượt đã sử dụng hiện tại.
                    """
    )
    @GetMapping
    public BaseResponse<Page<CouponResponse>> getCoupons(
            @Parameter(
                    description = "Thông tin phân trang và sắp xếp. Ví dụ: page=0&size=10&sort=couponId,desc"
            )
            Pageable pageable
    ) {
        return BaseResponse.success(
                couponService.getCoupons(pageable),
                "Lấy danh sách coupon thành công"
        );
    }

    @Operation(
            summary = "Tạo mới coupon",
            description = """
                    API tạo mới coupon/mã giảm giá.

                    Hệ thống sẽ kiểm tra mã coupon đã tồn tại hay chưa.
                    Khi tạo thành công, startDate được tự động lấy theo ngày hiện tại
                    và currentUsage được khởi tạo bằng 0.

                    Request body bao gồm:
                    - couponCode: mã giảm giá
                    - discountValue: giá trị giảm
                    - endDate: ngày hết hạn
                    - minimumOrderAmount: giá trị đơn hàng tối thiểu để áp dụng
                    - maxUsage: tổng số lượt sử dụng tối đa
                    - maxUsagePerUser: số lượt sử dụng tối đa cho mỗi người dùng
                    """
    )
    @PostMapping
    public BaseResponse<CouponResponse> createCoupon(
            @RequestBody CreateCouponRequest request
    ) {
        return BaseResponse.success(
                couponService.createCoupon(request),
                "Tạo coupon thành công"
        );
    }

    @Operation(
            summary = "Xóa coupon theo id",
            description = """
                    API xóa coupon/mã giảm giá theo id.

                    Trước khi xóa coupon, hệ thống sẽ tìm các đơn hàng đang sử dụng coupon này
                    và gỡ liên kết coupon khỏi các đơn hàng đó. Sau đó coupon sẽ được xóa khỏi hệ thống.
                    """
    )
    @DeleteMapping
    public BaseResponse<Boolean> deleteCoupon(
            @Parameter(
                    description = "ID của coupon cần xóa",
                    example = "1",
                    required = true
            )
            @RequestParam("id") Integer id
    ) {
        return BaseResponse.success(
                couponService.deleteCoupon(id),
                "Xoá coupon thành công"
        );
    }
}