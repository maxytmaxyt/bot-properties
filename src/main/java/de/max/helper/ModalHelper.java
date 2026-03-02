package de.max.helper;

import net.dv8tion.jda.api.components.label.Label;
import net.dv8tion.jda.api.components.textinput.TextInput;
import net.dv8tion.jda.api.components.textinput.TextInputStyle;
import net.dv8tion.jda.api.components.LayoutComponent;
import net.dv8tion.jda.api.modals.Modal;
import java.util.ArrayList;
import java.util.List;

/**
 * Advanced Multi-Input Modal Builder for JDA 6.3.0.
 * Adjusted to match the structure of ChatFilterListener.
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
     * Adds a customizable text input field using Label.of.
     */
    public ModalHelper addInput(String id, String label, TextInputStyle style, boolean required, int min, int max) {
        /*
         * Following your reference: TextInput.create(id, style) without the label.
         * The label is handled by the Label.of wrapper below.
         */
        TextInput input = TextInput.create(id, style)
                .setRequired(required)
                .setMinLength(min)
                .setMaxLength(max)
                .build();
        
        // Wrapping the input in Label.of as seen in ChatFilterListener
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
