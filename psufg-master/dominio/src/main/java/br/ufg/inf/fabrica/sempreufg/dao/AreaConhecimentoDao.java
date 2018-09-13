package br.ufg.inf.fabrica.sempreufg.dao;

import br.ufg.inf.fabrica.sempreufg.dominio.AreaConhecimento;
import java.util.List;

/**
 *
 * @author Danillo
 */
public class AreaConhecimentoDao
        extends GenericDao<AreaConhecimento>
        implements IEntityDao<AreaConhecimento> {

    @Override
    public AreaConhecimento buscar(Long id) {
        return super.buscar(AreaConhecimento.class, id);
    }

    @Override
    public List<AreaConhecimento> buscarTodos() {
        return super.buscarTodos(AreaConhecimento.class);
    }

    public List<AreaConhecimento> pesquisarPorNome(String nome) {
        if (nome == null || nome.isEmpty()) {
            return buscarTodos();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("select ac ").
                append("from AreaConhecimento ac ").
                append("where ac.nomeArea like '").
                append(nome).
                append("%'");
        return super.pesquisarJPQLCustomizada(AreaConhecimento.class, sb.toString());
    }
}
