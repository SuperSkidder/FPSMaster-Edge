package top.fpsmaster.ui.common.control;

import top.fpsmaster.ui.common.TextField;
import top.fpsmaster.ui.common.binding.Subscription;
import top.fpsmaster.ui.common.binding.ValueBinding;
import top.fpsmaster.utils.render.gui.ScaledGuiScreen;

import java.util.Objects;
import java.util.function.Consumer;

public final class BoundTextFieldControl implements UiControl {
    private final TextField textField;
    private final ValueBinding<String> binding;
    private final Subscription subscription;

    private Consumer<String> onValueChanged;
    private boolean internalSync;

    public BoundTextFieldControl(TextField textField, ValueBinding<String> binding) {
        this.textField = textField;
        this.binding = binding;
        this.subscription = binding.subscribe((oldValue, newValue) -> {
            if (internalSync) {
                return;
            }
            if (textField.isFocused()) {
                return;
            }
            if (!Objects.equals(textField.getText(), newValue)) {
                internalSync = true;
                try {
                    textField.setText(newValue);
                } finally {
                    internalSync = false;
                }
            }
        });
    }

    public void setOnValueChanged(Consumer<String> onValueChanged) {
        this.onValueChanged = onValueChanged;
    }

    public void dispose() {
        subscription.unsubscribe();
    }

    public TextField getTextField() {
        return textField;
    }

    @Override
    public void render(float x, float y, float width, float height, float mouseX, float mouseY) {
        textField.drawTextBox(x, y, width, height);
    }

    @Override
    public void renderInScreen(ScaledGuiScreen screen, float x, float y, float width, float height, float mouseX, float mouseY) {
        render(x, y, width, height, mouseX, mouseY);
        if (screen.hasClickEventThisFrame()) {
            int clickX = screen.peekPendingClickX();
            int clickY = screen.peekPendingClickY();
            boolean inside = clickX >= x && clickX <= x + width && clickY >= y && clickY <= y + height;
            if (!inside) {
                textField.setFocused(false);
            }
        }
        ScaledGuiScreen.ConsumedClick click = screen.consumeClickInBounds(x, y, width, height);
        if (click != null) {
            mouseClicked(click.x, click.y, click.button);
        }
    }

    @Override
    public void mouseClicked(float mouseX, float mouseY, int button) {
        textField.mouseClicked((int) mouseX, (int) mouseY, button);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        if (internalSync) {
            return;
        }

        boolean handled = textField.textboxKeyTyped(typedChar, keyCode);
        if (!handled) {
            return;
        }

        String newValue = textField.getText();
        String current = binding.get();
        if (!Objects.equals(current, newValue)) {
            binding.set(newValue);
            if (onValueChanged != null) {
                onValueChanged.accept(binding.get());
            }
        }
    }
}
