package AniChart.Bit;

import AniChart.Colors;
import AniChart.Fonts;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public final class YAxisTextBit
{
    //========================================================================= VARIABLE
    private static final float PADDING_RIGHT = 20;

    private Rectangle2D _rect = new Rectangle(0, 0, 0, 0);
    private String _text;

    //========================================================================= FUNCTIONS
    public final void draw(Graphics2D g, float x, float y, boolean centerY)
    {
        if (_text == null)
            return;

        if (centerY)
            y += _rect.getCenterX();

        float rad = 90 * (float)Math.PI / 180;

        g.rotate(-rad, x, y);

        g.setColor(Colors.TEXT);
        g.setFont(Fonts.AXIS);
        g.drawString(_text, x, y - (float)_rect.getY());

        g.rotate(rad, x, y);
    }

    //========================================================================= PROPERTIES
    public final float width()
    {
        // use height since text is rotated
        if (_text != null)
            return (float)(_rect.getHeight() + PADDING_RIGHT);

        return 0;
    }
    public final float height()
    {
        if (_text != null)
            return (float)_rect.getWidth();

        return 0;
    }

    public final void setText(String text)
    {
        _text = text;

        // update bounding rectangle
        Graphics g = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB).getGraphics();

        FontMetrics metrics = g.getFontMetrics(Fonts.AXIS);
        g.setFont(Fonts.AXIS);
        _rect = metrics.getStringBounds(_text, g);

        g.dispose();
    }
}
