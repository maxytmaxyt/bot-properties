package de.max.helper;

import net.dv8tion.jda.api.components.label.Label;
import net.dv8tion.jda.api.components.label.LabelChildComponent;
import net.dv8tion.jda.api.components.textinput.TextInput;
import net.dv8tion.jda.api.components.textinput.TextInputStyle;
import net.dv8tion.jda.api.modals.Modal;

import java.util.ArrayList;
import java.util.List;

public class ModalHelper {

    private final String modalId;
    private final String modalTitle;
    private final List<Label> components = new ArrayList<>();

    private ModalHelper(String modalId, String modalTitle) {
        this.modalId = modalId;
        this.modalTitle = modalTitle;
    }

    public static ModalHelper create(String id, String title) {
        return new ModalHelper(id, title);
    }

    /**
     * Fügt ein TextInput-Feld mit optionalen Parametern hinzu
     */
    public ModalHelper addInput(String id,
                                String labelText,
                                TextInputStyle style,
                                boolean required,
                                int minLength,
                                int maxLength,
                                String placeholder,
                                String defaultValue,
                                String description) {

        TextInput input = TextInput.create(id, style)
                .setRequired(required)
                .setMinLength(minLength)
                .setMaxLength(maxLength);

        if (placeholder != null && !placeholder.isEmpty()) {
            input.setPlaceholder(placeholder);
        }

        if (defaultValue != null && !defaultValue.isEmpty()) {
            input.setValue(defaultValue);
        }

        input = input.build();

        if (description != null && !description.isEmpty()) {
            components.add(Label.of(labelText, description, input));
        } else {
            components.add(Label.of(labelText, input));
        }

        return this;
    }

    public ModalHelper addParagraph(String id, String labelText) {
        return addInput(id, labelText, TextInputStyle.PARAGRAPH, true, 1, 2000, null, null, null);
    }

    public ModalHelper addShortInput(String id, String labelText) {
        return addInput(id, labelText, TextInputStyle.SHORT, true, 1, 100, null, null, null);
    }

    /**
     * Fügt beliebige LabelChildComponent hinzu (z. B. Dropdowns)
     */
    public ModalHelper addComponent(String labelText, LabelChildComponent component) {
        components.add(Label.of(labelText, component));
        return this;
    }

    public Modal build() {
        if (components.isEmpty()) {
            throw new IllegalStateException("Modal must contain at least one component");
        }

        return Modal.create(modalId, modalTitle)
                .addComponents(components)
                .build();
    }
}
