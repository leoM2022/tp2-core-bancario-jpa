import com.example.demo.EstadoCuenta;
import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder

public class CuentaBancaria {
    private Long idCuentaBancaria;
    private String cbu;
    private String alias;
    private BigDecimal saldoOperativo;
    private EstadoCuenta estado;
    private List<Cliente> titulares;
    private List<Transaccion> transacciones;

    public void agregarTransaccion(Transaccion transaccion){
        this.transacciones.add(transaccion);
        transaccion.setCuenta(this);
    }

    public void agregarTitular(Cliente cliente){
        this.titulares.add(cliente);
        cliente.getCuentas().add(this);
    }
}
