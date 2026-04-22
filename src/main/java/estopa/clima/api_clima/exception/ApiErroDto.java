package estopa.clima.api_clima.exception;

import java.time.LocalDateTime;

public record ApiErroDto(LocalDateTime timestamp,
                         Integer status,
                         String erro,
                         String mensagem,
                         String caminhoDaRequisicao) {
}
