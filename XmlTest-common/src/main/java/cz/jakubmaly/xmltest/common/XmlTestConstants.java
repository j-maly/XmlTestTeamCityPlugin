package cz.jakubmaly.xmltest.common;

public interface XmlTestConstants {
  String XMLTEST_ROOT_PROPERTY = "system.XmlTestRoot";
  String XMLTESTCMD_FILE_VERSION_PROPERTY = "system.XmlTestCmdFileVersion";

  String RUNNER_TYPE = "XmlTest";
  String XMLTESTCMD_BINARY = "XmlTestCmd.exe";

  String SETTINGS_FILES = "fxcop.files";
  String SETTINGS_FILES_EXCLUDE = "fxcop.files_exclude";
  String SETTINGS_PROJECT = "fxcop.project";
  String SETTINGS_ADDITIONAL_OPTIONS = "fxcop.addon_options";
  String SETTINGS_REPORT_XSLT = "fxcop.report_xslt";
  String SETTINGS_XMLTEST_ROOT = "fxcop.root";
  String SETTINGS_XMLTEST_VERSION = "fxcop.version";
  String SETTINGS_SEARCH_DIRS = "fxcop.search_in_dirs";
  String SETTINGS_SEARCH_IN_GAC = "fxcop.search_in_gac";
  String SETTINGS_IGNORE_GENERATED_CODE = "fxcop.ignore_generated_code";
  String SETTINGS_FAIL_ON_ANALYSIS_ERROR = "fxcop.fail_on_analysis_error";

  String SETTINGS_DETECTION_MODE = "fxcop.detection_mode";
  String DETECTION_MODE_AUTO = "auto";
  String DETECTION_MODE_MANUAL = "manual";

  String SETTINGS_WHAT_TO_INSPECT = "fxcop.what";
  String WHAT_TO_INSPECT_FILES = "files";
  String WHAT_TO_INSPECT_PROJECT = "project";

  String RUNNER_DISPLAY_NAME = "XmlTest";
  String RUNNER_DESCRIPTION = "XmlTest static code analysis tool runner (.NET)";

  String REPORT_FILE = "fxcop-report.html";
  String OUTPUT_FILE = "fxcop-result.xml";
}
