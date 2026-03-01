package de.max.modal;

import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import net.dv8tion.jda.api.interactions.components.Label;

/**
 * Ultimate Fluent-Builder for JDA 6.3.0 Modals.
 */
public class ModalHelper {

    private final String modalId;
    private final String modalTitle;
    private TextInput builtInput;
    private String labelText;

    private ModalHelper(String modalId, String modalTitle) {
        this.modalId = modalId;
        this.modalTitle = modalTitle;
    }

    /**
     * Starts the modal building process.
     * @param id The custom ID of the modal.
     * @param title The title displayed on top.
     * @return A new Helper instance.
     */
    public static ModalHelper create(String id, String title) {
        return new ModalHelper(id, title);
    }

    /**
     * Configures the text input field with full flexibility.
     * @param id The input ID.
     * @param label The label text.
     * @param style The style (SHORT/PARAGRAPH).
     * @param required Whether it must be filled.
     * @param min Minimum characters.
     * @param max Maximum characters.
     * @return The current helper instance for chaining.
     */
    public ModalHelper withInput(String id, String label, TextInputStyle style, boolean required, int min, int max) {
        this.labelText = label;
        this.builtInput = TextInput.create(id, label, style)
                .setRequired(required)
                .setMinLength(min)
                .setMaxLength(max)
                .build();
        return this;
    }

    /**
     * Shortcut for a simple required paragraph input.
     * @param id Input ID.
     * @param label Label text.
     * @return The current helper instance.
     */
    public ModalHelper quickParagraph(String id, String label) {
        return withInput(id, label, TextInputStyle.PARAGRAPH, true, 1, 2000);
    }

    /**
     * Finalizes and builds the JDA Modal using the 6.3.0 Label layout.
     * @return The finished JDA Modal.
     * @throws IllegalStateException if no input was configured.
     */
    public Modal build() {
        if (builtInput == null) {
            throw new IllegalStateException("You must configure an input using withInput() before building!");
        }

        /* JDA 6.3.0 style using Label.of instead of manual ActionRows */
        return Modal.create(modalId, modalTitle)
                .addComponents(Label.of(labelText, builtInput))
                .build();
    }
}
