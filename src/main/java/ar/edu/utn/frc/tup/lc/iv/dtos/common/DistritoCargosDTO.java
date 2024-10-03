package ar.edu.utn.frc.tup.lc.iv.dtos.common;

import ar.edu.utn.frc.tup.lc.iv.clients.elections.records.Cargo;
import ar.edu.utn.frc.tup.lc.iv.clients.elections.records.Distrito;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DistritoCargosDTO {
    private Distrito distrito;
    private ArrayList<Cargo> cargos;
}
