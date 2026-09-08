import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@DiscriminatorValue("CAJA_AHORRO")
public class CajaAhorro extends CuentaBancaria{
    private BigDecimal tasaInteresAnual;
    private BigDecimal cupoEntero;
}
