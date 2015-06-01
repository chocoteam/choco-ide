package datas.report;

import org.apache.commons.mail.*;
import play.Logger;
import play.Play;

import javax.mail.util.ByteArrayDataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to create and sent report to admin.
 * Created by Mathieu on 26/05/2015.
 */
public class ReportManager {
    private static ReportManager instance;

    private String smtpHost, user, password;
    private int smtpPort;
    private boolean useSSL;
    private List<String> emailTo;

    /**
     * @return a unique instance of the report manager
     */
    public static ReportManager getInstance() {
        if (instance == null) instance = new ReportManager();
        return instance;
    }

    /**
     * Get all the params in configuration file
     */
    private void initMailParam() {
        this.emailTo = new ArrayList<>();
        this.smtpHost = Play.application().configuration().getString("mail.host");
        this.smtpPort = Play.application().configuration().getInt("mail.smtp.port");
        this.useSSL = Play.application().configuration().getBoolean("mail.ssl");
        this.user = Play.application().configuration().getString("mail.user");
        this.password = Play.application().configuration().getString("mail.password");
        String[] toEmails = Play.application().configuration().getString("mail.to").split(",");
        for (String email : toEmails) {
            emailTo.add(email);
        }
    }

    /**
     * Configure the mail server for a given email
     *
     * @param email the email that will be sent
     */
    private void initSendingParam(Email email) {
        try {
            email.setHostName(this.smtpHost);
            email.setSmtpPort(this.smtpPort);
            email.setAuthenticator(new DefaultAuthenticator(this.user, this.password));
            email.setSSLOnConnect(this.useSSL);
            for (String mailAddress : emailTo) {
                email.addTo(mailAddress);
            }
        } catch (Exception e) {
            Logger.error("Problem when initialize the client", e);
        }
    }

    public boolean sendReport(Report report) {
        try {
            //Init
            MultiPartEmail email = new MultiPartEmail();
            initSendingParam(email);
            email.setFrom(report.getSenderEmail());
            //Console
            email.setMsg("Bonjour,\nUn utilisateur a reporté un problème sur le site ChocoIDE.\nVoici son message :\n\n"+report.getMessage());
            //Attachement
            createAttachement(email,report);
            //Send
            email.send();
            return true;
        } catch (Exception e) {
            Logger.error("Couldn't send the report email ", e);
            return false;
        }
    }

    private ByteArrayDataSource getSource(String str) {
        return new ByteArrayDataSource(str.getBytes(), "text/plain");
    }

    private void createAttachement(MultiPartEmail email,Report report) throws EmailException {
        //Compilation
        if (report.getCompilationError() != null) {
            email.attach(getSource(report.getCompilationError()),"compilationError.txt", EmailAttachment.ATTACHMENT);
        }
        //Output
        if (report.getConsoleOutput() != null) {
            email.attach(getSource(report.getConsoleOutput()),"stdOut.txt", EmailAttachment.ATTACHMENT);
        }
        //Err output
        if (report.getExceptionStackTrace() != null) {
            email.attach(getSource(report.getExceptionStackTrace()),"stdErr.txt", EmailAttachment.ATTACHMENT);
        }
    }

    public Report createReportCompilation(String message,String sourceCode, String sdtOut, String compilationError, String userEmail) {
        Report report = new Report(sdtOut, sourceCode, userEmail);
        report.setCompilationError(compilationError);
        return report;
    }

    public Report createReportExecution(String message,String sourceCode, String sdtOut, String errOut, String userEmail) {
        Report report = new Report(sdtOut, sourceCode, userEmail);
        report.setExceptionStackTrace(errOut);
        return report;
    }
}
