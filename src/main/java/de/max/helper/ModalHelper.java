package de.max.helper;

import net.dv8tion.jda.api.components.LayoutComponent;
import net.dv8tion.jda.api.modals.Modal;
import net.dv8tion.jda.api.components.label.Label;
import net.dv8tion.jda.api.components.textinput.TextInput;
import net.dv8tion.jda.api.components.textinput.TextInputStyle;
import java.util.ArrayList;
import java.util.List;

/**
 * Advanced Multi-Input Modal Builder for JDA 6.3.0.
 */
public class ModalHelper {

    private final String modalId;
    private final String modalTitle;
    private final List<LayoutComponent> components = new ArrayList<>();

    private ModalHelper(String modalId, String modalTitle) {
        this.modalId = modalId;
        this.modalTitle = modalTitle;
    }

    public static ModalHelper create(String id, String title) {
        return new ModalHelper(id, title);
    }

    /**
     * Adds a customizable text input field.
     */
    public ModalHelper addInput(String id, String label, TextInputStyle style, boolean required, int min, int max) {
        /* * FIX: TextInput.create(id, style) instead of (id, label, style)
         * We then set the label via .setLabel(label)
         */
        TextInput input = TextInput.create(id, style)
                .setLabel(label)
                .setRequired(required)
                .setMinLength(min)
                .setMaxLength(max)
                .build();
        
        this.components.add(Label.of(label, input));
        return this;
    }

    public ModalHelper addParagraph(String id, String label) {
        return addInput(id, label, TextInputStyle.PARAGRAPH, true, 1, 2000);
    }

    public ModalHelper addShortInput(String id, String label) {
        return addInput(id, label, TextInputStyle.SHORT, true, 1, 100);
    }

    public Modal build() {
        if (components.isEmpty()) {
            throw new IllegalStateException("A modal must have at least one input field!");
        }

        return Modal.create(modalId, modalTitle)
                .addComponents(components)
                .build();
    }
}
