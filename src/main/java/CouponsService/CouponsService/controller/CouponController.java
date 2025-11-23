package CouponsService.CouponsService.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/v1/coupons")
@Tag(name = "Coupons", description = "Operaciones CRUD de cupones")
public class CouponController {

    private final Map<Long, CouponDto> store = new ConcurrentHashMap<>();
    private final AtomicLong seq = new AtomicLong(1);

    @GetMapping
    @Operation(summary = "Listar cupones")
    public ResponseEntity<List<CouponDto>> findAll() {
        return ResponseEntity.ok(new ArrayList<>(store.values()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener cupón por id")
    public ResponseEntity<CouponDto> findById(@PathVariable Long id) {
        CouponDto dto = store.get(id);
        if (dto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    @Operation(summary = "Crear cupón")
    public ResponseEntity<CouponDto> create(@RequestBody CreateCouponRequest req) {
        long id = seq.getAndIncrement();
        CouponDto dto = new CouponDto(id, req.discount_code, req.discount_value, req.state);
        store.put(id, dto);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar cupón")
    public ResponseEntity<CouponDto> update(@PathVariable Long id, @RequestBody UpdateCouponRequest req) {
        CouponDto existing = store.get(id);
        if (existing == null) return ResponseEntity.notFound().build();
        CouponDto dto = new CouponDto(id,
                req.discount_code != null ? req.discount_code : existing.discount_code,
                req.discount_value != null ? req.discount_value : existing.discount_value,
                req.state != null ? req.state : existing.state);
        store.put(id, dto);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar cupón")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (store.remove(id) == null) return ResponseEntity.notFound().build();
        return ResponseEntity.noContent().build();
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