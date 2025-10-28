package school.sptech;

import java.sql.Timestamp;

public class Leitura {
    private Integer idLeitura;
    private Integer fkIdComponente;
    private String fkIdMaquina;
    private String fkTokenEmpresa;
    private String dadosFloat;
    private String dadosTexto;
    private Timestamp dataHoraCaptura;
    private Integer fkIdNucleo;

    public Integer getIdLeitura() {
        return idLeitura;
    }

    public void setIdLeitura(Integer idLeitura) {
        this.idLeitura = idLeitura;
    }

    public Integer getFkIdComponente() {
        return fkIdComponente;
    }

    public void setFkIdComponente(Integer fkIdComponente) {
        this.fkIdComponente = fkIdComponente;
    }

    public String getFkIdMaquina() {
        return fkIdMaquina;
    }

    public void setFkIdMaquina(String fkIdMaquina) {
        this.fkIdMaquina = fkIdMaquina;
    }

    public String getDadosFloat() {
        return dadosFloat;
    }

    public void setDadosFloat(String dadosFloat) {
        this.dadosFloat = dadosFloat;
    }

    public String getDadosTexto() {
        return dadosTexto;
    }

    public void setDadosTexto(String dadosTexto) {
        this.dadosTexto = dadosTexto;
    }

    public Timestamp getDataHoraCaptura() {
        return dataHoraCaptura;
    }

    public void setDataHoraCaptura(Timestamp dataHoraCaptura) {
        this.dataHoraCaptura = dataHoraCaptura;
    }

    public Integer getFkIdNucleo() {
        return fkIdNucleo;
    }

    public void setFkIdNucleo(Integer fkIdNucleo) {
        this.fkIdNucleo = fkIdNucleo;
    }

    @Override
    public String toString() {
        return "Leitura{" +
                "idLeitura=" + idLeitura +
                ", fkIdComponente=" + fkIdComponente +
                ", fkIdMaquina='" + fkIdMaquina + '\'' +
                ", fkTokenEmpresa='" + fkTokenEmpresa + '\'' +
                ", dadosFloat='" + dadosFloat + '\'' +
                ", dadosTexto='" + dadosTexto + '\'' +
                ", dataHoraCaptura=" + dataHoraCaptura +
                ", fkIdNucleo=" + fkIdNucleo +
                '}';
    }

    public String getFkTokenEmpresa() {
        return fkTokenEmpresa;
    }

    public void setFkTokenEmpresa(String fkTokenEmpresa) {
        this.fkTokenEmpresa = fkTokenEmpresa;
    }
}
