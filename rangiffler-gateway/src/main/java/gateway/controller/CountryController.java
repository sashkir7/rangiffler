package gateway.controller;

import java.util.Set;

import gateway.model.CountryDto;
import gateway.service.api.GeoGrpcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CountryController {

    private final GeoGrpcClient geoGrpcClient;

    @Autowired
    public CountryController(GeoGrpcClient geoGrpcClient) {
        this.geoGrpcClient = geoGrpcClient;
    }

    @GetMapping("/countries")
    public Set<CountryDto> getAllCountries() {
        return geoGrpcClient.getAllCountries();
    }

}
