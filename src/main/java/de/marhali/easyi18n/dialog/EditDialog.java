package de.marhali.easyi18n.dialog;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogBuilder;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTextField;
import de.marhali.easyi18n.InstanceManager;
import de.marhali.easyi18n.model.KeyedTranslation;
import de.marhali.easyi18n.model.Translation;
import de.marhali.easyi18n.model.TranslationDelete;
import de.marhali.easyi18n.dialog.descriptor.DeleteActionDescriptor;
import de.marhali.easyi18n.model.TranslationUpdate;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Edit translation dialog.
 * @author marhali
 */
public class EditDialog {

    private final Project project;
    private final KeyedTranslation origin;

    private JBTextField keyTextField;
    private Map<String, JBTextField> valueTextFields;

    public EditDialog(Project project, KeyedTranslation origin) {
        this.project = project;
        this.origin = origin;
    }

    public void showAndHandle() {
        int code = prepare().show();

        if(code == DialogWrapper.OK_EXIT_CODE) { // Edit
            InstanceManager.get(project).processUpdate(new TranslationUpdate(origin, getChanges()));
        } else if(code == DeleteActionDescriptor.EXIT_CODE) { // Delete
            InstanceManager.get(project).processUpdate(new TranslationDelete(origin));
        }
    }

    private KeyedTranslation getChanges() {
        Translation translation = new Translation();

        valueTextFields.forEach((k, v) -> {
            if(!v.getText().isEmpty()) {
                translation.put(k, v.getText());
            }
        });

        return new KeyedTranslation(keyTextField.getText(), translation);
    }

    private DialogBuilder prepare() {
        JPanel rootPanel = new JPanel();
        rootPanel.setLayout(new BoxLayout(rootPanel, BoxLayout.PAGE_AXIS));

        JPanel keyPanel = new JPanel(new GridLayout(0, 1, 2,2));
        JBLabel keyLabel = new JBLabel(ResourceBundle.getBundle("messages").getString("translation.key"));
        keyTextField = new JBTextField(this.origin.getKey());
        keyLabel.setLabelFor(keyTextField);
        keyPanel.add(keyLabel);
        keyPanel.add(keyTextField);
        keyPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        rootPanel.add(keyPanel);

        JPanel valuePanel = new JPanel(new GridLayout(0, 1, 2, 2));
        valueTextFields = new HashMap<>();

        for(String locale : InstanceManager.get(project).store().getData().getLocales()) {
            JBLabel localeLabel = new JBLabel(locale);
            JBTextField localeText = new JBTextField(this.origin.getTranslation().get(locale));
            localeLabel.setLabelFor(localeText);

            valuePanel.add(localeLabel);
            valuePanel.add(localeText);
            valueTextFields.put(locale, localeText);
        }

        JBScrollPane valuePane = new JBScrollPane(valuePanel);
        valuePane.setBorder(BorderFactory.createTitledBorder(new EtchedBorder(),
                ResourceBundle.getBundle("messages").getString("translation.locales")));
        rootPanel.add(valuePane);

        DialogBuilder builder = new DialogBuilder();
        builder.setTitle(ResourceBundle.getBundle("messages").getString("action.edit"));
        builder.removeAllActions();
        builder.addCancelAction();
        builder.addActionDescriptor(new DeleteActionDescriptor());
        builder.addOkAction();
        builder.setCenterPanel(rootPanel);

        return builder;
    }
}