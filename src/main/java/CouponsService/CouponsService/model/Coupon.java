package CouponsService.CouponsService.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "coupon")
@Data
@NoArgsConstructor
@AllArgsConstructor
/**
 * Entidad JPA que representa un producto del catálogo.
 * Mapeada a la tabla {@code product}. Usa Lombok para generar
 * automáticamente getters, setters y constructores.
 */
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String discount_code;

    @Column(nullable = false)
    private Float discount_value;

    @Column(nullable = false)
    private String state;

}