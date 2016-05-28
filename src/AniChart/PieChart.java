package AniChart;

import AniChart.Bit.LegendBit;
import AniChart.Bit.TitleBit;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.util.ArrayList;
import java.util.List;

public final class PieChart extends AnimatedPanel
{
    //========================================================================= VARIABLES
    private static final float PADDING = 20;

    private final TitleBit _title = new TitleBit();
    private final LegendBit _legend = new LegendBit();

    private List<Float> _values = new ArrayList<>();
    private float _total = 0;

    //========================================================================= FUNCTIONS
    protected final void paintComponent(Graphics graphics)
    {
        super.paintComponent(graphics);

        Graphics2D g = (Graphics2D)graphics;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        drawTitle(g);
        drawLegend(g);

        drawPie(g);
    }

    private void drawTitle(Graphics2D g)
    {
        _title.draw(g, getWidth() / 2f, PADDING, true);
    }
    private void drawLegend(Graphics2D g)
    {
        // TODO draw in correct position
        _legend.draw(g, PADDING, PADDING);
    }

    private void drawPie(Graphics2D g)
    {
        // no values to draw
        if (_values.isEmpty())
            return;

        float size = Math.min(contentWidth(), contentHeight());
        float x = (getWidth() - size) / 2f;
        float y = (getHeight() - size + _title.height()) / 2f;

        float totalAngle = 360 * getAniProgress();
        float angle = 0;

        for (int i = 0; i < _values.size(); i++)
        {
            float sweep = _values.get(i) / _total * totalAngle;
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


    public final void addValue(String name, float value)
    {
        startAnimation();

        _legend.addText(name, Colors.get(_values.size()));

        _values.add(value);
        _total += value;

        repaint();
    }

    public final void clear()
    {
        removeAllAnimations();

        _legend.clear();

        _values.clear();
        _total = 0;

        repaint();
    }

    //========================================================================= PROPERTIES
    private float contentWidth()
    {
        return getWidth() - PADDING * 2;
    }
    private float contentHeight()
    {
        return getHeight() - PADDING * 2 - _title.height();
    }

    public final void setTitle(String title)
    {
        _title.setText(title);
        repaint();
    }

    //========================================================================= EVENTS
	protected final void onAniProgressChanged()
    {
        repaint();
    }
}
