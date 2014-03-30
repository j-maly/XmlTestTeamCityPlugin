package cz.jakubmaly.xmltest.server;

import cz.jakubmaly.xmltest.common.XmlTestConstants;
import cz.jakubmaly.xmltest.common.XmlTestVersion;
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

  @NotNull
  public String getAddtionalOptionsKey() {
    return XmlTestConstants.SETTINGS_ADDITIONAL_OPTIONS;
  }

  @NotNull
  public String getReportXsltKey() {
    return XmlTestConstants.SETTINGS_REPORT_XSLT;
  }

  @NotNull
  public String getRootKey() {
    return XmlTestConstants.SETTINGS_XMLTEST_ROOT;
  }

  @NotNull
  public String getVersionKey() {
    return XmlTestConstants.SETTINGS_XMLTEST_VERSION;
  }

  @NotNull
  public String getSearchDirsKey() {
    return XmlTestConstants.SETTINGS_SEARCH_DIRS;
  }

  @NotNull
  public String getSearchInGacKey() {
    return XmlTestConstants.SETTINGS_SEARCH_IN_GAC;
  }

  @NotNull
  public String getIgnoreGeneratedCodeKey() {
    return XmlTestConstants.SETTINGS_IGNORE_GENERATED_CODE;
  }

  @NotNull
  public String getFailOnAnalysisErrorKey() {
    return XmlTestConstants.SETTINGS_FAIL_ON_ANALYSIS_ERROR;
  }

  @NotNull
  public XmlTestVersion[] getAvaliableVersions() {
    return XmlTestVersion.values();
  }

  @NotNull
  public String getDetectionModeKey() {
    return XmlTestConstants.SETTINGS_DETECTION_MODE;
  }

  @NotNull
  public String getDetectionModeAuto() {
    return XmlTestConstants.DETECTION_MODE_AUTO;
  }

  @NotNull
  public String getDetectionModeManual() {
    return XmlTestConstants.DETECTION_MODE_MANUAL;
  }
}
