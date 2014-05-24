package cz.jakubmaly.xmltest.agent.xspec;

import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

import java.util.ArrayList;

import static org.custommonkey.xmlunit.XMLAssert.assertXMLEqual;

public class SummaryReportBuilderTest {
    SummaryReportBuilder builder;

    @Before
    public void setUp() throws Exception {
        builder = new SummaryReportBuilder();
    }

    @Test
    public void testBuildReport() throws Exception {
        // ARRANGE
        ArrayList<ScenarioResultInfo> outputFiles = new ArrayList<ScenarioResultInfo>();
        // all passed
        outputFiles.add(new ScenarioResultInfo(2,0,0,2, "/dir1/file1.xspec", "/dir1/file1.html", "/dir1/file1.xml"));
        // some failed
        outputFiles.add(new ScenarioResultInfo(1,2,3,6, "/dir1/file2.xspec", "/dir1/file2.html", "/dir1/file2.xml"));
        // some pending
        outputFiles.add(new ScenarioResultInfo(2,2,0,4, "/file3.xspec", "/file3.html", "/file3.xml"));
        builder.setXspecResults(outputFiles);
        // ACT
        Document document = builder.buildXmlReport();
        // ASSERT
        String expected =
                "<xspec-reports>" +
                        "<report inputFile='/dir1/file1.xspec' result='successful' " +
                            "outputHtmlFile='/dir1/file1.html' " +
                            "outputXmlFile='/dir1/file1.xml' " +
                            " passed='2' pending='0' failed='0' total='2' />" +
                        "<report inputFile='/dir1/file2.xspec' result='failed' " +
                            "outputHtmlFile='/dir1/file2.html' " +
                            "outputXmlFile='/dir1/file2.xml' " +
                            " passed='1' pending='2' failed='3' total='6' />" +
                        "<report inputFile='/file3.xspec' result='pending' " +
                            "outputHtmlFile='/file3.html' " +
                            "outputXmlFile='/file3.xml' " +
                            " passed='2' pending='2' failed='0' total='4' />" +
                "</xspec-reports>";
        assertXMLEqual(XMLUnit.buildControlDocument(expected), document);
    }
}
