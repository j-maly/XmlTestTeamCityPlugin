package cz.jakubmaly.xmltest.agent.xspec;

import jetbrains.buildServer.agent.BuildProgressLogger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class OutputParserTest {

    private OutputParser parser;

    @Mock
    BuildProgressLogger logger;

    @Before
    public void setUp() {
        parser = new OutputParser();
    }
    @Test
    public void testCleanup() {
        String input = "INFO: file:///TeamCity/buildAgent/plugins/XmlTest-agent/runtime/xproc/xspec-xslt.xpl:22:53:Testing with: SAXON HE 9.4.0.7";
        parser.printCleanedLine(input, logger);
        verify(logger).message("xspec-xslt.xpl:22:53:Testing with: SAXON HE 9.4.0.7");
    }

    @Test
    public void testCleanupEmpty() {
        String input = "INFO: file:///TeamCity/buildAgent/plugins/XmlTest-agent/runtime/xproc/xspec-xslt.xpl:22:53:";
        parser.printCleanedLine(input, logger);
        verifyNoMoreInteractions(logger);
    }
}
