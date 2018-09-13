package br.ufg.inf.fabrica.sempreufg.scheduler;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
//import org.quartz.JobBuilder;
//import org.quartz.JobDetail;
//import org.quartz.Scheduler;
//import org.quartz.SchedulerException;
//import org.quartz.SimpleScheduleBuilder;
//import org.quartz.Trigger;
//import org.quartz.TriggerBuilder;
//import org.quartz.impl.StdSchedulerFactory;

public class RemetenteEmail implements Runnable {

    private final List<Email> emailsEnviar;

    public RemetenteEmail(List<Email> emailsEnviar) {
        this.emailsEnviar = emailsEnviar;
    }

    public void run() {

        for (Email email : emailsEnviar) {
            enviarEmail(email);
        }
    }

    private void enviarEmail(Email email) {

        String emailDestinatario = email.getEnderecoDestinatario();
        String assunto = email.getAssunto();
        String mensagem = email.getMensagem();

        if (emailDestinatario == null || emailDestinatario.trim().isEmpty()) {
            return;
        }

        final String nomeRemetente = "SEMPRE UFG";

//ENVIO DE EMAIL VIA SSL       
        Properties props = new Properties();

        Session session = Session.getDefaultInstance(props);

        try {
            MimeMessage message = new MimeMessage(session);

//            message.setFrom(new InternetAddress(emailRemetente, nomeRemetente));

            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailDestinatario));
//            message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(emailRemetente));
            message.setSubject(assunto, "utf-8");
            message.setText(mensagem, "utf-8", "html");

            Transport tr = session.getTransport("smtp");
//            tr.connect("conta", senhaRementente);
            message.saveChanges();
            tr.sendMessage(message, message.getAllRecipients());
            tr.close();

        } catch (MessagingException e) {
            throw new RuntimeException(e);
//        } catch (UnsupportedEncodingException uex) {
//            throw new RuntimeException(uex);
        } catch (Exception x) {
            throw new RuntimeException(x);
        }
    }

//    public static void main(String[] args) throws InterruptedException {
//        try {
//            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
//            scheduler.start();
//            JobDetail job = JobBuilder.newJob(HelloJob.class)
//                    .withIdentity("job1", "group1")
//                    .build();
//
//            // Trigger the job to run now, and then repeat every 40 seconds
//            Trigger trigger = TriggerBuilder.newTrigger()
//                    .withIdentity("trigger1", "group1")
//                    .startNow()
//                    .withSchedule(SimpleScheduleBuilder.simpleSchedule()
//                            .withIntervalInSeconds(1)
//                            .repeatForever())
//                    .build();
//
//            // Tell quartz to schedule the job using our trigger
//            
//            scheduler.scheduleJob(job, trigger);
//            Thread.sleep(6000);
//            System.out.println("teste finalizado!");
//            scheduler.shutdown();
//
//        } catch (SchedulerException se) {
//            se.printStackTrace();
//        }
//    }
}
