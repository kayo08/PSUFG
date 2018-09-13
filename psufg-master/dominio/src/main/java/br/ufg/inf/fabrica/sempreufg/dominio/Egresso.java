package br.ufg.inf.fabrica.sempreufg.dominio;

import br.ufg.inf.fabrica.sempreufg.dao.EgressoDao;
import br.ufg.inf.fabrica.sempreufg.dao.utils.FiltroConsultaLazy;
import br.ufg.inf.fabrica.sempreufg.dao.validadores.ValidadorDeDados;
import br.ufg.inf.fabrica.sempreufg.enums.Sexo;
import br.ufg.inf.fabrica.sempreufg.enums.VisibilidadeDados;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import org.hibernate.LazyInitializationException;

/**
 *
 * @author auf
 */
@Entity
@DiscriminatorValue("EGR")
public class Egresso extends Usuario {

    private String nomeDaMae;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dataDeNascimento;
    private Sexo sexo;
    private String emailAlternativo;
    private String fotoPrincipal;
    private String fotosAdicionais;
    private VisibilidadeDados visibiliadeDeDados;

    @ManyToOne
    private LocalizacaoGeografica naturalidade;

    @OneToMany(mappedBy = "egresso", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HistoricoNaUFG> historicosNaUFG;

    @OneToMany(mappedBy = "egresso", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HistoricoIEM> historicosEnsinoMedio;

    @OneToMany(mappedBy = "egresso", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HistoricoEmOutraIES> historicosEmOutrasIES;

    @ManyToMany(mappedBy = "egressos")
    private List<ImportacaoDadosEgresso> importacao;

    @OneToMany(mappedBy = "egresso", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Atuacao> atuacoes;

    @OneToMany(mappedBy = "egresso", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Residencia> residencias;

    public void addResidencia(Residencia residencia) {
        List<Residencia> res = getResidencias();
        res.add(residencia);
        residencia.setEgresso(this);
    }

    public void remResidencia(Residencia residencia) {
        List<Residencia> res = getResidencias();
        res.remove(residencia);
        residencia.setEgresso(null);
    }

    public void addHistoricoEnsinoMedio(HistoricoIEM historicoIEM) {
        List<HistoricoIEM> historicos = getHistoricosEnsinoMedio();
        historicos.add(historicoIEM);
        historicoIEM.setEgresso(this);
    }

    public void addHistoricoNaUFG(HistoricoNaUFG historicoNaUFG) {
        List<HistoricoNaUFG> historicos = getHistoricosNaUFG();
        historicos.add(historicoNaUFG);
        historicoNaUFG.setEgresso(this);
    }

    public void addHistoricoOutraIES(HistoricoEmOutraIES historicoEmOutraIES) {
        List<HistoricoEmOutraIES> historicos = getHistoricosEmOutrasIES();
        historicos.add(historicoEmOutraIES);
        historicoEmOutraIES.setEgresso(this);
    }

    public void remHistoricoOutraIES(HistoricoEmOutraIES historicoEmOutraIES) {
        List<HistoricoEmOutraIES> historico = getHistoricosEmOutrasIES();
        historico.remove(historicoEmOutraIES);
        historicoEmOutraIES.setEgresso(null);
    }

    public void addAtuacao(Atuacao atuacao) {
        List<Atuacao> atpro = getAtuacoes();
        atpro.add(atuacao);
        atuacao.setEgresso(this);
    }

    public void remAtuacao(Atuacao atuacao) {
        List<Atuacao> atuacoes = getAtuacoes();
        atuacoes.remove(atuacao);
        atuacao.setEgresso(null);
    }

    public String getNomeDaMae() {
        return nomeDaMae;
    }

    public void setNomeDaMae(String nomeDaMae) {
        this.nomeDaMae = nomeDaMae;
    }

    public Date getDataDeNascimento() {
        return dataDeNascimento;
    }

    public void setDataDeNascimento(Date dataDeNascimento) {
        this.dataDeNascimento = dataDeNascimento;
    }

    public Sexo getSexo() {
        return sexo;
    }

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }

    public String getEmailAlternativo() {
        return emailAlternativo;
    }

    public void setEmailAlternativo(String emailAlternativo) {
        this.emailAlternativo = emailAlternativo;
    }

    public String getFotoPrincipal() {
        return fotoPrincipal;
    }

    public void setFotosAdicionais(String fotosAdicionais) {
        this.fotosAdicionais = fotosAdicionais;
    }

    public void setFotoPrincipal(String fotoPrincipal) {
        this.fotoPrincipal = fotoPrincipal;
    }

    public List<String> getFotosAdicionais() {
        if (fotosAdicionais == null || fotosAdicionais.isEmpty()) {
            return new ArrayList();
        }
        String[] parts = fotosAdicionais.split(";");
        List<String> listaFotos = new ArrayList<>();
        if (parts != null || parts.length > 0) {
            for (String foto : parts) {
                listaFotos.add(foto);
            }
        }
        return listaFotos;
    }

    public void setFotosAdicionais(List<String> listaFotos) {
        this.setFotosAdicionais("");
        for (int f = 0; f < listaFotos.size(); f++) {
            this.setFotosAdicionais(this.getFotosAdicionais() + listaFotos.get(f).toString());
            if (f < listaFotos.size() - 1) {
                this.setFotosAdicionais(this.getFotosAdicionais() + ";");
            }
        }
    }

    public VisibilidadeDados getVisibiliadeDeDados() {
        return visibiliadeDeDados;
    }

    public void setVisibiliadeDeDados(VisibilidadeDados visibiliadeDeDados) {
        this.visibiliadeDeDados = visibiliadeDeDados;
    }

    public LocalizacaoGeografica getNaturalidade() {
        return naturalidade;
    }

    public void setNaturalidade(LocalizacaoGeografica naturalidade) {
        this.naturalidade = naturalidade;
    }

    public List<Atuacao> getAtuacoes() {

        if (atuacoes == null) {
            this.atuacoes = new ArrayList<>();
        } else {
            try {
                this.atuacoes.size();
            } catch (LazyInitializationException lie) {
                this.setAtuacoes(recuperarInstanciasDoBanco(Atuacao.class, "aprof"));
                return atuacoes;
            }
        }
        return atuacoes;
    }

    public void setAtuacoes(List<Atuacao> atuacoes) {
        this.atuacoes = atuacoes;
    }

    public List<Residencia> getResidencias() {
        if (residencias == null) {
            residencias = new ArrayList();
        } else {
            try {
                this.residencias.size();
            } catch (LazyInitializationException lie) {
                this.setResidencias(recuperarInstanciasDoBanco(Residencia.class, "r"));
                return residencias;
            }
        }
        return residencias;
    }

    public void setResidencias(List<Residencia> residencias) {
        this.residencias = residencias;
    }

    public List<HistoricoNaUFG> getHistoricosNaUFG() {
        if (this.historicosNaUFG == null) {
            this.historicosNaUFG = new ArrayList();
        } else {
            try {
                this.historicosNaUFG.size();
            } catch (LazyInitializationException lie) {
                this.setHistoricosNaUFG(recuperarInstanciasDoBanco(HistoricoNaUFG.class, "hufg"));
                return historicosNaUFG;
            }
        }
        return historicosNaUFG;
    }

    public void setHistoricosNaUFG(List<HistoricoNaUFG> historicosNaUFG) {
        this.historicosNaUFG = historicosNaUFG;
    }

    public List<HistoricoEmOutraIES> getHistoricosEmOutrasIES() {
        if (this.historicosEmOutrasIES == null) {
            this.historicosEmOutrasIES = new ArrayList<>();
        } else {
            try {
                this.historicosEmOutrasIES.size();
            } catch (LazyInitializationException lie) {
                this.setHistoricosEmOutrasIES(recuperarInstanciasDoBanco(HistoricoEmOutraIES.class, "hoies"));
                return historicosEmOutrasIES;
            }
        }
        return historicosEmOutrasIES;
    }

    public void setHistoricosEmOutrasIES(List<HistoricoEmOutraIES> historicosEmOutrasIES) {
        this.historicosEmOutrasIES = historicosEmOutrasIES;
    }

    public List<HistoricoIEM> getHistoricosEnsinoMedio() {

        if (this.historicosEnsinoMedio == null) {
            this.historicosEnsinoMedio = new ArrayList();
        } else {
            try {
                this.historicosEnsinoMedio.size();
            } catch (LazyInitializationException lie) {
                this.setHistoricosEnsinoMedio(recuperarInstanciasDoBanco(HistoricoIEM.class, "em"));
                return historicosEnsinoMedio;
            }
        }
        return historicosEnsinoMedio;
    }

    public void setHistoricosEnsinoMedio(List<HistoricoIEM> historicosEnsinoMedio) {
        this.historicosEnsinoMedio = historicosEnsinoMedio;
    }

    public List<ImportacaoDadosEgresso> getImportacao() {
        return importacao;
    }

    public void setImportacao(List<ImportacaoDadosEgresso> importacao) {
        this.importacao = importacao;
    }

    @Override
    public List<String> validar() {
        List<String> inconsistencias = super.validar();
        ValidadorDeDados validador = new ValidadorDeDados(inconsistencias);
        validador.validarNaoNuloEVazio(nomeDaMae, "nome da mãe");
        validador.validarNaoNulo(dataDeNascimento, "data de nascimento");
        validador.validarNaoNulo(visibiliadeDeDados, "visibilidade dos dados");
        validador.validarListaNaoNulaEVazia(this.getResidencias(), "endereço");
        validador.validarListaNaoNulaEVazia(this.getHistoricosNaUFG(), "histórico na UFG");
        return inconsistencias;
    }

    private List recuperarInstanciasDoBanco(Class k, String prefClass) {
        List<FiltroConsulta> filtro = new ArrayList<>();
        filtro.add(new FiltroConsulta("Egr : CPF", "igual a", this.getCpf(), "", ""));
        FiltroConsultaLazy filtros = new FiltroConsultaLazy(0, Integer.MAX_VALUE, ".id", true, k, prefClass, filtro);
        EgressoDao dao = new EgressoDao();
        return dao.consultarJpqlLazyComFiltros(filtros);
    }
}
