package cz.jakubmaly.xmltest.agent.xspec;

import cz.jakubmaly.xmltest.common.files.PathUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.util.List;


public class SummaryReportBuilder {
    private List<ScenarioResultInfo> xspecResults;
    private String reportsOutputDirectory;
    private String summaryXmlPath;
    private String indexHtmlPath;

    public Document buildXmlReport() {
        summaryXmlPath = PathUtils.joinToPath(getReportsOutputDirectory(), "summary.xml");
        indexHtmlPath = PathUtils.joinToPath(getReportsOutputDirectory(), "index.html");
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("xspec-reports");
            doc.appendChild(rootElement);
            for (ScenarioResultInfo xspecResult : xspecResults) {
                addResultElement(doc, rootElement, xspecResult);
            }
            return doc;
        } catch (ParserConfigurationException e) {

        }
        return null;
    }

    private void addResultElement(Document doc, Element rootElement, ScenarioResultInfo xspecResult) {
        Element reportElement = doc.createElement("report");
        rootElement.appendChild(reportElement);

        reportElement.setAttribute("inputFile", xspecResult.getInputFile());
        reportElement.setAttribute("outputHtmlFile", xspecResult.getOutputHtmlFile());
        reportElement.setAttribute("outputXmlFile", xspecResult.getOutputXmlFile());
        reportElement.setAttribute("passed", Integer.toString(xspecResult.getPassed()));
        reportElement.setAttribute("pending", Integer.toString(xspecResult.getPending()));
        reportElement.setAttribute("failed", Integer.toString(xspecResult.getFailed()));
        reportElement.setAttribute("total", Integer.toString(xspecResult.getTotal()));
        String result;
        if (xspecResult.getFailed() > 0)
            result = "failed";
        else if (xspecResult.getPending() > 0)
            result = "pending";
        else
            result = "successful";
        reportElement.setAttribute("result", result);
    }

    public void setReportsOutputDirectory(String reportsOutputDirectory) {
        this.reportsOutputDirectory = reportsOutputDirectory;
    }

    public String getReportsOutputDirectory() {
        return reportsOutputDirectory;
    }

    public List<ScenarioResultInfo> getXspecResults() {
        return xspecResults;
    }

    public void setXspecResults(List<ScenarioResultInfo> xspecResults) {
        this.xspecResults = xspecResults;
    }

    public void saveXmlReportToFile(Document xmlReport, String file) throws TransformerException {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        Result output = new StreamResult(new File(file));
        Source input = new DOMSource(xmlReport);
        transformer.transform(input, output);
    }

    public void saveHtmlReportToFile(Document xmlReport, String file, File stylesheet)  throws TransformerException {
        Source stylesheetSource = new StreamSource(stylesheet);
        Transformer transformer = TransformerFactory.newInstance().newTransformer(stylesheetSource);
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        Result output = new StreamResult(new File(file));
        Source input = new DOMSource(xmlReport);
        transformer.transform(input, output);

    }
}
