package estopa.clima.api_clima.model.dto;

import java.util.List;

public record ClimaCompletoDto(String cidade,
                               ClimaAtualDto agora,
                               List<PrevisaoDiariaDto> previsao5Dias) {
}
