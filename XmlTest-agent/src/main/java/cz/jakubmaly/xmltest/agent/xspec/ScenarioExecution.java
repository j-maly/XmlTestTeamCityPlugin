package cz.jakubmaly.xmltest.agent.xspec;

import cz.jakubmaly.xmltest.common.ArtifactsUtil;
import cz.jakubmaly.xmltest.common.files.PathUtils;
import jetbrains.buildServer.BuildProblemData;
import jetbrains.buildServer.RunBuildException;
import jetbrains.buildServer.agent.AgentRunningBuild;
import jetbrains.buildServer.agent.BuildFinishedStatus;
import jetbrains.buildServer.agent.BuildParametersMap;
import jetbrains.buildServer.agent.BuildProgressLogger;
import jetbrains.buildServer.agent.runner.CommandExecution;
import jetbrains.buildServer.agent.runner.ProgramCommandLine;
import jetbrains.buildServer.agent.runner.TerminationAction;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents one calabash call (processing one xspec file)
 */
public class ScenarioExecution implements CommandExecution {

    private ScenariosSession scenariosSession;
    private String xspecInput;
    private String htmlOutput;
    private String xmlOutput;
    private ScenarioResultInfo scenarioResultInfo;
    private String currentScenario;
    private OutputParser outputParser = new OutputParser();
    private HarnessAsigner harnessAsigner = new HarnessAsigner();
    private BuildProgressLogger logger;

    public ScenarioExecution(ScenariosSession scenariosSession, String xspecInput) {
        this.scenariosSession = scenariosSession;
        this.xspecInput = xspecInput;
    }

    @NotNull
    public ProgramCommandLine makeProgramCommandLine() throws RunBuildException {
        return new ProgramCommandLine() {

            @NotNull
            public String getExecutablePath() throws RunBuildException {
                return "java";
            }

            @NotNull
            public String getWorkingDirectory() throws RunBuildException {
                return getScenariosSession().getBuildRunnerContext().getWorkingDirectory().getPath();
            }

            @NotNull
            public Map<String, String> getEnvironment() throws RunBuildException {
                return getBuildParameters().getEnvironmentVariables();
            }

            @NotNull
            public List<String> getArguments() throws RunBuildException {
                htmlOutput = PathUtils.joinToPath(getScenariosSession().getReportsOutputDirectory(),
                        FilenameUtils.removeExtension(xspecInput) + "." + "html");
                xmlOutput = PathUtils.joinToPath(getScenariosSession().getReportsOutputDirectory(),
                        FilenameUtils.removeExtension(xspecInput) + "." + "xml");
                String relativeIndexHtml = PathUtils.getRelativePath(scenariosSession.getIndexHtmlPath(),
                        htmlOutput, File.separator);

                HarnessAsigner.Harness harness;
                try {
                    harness = harnessAsigner.decideHarness(PathUtils.joinToPath(getAgentRunningBuild().getCheckoutDirectory().getAbsolutePath(), xspecInput));
                    FileUtils.forceMkdir(new File(new File(htmlOutput).getParent()));
                } catch (IOException e) {
                    throw new RunBuildException(e);
                }
                ArrayList<String> args = new ArrayList<String>();
                args.add("-classpath");
                args.add(PathUtils.joinToPath(getScenariosSession().getXmlPluginDirectory(), "lib", "*"));
                args.add("com.xmlcalabash.drivers.Main");
                args.add("-isource=" + xspecInput.replace("\\","/"));
                args.add("-oresult=" + htmlOutput.replace("\\","/"));
                args.add("xmlResult=" + new File(xmlOutput).toURI());
                args.add("pathToIndexHtml=" + relativeIndexHtml.replace('\\', '/'));
                String pipeline;
                if (harness == HarnessAsigner.Harness.XSLT) {
                    pipeline = PathUtils.joinToPath(getScenariosSession().getXmlPluginDirectory(), "runtime", "xproc", "xspec-xslt.xpl");
                } else {
                    pipeline = PathUtils.joinToPath(getScenariosSession().getXmlPluginDirectory(), "runtime", "xproc", "xspec-xquery.xpl");
                    // TODO relative path
                    args.add("-p");
                    args.add("xspec-home=file:/d:/GitHub/XmlTestTeamcityPlugin/XmlTest-agent/src/runtime/xproc/");
                }
                args.add(pipeline);
                return args;
            }
        };
    }

