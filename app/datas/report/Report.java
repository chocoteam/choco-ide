package datas.report;

/**
 * Class that represent a report that will be sent to admin.<br>
 * A report contains all the informations to find problems in Choco.
 * Created by Mathieu on 26/05/2015.
 */
public class Report {
    private static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    /**
     * The compilation error, can be null
     */
    private String compilationError;

    /**
     * The execution stack trace, can be null if compationError exist
     */
    private String exceptionStackTrace;

    /**
     * Standard output
     */
    private String consoleOutput;

    /**
     * The source code that produced the error
     */
    private String sourceCode;

    /**
     * User email address
     */
    private String senderEmail;


    Report(String consoleOutput, String sourceCode, String senderEmail) {
        this.consoleOutput = consoleOutput;
        this.sourceCode = sourceCode;
        this.senderEmail = senderEmail;
    }

    public String getCompilationError() {
        return compilationError;
    }

    public void setCompilationError(String compilationError) {
        this.compilationError = compilationError;
    }

    public String getExceptionStackTrace() {
        return exceptionStackTrace;
    }

    public void setExceptionStackTrace(String exceptionStackTrace) {
        this.exceptionStackTrace = exceptionStackTrace;
    }

    public String getConsoleOutput() {
        return consoleOutput;
    }

    public void setConsoleOutput(String consoleOutput) {
        this.consoleOutput = consoleOutput;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    /**
     * Check a email address to know if it's a valid adress
     *
     * @param email the email to check
     * @return true if email is valid, false
     */
    public static boolean isValidEmail(String email) {
        return email.matches(EMAIL_REGEX);
    }
}
