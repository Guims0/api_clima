package estopa.clima.api_clima.model.dto;

public record ClimaAtualResponseDto(
        String nome,
        Double temperatura,
        Double sensacao,
        String descricao
) {}