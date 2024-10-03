package ar.edu.utn.frc.tup.lc.iv.dtos.common;

import ar.edu.utn.frc.tup.lc.iv.clients.elections.records.Resultado;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultadoDTO {
    private String distrito;
    private String seccion;
    private ArrayList<ResultadoDetalleDTO> resultados;
}
