package cz.jakubmaly.xmltest.agent;


import cz.jakubmaly.xmltest.agent.xspec.ScenarioExecution;
import cz.jakubmaly.xmltest.agent.xspec.ScenariosSession;
import jetbrains.buildServer.RunBuildException;
import jetbrains.buildServer.agent.AgentRunningBuild;
import jetbrains.buildServer.agent.BuildProgressLogger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ScenariosSessionTest {

    ScenarioExecution xmlTestSessionExecution;

    @Mock
    ScenariosSession session;

    @Mock
    AgentRunningBuild agentRunningBuild;

    @Mock
    BuildProgressLogger logger;

    @Before
    public void setup(){
        xmlTestSessionExecution = new ScenarioExecution(session,
                "\\XmlTestTeamcityPlugin\\XmlTest-agent\\src\\test\\resources\\test.xspec");
        when(session.getAgentRunningBuild()).thenReturn(agentRunningBuild);
        when(agentRunningBuild.getBuildLogger()).thenReturn(logger);
        doAnswer(new Answer() {
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                String message = (String) invocationOnMock.getArguments()[0];
                System.out.println("LOG: " + message);
                return null;
            }
        }).when(logger).message(anyString());
    }

    @Test
    public void test() throws RunBuildException {
        xmlTestSessionExecution.beforeProcessStarted();
    }

    @Test
    public void testDoneScenarioParsed() {
        xmlTestSessionExecution.onErrorOutput("INFO: file:///TeamCity/buildAgent/plugins/XmlTest-agent/runtime/xproc/xspec-xslt.xpl:22:53:Done scenario: Test simple patterns/When encountering a whitespace character class");
        verify(logger).logTestFinished("Test simple patterns/When encountering a whitespace character class");
    }
}
