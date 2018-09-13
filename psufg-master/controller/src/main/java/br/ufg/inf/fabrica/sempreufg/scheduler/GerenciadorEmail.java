package br.ufg.inf.fabrica.sempreufg.scheduler;

//import br.ufg.inf.fabrica.psufg.dao.EventoDao;
//import br.ufg.inf.fabrica.psufg.dao.ConnectionFactory;
//import javax.persistence.EntityManager;
//import javax.persistence.Query;
import br.ufg.inf.fabrica.sempreufg.dao.EgressoDao;
import br.ufg.inf.fabrica.sempreufg.dao.UsuarioDao;
import br.ufg.inf.fabrica.sempreufg.dao.utils.UsuarioContato;
import br.ufg.inf.fabrica.sempreufg.dominio.AprovacaoDivulgacao;
import br.ufg.inf.fabrica.sempreufg.dominio.Egresso;
import br.ufg.inf.fabrica.sempreufg.dominio.Evento;
import br.ufg.inf.fabrica.sempreufg.dominio.Usuario;
import br.ufg.inf.fabrica.sempreufg.enums.FormaDivulgacao;
import br.ufg.inf.fabrica.sempreufg.enums.FrequenciaDivulgacao;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GerenciadorEmail {

    private final List<Email> emailsEnviar;

    public GerenciadorEmail() {
//        Conexoes.lerParametros();
        this.emailsEnviar = new ArrayList<Email>();
    }

    /*
     Mensagem de e-mail ME.001 - Para um ou mais destinatários (a depender do escopo do Evento (apenas egresso recebe e-mail))
     */
    public void criarEmailsEventoAprovado(AprovacaoDivulgacao aprov) {
        //Verifica se evento existe e se FormaDivulgacao eh/contem MENSAGEM 
        Evento ev = aprov.getEvento();
        if (aprov.getEvento() == null || aprov.getEvento().getId() == 0
                || (ev.getFormaDivulgacao().getValue() == null ? FormaDivulgacao.NOTICIA.getValue() == null : ev.getFormaDivulgacao().getValue().equals(FormaDivulgacao.NOTICIA.getValue()))) {
            return;
        }

        //Verifica todos os destinatarios da mensagem de acordo com a areasDestino, e que tenham campo frequenciaDivulgacao != NAO_RECEBE (5)
        //areasDestino refere-se a area de conhecimento de atuação do usuário(egresso)
        EgressoDao egDao = new EgressoDao();
        List<Egresso> destinatarios = new ArrayList<>();

        StringBuilder jpql = new StringBuilder();
        jpql.append("SELECT DISTINCT e FROM Egresso e");
        jpql.append(" JOIN e.historicosNaUFG hist");
        jpql.append(" WHERE e.timeStampCadastramento != null and e.timeStampExclusaoLogica == null and e.frequenciaDivulgacao != ").append(FrequenciaDivulgacao.NAO_RECEBE.ordinal());
        jpql.append(" AND hist.cursoDaUFG.id = ").append(aprov.getInstanciaAdministrativaUFG().getId());
        destinatarios = egDao.pesquisarJPQLCustomizada(Egresso.class, jpql.toString());

        try {
            Properties prop = new Properties();
            prop.load(GerenciadorEmail.class.getClassLoader().getResourceAsStream("mensagensEmail.properties"));
            String assunto = MessageFormat.format(prop.getProperty("ME.001.Assunto"), ev.getTipoEvento().getValue(), ev.getAssunto().trim());

            for (Egresso userDestinatario : destinatarios) {
                String emailDestinatario = userDestinatario.getEmailPrincipal();
                String nomeUsuarioDestinatario = userDestinatario.getNomeOficial().toUpperCase();
                String corpo = MessageFormat.format(prop.getProperty("ME.001.Corpo"), nomeUsuarioDestinatario, ev.getDescricaoEvento());
                adicionarEmail(emailDestinatario, assunto, corpo);
            }
        } catch (IOException ex) {
            Logger.getLogger(GerenciadorEmail.class.getName()).log(Level.WARNING, null, ex);
        }
    }

    /*
     Mensagem de e-mail ME.001 - Para um ou mais destinatários (a depender do escopo do Evento (apenas egresso recebe e-mail))
     */
    public void criarEmailUsuarioBloqueado(Egresso egresso) {
        final int ID_ADM = 1;

        UsuarioDao userDao = new UsuarioDao();
        List<Usuario> destinatarios = new ArrayList<>();

        StringBuilder jpql = new StringBuilder();
        jpql.append("SELECT DISTINCT u FROM Usuario u");
        jpql.append(" JOIN u.papeis papel");
        jpql.append(" WHERE papel.id = ").append(ID_ADM);

        destinatarios = userDao.pesquisarJPQLCustomizada(Usuario.class, jpql.toString());

        try {
            Properties prop = new Properties();
            prop.load(GerenciadorEmail.class.getClassLoader().getResourceAsStream("mensagensEmail.properties"));
            String assunto = prop.getProperty("ME.002.Assunto");

            for (Usuario userDestinatario : destinatarios) {
                String emailDestinatario = userDestinatario.getEmailPrincipal();
                String nomeUsuarioDestinatario = userDestinatario.getNomeSocial();
                String corpo = MessageFormat.format(prop.getProperty("ME.002.Corpo"), nomeUsuarioDestinatario, egresso.getNomeOficial(), egresso.getCpf(), egresso.getEmailPrincipal());
                adicionarEmail(emailDestinatario, assunto, corpo);
            }
        } catch (IOException ex) {
            Logger.getLogger(GerenciadorEmail.class.getName()).log(Level.WARNING, null, ex);
        }
    }

    public void criarEmailContato(UsuarioContato usuarioContato) {

        String emailDestinatario = "juliano@inf.ufg.br";
        String nomeUsuarioDestinatario = "Juliano Lopes Oliveira";

        try {
            Properties prop = new Properties();
            prop.load(GerenciadorEmail.class.getClassLoader().getResourceAsStream("mensagensEmail.properties"));
            String assunto = prop.getProperty("ME.003.Assunto");
            String corpo = MessageFormat.format(prop.getProperty("ME.003.Corpo"), nomeUsuarioDestinatario, usuarioContato.getNome(), usuarioContato.getEmail(), usuarioContato.getAssunto(), usuarioContato.getMensagem());
            adicionarEmail(emailDestinatario, assunto, corpo);

        } catch (IOException ex) {
            Logger.getLogger(GerenciadorEmail.class.getName()).log(Level.WARNING, null, ex);
        }
    }

    private void adicionarEmail(String emailDestinatario, String assunto, String mensagem) {
        this.emailsEnviar.add(new Email(emailDestinatario, assunto, mensagem));
    }

    public void enviarEmails() {
        RemetenteEmail remetenteEmail = new RemetenteEmail(this.emailsEnviar);
        Thread threadEmail = new Thread(remetenteEmail);
        threadEmail.start();
    }

    public static void main(String[] args) {

        //TESTE        
//        EventoDao eDao = new EventoDao();
//        EgressoDao egDao = new EgressoDao();
//        List<Egresso> destinatarios = new ArrayList<>();
//        Evento e = new Evento();
//        e = eDao.buscar(Long.parseLong("2"));
//        List<Long> ids = new ArrayList<>();
//        for (InstanciaAdministrativaUFG ia : e.getInstanciasAdministrativas()) {
//            ids.add(ia.getId());
//        }
//        TESTE createQuery
//        EntityManager em = ConnectionFactory.obterManager();
//        Query jpql = em.createQuery("SELECT DISTINCT e FROM Egresso e JOIN e.historicosNaUFG hist WHERE e.frequenciaDivulgacao != :freq AND hist.cursoDaUFG.id IN :ids");
//        jpql.setParameter("ids", ids);
//        jpql.setParameter("freq", FrequenciaDivulgacao.DIARIA);        
//        destinatarios = jpql.getResultList();
//        TESTE StringBuilder
//        StringBuilder jpql = new StringBuilder();        
//        jpql.append("SELECT DISTINCT e FROM Egresso e");
//        jpql.append(" JOIN e.historicosNaUFG hist");
//        jpql.append(" WHERE e.frequenciaDivulgacao != ").append(FrequenciaDivulgacao.NAO_RECEBE.ordinal());
//        jpql.append(" AND hist.cursoDaUFG.id IN ").append(ids.toString().replace("[", "(").replace("]", ")"));
//        destinatarios = egDao.pesquisarJPQLCustomizada(Egresso.class, jpql.toString());       
        System.exit(0);
    }
}
