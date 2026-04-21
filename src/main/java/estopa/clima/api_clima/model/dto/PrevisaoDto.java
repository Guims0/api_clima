package estopa.clima.api_clima.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record PrevisaoDto(
        @JsonProperty("list") List<ItemPrevisaoDTO> listaPrevisoes) {

    public record ItemPrevisaoDTO(
            @JsonProperty("dt_txt") String dataHora,
            MainDto main,
            List<WeatherDto> weather
    ) {}

    public record MainDto(
            @JsonProperty("temp_min") Double tempMinima,
            @JsonProperty("temp_max") Double tempMaxima
    ) {}
    public record WeatherDto(
            @JsonProperty("description") String descricao
    ) {}
}
