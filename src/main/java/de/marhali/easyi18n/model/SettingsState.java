package de.marhali.easyi18n.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents the persistent settings which can be configured.
 * @author marhali
 */
public class SettingsState {

    public static final String DEFAULT_PREVIEW_LOCALE = "en";
    public static final String DEFAULT_FILE_PATTERN = ".*";
    public static final String DEFAULT_PATH_PREFIX = "";
    public static final boolean DEFAULT_SORT_KEYS = true;
    public static final boolean DEFAULT_NESTED_KEYS = true;
    public static final boolean DEFAULT_CODE_ASSISTANCE = true;

    private String localesPath;
    private String filePattern;
    private String previewLocale;
    private String pathPrefix;
    private Boolean sortKeys;
    private Boolean nestedKeys;
    private Boolean codeAssistance;

    public SettingsState() {}

    public @Nullable String getLocalesPath() {
        return localesPath;
    }

    public void setLocalesPath(String localesPath) {
        this.localesPath = localesPath;
    }

    public @NotNull String getFilePattern() {
        return filePattern != null ? filePattern : DEFAULT_FILE_PATTERN;
    }

    public void setFilePattern(String filePattern) {
        this.filePattern = filePattern;
    }

    public @NotNull String getPreviewLocale() {
        return previewLocale != null ? previewLocale : DEFAULT_PREVIEW_LOCALE;
    }

    public void setPreviewLocale(String previewLocale) {
        this.previewLocale = previewLocale;
    }

    public @NotNull String getPathPrefix() {
        return pathPrefix != null ? pathPrefix : DEFAULT_PATH_PREFIX;
    }

    public void setPathPrefix(String pathPrefix) {
        this.pathPrefix = pathPrefix;
    }

    public boolean isSortKeys() {
        return sortKeys == null ? DEFAULT_SORT_KEYS : sortKeys;
    }

    public void setSortKeys(boolean sortKeys) {
        this.sortKeys = sortKeys;
    }

    public boolean isNestedKeys() {
        return nestedKeys == null ? DEFAULT_NESTED_KEYS : nestedKeys;
    }

    public void setNestedKeys(boolean nestedKeys) {
        this.nestedKeys = nestedKeys;
    }

    public boolean isCodeAssistance() {
        return codeAssistance == null ? DEFAULT_CODE_ASSISTANCE : codeAssistance;
    }

    public void setCodeAssistance(boolean codeAssistance) {
        this.codeAssistance = codeAssistance;
    }
}