package cz.jakubmaly.xmltest.agent.xspec;

import com.intellij.openapi.util.SystemInfo;
import cz.jakubmaly.xmltest.common.XmlTestConstants;
import jetbrains.buildServer.RunBuildException;
import jetbrains.buildServer.agent.BuildProgressLogger;
import jetbrains.buildServer.messages.DefaultMessagesInfo;
import jetbrains.buildServer.util.AntPatternFileFinder;
import jetbrains.buildServer.util.FileUtil;
import jetbrains.buildServer.util.StringUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Searches for xspec files based on user's setting in the build step.
 */
public class ScenariosLookup {
    private BuildProgressLogger logger;

    public ScenariosLookup(BuildProgressLogger logger) {
        this.logger = logger;
    }

    public List<String> detectFiles(final Map<String, String> runParameters, File checkoutDirectory, File workingDirectory) throws RunBuildException {
        getLogger().progressStarted("Detecting files");
        final String what = runParameters.get(XmlTestConstants.SETTINGS_WHAT_TO_INSPECT);
        final List<String> files;
        if (XmlTestConstants.WHAT_TO_INSPECT_FILES.equals(what) && StringUtils.isNotEmpty(what)) {
            try {
                files = matchFiles(runParameters, checkoutDirectory, workingDirectory);
            } catch (IOException e) {
                throw new RunBuildException("I/O error while collecting files", e);
            }
            if (files.size() == 0) {
                throw new RunBuildException("No files matched the pattern");
            }
        } else {
            throw new RunBuildException("No file pattern specified");
        }
        getLogger().message(String.format("Found %s files.", files.size()));
        getLogger().progressFinished();
        return files;
    }

    List<String> matchFiles(Map<String, String> runParameters, File checkoutDirectory, File workingDirectory) throws IOException {
        final AntPatternFileFinder finder = new AntPatternFileFinder(
                splitFileWildcards(runParameters.get(XmlTestConstants.SETTINGS_FILES)),
                splitFileWildcards(runParameters.get(XmlTestConstants.SETTINGS_FILES_EXCLUDE)),
                SystemInfo.isFileSystemCaseSensitive);
        final File[] files = finder.findFiles(checkoutDirectory);

        getLogger().logMessage(DefaultMessagesInfo.createTextMessage("Matched xspec files:"));

        final List<String> result = new ArrayList<String>(files.length);
        for (File file : files) {
            final String relativeName = FileUtil.getRelativePath(workingDirectory, file);
            result.add(relativeName);
            getLogger().logMessage(DefaultMessagesInfo.createTextMessage("  " + relativeName));
        }

        if (files.length == 0) {
            getLogger().logMessage(DefaultMessagesInfo.createTextMessage("  none"));
        }

        return result;
    }

    static String[] splitFileWildcards(final String string) {
        if (string != null) {
            final String filesStringWithSpaces = string.replace('\n', ' ').replace('\r', ' ').replace('\\', '/');
            final List<String> split = StringUtil.splitCommandArgumentsAndUnquote(filesStringWithSpaces);
            return split.toArray(new String[split.size()]);
        }
        return new String[0];
    }

    public BuildProgressLogger getLogger() {
        return logger;
    }
}