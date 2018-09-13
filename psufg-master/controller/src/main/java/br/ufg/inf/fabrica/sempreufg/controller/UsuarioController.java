package br.ufg.inf.fabrica.sempreufg.controller;

import br.ufg.inf.fabrica.sempreufg.dao.EgressoDao;
import br.ufg.inf.fabrica.sempreufg.dao.TokenDeResetDeSenhaDao;
import br.ufg.inf.fabrica.sempreufg.dao.UsuarioDao;
import br.ufg.inf.fabrica.sempreufg.dominio.Egresso;
import br.ufg.inf.fabrica.sempreufg.dominio.TokenDeResetDeSenha;
import br.ufg.inf.fabrica.sempreufg.dominio.Usuario;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author auf
 */
public class UsuarioController {

    private final UsuarioDao dao;

    public UsuarioController() {
        dao = new UsuarioDao();
    }

    public List<Usuario> buscarTodos() {
        return dao.buscarTodos();
    }

    public Usuario buscarPorID(Long id) {
        return dao.buscar(id);
    }

    public boolean checarLogin(String CPF, String senha) {
        Usuario u = dao.buscarPorCPF(CPF);
        return u != null ? u.getSenha().equals(senha) : false;
    }

    public Usuario buscarPorCPF(String cpf) {
        return dao.buscarPorCPF(cpf);
    }

    public Egresso buscarTodosOsDadosDoEgressoPorCPF(Long idEgresso) {
        EgressoDao egressoDao = new EgressoDao();
        return egressoDao.buscarTodosOsDadosDoEgresso(idEgresso);
    }

    public void prepararSubstituicaoDeSenha(Session session,
            String url, String cpf) {
        Usuario usuario = dao.buscarPorCPF(cpf);
        if (usuario != null) {
            String uid = UUID.randomUUID().toString();
            TokenDeResetDeSenha token = new TokenDeResetDeSenha(uid, usuario);
            TokenDeResetDeSenhaDao daoToken = new TokenDeResetDeSenhaDao();
            daoToken.salvar(token);

            //Enviar email
            String emailDestinatario = usuario.getEmailPrincipal();
            String assunto = "Recuperação de senha - Sempre UFG";
            StringBuilder mensagem = new StringBuilder();
            mensagem.append("<h2>Recuperação de senha</h2>")
                    .append("<p>Olá, ");
            if (usuario.getNomeSocial() != null && !(usuario.getNomeSocial().isEmpty())) {
                mensagem.append(usuario.getNomeSocial());
            } else {
                mensagem.append(usuario.getNomeOficial());
            }
            mensagem
                    .append(",</p><br/><p>")
                    .append("Foi solicitada a alteração de sua senha. ")
                    .append("Para proseguir, favor acessar o ")
                    .append("<a href='")
                    .append(url)
                    .append("/faces/esqueciSenha.xhtml?t=")
                    .append(token.getToken())
                    .append("'>Link.</a>")
                    .append(" Esta requisição é válida por 24 horas.")
                    .append("<br/>Se você não realizou o pedido, favor ignorar esse email.")
                    .append("</p>");

            try {
                MimeMessage message = new MimeMessage(session);
                message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(emailDestinatario));
                message.setSubject(assunto, "utf-8");
                message.setText(mensagem.toString(), "utf-8", "html");
                message.saveChanges();

                Transport.send(message);
            } catch (MessagingException ex) {
                System.out.println(ex.getMessage());
            }

        }
    }

    public List<String> atualizar(Usuario usuario) {
        List<String> inconsistencias = usuario.validar();
        if (inconsistencias.isEmpty()) {
            usuario.criptografar();
            dao.salvar(usuario);
        }
        return inconsistencias;
    }

    public void atualizarDataUltimoLogin(Usuario usuario) {
        Date ultimoLogin = new Date();
        usuario.setTimeStampUltimaUtilizacao(ultimoLogin);
        dao.salvar(usuario);
    }
}
