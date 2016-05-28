package AniChart;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public final class BarChart extends AnimatedPanel
{
    private static final int PADDING = 25;
    private static final int PADDING_BOTTOM = 90;

    private static final float AXIS_WIDTH = 1.5f;

    private static final Font FONT_FORECAST = new Font("Century Gothic", Font.PLAIN, 14);

    private static final Color COL_AXIS = new Color(160, 160, 160);

    private double[] values;

    private double min = Float.MAX_VALUE;
    private double max = Float.MIN_VALUE;

    //========================================================================= FUNCTIONS
    public final void paintComponent(Graphics graphics)
    {
        super.paintComponent(graphics);

        Graphics2D g = (Graphics2D)graphics;

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        drawEmptyText(g);
        drawAxis(g);

        drawBars(g);
    }

    private void drawEmptyText(Graphics2D g)
    {
        if (values != null && values.length > 0)
            return;

        String text = "no data";

        FontMetrics metrics = g.getFontMetrics(FONT_FORECAST);
        Rectangle2D rect = metrics.getStringBounds(text, g);

        // draw text in middle of chart when no data loaded
        g.setFont(FONT_FORECAST);
        g.setColor(COL_AXIS);
        g.drawString(text, getWidth() / 2f - (float)rect.getCenterX(), getHeight() / 2f - (float)rect.getCenterY());
    }

    private void drawAxis(Graphics2D g)
    {
        g.setColor(COL_AXIS);
        g.setStroke(new BasicStroke(AXIS_WIDTH));

        // constrain axis to chart bounds
        int y = (int)getY(0);
        y = Math.min(Math.max(y, PADDING), getHeight() - PADDING_BOTTOM);

        // set axis to bottom when no forecasts
        if (values == null || values.length == 0)
            y = getHeight() - PADDING_BOTTOM;

        g.drawLine(PADDING, y, getWidth() - PADDING, y); // horizontal axis
    }

    private void drawBars(Graphics2D g)
    {
        if (values == null || values.length == 0)
            return;

        // calculate bar width
        float width = getWidth() - PADDING * 2;
        float sep = width / 200;
        float bar = (width - sep * (values.length + 1)) / values.length;

        for (int i = 0; i < values.length; i++)
        {
            Color col = Colors.get(2);
            g.setColor(new Color(col.getRed(), col.getGreen(), col.getBlue(), getAlpha(values[i])));

            int x1 = (int)(PADDING + sep + i * (bar + sep));
            int y1 = (int)getY(getAnimatedValue(i));
            int y2 = (int)getY(0);

            g.fillRect(x1, y1 < y2 ? y1 : y2, (int)bar, Math.abs(y2 - y1));
        }
    }

    /**
     * Get y-coord of value based on min/max values.
     */
    private double getY(double value)
    {
        double top = PADDING;
        double bottom = getHeight() - PADDING_BOTTOM;

        if (min < 0 && max > 0) // both positive and negative values, draw axis in the middle
            return bottom - (bottom - top) * (value - min) / (max - min);
        else if (min >= 0) // all positive
            return bottom - (bottom - top) * value / max;
        else if (max <= 0) // all negative
            return bottom - (bottom - top) * (value - min) / -min;

        return 0;
    }

    private double getAnimatedValue(int index)
    {
        return values[index] * getAniProgress();
    }

    /**
     * Get alpha value for drawing bars.
     */
    private int getAlpha(double value)
    {
        int minAlpha = 155;
        double divisor = value >= 0 ? max : min;
        int alpha = (int)((255 - minAlpha) * Math.abs(value / divisor) + minAlpha);

        return Math.min(Math.max(alpha, 0), 255);
    }

    //========================================================================= PROPERTIES
    public final void setValues(double[] values)
    {
        startAnimation();

        this.values = values;

        min = Float.MAX_VALUE;
        max = Float.MIN_VALUE;

        // calculate new min
        for (double value : values)
            if (value < min)
                min = value;

        // calculate new max
        for (double value : values)
            if (value > max)
                max = value;

        repaint();
    }

    //========================================================================= EVENTS
    protected final void onAniProgressChanged()
    {
        repaint();
    }
}