    public void beforeProcessStarted() throws RunBuildException { }

    @NotNull
    public TerminationAction interruptRequested() {
        getLogger().error("Run interrupted");
        getScenariosSession().setStatus(BuildFinishedStatus.INTERRUPTED);
        return TerminationAction.KILL_CREATED_PROCESS;
    }

    public boolean isCommandLineLoggingEnabled() {
        return true;
    }

    public void onStandardOutput(@NotNull String outputLine) {
        getLogger().message(outputLine);
    }

    public void onErrorOutput(@NotNull String outputLine) {
        if (!outputLine.trim().endsWith("com.xmlcalabash.util.DefaultXProcMessageListener info")) {
            outputParser.printCleanedLine(outputLine, getLogger());
            if (outputParser.isStartLine(outputLine)) {
                currentScenario = outputParser.getCurrentScenario(outputLine);
                getLogger().logTestStarted(currentScenario);
            }
            if (outputParser.isDoneLine(outputLine)) {
                String doneScenario = outputParser.getDoneScenario(outputLine);
                getLogger().logTestFinished(doneScenario);
            }
            if (outputParser.isFailedLine(outputLine)) {
                getLogger().logTestFailed(currentScenario, "scenario failed", null);
            }
            if (outputParser.isResultsLine(outputLine)) {
                outputParser.writeResults(scenarioResultInfo, outputLine);
            }
        }
    }

    public void processStarted(@NotNull String commandLine, @NotNull File checkoutDirectory) {
        getLogger().logSuiteStarted(xspecInput);
        scenarioResultInfo = new ScenarioResultInfo();
        String relativeHtml = PathUtils.getRelativePath(htmlOutput, scenariosSession.getReportsOutputDirectory(), File.separator);
        String relativeXml = PathUtils.getRelativePath(xmlOutput, scenariosSession.getReportsOutputDirectory(), File.separator);
        scenarioResultInfo.setOutputHtmlFile(relativeHtml);
        scenarioResultInfo.setOutputXmlFile(relativeXml);
        scenarioResultInfo.setInputFile(xspecInput);
    }

    public void processFinished(int processReturnCode) {
        if (processReturnCode != 0) {
            getLogger().logTestFailed(xspecInput, "Error occurred in XSpec pipeline, examine calabash output.", null);
            getLogger().logBuildProblem(BuildProblemData.createBuildProblem("ERR_" + xspecInput, "XPROC_ERROR", "XProc error occurred when running: " + xspecInput));
            getScenariosSession().setStatus(BuildFinishedStatus.FINISHED_FAILED);
        } else {
            getScenariosSession().getArtifactsWatcher().addNewArtifactsPath(
                    htmlOutput + "=>" + ArtifactsUtil.getInternalArtifactPath(""));
            getScenariosSession().getArtifactsWatcher().addNewArtifactsPath(
                    xmlOutput + "=>" + ArtifactsUtil.getInternalArtifactPath(""));
            getScenariosSession().getScenariosResults().add(scenarioResultInfo);
            if (scenarioResultInfo != null && scenarioResultInfo.isScenarioCompleted()) {
                if (scenarioResultInfo.getFailed() > 0) {
                    scenarioResultInfo.printWithLogger(getLogger());
                } else {
                    getLogger().message("All xspec scenarios succeeded");
                }
            } else {
                getLogger().message("Test results inconclusive, failed to find result line in calabash output.");
                getLogger().logTestFailed(xspecInput, "Test results inconclusive, failed to find result line in calabash output.", null);
            }
        }
        getLogger().logSuiteFinished(xspecInput);
    }

    private BuildProgressLogger getLogger() {
        return getAgentRunningBuild().getBuildLogger();
    }

    private BuildParametersMap getBuildParameters() {
        return getScenariosSession().getBuildRunnerContext().getBuildParameters();
    }

    public AgentRunningBuild getAgentRunningBuild() {
        return getScenariosSession().getAgentRunningBuild();
    }

    public ScenariosSession getScenariosSession() {
        return scenariosSession;
    }


}