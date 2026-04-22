package estopa.clima.api_clima.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_clima")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Clima {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    private String cidade;

    private Double temperatura;

    private Double sensacaoTermica;

    private String descricao;

    private Double tempMinima;

    private Double tempMaxima;

    private LocalDateTime dataConsulta;

}
