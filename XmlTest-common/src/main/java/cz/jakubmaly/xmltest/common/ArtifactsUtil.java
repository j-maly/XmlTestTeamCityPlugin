package cz.jakubmaly.xmltest.common;

public class ArtifactsUtil {
    public static String getInternalArtifactPath(final String relativePath) {
        return String.format("%s/%s/%s", ".teamcity", XmlTestConstants.RUNNER_TYPE, relativePath);
    }
}
