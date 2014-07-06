package cz.jakubmaly.xmltest.agent.xspec;

import org.apache.commons.io.IOUtils;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class HarnessAsigner {

    public enum Harness {
        XSLT,
        XQuery
    }

    public Harness decideHarness(String xspecInput) throws IOException {
        InputStream is = new FileInputStream(xspecInput);
        try {
            return decideHarness(is);
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

    public Harness decideHarness(InputStream xspecInput) throws IOException {
        HarnessAssignerHandler handler = new HarnessAssignerHandler();

        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setNamespaceAware(true);
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(xspecInput, handler);
            return handler.getHarness();
        } catch (Exception e) {
            throw new IOException("Failed to decide XSLT/XQuery harness for input. ", e);
        }
    }

    private class HarnessAssignerHandler extends DefaultHandler {
        private Harness harness;

        public Harness getHarness() {
            return harness;
        }

        public void startPrefixMapping(java.lang.String prefix, java.lang.String uri)
                throws SAXException {
        }

        public void startElement(String uri, String localName, String qName, org.xml.sax.Attributes attributes)
                throws SAXException {
            if (uri != null && localName != null
                    && localName.equals("description")
                    && uri.equals("http://www.jenitennison.com/xslt/xspec")) {
                int ind = attributes.getIndex("stylesheet");
                if (ind >= 0) {
                    harness = Harness.XSLT;
                }

                ind = attributes.getIndex("query");
                if (ind >= 0) {
                    harness = Harness.XQuery;
                }
            }
        }
    }
}
