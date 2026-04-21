package estopa.clima.api_clima.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ClimaAtualDto(
        String name,
        MainDto main,
        List<WeatherDto> weather) {

    public record MainDto(
            Double temp,
            @JsonProperty("feels_like") Double sensacao,
            @JsonProperty("temp_min") Double tempMin,
            @JsonProperty("temp_max") Double tempMax
    ) {}

    public record WeatherDto(@JsonProperty("description") String descricao){}
}
