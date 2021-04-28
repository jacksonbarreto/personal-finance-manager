package view;

import javafx.scene.control.TextField;

public class TextFieldUtilities {

    public static void addTextLimiter(final TextField tf, final int maxLength) {
        tf.textProperty().addListener((ov, oldValue, newValue) -> {
            if (tf.getText().length() > maxLength)
                tf.setText(tf.getText().substring(0, maxLength));
        });
    }
}
