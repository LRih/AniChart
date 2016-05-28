package AniChart.Bit;

import AniChart.Colors;
import AniChart.Fonts;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public final class LegendBit
{
    //========================================================================= VARIABLE
    private static final float PADDING_LEFT = 20;

    private static final float WIDTH = 40;
    private static final float SQUARE_SIZE = 30;

    private final List<String> _texts = new ArrayList<>();
    private final List<Color> _colors = new ArrayList<>();

    //========================================================================= FUNCTIONS
    public final void draw(Graphics2D g, float x, float y)
    {
        g.setColor(Colors.TEXT);
        g.setFont(Fonts.TEXT);

        for (int i = 0; i < _texts.size(); i++)
        {
            // TODO draw square
            // TODO measure text
            g.drawString(_texts.get(i), x, y + i * 30);
        }
    }

    //========================================================================= FUNCTIONS
    public final void addText(String text, Color color)
    {
        _texts.add(text);
        _colors.add(color);
    }

    public final void clear()
    {
        _texts.clear();
        _colors.clear();
    }

    //========================================================================= PROPERTIES
    public final float width()
    {
        return PADDING_LEFT + WIDTH;
    }
}
