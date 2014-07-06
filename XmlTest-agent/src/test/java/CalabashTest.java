import jetbrains.buildServer.RunBuildException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class CalabashTest {


    @Test
    public void test() throws RunBuildException, IOException {
        List<String> args = new ArrayList<String>();

        args.add("-isource=D:\\Install\\TeamCity\\buildAgent\\work\\52f7dad4c57c1874\\SampleXSpecProject\\src\\test\\xspec\\xquery-test.xspec");
        args.add("-oresult=D:\\Install\\TeamCity\\buildAgent\\work\\52f7dad4c57c1874\\target\\xmltest-xspec-results\\samplexspecproject\\src\\test\\xspec\\xquery-test.html");
        args.add("xmlResult=file:/D:/Install/TeamCity/buildAgent/work/52f7dad4c57c1874/target/xmltest-xspec-results/samplexspecproject/src/test/xspec/xquery-test.xml");
        args.add("pathToIndexHtml=../../../../index.html");
        args.add("-p");
        args.add("xspec-home=file:/D:/Install/TeamCity/buildAgent/plugins/XmlTest-agent/runtime/xproc/");
        args.add("D:\\Install\\TeamCity\\buildAgent\\plugins\\XmlTest-agent\\runtime\\xproc\\xspec-xquery.xpl");

        com.xmlcalabash.drivers.Main.main(args.toArray(new String[args.size()]));
    }
}
