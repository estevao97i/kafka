package adress.estevao.app_adress.resources;

import adress.estevao.app_adress.feignClient.AdressFeign;
import adress.estevao.app_adress.service.dto.AdressDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("cep")
@RequiredArgsConstructor
public class AdressController {

    private final AdressFeign adressFeign;

    @GetMapping(value = "/{cep}")
    public ResponseEntity<AdressDto> getAdress(@PathVariable("cep") String cep) {
        AdressDto adresss = adressFeign.getAdress(cep);

        return ResponseEntity.ok(adresss);
    }
}
