package AniChart;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public final class PieChart extends AnimatedPanel
{
    //========================================================================= VARIABLES
    private static final float PADDING = 20;
    private static final float TITLE_MARGIN = 20;

    private Rectangle2D _rectTitle;

    private float[] _values;
    private float _total;

    private String _title;

    //========================================================================= INITIALIZE
    public PieChart()
    {
        updateDimensions();
    }

    //========================================================================= FUNCTIONS
    private void updateDimensions()
    {
        Graphics g = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB).getGraphics();

        if (_title != null && !_title.isEmpty())
        {
            FontMetrics metrics = g.getFontMetrics(Fonts.TITLE);
            g.setFont(Fonts.TITLE);
            _rectTitle = metrics.getStringBounds(_title, g);
        }
        else
            _rectTitle = new Rectangle(0, 0, 0, 0);
    }


    protected final void paintComponent(Graphics graphics)
    {
        super.paintComponent(graphics);

        Graphics2D g = (Graphics2D)graphics;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        drawTitle(g);
        drawPie(g);
    }

    private void drawTitle(Graphics2D g)
    {
        if (_title == null || _title.isEmpty())
            return;

        float x = (float)(getWidth() / 2f - _rectTitle.getCenterX());
        float y = (float)(PADDING + _rectTitle.getHeight());

        g.setFont(Fonts.TITLE);
        g.drawString(_title, x, y);
    }

    private void drawPie(Graphics2D g)
    {
        // no values to draw
        if (_values == null || _values.length == 0)
            return;

        float size = Math.min(contentWidth(), contentHeight());
        float x = (getWidth() - size) / 2f;
        float y = (getHeight() - size) / 2f;

        if (_title != null && !_title.isEmpty())
            y += (_rectTitle.getHeight() + TITLE_MARGIN) / 2f;

        float totalAngle = 360 * getAniProgress();
        float angle = 0;

        for (int i = 0; i < _values.length; i++)
        {
            float sweep = _values[i] / _total * totalAngle;
            drawWedge(g, x, y, size, angle, sweep, Colors.get(i));
            angle += sweep;
        }
    }

    private void drawWedge(Graphics2D g, float x, float y, float size, float angle, float sweep, Color color)
    {
        g.setColor(color);

        Arc2D.Double arc = new Arc2D.Double(x, y, size, size, angle, sweep, Arc2D.PIE);
        g.fill(arc);
    }

    //========================================================================= PROPERTIES
    private float contentWidth()
    {
        return getWidth() - PADDING * 2;
    }
    private float contentHeight()
    {
        float height = getHeight() - PADDING * 2;

        if (_title != null && !_title.isEmpty())
            height -= (_rectTitle.getHeight() + TITLE_MARGIN);

        return height;
    }

    public final void setValues(float[] values)
    {
        startAnimation();

        _values = values;
        _total = 0;

        if (_values == null)
            return;

        for (float value : _values)
            _total += value;

        repaint();
    }

    public final void setTitle(String title)
    {
        _title = title;

        updateDimensions();
        repaint();
    }

    //========================================================================= EVENTS
	protected final void onAniProgressChanged()
    {
        repaint();
    }
}
