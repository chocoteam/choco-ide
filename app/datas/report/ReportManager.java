package datas.report;

/**
 * Class to create and sent report to admin.
 * Created by Mathieu on 26/05/2015.
 */
public class ReportManager {
    private static ReportManager instance;

    /**
     * @return a unique instance of the report manager
     */
    public static ReportManager getInstance() {
        if (instance == null) instance = new ReportManager();
        return instance;
    }

    public boolean sendReport(Report report) {
        //TODO
        return false;
    }

    public Report createReportCompilation(String sourceCode, String sdtOut, String compilationError, String userEmail) {
        Report report = new Report(sdtOut,sourceCode,userEmail);
        report.setCompilationError(compilationError);
        return report;
    }

    public Report createReportExecution(String sourceCode, String sdtOut, String errOut, String userEmail) {
        Report report = new Report(sdtOut,sourceCode,userEmail);
        report.setExceptionStackTrace(errOut);
        return report;
    }
}
