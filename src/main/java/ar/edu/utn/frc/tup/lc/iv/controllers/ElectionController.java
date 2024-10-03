package ar.edu.utn.frc.tup.lc.iv.controllers;

import ar.edu.utn.frc.tup.lc.iv.clients.elections.records.Distrito;
import ar.edu.utn.frc.tup.lc.iv.clients.elections.records.Seccion;
import ar.edu.utn.frc.tup.lc.iv.dtos.common.DistritoCargosDTO;
import ar.edu.utn.frc.tup.lc.iv.dtos.common.ResultadoDTO;
import ar.edu.utn.frc.tup.lc.iv.services.EleccionesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Array;
import java.util.ArrayList;

@RestController
public class ElectionController {
    @Autowired
    private EleccionesService eleccionesService;

    @GetMapping("/distritos")
    public ResponseEntity<ArrayList<Distrito>> getDistritos(@RequestParam(required = false) String distrito_nombre) {
        return ResponseEntity.ok(eleccionesService.getDistritos(distrito_nombre));
    }

    @GetMapping("/cargos")
    public ResponseEntity<DistritoCargosDTO> getCargosByDistrito(@RequestParam Long distrito_id) {
        return ResponseEntity.ok(eleccionesService.getCargos(distrito_id));
    }

    @GetMapping("/secciones")
    public ResponseEntity<ArrayList<Seccion>> getSecciones(@RequestParam Long distrito_id, @RequestParam(required = false) Long seccion_id) {
        return ResponseEntity.ok(eleccionesService.getSecciones(distrito_id, seccion_id));
    }

    @GetMapping("/resultados")
    public ResponseEntity<ResultadoDTO> getResultado(@RequestParam Long distrito_id, @RequestParam Long seccion_id) {
        return ResponseEntity.ok(eleccionesService.getResultado(distrito_id, seccion_id));
    }
}
