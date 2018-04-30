package view.Objects;

import javafx.scene.control.TextField;

public class NumberTextField extends TextField
{
    public String label;

    public NumberTextField(String value) {
        super(value);
    }

    @Override
    public void replaceText(int start, int end, String text)
    {
        if (validate(text))
        {
            super.replaceText(start, end, text);
        }
    }

    @Override
    public void replaceSelection(String text)
    {
        if (validate(text))
        {
            super.replaceSelection(text);
        }
    }

    private boolean validate(String text)
    {
        return (getText() + text).matches("^[0-9]+(\\.[0-9]*?)?$");
    }
}