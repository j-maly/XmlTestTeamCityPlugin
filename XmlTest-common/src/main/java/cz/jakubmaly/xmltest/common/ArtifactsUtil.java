package cz.jakubmaly.xmltest.common;

import java.lang.String;

/**
 * Created by IntelliJ IDEA.
 * User: Evgeniy.Koshkin
 * Date: 06.12.12
 * Time: 15:32
 */
public class ArtifactsUtil {
  public static String getInternalArtifactPath(final String relativePath){
    return String.format("%s/%s/%s", ".teamcity", XmlTestConstants.RUNNER_TYPE, relativePath);
  }
}
