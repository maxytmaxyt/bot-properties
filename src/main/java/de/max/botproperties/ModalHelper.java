package de.max.modal;

import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import net.dv8tion.jda.api.interactions.components.Label;

/**
 * Utility for easy Modal creation in JDA 6.3.0.
 */
public class ModalHelper {

    /**
     * Quickly creates a TextInput.
     * @param id Unique ID for the input.
     * @param label The label shown above the input.
     * @param style Short or Paragraph.
     * @param required If the field must be filled.
     * @param min Minimum characters.
     * @param max Maximum characters.
     * @return A configured TextInput.
     */
    public TextInput createInput(String id, String label, TextInputStyle style, boolean required, int min, int max) {
        return TextInput.create(id, label, style)
                .setRequired(required)
                .setMinLength(min)
                .setMaxLength(max)
                .build();
    }

    /**
     * Creates a standard Paragraph input for longer texts.
     * @param id Unique ID.
     * @param label The label.
     * @return A pre-configured Paragraph input.
     */
    public TextInput createParagraphInput(String id, String label) {
        return createInput(id, label, TextInputStyle.PARAGRAPH, true, 1, 2000);
    }

    /**
     * Assembles a Modal using the JDA 6.3.0 Label layout.
     * @param modalId The unique ID of the modal (e.g. module:action:token).
     * @param title The title displayed at the top.
     * @param inputLabel The text describing the input field.
     * @param input The TextInput component.
     * @return The finished Modal.
     */
    public Modal buildSingleInputModal(String modalId, String title, String inputLabel, TextInput input) {
        return Modal.create(modalId, title)
                .addComponents(
                        Label.of(inputLabel, input)
                )
                .build();
    }
}
