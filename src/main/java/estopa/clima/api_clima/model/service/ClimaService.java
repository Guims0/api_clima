package estopa.clima.api_clima.model.service;

import estopa.clima.api_clima.model.dto.*;
import estopa.clima.api_clima.model.entity.Clima;
import estopa.clima.api_clima.model.repository.ClimaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ClimaService {

    @Autowired
    private ClimaRepository climaRepository;

    @Value("${WEATHER_API_KEY}")
    private String apiKey;

    private final String URL_BASE = "https://api.openweathermap.org/data/2.5/";
    private final RestTemplate restTemplate = new RestTemplate();

    public ClimaAtualDto buscarClimaAtual(String cidade){

        String url = URL_BASE + "weather?q={cidade}&appid={apiKey}&units=metric&lang=pt_br";


        ClimaAtualDto dto = restTemplate.getForObject(url, ClimaAtualDto.class, cidade, apiKey);

        if (dto != null){
            salvarNoBanco(dto, cidade);
        }

        return dto;
    }


    private void salvarNoBanco(ClimaAtualDto dto, String cidade){
        Clima entidade = Clima.builder()
                .cidade(cidade)
                .temperatura(dto.main().temp())
                .sensacaoTermica(dto.main().sensacao())
                .tempMinima(dto.main().tempMin())
                .tempMaxima(dto.main().tempMax())
                .descricao(dto.weather().get(0).descricao())
                .dataConsulta(LocalDateTime.now())
                .build();

        climaRepository.save(entidade);
    }

    public String descobrirCidadePorIp(String ip) {
        if (ip.equals("127.0.0.1") || ip.equals("0:0:0:0:0:0:0:1")) {
            return "Rio de Janeiro";
        }
        try {
            String url = "http://ip-api.com/json/" + ip;
            GeoIpDto geo = restTemplate.getForObject(url, GeoIpDto.class);
            if (geo != null && "success".equals(geo.status())) {
                return geo.city();
            }
        } catch (Exception e) {
            System.out.println("Erro na API de IP. Usando fallback.");
        }
        return "Rio de Janeiro";
    }


    public List<HistoricoDto> buscarHistoricoSeguro() {
        List<Clima> historicoBanco = climaRepository.findAll();

        return historicoBanco.stream()
                .map(clima -> new HistoricoDto(
                        clima.getCidade(),
                        clima.getTemperatura(),
                        clima.getDescricao(),
                        clima.getDataConsulta()
                ))
                .collect(Collectors.toList());
    }


    public ClimaCompletoDto buscarClimaCompleto(String cidade) {
        ClimaAtualDto atual = buscarClimaAtual(cidade);

        List<PrevisaoDiariaDto> previsao = buscarPrevisao5Dias(cidade);

        return new ClimaCompletoDto(atual.name(), atual, previsao);
    }

    public List<PrevisaoDiariaDto> buscarPrevisao5Dias(String cidade) {
        String url = URL_BASE + "forecast?q={cidade}&appid={apiKey}&units=metric&lang=pt_br";
        PrevisaoDto respostaCompleta = restTemplate.getForObject(url, PrevisaoDto.class, cidade, apiKey);

        if (respostaCompleta == null || respostaCompleta.listaPrevisoes() == null) {
            return List.of();
        }

        Map<String, List<PrevisaoDto.ItemPrevisaoDTO>> previsoesPorDia = respostaCompleta.listaPrevisoes().stream()
                .collect(Collectors.groupingBy(item -> item.dataHora().substring(0, 10)));

        return previsoesPorDia.entrySet().stream()
                .map(dia -> {
                    String dataDoDia = dia.getKey();
                    List<PrevisaoDto.ItemPrevisaoDTO> oitoHorarios = dia.getValue();

                    Double minAbsoluto = oitoHorarios.stream()
                            .map(p -> p.main().tempMinima())
                            .min(Double::compareTo)
                            .orElse(0.0);

                    Double maxAbsoluto = oitoHorarios.stream()
                            .map(p -> p.main().tempMaxima())
                            .max(Double::compareTo)
                            .orElse(0.0);

                    String descricao = oitoHorarios.stream()
                            .filter(p -> p.dataHora().contains("12:00:00"))
                            .findFirst()
                            .orElse(oitoHorarios.get(0))
                            .weather().get(0).descricao();

                    return new PrevisaoDiariaDto(dataDoDia, minAbsoluto, maxAbsoluto, descricao);
                })
                .sorted(Comparator.comparing(PrevisaoDiariaDto::data))
                .collect(Collectors.toList());
    }
}