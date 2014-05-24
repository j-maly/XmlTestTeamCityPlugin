package cz.jakubmaly.xmltest.server;

import jetbrains.buildServer.requirements.Requirement;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class XmlTestRequirementsUtil {
    @NotNull
    public static List<Requirement> getXmlTestRequirements(final Map<String, String> runParameters) {
        final List<Requirement> list = new ArrayList<Requirement>();
        return list;
    }
}
