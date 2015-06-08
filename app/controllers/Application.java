package controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import datas.compilation.StringCompilerAndRunner;
import datas.keywords.KeywordsManager;
import datas.report.Report;
import datas.report.ReportManager;
import datas.samples.Sample;
import datas.samples.SampleManager;
import play.Logger;
import play.libs.F;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.WebSocket;
import views.html.index;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render("Let's get working guys !"));
    }

    public static WebSocket<String> socket(){
        return new WebSocket<String>() {
            @Override
            public void onReady(In<String> in, Out<String> out) {
                in.onMessage(code -> {
                    System.out.println("Code reçu : " + code);
                    new StringCompilerAndRunner().compileAndRun(code,out);
                });
            }
        };
    }

    public static Result getCodeSampleList() {
        try {
            Collection<Sample> availableSample = SampleManager.getInstance().getAvailableSample();
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(availableSample);
            return ok(json);
        } catch (Exception e) {
            Logger.warn("Problem while getting the samples", e);
            return internalServerError("Problem while getting the samples : " + e.getMessage());
        }
    }

    public static Result reportError() {
        final Map<String, String[]> mapParameters = request().body().asFormUrlEncoded();
        String sourceCode = mapParameters.get("sourceCode")[0];
        String comment = mapParameters.get("comment")[0];
        String userEmail = mapParameters.get("userEmail")[0];
        String stdOut = mapParameters.get("stdOut")[0];
        String stdErr = mapParameters.get("stdErr")[0];
        String compilationErr = mapParameters.get("compilationErr")[0];

        Report report;
        //Compilation error
        if (stdErr == null || stdErr.trim().isEmpty()) {
            report = ReportManager.getInstance().createReportCompilation(comment,sourceCode, stdOut, compilationErr, userEmail);
        }
        //Execution error
        else {
            report = ReportManager.getInstance().createReportExecution(comment,sourceCode, stdOut, stdErr, userEmail);
        }
        boolean sent = false;
        if (Report.isValidEmail(report.getSenderEmail())) {
            sent = ReportManager.getInstance().sendReport(report);
        }
        if (!sent) {
            return internalServerError();
        } else {
            return ok();
        }
    }

    public static Result getKeywords() throws IOException, ClassNotFoundException {
        String chocoClasses = KeywordsManager.getChocoClassesName();
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(chocoClasses);
        return ok(json);
    }
}
