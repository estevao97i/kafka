package adress.estevao.app_adress.service.dto;

public record AdressDto(String cep,
                        String logradouro,
                        String complemento,
                        String bairro,
                        String localidade,
                        String uf,
                        String ibge,
                        String gia,
                        String ddd,
                        String siafi) {
}
