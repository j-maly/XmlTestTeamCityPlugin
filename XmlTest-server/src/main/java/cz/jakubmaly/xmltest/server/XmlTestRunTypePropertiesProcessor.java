package cz.jakubmaly.xmltest.server;

import cz.jakubmaly.xmltest.common.XmlTestConstants;
import jetbrains.buildServer.serverSide.InvalidProperty;
import jetbrains.buildServer.serverSide.PropertiesProcessor;
import jetbrains.buildServer.util.PropertiesUtil;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class XmlTestRunTypePropertiesProcessor implements PropertiesProcessor {
    public Collection<InvalidProperty> process(Map<String, String> properties) {
        List<InvalidProperty> result = new Vector<InvalidProperty>();

        final String files = properties.get(XmlTestConstants.SETTINGS_FILES);
        final String project = properties.get(XmlTestConstants.SETTINGS_PROJECT);

        if (PropertiesUtil.isEmptyOrNull(project) && PropertiesUtil.isEmptyOrNull(files)) {
            result.add(new InvalidProperty(XmlTestConstants.SETTINGS_FILES, "Files or project option must be specified"));
        }

        return result;
    }
}
