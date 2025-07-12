package br.com.kadoozin.usuario.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class EnderecoDTO {
    private Long id;
    private Long numero;
    private String rua;
    private String complemento;
    private String cep;
    private String cidade;
    private String estado;
}
