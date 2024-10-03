package ar.edu.utn.frc.tup.lc.iv.services.impl;

import ar.edu.utn.frc.tup.lc.iv.clients.ElectionsRestClient;
import ar.edu.utn.frc.tup.lc.iv.clients.elections.records.*;
import ar.edu.utn.frc.tup.lc.iv.dtos.common.DistritoCargosDTO;
import ar.edu.utn.frc.tup.lc.iv.dtos.common.ResultadoDTO;
import ar.edu.utn.frc.tup.lc.iv.dtos.common.ResultadoDetalleDTO;
import ar.edu.utn.frc.tup.lc.iv.services.EleccionesService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EleccionesServiceImpl implements EleccionesService {
    @Autowired
    private ElectionsRestClient electionsRestClient;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ArrayList<Distrito> getDistritos(String nombre) {
        ResponseEntity<Distrito[]> response = electionsRestClient.getDistritos();
        if(response.getBody()==null){
            return null;
        }
        else if(nombre==null){
            ArrayList<Distrito> auxReturn = new ArrayList<>();
            Collections.addAll(auxReturn, response.getBody());
            return auxReturn;
        }
        else{
            ArrayList<Distrito> auxNombre = new ArrayList<>();
            for (int i = 0; i < response.getBody().length; i++) {
                if(response.getBody()[i].distritoNombre().contains(nombre)){
                    auxNombre.add(modelMapper.map(response.getBody()[i], Distrito.class));
                }
            }
            return auxNombre;
        }
    }

    @Override
    public DistritoCargosDTO getCargos(Long distritoId) {
        DistritoCargosDTO auxReturn = new DistritoCargosDTO();
        auxReturn.setDistrito(getDistritoById(distritoId));
        auxReturn.setCargos(getCargosByDistritoId(distritoId));
        return auxReturn;
    }

    @Override
    public ArrayList<Seccion> getSecciones(Long distritoId, Long seccionId) {
        ResponseEntity<Seccion[]> auxSecciones = electionsRestClient.getSecciones();
        if(auxSecciones.getBody()==null||auxSecciones.getBody().length==0){
            return null;
        }
        ArrayList<Seccion> auxReturn = new ArrayList<>();
        for (int i = 0; i < auxSecciones.getBody().length; i++) {
            if(seccionId!=null&&auxSecciones.getBody()[i].seccionId()==seccionId&&auxSecciones.getBody()[i].distritoId()==distritoId){
                auxReturn.add(auxSecciones.getBody()[i]);
            }
            else{
                if(seccionId==null&&auxSecciones.getBody()[i].distritoId()==distritoId){
                    auxReturn.add(auxSecciones.getBody()[i]);
                }
            }
        }
        return auxReturn;
    }

    @Override
    public ResultadoDTO getResultado(Long distritoId, Long seccionId) {
        ResponseEntity<Resultado[]> auxResultados = electionsRestClient.getResultados(distritoId);
        ResultadoDTO auxReturn = new ResultadoDTO();
        auxReturn.setDistrito(Objects.requireNonNull(getDistritoById(distritoId)).distritoNombre());
        auxReturn.setSeccion(Objects.requireNonNull(getSecciones(distritoId,seccionId).get(0).seccionNombre()));
        auxReturn.setResultados(obtenerResultadoDto(Objects.requireNonNull(auxResultados.getBody())));
        return auxReturn;
    }

    private ArrayList<Cargo> getCargosByDistritoId(Long distritoId){
        ResponseEntity<Cargo[]> auxCargos = electionsRestClient.getCargos();
        if(auxCargos==null||auxCargos.getBody()==null){
            return null;
        }
        ArrayList<Cargo> auxReturn = new ArrayList<>();
        for (int i = 0; i < auxCargos.getBody().length; i++) {
            if(auxCargos.getBody()[i].distritoId()==distritoId){
                auxReturn.add(auxCargos.getBody()[i]);
            }
        }
        return auxReturn;
    }

    private Distrito getDistritoById(Long distritoId){
        ResponseEntity<Distrito[]> auxDist = electionsRestClient.getDistritos();
        if(auxDist==null||auxDist.getBody()==null){
            return null;
        }
        for (int i = 0; i < auxDist.getBody().length; i++) {
            if (Objects.equals(auxDist.getBody()[i].distritoId(), distritoId)){
                return auxDist.getBody()[i];
            }
        }
        return null;
    }

    private String getAgrupacionById(Long agrupacionId){
        ResponseEntity<Agrupacion> auxAgrup = electionsRestClient.getAgrupacion(agrupacionId);
        if (auxAgrup.getBody()==null||auxAgrup.getBody().agrupacionNombre()==null){
            return null;
        }
        return auxAgrup.getBody().agrupacionNombre();
    }

    private float calcularPorcentaje(int votosTotales, int votos){
        return (float) votos/votosTotales;
    }

    private ArrayList<ResultadoDetalleDTO> obtenerResultadoDto(Resultado[] body){
        ArrayList<ResultadoDetalleDTO> result = new ArrayList<>();
        int countVotos = 0;
        Map<String, ResultadoDetalleDTO> map = new HashMap<>();
        for (int i = 0; i < body.length; i++) {
            if(body[i].agrupacionId()==0){
                map.putIfAbsent(body[i].votosTipo(), new ResultadoDetalleDTO());
                map.get(body[i].votosTipo()).setVotos(map.get(body[i].votosTipo()).getVotos()+body[i].votosCantidad());
            }
            else{
                map.putIfAbsent(getAgrupacionById(body[i].agrupacionId()),new ResultadoDetalleDTO());
                map.get(getAgrupacionById(body[i].agrupacionId())).setVotos(map.get(getAgrupacionById(body[i].agrupacionId())).getVotos()+body[i].votosCantidad());
            }
            countVotos+=body[i].votosCantidad();
        }
        for(Map.Entry<String, ResultadoDetalleDTO> s: map.entrySet()){
            map.get(s.getKey()).setNombre(s.getKey());
            map.get(s.getKey()).setPorcentaje(calcularPorcentaje(countVotos,map.get(s.getKey()).getVotos()));
            result.add(s.getValue());
        }
        result.sort((dto1, dto2) -> Integer.compare(dto2.getVotos(), dto1.getVotos()));
        for (int i = 0; i < result.size(); i++) {
            result.get(i).setOrden(i+1);
        }
        return result;
    }
}
