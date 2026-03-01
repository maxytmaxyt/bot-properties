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

    /**
     * Initialize the modal builder.
     * @param id Unique modal identifier.
     * @param title Title shown on the modal UI.
     * @return New helper instance.
     */
    public static ModalHelper create(String id, String title) {
        return new ModalHelper(id, title);
    }

    /**
     * Adds a highly customizable text input field.
     * @param id Field identifier.
     * @param label Text shown above the field.
     * @param style SHORT or PARAGRAPH.
     * @param required If true, user must provide input.
     * @param min Minimum character length.
     * @param max Maximum character length.
     * @return Chained helper instance.
     */
    public ModalHelper addInput(String id, String label, TextInputStyle style, boolean required, int min, int max) {
        /* JDA 6.x change: create(id, style) then setLabel(label) */
        TextInput input = TextInput.create(id, style)
                .setLabel(label)
                .setRequired(required)
                .setMinLength(min)
                .setMaxLength(max)
                .build();
        
        /* Using the new Label.of for automatic ActionRow wrapping */
        this.components.add(Label.of(label, input));
        return this;
    }

    /**
     * Shortcut to add a mandatory paragraph (large) input.
     * @param id Field identifier.
     * @param label Label text.
     * @return Chained helper instance.
     */
    public ModalHelper addParagraph(String id, String label) {
        return addInput(id, label, TextInputStyle.PARAGRAPH, true, 1, 2000);
    }

    /**
     * Shortcut to add a mandatory short (single-line) input.
     * @param id Field identifier.
     * @param label Label text.
     * @return Chained helper instance.
     */
    public ModalHelper addShortInput(String id, String label) {
        return addInput(id, label, TextInputStyle.SHORT, true, 1, 100);
    }

    /**
     * Finalizes the modal construction.
     * @return The completed JDA Modal.
     * @throws IllegalStateException if no components were added.
     */
    public Modal build() {
        if (components.isEmpty()) {
            throw new IllegalStateException("A modal must have at least one input field!");
        }

        /* JDA 6.x Modal.create(id, title) */
        return Modal.create(modalId, modalTitle)
                .addComponents(components)
                .build();
    }
}
