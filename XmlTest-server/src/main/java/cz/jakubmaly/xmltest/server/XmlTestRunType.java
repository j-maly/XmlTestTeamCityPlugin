package cz.jakubmaly.xmltest.server;

import cz.jakubmaly.xmltest.common.XmlTestConstants;
import jetbrains.buildServer.requirements.Requirement;
import jetbrains.buildServer.serverSide.PropertiesProcessor;
import jetbrains.buildServer.serverSide.RunType;
import jetbrains.buildServer.serverSide.RunTypeRegistry;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XmlTestRunType extends RunType {
    private final PluginDescriptor myPluginDescriptor;

    public XmlTestRunType(final RunTypeRegistry runTypeRegistry, final PluginDescriptor pluginDescriptor) {
        myPluginDescriptor = pluginDescriptor;
        runTypeRegistry.registerRunType(this);
    }

    @Override
    public PropertiesProcessor getRunnerPropertiesProcessor() {
        return new XmlTestRunTypePropertiesProcessor();
    }

    @NotNull
    @Override
    public String getDescription() {
        return XmlTestConstants.RUNNER_DESCRIPTION;
    }

    @Override
    public String getEditRunnerParamsJspFilePath() {
        return myPluginDescriptor.getPluginResourcesPath("editXmlTestRunParams.jsp");
    }

    @Override
    public String getViewRunnerParamsJspFilePath() {
        return myPluginDescriptor.getPluginResourcesPath("viewXmlTestRunParams.jsp");
    }

    @Override
    public Map<String, String> getDefaultRunnerProperties() {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put(XmlTestConstants.SETTINGS_WHAT_TO_INSPECT, XmlTestConstants.WHAT_TO_INSPECT_FILES);
        return parameters;
    }

    @Override
    @NotNull
    public String getType() {
        return XmlTestConstants.RUNNER_TYPE;
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return XmlTestConstants.RUNNER_DISPLAY_NAME;
    }

    @NotNull
    @Override
    public String describeParameters(@NotNull final Map<String, String> parameters) {
        StringBuilder result = new StringBuilder();
        String what = parameters.get(XmlTestConstants.SETTINGS_WHAT_TO_INSPECT);
        if (what == null || XmlTestConstants.WHAT_TO_INSPECT_FILES.equals(what)) {
            String settings = parameters.get(XmlTestConstants.SETTINGS_FILES);
            result.append("XSpec Files: ").append(settings != null ? settings : "");
        } else {
            String settings = parameters.get(XmlTestConstants.SETTINGS_PROJECT);
            result.append("XmlTest project: ").append(settings != null ? settings : "");
        }
        return result.toString();
    }

    @NotNull
    @Override
    public List<Requirement> getRunnerSpecificRequirements(@NotNull final Map<String, String> runParameters) {
        return XmlTestRequirementsUtil.getXmlTestRequirements(runParameters);
    }
}
