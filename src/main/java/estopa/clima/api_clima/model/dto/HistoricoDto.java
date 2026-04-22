package estopa.clima.api_clima.model.dto;

import java.time.LocalDateTime;

public record HistoricoDto(String cidade,
                           Double temperatura,
                           String descricao,
                           LocalDateTime dataPesquisa) {
}
