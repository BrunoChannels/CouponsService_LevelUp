package CouponsService.CouponsService.repository;

import CouponsService.CouponsService.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long> { }
