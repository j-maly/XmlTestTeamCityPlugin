package cz.jakubmaly.xmltest.agent.xspec;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class HarnessAsignerTest {

    @Test
    public void should_recognize_xslt_harness() throws IOException {
        HarnessAsigner harnessAsigner = new HarnessAsigner();
        InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("test.xspec");
        HarnessAsigner.Harness harness = harnessAsigner.decideHarness(resourceAsStream);
        assertThat(harness).isEqualTo(HarnessAsigner.Harness.XSLT);
    }

    @Test
    public void should_recognize_xquery_harness() throws IOException {
        HarnessAsigner harnessAsigner = new HarnessAsigner();
        InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("xquery-test.xspec");
        HarnessAsigner.Harness harness = harnessAsigner.decideHarness(resourceAsStream);
        assertThat(harness).isEqualTo(HarnessAsigner.Harness.XQuery);
    }
}
