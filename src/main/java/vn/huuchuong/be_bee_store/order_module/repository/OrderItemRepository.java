package vn.huuchuong.be_bee_store.order_module.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.huuchuong.be_bee_store.order_module.Enum.OrderStatus;
import vn.huuchuong.be_bee_store.order_module.entity.OrderItem;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

    @Query("""
					 select oi
					 from OrderItem oi
					 join fetch oi.order o
					 join fetch oi.productVariant pv
					 join fetch pv.product p
					 where o.status = :status
						 and o.orderDate between :from and :to
					 """)
    List<OrderItem> findDeliveredItemsBetween(@Param("status") OrderStatus status,
                                              @Param("from") LocalDateTime from,
                                              @Param("to") LocalDateTime to);

}
