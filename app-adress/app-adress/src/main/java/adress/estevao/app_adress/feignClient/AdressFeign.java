package adress.estevao.app_adress.feignClient;

import adress.estevao.app_adress.service.dto.AdressDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "viacep", url = "https://viacep.com.br/ws/")
public interface AdressFeign {

    @GetMapping("{cep}/json")
    AdressDto getAdress(@PathVariable("cep") String cep);
}
