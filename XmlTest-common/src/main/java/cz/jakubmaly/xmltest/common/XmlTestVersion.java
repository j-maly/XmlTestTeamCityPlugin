package cz.jakubmaly.xmltest.common;

import jetbrains.buildServer.requirements.Requirement;
import jetbrains.buildServer.requirements.RequirementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by IntelliJ IDEA.
 * User: Evgeniy.Koshkin
 * Date: 25.10.11
 * Time: 13:28
 */
public enum XmlTestVersion {

  not_specified("not_specified", "Any Detected") {
    @Override
    public Requirement createRequirement() {
      return new Requirement(XmlTestConstants.XMLTESTCMD_FILE_VERSION_PROPERTY, null, RequirementType.EXISTS);
    }
  },
  v1_35("1.35", "1.35"),
  v1_36("9.0", "1.36 (9.0)"),
  v10_0("10.0", "10.0");

  private final String myTechnicalVersionPrefix;
  private final String myDisplayName;

  XmlTestVersion(final String technicalVersionPrefix, final String displayName) {
    myTechnicalVersionPrefix = technicalVersionPrefix;
    myDisplayName = displayName;
  }

  @NotNull
  public String getTechnicalVersionPrefix() {
    return myTechnicalVersionPrefix;
  }

  @NotNull
  public String getDisplayName() {
    return myDisplayName;
  }

  @Nullable
  public Requirement createRequirement(){
    return new Requirement(XmlTestConstants.XMLTESTCMD_FILE_VERSION_PROPERTY, getTechnicalVersionPrefix(), RequirementType.STARTS_WITH);
  }
}
