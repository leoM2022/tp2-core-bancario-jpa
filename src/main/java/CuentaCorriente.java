import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder

public class CuentaCorriente extends CuentaBancaria {
    String margenDescubierto;
    float costoMantenimiento;
}
