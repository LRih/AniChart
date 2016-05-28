package AniChart;

import AniChart.Bit.LegendBit;
import AniChart.Bit.PieBit;
import AniChart.Bit.TitleBit;

import java.awt.*;

public final class PieChart extends AnimatedPanel
{
    //========================================================================= VARIABLES
    private static final float PADDING = 20;

    private final TitleBit _title = new TitleBit();
    private final LegendBit _legend = new LegendBit();
    private final PieBit _pie = new PieBit();

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
        float x = getWidth() - PADDING - _legend.width();
        float cy = PADDING + _title.height() + contentHeight() / 2;
        _legend.draw(g, x, cy, true);
    }

    private void drawPie(Graphics2D g)
    {
        _pie.draw(g, PADDING, PADDING + _title.height(), contentWidth(), contentHeight(), getAniProgress());
    }


    public final void addValue(String name, float value)
    {
        startAnimation();

        Color col = Colors.get(_pie.size());
        _legend.addText(name, col);
        _pie.addValue(value, col);

        repaint();
    }

    public final void clear()
    {
        removeAllAnimations();

        _legend.clear();
        _pie.clear();

        repaint();
    }

    //========================================================================= PROPERTIES
    private float contentWidth()
    {
        return getWidth() - PADDING * 2 - _legend.width();
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
