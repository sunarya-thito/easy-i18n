package de.marhali.easyi18n.io.properties;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;

import de.marhali.easyi18n.io.IOStrategy;
import de.marhali.easyi18n.model.SettingsState;
import de.marhali.easyi18n.model.TranslationData;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.function.Consumer;

/**
 * Strategy for simple 'properties' locale files. Each locale has its own file.
 * For example localesPath/en.properties, localesPath/de.properties.
 * @author marhali
 */
public class PropertiesIOStrategy implements IOStrategy {

    private static final String FILE_EXTENSION = "properties";

    @Override
    public boolean canUse(@NotNull Project project, @NotNull String localesPath, @NotNull SettingsState state) {
        VirtualFile directory = LocalFileSystem.getInstance().findFileByIoFile(new File(localesPath));

        if(directory == null || directory.getChildren() == null) {
            return false;
        }

        for(VirtualFile children : directory.getChildren()) {
            if(!children.isDirectory() && isFileRelevant(state, children)) {
                if(children.getExtension().equalsIgnoreCase(FILE_EXTENSION)) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void read(@NotNull Project project, @NotNull String localesPath,
                     @NotNull SettingsState state, @NotNull Consumer<@Nullable TranslationData> result) {
        ApplicationManager.getApplication().saveAll(); // Save opened files (required if new locales were added)

        ApplicationManager.getApplication().runReadAction(() -> {
            VirtualFile directory = LocalFileSystem.getInstance().findFileByIoFile(new File(localesPath));

            if(directory == null || directory.getChildren() == null) {
                throw new IllegalArgumentException("Specified folder is invalid (" + localesPath + ")");
            }

            TranslationData data = new TranslationData(state.isSortKeys(), state.isNestedKeys());

            try {
                for(VirtualFile file : directory.getChildren()) {
                    if(file.isDirectory() || !isFileRelevant(state, file)) {
                        continue;
                    }

                    String locale = file.getNameWithoutExtension();
                    data.addLocale(locale);

                    SortableProperties properties = new SortableProperties(state.isSortKeys());
                    properties.load(new InputStreamReader(file.getInputStream(), file.getCharset()));
                    PropertiesMapper.read(locale, properties, data);
                }

                result.accept(data);

            } catch(IOException e) {
                e.printStackTrace();
                result.accept(null);
            }
        });
    }

    @Override
    public void write(@NotNull Project project, @NotNull String localesPath,
                      @NotNull SettingsState state, @NotNull TranslationData data, @NotNull Consumer<Boolean> result) {
        ApplicationManager.getApplication().runWriteAction(() -> {
            try {
                for(String locale : data.getLocales()) {
                    SortableProperties properties = new SortableProperties(state.isSortKeys());
                    PropertiesMapper.write(locale, properties, data);

                    File file = new File(localesPath + "/" + locale + "." + FILE_EXTENSION);
                    boolean exists = file.createNewFile();

                    VirtualFile vf = exists
                            ? LocalFileSystem.getInstance().refreshAndFindFileByIoFile(file)
                            : LocalFileSystem.getInstance().findFileByIoFile(file);

                    StringWriter writer = new StringWriter();
                    properties.store(writer, null);

                    vf.setBinaryContent(writer.toString().getBytes(vf.getCharset()));
                }

                result.accept(true);

            } catch(IOException e) {
                e.printStackTrace();
                result.accept(false);
            }
        });
    }
}
