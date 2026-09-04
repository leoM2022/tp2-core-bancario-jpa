import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Cliente {
    private Long idCliente;
    private String nombre;
    private String razonSocial;
    private String cuil;
    private String email;
    private String telefono;
    private String direccion;
    private List<CuentaBancaria> cuentas = new ArrayList<CuentaBancaria>();
}
