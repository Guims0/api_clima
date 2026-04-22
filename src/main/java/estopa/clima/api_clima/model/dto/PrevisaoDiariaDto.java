package estopa.clima.api_clima.model.dto;

public record PrevisaoDiariaDto(String data,
                                Double tempMinima,
                                Double tempMaxima,
                                String descricao) {
}
