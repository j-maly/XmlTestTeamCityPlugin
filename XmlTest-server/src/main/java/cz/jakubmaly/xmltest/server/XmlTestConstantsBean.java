package cz.jakubmaly.xmltest.server;

import cz.jakubmaly.xmltest.common.XmlTestConstants;
import org.jetbrains.annotations.NotNull;

public class XmlTestConstantsBean {
    @NotNull
    public String getWhatToInspectKey() {
        return XmlTestConstants.SETTINGS_WHAT_TO_INSPECT;
    }

    @NotNull
    public String getFilesKey() {
        return XmlTestConstants.SETTINGS_FILES;
    }

    @NotNull
    public String getFilesExcludeKey() {
        return XmlTestConstants.SETTINGS_FILES_EXCLUDE;
    }

    @NotNull
    public String getProjectKey() {
        return XmlTestConstants.SETTINGS_PROJECT;
    }

    @NotNull
    public String getWhatToInspectProjectValue() {
        return XmlTestConstants.WHAT_TO_INSPECT_PROJECT;
    }

    @NotNull
    public String getWhatToInspectFilesValue() {
        return XmlTestConstants.WHAT_TO_INSPECT_FILES;
    }
}
