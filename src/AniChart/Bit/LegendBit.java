package AniChart.Bit;

import AniChart.Colors;
import AniChart.Fonts;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public final class LegendBit
{
    //========================================================================= VARIABLE
    private static final float PADDING_LEFT = 20;
    private static final float WIDTH = 80;

    private static final float SQUARE_MARGIN = 5;
    private static final float SQUARE_SIZE = 20;

    private List<Rectangle2D> _rects = new ArrayList<>();

    private final List<String> _texts = new ArrayList<>();
    private final List<Color> _colors = new ArrayList<>();

    //========================================================================= FUNCTIONS
    public final void draw(Graphics2D g, float x, float y, boolean centerY)
    {
        x += PADDING_LEFT;

        if (centerY)
            y -= height() / 2;

        g.setFont(Fonts.TEXT);

        for (int i = 0; i < _texts.size(); i++)
        {
            float iy = y + (SQUARE_SIZE + SQUARE_MARGIN) * i;
            float textY = iy + SQUARE_SIZE / 2 - (float)_rects.get(i).getCenterY();

            // draw square
            Rectangle2D rect = new Rectangle2D.Float(x, iy, SQUARE_SIZE, SQUARE_SIZE);
            g.setColor(_colors.get(i));
            g.fill(rect);

            // draw text
            g.setColor(Colors.TEXT);
            g.drawString(_texts.get(i), x + SQUARE_SIZE + SQUARE_MARGIN, textY);
        }
    }

    public final void addText(String text, Color color)
    {
        _texts.add(text);
        _colors.add(color);

        // calculate text bounds
        Graphics g = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB).getGraphics();

        FontMetrics metrics = g.getFontMetrics(Fonts.TEXT);
        g.setFont(Fonts.TEXT);
        _rects.add(metrics.getStringBounds(text, g));

        g.dispose();
    }

    public final void clear()
    {
        _texts.clear();
        _colors.clear();
        _rects.clear();
    }

    //========================================================================= PROPERTIES
    public final float width()
    {
        return PADDING_LEFT + WIDTH;
    }

    public final float height()
    {
        return (_texts.size() - 1) * (SQUARE_SIZE + SQUARE_MARGIN) + SQUARE_SIZE;
    }
}
