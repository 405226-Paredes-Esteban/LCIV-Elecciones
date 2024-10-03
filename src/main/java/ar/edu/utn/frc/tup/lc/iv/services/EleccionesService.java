package ar.edu.utn.frc.tup.lc.iv.services;

import ar.edu.utn.frc.tup.lc.iv.clients.elections.records.Distrito;
import ar.edu.utn.frc.tup.lc.iv.clients.elections.records.Seccion;
import ar.edu.utn.frc.tup.lc.iv.dtos.common.DistritoCargosDTO;
import ar.edu.utn.frc.tup.lc.iv.dtos.common.ResultadoDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public interface EleccionesService {
    public ArrayList<Distrito> getDistritos(String nombre);
    public DistritoCargosDTO getCargos(Long distritoId);
    public ArrayList<Seccion> getSecciones(Long distritoId, Long seccionId);
    public ResultadoDTO getResultado(Long distritoId, Long seccionId);
}
