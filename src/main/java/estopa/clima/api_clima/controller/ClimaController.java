package estopa.clima.api_clima.controller;
import estopa.clima.api_clima.model.dto.ClimaCompletoDto;
import estopa.clima.api_clima.model.dto.HistoricoDto;
import estopa.clima.api_clima.model.service.ClimaService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/clima")
public class ClimaController {

    @Autowired
    private ClimaService climaService;

    @Operation(summary = "Clima Automático", description = "Detecta a cidade pelo IP do usuário e devolve o clima atual e 5 dias.")
    @GetMapping
    public ResponseEntity<ClimaCompletoDto> climaAutomatico(HttpServletRequest request) {

        String ipDoUsuario = request.getHeader("X-Forwarded-For");

        if (ipDoUsuario == null || ipDoUsuario.isEmpty()) {
            ipDoUsuario = request.getRemoteAddr();
        }

        String cidadeDetectada = climaService.descobrirCidadePorIp(ipDoUsuario);
        ClimaCompletoDto resposta = climaService.buscarClimaCompleto(cidadeDetectada);

        return ResponseEntity.ok(resposta);
    }

    @Operation(summary = "Pesquisar por Cidade", description = "Digite o nome da cidade para buscar clima e previsão.")
    @GetMapping("/busca/{cidade}")
    public ResponseEntity<ClimaCompletoDto> buscaPorCidade(@PathVariable String cidade) {

        ClimaCompletoDto resposta = climaService.buscarClimaCompleto(cidade);
        return ResponseEntity.ok(resposta);
    }

    @Operation(summary = "Consultar Histórico", description = "Lista as consultas salvas.")
    @GetMapping("/historico")
    public ResponseEntity<List<HistoricoDto>> verHistorico() {
        List<HistoricoDto> historico = climaService.buscarHistoricoSeguro();

        if (historico.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(historico);
    }
}
