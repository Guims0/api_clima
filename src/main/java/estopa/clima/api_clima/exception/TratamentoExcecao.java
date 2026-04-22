package estopa.clima.api_clima.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class TratamentoExcecao {

        @ExceptionHandler(HttpClientErrorException.NotFound.class)
        public ResponseEntity<ApiErroDto> tratarCidadeNaoEncontrada(HttpClientErrorException.NotFound ex, HttpServletRequest request) {

            ApiErroDto erro = new ApiErroDto(
                    LocalDateTime.now(),
                    HttpStatus.NOT_FOUND.value(),
                    "Cidade não encontrada",
                    "Não conseguimos localizar a cidade digitada. Verifique a ortografia e tente novamente.",
                    request.getRequestURI()
            );

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ApiErroDto> tratarErrosInternos(Exception ex, HttpServletRequest request) {

            ApiErroDto erro = new ApiErroDto(
                    LocalDateTime.now(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Erro Interno",
                    "Ops! Tivemos um problema no nosso sistema. Tente novamente mais tarde.",
                    request.getRequestURI()
            );

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
        }
}
