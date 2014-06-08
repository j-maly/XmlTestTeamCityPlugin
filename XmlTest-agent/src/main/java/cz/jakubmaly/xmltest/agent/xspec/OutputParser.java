package cz.jakubmaly.xmltest.agent.xspec;

import jetbrains.buildServer.agent.BuildProgressLogger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Reads progress and results from calabash output.
 */
public class OutputParser {
    static Pattern testResultsPattern = Pattern.compile(".*passed:(\\d+) /pending:(\\d+) /failed:(\\d+) /total:(\\d+).*");
    static Pattern scenarioStartedPattern = Pattern.compile(".*Running scenario: (.*)");
    static Pattern scenarioDonePattern = Pattern.compile(".*Done scenario: (.*)");
    static Pattern cleanupPattern = Pattern.compile(".*INFO: file:///.*xspec-xslt.xpl:(\\d*):(\\d*):(.*)");

    public boolean isStartLine(String outputLine) {
        return isMatch(outputLine, scenarioStartedPattern);
    }

    public boolean isDoneLine(String outputLine) {
        return isMatch(outputLine, scenarioDonePattern);
    }

    public boolean isFailedLine(String outputLine) {
        return outputLine.contains("FAILED") || outputLine.contains("ERROR");
    }

    public boolean isResultsLine(String outputLine) {
        return isMatch(outputLine, testResultsPattern);
    }

    private static boolean isMatch(String line, Pattern pattern) {
        Matcher matcher = pattern.matcher(line);
        return matcher.matches();
    }

    private static String getFirstGroup(String line, Pattern pattern) {
        Matcher matcher = pattern.matcher(line);
        return matcher.matches() ? matcher.group(1) : "";
    }

    public String getCurrentScenario(String outputLine) {
        return getFirstGroup(outputLine, scenarioStartedPattern);
    }

    public String getDoneScenario(String outputLine) {
        return getFirstGroup(outputLine, scenarioDonePattern);
    }

    public ScenarioResultInfo writeResults(ScenarioResultInfo scenarioResultInfo, String outputLine) {
        Matcher matcher = testResultsPattern.matcher(outputLine);
        if (matcher.matches()) {
            scenarioResultInfo.setPassed(Integer.parseInt(matcher.group(1)));
            scenarioResultInfo.setPending(Integer.parseInt(matcher.group(2)));
            scenarioResultInfo.setFailed(Integer.parseInt(matcher.group(3)));
            scenarioResultInfo.setTotal(Integer.parseInt(matcher.group(4)));
            scenarioResultInfo.setScenarioCompleted(true);
        }
        return scenarioResultInfo;
    }

    public void printCleanedLine(String outputLine, BuildProgressLogger logger) {
        Matcher matcher = cleanupPattern.matcher(outputLine);
        if (matcher.matches()){
            if (matcher.group(3).length() > 0)
                logger.message(String.format("xspec-xslt.xpl:%s:%s:%s", matcher.group(1), matcher.group(2), matcher.group(3)));
        } else {
            logger.message(outputLine);
        }
    }
}
