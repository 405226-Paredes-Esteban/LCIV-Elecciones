package ar.edu.utn.frc.tup.lc.iv.clients;

import ar.edu.utn.frc.tup.lc.iv.clients.elections.records.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
public class ElectionsRestClient {
    @Autowired
    private RestTemplate restTemplate;

    private String baseUrl = "http://localhost:8080";

    public ResponseEntity<Distrito[]> getDistritos(){
        return restTemplate.getForEntity(baseUrl+"/distritos", Distrito[].class);
    }

    public ResponseEntity<Cargo[]> getCargos(){
        return restTemplate.getForEntity(baseUrl+"/cargos", Cargo[].class);
    }

    public ResponseEntity<Seccion[]> getSecciones(){
        return restTemplate.getForEntity(baseUrl+"/secciones", Seccion[].class);
    }

    public ResponseEntity<Resultado[]> getResultados(Long distritoId){
        return restTemplate.getForEntity(baseUrl+"/resultados?districtId="+distritoId, Resultado[].class);
    }

    public ResponseEntity<Agrupacion> getAgrupacion(Long id){
        return restTemplate.getForEntity(baseUrl+"/agrupaciones/"+id, Agrupacion.class);
    }
}
