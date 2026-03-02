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

    public ModalHelper addInput(String id, String label, TextInputStyle style, boolean required, int min, int max) {
        TextInput input = TextInput.create(id, label, style)
                .setRequired(required)
                .setMinLength(min)
                .setMaxLength(max)
                .build();

        components.add(Label.of(label, input));
        return this;
    }

    public ModalHelper addParagraph(String id, String label) {
        return addInput(id, label, TextInputStyle.PARAGRAPH, true, 1, 2000);
    }

    public ModalHelper addShortInput(String id, String label) {
        return addInput(id, label, TextInputStyle.SHORT, true, 1, 100);
    }

    public ModalHelper addComponent(String label, LabelChildComponent component) {
        components.add(Label.of(label, component));
        return this;
    }

    public Modal build() {
        if (components.isEmpty()) {
            throw new IllegalStateException("A modal must have at least one component");
        }

        return Modal.create(modalId, modalTitle)
                .addComponents(components)
                .build();
    }
}
