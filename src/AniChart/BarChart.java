package AniChart;

import AniChart.Bit.GraphBit;
import AniChart.Bit.TitleBit;
import AniChart.Bit.XAxisTextBit;
import Utils.MathUtils;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public final class BarChart extends AnimatedPanel
{
    //========================================================================= VARIABLES
    private static final int PADDING = 20;

    private final TitleBit _title = new TitleBit();
    private final XAxisTextBit _xAxisText = new XAxisTextBit();
    private final GraphBit _graph = new GraphBit();

    private float[] _values;

    //========================================================================= FUNCTIONS
    public final void paintComponent(Graphics graphics)
    {
        super.paintComponent(graphics);

        Graphics2D g = (Graphics2D)graphics;

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        drawText(g);

        _graph.setBounds(PADDING, PADDING + _title.height(), getWidth() - PADDING, getHeight() - PADDING - _xAxisText.height());
        _graph.draw(g);

        drawBars(g);
    }

    private void drawText(Graphics2D g)
    {
        _title.draw(g, getWidth() / 2f, PADDING, true);
        _xAxisText.draw(g, getWidth() / 2f, getHeight() - PADDING - _xAxisText.height(), true);
    }

    private void drawBars(Graphics2D g)
    {
        if (_values == null || _values.length == 0)
            return;

        // TODO set separation to be half the bar width

        // calculate bar width
        float gridX = (float)_graph.gridBounds().getX();
        float width = getWidth() - PADDING * 2;
        float sep = width / 200;
        float bar = (width - sep * (_values.length + 1)) / _values.length;

        for (int i = 0; i < _values.length; i++)
        {
            Color col = Colors.get(2);
            g.setColor(new Color(col.getRed(), col.getGreen(), col.getBlue(), getAlpha(_values[i])));

            float x1 = gridX + sep / 2 + (bar + sep) * i;
            float y1 = _graph.getY(getAnimatedValue(i));
            float y2 = _graph.getY(0);

            g.fill(new Rectangle2D.Float(x1, y1 < y2 ? y1 : y2, bar, Math.abs(y2 - y1)));
        }
    }

    private float getAnimatedValue(int index)
    {
        return _values[index] * getAniProgress();
    }

    /**
     * Get alpha value for drawing bars.
     */
    private int getAlpha(float value)
    {
        int minAlpha = 155;
        double divisor = value >= 0 ? _graph.max() : _graph.min();
        int alpha = (int)((255 - minAlpha) * Math.abs(value / divisor) + minAlpha);

        return Math.min(Math.max(alpha, 0), 255);
    }

    //========================================================================= PROPERTIES
    public final void setTitle(String title)
    {
        _title.setText(title);
        repaint();
    }
    public final void setXAxisText(String text)
    {
        _xAxisText.setText(text);
        repaint();
    }

    public final void setValues(float[] values)
    {
        startAnimation();

        _values = values;

        // force zero to always be visible
        float min = Math.min(MathUtils.getMin(values), 0);
        float max = Math.max(MathUtils.getMax(values), 0);

        _graph.setMinMax(min, max);

        repaint();
    }

    //========================================================================= EVENTS
    protected final void onAniProgressChanged()
    {
        repaint();
    }
}