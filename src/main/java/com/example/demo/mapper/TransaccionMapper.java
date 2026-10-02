package com.example.demo.mapper;

import com.example.demo.dto.TransaccionResponseDto;
import com.example.demo.model.Transaccion;

/**
 * Mapper utilitario responsable de transformar la entidad persistida {@link Transaccion}
 * a su correspondiente objeto de transferencia {@link TransaccionResponseDto}.
 * <p>
 * Aísla el grafo de entidades de la base de datos extrayendo únicamente las Claves
 * Bancarias Uniformes (CBU), previniendo bucles de serialización cíclica en Jackson.
 * </p>
 *
 * @author Dyevara23 & leoM2022
 * @version 1.2.0
 * @see Transaccion
 * @see TransaccionResponseDto
 */
public final class TransaccionMapper {

    /**
     * Constructor privado para impedir la instanciación de una clase utilitaria puramente estática.
     */
    private TransaccionMapper() {
        throw new UnsupportedOperationException("TransaccionMapper es una clase utilitaria y no debe ser instanciada.");
    }

    /**
     * Transforma una entidad {@link Transaccion} en su comprobante público {@link TransaccionResponseDto}.
     *
     * @param entidad Instancia persistida de la transacción financiera.
     * @return DTO desacoplado para consumo de la API REST o null si la entidad es nula.
     */
    public static TransaccionResponseDto toResponseDto(Transaccion entidad) {
        if (entidad == null) {
            return null;
        }

        String cbuOrigen = entidad.getCuentaOrigen() != null ? entidad.getCuentaOrigen().getCbu() : null;
        String cbuDestino = entidad.getCuentaDestino() != null ? entidad.getCuentaDestino().getCbu() : null;

        return TransaccionResponseDto.builder()
                .idTransaccion(entidad.getIdTransaccion())
                .cbuOrigen(cbuOrigen)
                .cbuDestino(cbuDestino)
                .monto(entidad.getMonto())
                .tipoTransaccion(entidad.getTipoTransaccion())
                .estadoTransaccion(entidad.getEstadoTransaccion())
                .fechaHora(entidad.getFechaHora())
                .build();
    }
}