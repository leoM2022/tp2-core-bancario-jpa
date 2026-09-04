import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder

public class CajaAhorro extends CuentaBancaria{
    private BigDecimal tasaInteresAnual;
    private BigDecimal cupoEntero;
}
