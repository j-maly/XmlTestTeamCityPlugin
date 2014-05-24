package cz.jakubmaly.xmltest.agent;

import cz.jakubmaly.xmltest.agent.xspec.ScenariosSession;
import cz.jakubmaly.xmltest.common.XmlTestConstants;
import jetbrains.buildServer.RunBuildException;
import jetbrains.buildServer.agent.AgentBuildRunnerInfo;
import jetbrains.buildServer.agent.BuildAgentConfiguration;
import jetbrains.buildServer.agent.BuildRunnerContext;
import jetbrains.buildServer.agent.artifacts.ArtifactsWatcher;
import jetbrains.buildServer.agent.runner.MultiCommandBuildSession;
import jetbrains.buildServer.agent.runner.MultiCommandBuildSessionFactory;
import org.jetbrains.annotations.NotNull;

public class XmlTestBuildServiceFactory implements MultiCommandBuildSessionFactory, AgentBuildRunnerInfo {

    private final ArtifactsWatcher myArtifactsWatcher;

    public XmlTestBuildServiceFactory(@NotNull final ArtifactsWatcher artifactsWatcher) {
        myArtifactsWatcher = artifactsWatcher;
    }

    @NotNull
    public String getType() {
        return XmlTestConstants.RUNNER_TYPE;
    }

    public boolean canRun(@NotNull final BuildAgentConfiguration agentConfiguration) {
        return true;
    }

    @NotNull
    public AgentBuildRunnerInfo getBuildRunnerInfo() {
        return this;
    }

    @NotNull
    public MultiCommandBuildSession createSession(@NotNull BuildRunnerContext buildRunnerContext) throws RunBuildException {
        return new ScenariosSession(buildRunnerContext, myArtifactsWatcher);
    }
}