package de.max.modal;

import net.dv8tion.jda.api.interactions.components.LayoutComponent;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import net.dv8tion.jda.api.interactions.components.Label;

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
     * @param label Label shown above the field.
     * @param style SHORT or PARAGRAPH.
     * @param required If true, user must provide input.
     * @param min Minimum character length.
     * @param max Maximum character length.
     * @return Chained helper instance.
     */
    public ModalHelper addInput(String id, String label, TextInputStyle style, boolean required, int min, int max) {
        TextInput input = TextInput.create(id, label, style)
                .setRequired(required)
                .setMinLength(min)
                .setMaxLength(max)
                .build();
        
        /* JDA 6.3.0: Label.of automatically handles the ActionRow wrapping */
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

        return Modal.create(modalId, modalTitle)
                .addComponents(components)
                .build();
    }
}
