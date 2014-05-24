package cz.jakubmaly.xmltest.agent.xspec;

import jetbrains.buildServer.agent.BuildProgressLogger;

/**
 * Object representing the results of executing one xspec file.
 */
public class ScenarioResultInfo {
    private int passed;
    private int pending;
    private int failed;
    private int total;
    private String inputFile;
    private String outputHtmlFile;
    private String outputXmlFile;
    private boolean scenarioCompleted;

    public ScenarioResultInfo() {
    }

    public ScenarioResultInfo(int passed, int pending, int failed, int total,
                              String input, String outputHtml, String outputXml) {
        this.passed = passed;
        this.pending = pending;
        this.failed = failed;
        this.total = total;
        this.inputFile = input;
        this.outputXmlFile = outputXml;
        this.outputHtmlFile = outputHtml;
    }

    public int getPassed() {
        return passed;
    }

    public void setPassed(int passed) {
        this.passed = passed;
    }

    public int getPending() {
        return pending;
    }

    public void setPending(int pending) {
        this.pending = pending;
    }

    public int getFailed() {
        return failed;
    }

    public void setFailed(int failed) {
        this.failed = failed;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void printWithLogger(BuildProgressLogger logger) {
        logger.message(String.format("Scenarios passed: %s", passed));
        logger.message(String.format("Scenarios pending: %s", pending));
        logger.message(String.format("Scenarios failed: %s", failed));
        logger.message(String.format("Scenarios total: %s", total));
    }

    public String getInputFile() {
        return inputFile;
    }

    public void setInputFile(String inputFile) {
        this.inputFile = inputFile;
    }

    public String getOutputHtmlFile() {
        return outputHtmlFile;
    }

    public void setOutputHtmlFile(String outputHtmlFile) {
        this.outputHtmlFile = outputHtmlFile;
    }

    public String getOutputXmlFile() {
        return outputXmlFile;
    }

    public void setOutputXmlFile(String outputXmlFile) {
        this.outputXmlFile = outputXmlFile;
    }

    public void setScenarioCompleted(boolean scenarioCompleted) {
        this.scenarioCompleted = scenarioCompleted;
    }

    public boolean isScenarioCompleted() {
        return scenarioCompleted;
    }
}
