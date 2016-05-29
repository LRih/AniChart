package AniChart.Bit;

import AniChart.Colors;
import AniChart.Fonts;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public final class XAxisTextBit
{
    //========================================================================= VARIABLE
    private static final float PADDING_TOP = 20;

    private Rectangle2D _rect = new Rectangle(0, 0, 0, 0);
    private String _text;

    //========================================================================= FUNCTIONS
    public final void draw(Graphics2D g, float x, float y, boolean centerX)
    {
        if (_text == null)
            return;

        if (centerX)
            x -= _rect.getCenterX();

        g.setColor(Colors.TEXT);
        g.setFont(Fonts.AXIS);
        g.drawString(_text, x, y + PADDING_TOP - (float)_rect.getY());
    }

    //========================================================================= PROPERTIES
    public final float width()
    {
        if (_text != null)
            return (float)_rect.getWidth();

        return 0;
    }
    public final float height()
    {
        if (_text != null)
            return (float)(PADDING_TOP + _rect.getHeight());

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
