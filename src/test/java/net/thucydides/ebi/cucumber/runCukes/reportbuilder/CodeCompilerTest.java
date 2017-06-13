package net.thucydides.ebi.cucumber.runCukes.reportbuilder;


import net.thucydides.ebi.cucumber.framework.report.CodeCompiler;
import net.thucydides.ebi.cucumber.framework.report.MergeReports;
import org.junit.Test;


public class CodeCompilerTest {
    @Test
    public void compilerTest(){
        CodeCompiler codeCompiler = new CodeCompiler();
        try {
            codeCompiler.compilerTest();
        } catch (Exception e) {
            System.out.println("Report builder failed to merge the cucumber reports");
        }
    }
}
