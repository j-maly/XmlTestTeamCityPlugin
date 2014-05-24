package cz.jakubmaly.xmltest.agent.xspec;

import cz.jakubmaly.xmltest.common.ArtifactsUtil;
import cz.jakubmaly.xmltest.common.files.PathUtils;
import cz.jakubmaly.xmltest.common.XmlTestConstants;
import jetbrains.buildServer.RunBuildException;
import jetbrains.buildServer.agent.AgentRunningBuild;
import jetbrains.buildServer.agent.BuildFinishedStatus;
import jetbrains.buildServer.agent.BuildProgressLogger;
import jetbrains.buildServer.agent.BuildRunnerContext;
import jetbrains.buildServer.agent.artifacts.ArtifactsWatcher;
import jetbrains.buildServer.agent.runner.CommandExecution;
import jetbrains.buildServer.agent.runner.MultiCommandBuildSession;
import jetbrains.buildServer.messages.DefaultMessagesInfo;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Document;

import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Uses {@link cz.jakubmaly.xmltest.agent.xspec.ScenariosLookup} to find xspec files.
 * Executes all found xspec files, one by one. Creates {@link cz.jakubmaly.xmltest.agent.xspec.ScenarioExecution}
 * for each xspec file.
 */
public class ScenariosSession implements MultiCommandBuildSession {

    private BuildRunnerContext buildRunnerContext;
    private ArtifactsWatcher artifactsWatcher;
    private ScenariosLookup xspecScenarioLookup;
    private SummaryReportBuilder summaryReportBuilder;

    private BuildFinishedStatus status;
    private String reportsOutputDirectory;
    private List<String> inputFiles;
    private List<ScenarioResultInfo> scenariosResults;
    private int currentInputFileIndex;

    public ScenariosSession(@NotNull BuildRunnerContext buildRunnerContext, @NotNull ArtifactsWatcher artifactsWatcher) {
        this.buildRunnerContext = buildRunnerContext;
        this.artifactsWatcher = artifactsWatcher;
        xspecScenarioLookup = new ScenariosLookup(getLogger());
        summaryReportBuilder = new SummaryReportBuilder();
    }

    public void sessionStarted() throws RunBuildException {
        getLogger().activityStarted("xmltest session", "Session started", DefaultMessagesInfo.BLOCK_TYPE_TEST_SUITE);
        List<String> detectedFiles = xspecScenarioLookup.detectFiles(getBuildRunnerContext().getRunnerParameters(),
                getAgentRunningBuild().getCheckoutDirectory(), getBuildRunnerContext().getWorkingDirectory());
        setInputFiles(detectedFiles);
        prepareOutputDirectory();
        currentInputFileIndex = 0;
        scenariosResults = new ArrayList<ScenarioResultInfo>();
    }

    private void prepareOutputDirectory() throws RunBuildException {
        getLogger().progressStarted("Preparing output directory");
        try {
            reportsOutputDirectory = PathUtils.joinToPath(getAgentRunningBuild().getCheckoutDirectory().getPath(), "target", "xmltest-xspec-results");
            File dir = new File(reportsOutputDirectory);
            if (dir.exists()) {
                FileUtils.forceDelete(dir);
            }
            FileUtils.forceMkdir(dir);
            getLogger().message("Output dir: " + reportsOutputDirectory);
        } catch (IOException e) {
            throw new RunBuildException("Failed to create temporary output directory", e);
        }
        getLogger().progressFinished();
    }

    @NotNull
    public BuildFinishedStatus sessionFinished() {
        getLogger().activityFinished("xmltest session", DefaultMessagesInfo.BLOCK_TYPE_TEST_SUITE);
        getLogger().message("Session result: " + getStatus());
        createSummaryReports();
        return getStatus() != null ? getStatus() : BuildFinishedStatus.FINISHED_SUCCESS;
    }

    private void createSummaryReports() {
        getLogger().message("Compiling summary report");
        try {
            summaryReportBuilder.setReportsOutputDirectory(getReportsOutputDirectory());
            summaryReportBuilder.setXspecResults(getScenariosResults());
            Document xmlSummaryReport = summaryReportBuilder.buildXmlReport();
            String indexXmlPath = PathUtils.joinToPath(getReportsOutputDirectory(), "index.xml");
            summaryReportBuilder.saveXmlReportToFile(xmlSummaryReport, indexXmlPath);
            getArtifactsWatcher().addNewArtifactsPath(indexXmlPath + "=>" + ArtifactsUtil.getInternalArtifactPath(""));
            getLogger().message("Summary report created: " + indexXmlPath);
            String stylesheetPath = PathUtils.joinToPath(getXmlPluginDirectory(), "runtime", "xproc", "reporter", "format-summary-report.xsl");
            summaryReportBuilder.saveHtmlReportToFile(xmlSummaryReport, getIndexHtmlPath(), new File(stylesheetPath));
            getArtifactsWatcher().addNewArtifactsPath(getIndexHtmlPath() + "=>" + ArtifactsUtil.getInternalArtifactPath(""));
            getLogger().message("Summary report created: " + indexXmlPath);
        } catch (TransformerException e) {
            getLogger().internalError("TransformerConfigurationException", "Failed to create final xml report", e);
        }
    }

    public String getIndexHtmlPath() {
        return PathUtils.joinToPath(getReportsOutputDirectory(), "index.html");
    }

    @Nullable
    public CommandExecution getNextCommand() throws RunBuildException {
        if (currentInputFileIndex < getInputFiles().size() && getStatus() != BuildFinishedStatus.INTERRUPTED) {
            ScenarioExecution commandExecution = new ScenarioExecution(this, getInputFiles().get(currentInputFileIndex));
            currentInputFileIndex++;
            return commandExecution;
        } else {
            return null;
        }
    }

    @NotNull
    public BuildRunnerContext getBuildRunnerContext() {
        return buildRunnerContext;
    }

    @NotNull
    public AgentRunningBuild getAgentRunningBuild() {
        return getBuildRunnerContext().getBuild();
    }

    @NotNull
    public BuildProgressLogger getLogger() {
        return getAgentRunningBuild().getBuildLogger();
    }

    @Nullable
    public BuildFinishedStatus getStatus() {
        return status;
    }

    public void setStatus(@NotNull BuildFinishedStatus status) {
        this.status = status;
    }

    @NotNull
    public String getReportsOutputDirectory() {
        return reportsOutputDirectory;
    }

    @NotNull
    public ArtifactsWatcher getArtifactsWatcher() {
        return artifactsWatcher;
    }

    @NotNull
    public List<ScenarioResultInfo> getScenariosResults() {
        return scenariosResults;
    }

    @NotNull
    public List<String> getInputFiles() {
        return inputFiles;
    }

    public void setInputFiles(@NotNull List<String> inputFiles) {
        this.inputFiles = inputFiles;
    }

    public String getXmlPluginDirectory() {
        return PathUtils.joinToPath(getPluginsDirectory(), XmlTestConstants.AGENT_PLUGIN_DIR);
    }

    public String getPluginsDirectory() {
        return getAgentRunningBuild().getAgentConfiguration().getAgentPluginsDirectory().getAbsolutePath();
    }
}
