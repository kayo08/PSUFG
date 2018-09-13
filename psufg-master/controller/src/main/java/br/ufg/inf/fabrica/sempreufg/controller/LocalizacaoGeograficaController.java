package br.ufg.inf.fabrica.sempreufg.controller;

import br.ufg.inf.fabrica.sempreufg.dao.LocalizacaoGeograficaDao;
import br.ufg.inf.fabrica.sempreufg.dominio.LocalizacaoGeografica;
import java.util.List;

/**
 *
 * @author Danillo
 */
public class LocalizacaoGeograficaController {
    private final LocalizacaoGeograficaDao dao;

    public LocalizacaoGeograficaController() {
        this.dao = new LocalizacaoGeograficaDao();
    }
    
    public LocalizacaoGeografica buscar(Long id){
        return dao.buscar(id);
    }
    
    public List<LocalizacaoGeografica> pesquisarPorMunicipio(String municipio) {
        return dao.pesquisarPorMunicipio(municipio);
    }
    
    public List<String> pesquisarValoresCampo(String campo){
        return dao.pesquisarValoresCampo("LocalizacaoGeografica", campo);
    }
}
