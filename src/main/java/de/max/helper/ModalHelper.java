package de.max.helper;

import net.dv8tion.jda.api.components.label.Label;
import net.dv8tion.jda.api.components.textinput.TextInput;
import net.dv8tion.jda.api.components.textinput.TextInputStyle;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.modals.Modal;
import java.util.ArrayList;
import java.util.List;

/**
 * Advanced Multi-Input Modal Builder for JDA 6.3.0.
 * Adjusted based on ChatFilterListener structure.
 */
public class ModalHelper {

    private final String modalId;
    private final String modalTitle;
    /*
     * Using ActionRow instead of LayoutComponent to resolve compilation errors,
     * as Label.of typically returns an ActionRow.
     */
    private final List<ActionRow> components = new ArrayList<>();

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
        /*
         * Correct TextInput creation for JDA 6.3.0 as seen in ChatFilterListener.
         * The label text is passed to Label.of, not the create method.
         */
        TextInput input = TextInput.create(id, style)
                .setRequired(required)
                .setMinLength(min)
                .setMaxLength(max)
                .build();
        
        // Retaining Label.of as requested; it wraps the input into an ActionRow
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

        // Modal creation using the confirmed net.dv8tion.jda.api.modals.Modal package
        return Modal.create(modalId, modalTitle)
                .addComponents(components)
                .build();
    }
}
