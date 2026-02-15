package krishnan.billingsoftware.Repository;

import krishnan.billingsoftware.entity.OrderEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OrderEntityRepository extends JpaRepository<OrderEntity, Long> {

    Optional<OrderEntity> findByOrderId(String orderId);

    List<OrderEntity> findAllByOrderByCreatedAtDesc();

    @Query("SELECT SUM(o.grandTotal) FROM OrderEntity o where date(o.createdAt) = :date")
    Double sumSalesByDate(@Param("date") LocalDate date);

    @Query("select count(o) from OrderEntity o where date(o.createdAt) = :date")
    Long countByOrderDate(@Param("date") LocalDate date);

    @Query("select o from OrderEntity o order by o.createdAt desc")
    List<OrderEntity> findRecentOrder(PageRequest pageable);
}
