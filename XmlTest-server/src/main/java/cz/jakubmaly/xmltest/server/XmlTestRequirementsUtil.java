package cz.jakubmaly.xmltest.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import cz.jakubmaly.xmltest.common.XmlTestVersion;
import jetbrains.buildServer.requirements.Requirement;
import jetbrains.buildServer.requirements.RequirementType;
import org.jetbrains.annotations.NotNull;

import static cz.jakubmaly.xmltest.common.XmlTestConstants.*;
import static cz.jakubmaly.xmltest.common.XmlTestVersion.not_specified;

public class XmlTestRequirementsUtil {
  @NotNull
  public static List<Requirement> getXmlTestRequirements(final Map<String, String> runParameters) {
    final List<Requirement> list = new ArrayList<Requirement>();
    final String detectionMode = runParameters.get(SETTINGS_DETECTION_MODE);
    if (detectionMode != null && detectionMode.equals(DETECTION_MODE_AUTO)) {
      list.add(new Requirement(XMLTEST_ROOT_PROPERTY, null, RequirementType.EXISTS));

      final String specifiedXmlTestVersion = runParameters.get(SETTINGS_XMLTEST_VERSION);
      if (specifiedXmlTestVersion == null) {
        list.add(not_specified.createRequirement());
      } else {
        for (XmlTestVersion version : XmlTestVersion.values()) {
          if (version.getTechnicalVersionPrefix().equals(specifiedXmlTestVersion)) {
            final Requirement requirement = version.createRequirement();
            if (requirement != null) list.add(requirement);
            break;
          }
        }
      }
    }
    return list;
  }
}
