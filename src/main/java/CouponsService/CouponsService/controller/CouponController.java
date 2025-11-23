package CouponsService.CouponsService.controller;

import CouponsService.CouponsService.model.Coupon;
import CouponsService.CouponsService.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/coupons")
@Tag(name = "Coupons", description = "Operaciones CRUD de cupones")
public class CouponController {

    @Autowired
    private CouponService couponService;

    @GetMapping
    @Operation(summary = "Listar cupones")
    public ResponseEntity<List<CouponDto>> findAll() {
        List<CouponDto> dtos = couponService.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener cupón por id")
    public ResponseEntity<CouponDto> findById(@PathVariable Long id) {
        return couponService.findById(id)
                .map(c -> ResponseEntity.ok(toDto(c)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/code/{code}")
    @Operation(summary = "Obtener cupón por código")
    public ResponseEntity<CouponDto> findByCode(@PathVariable String code) {
        return couponService.findByDiscountCode(code)
                .map(c -> ResponseEntity.ok(toDto(c)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear cupón")
    public ResponseEntity<CouponDto> create(@RequestBody CreateCouponRequest req) {
        Coupon coupon = new Coupon();
        coupon.setDiscount_code(req.discount_code.toUpperCase());
        coupon.setDiscount_value(req.discount_value);
        coupon.setState(req.state);
        Coupon saved = couponService.save(coupon);
        return ResponseEntity.ok(toDto(saved));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar cupón")
    public ResponseEntity<CouponDto> update(@PathVariable Long id, @RequestBody UpdateCouponRequest req) {
        return couponService.findById(id)
                .map(existing -> {
                    if (req.discount_code != null) {
                        existing.setDiscount_code(req.discount_code.toUpperCase());
                    }
                    if (req.discount_value != null) {
                        existing.setDiscount_value(req.discount_value);
                    }
                    if (req.state != null) {
                        existing.setState(req.state);
                    }
                    Coupon updated = couponService.save(existing);
                    return ResponseEntity.ok(toDto(updated));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar cupón")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!couponService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        couponService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private CouponDto toDto(Coupon c) {
        return new CouponDto(c.getId(), c.getDiscount_code(), c.getDiscount_value(), c.getState());
    }

    public static class CouponDto {
        public Long id;
        public String discount_code;
        public Float discount_value;
        public String state;
        public CouponDto(Long id, String discount_code, Float discount_value, String state) {
            this.id = id;
            this.discount_code = discount_code;
            this.discount_value = discount_value;
            this.state = state;
        }
    }

    public static class CreateCouponRequest {
        public String discount_code;
        public Float discount_value;
        public String state;
    }

    public static class UpdateCouponRequest {
        public String discount_code;
        public Float discount_value;
        public String state;
    }
}