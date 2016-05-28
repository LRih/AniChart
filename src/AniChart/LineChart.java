package AniChart;

import AniChart.Bit.TitleBit;

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.LinkedHashMap;

public final class LineChart extends AnimatedPanel
{
    //========================================================================= VARIABLES
    private static final int PADDING = 40; // TODO reduce later after factoring axis size

    private static final int MINOR_AXIS_EXTRA = 6;

    private static final float AXIS_WIDTH = 1.5f;
    private static final float MINOR_AXIS_WIDTH = 1f;
    private static final float LINE_WIDTH = 2f;

    private final TitleBit _title = new TitleBit();
    private String _xAxisText = "";
    private String _yAxisText = "";

    private String[] _xValues = new String[] { };

    // linked hash map to preserve insertion order
    private final LinkedHashMap<String, double[]> _valuesList = new LinkedHashMap<>();

    private double min = Float.MAX_VALUE;
    private double max = Float.MIN_VALUE;

    //========================================================================= FUNCTIONS
    public final void paintComponent(Graphics graphics)
    {
        super.paintComponent(graphics);

        Graphics2D g = (Graphics2D)graphics;

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        drawText(g);

        drawMinorAxis(g);
        drawAxis(g);

        drawLines(g);
    }

    private void drawText(Graphics2D g)
    {
        g.setColor(Colors.TEXT);

        drawTitle(g);
        drawAxisText(g);
        drawEmptyText(g);
    }

    private void drawTitle(Graphics2D g)
    {
        _title.draw(g, getWidth() / 2f, PADDING, true);
    }

    private void drawAxisText(Graphics2D g)
    {
        FontMetrics metrics = g.getFontMetrics(Fonts.AXIS);
        g.setFont(Fonts.AXIS);

        // x-axis
        Rectangle2D rect = metrics.getStringBounds(_xAxisText, g);

        float x = getWidth() / 2f - (float)rect.getCenterX();
        float y = getHeight() - PADDING / 3f - (float)rect.getCenterY();

        g.drawString(_xAxisText, x, y);

        // y-axis
        rect = metrics.getStringBounds(_yAxisText, g);

        float rad = 90 * (float)Math.PI / 180;

        x = PADDING / 5f;
        y = getHeight() / 2f;

        g.rotate(-rad, x, y);
        g.drawString(_yAxisText, PADDING / 3f - (float)rect.getCenterX(), getHeight() / 2f - (float)rect.getCenterY());
        g.rotate(rad, x, y);
    }

    private void drawEmptyText(Graphics2D g)
    {
        if (!_valuesList.isEmpty())
            return;

        String text = "No data";
        FontMetrics metrics = g.getFontMetrics(Fonts.TEXT);
        Rectangle2D rect = metrics.getStringBounds(text, g);

        g.setFont(Fonts.TEXT);
        g.setColor(Colors.AXIS);
        g.drawString(text, getWidth() / 2f - (float)rect.getCenterX(), getHeight() / 2f - (float)rect.getCenterY());
    }

    private void drawMinorAxis(Graphics2D g)
    {
        // don't draw minor axis if panel too small
        if (getWidth() >= PADDING * 4)
            drawMinorXAxis(g);

        // don't draw minor axis if panel too small
        if (getHeight() >= PADDING * 4)
            drawMinorYAxis(g);
    }

    private void drawMinorXAxis(Graphics2D g)
    {
        FontMetrics metrics = g.getFontMetrics(Fonts.MINOR_AXIS);
        g.setFont(Fonts.MINOR_AXIS);
        g.setStroke(new BasicStroke(MINOR_AXIS_WIDTH));

        int interval = (int)(_xValues.length / (getWidth() / 100f));

        if (interval == 0)
            interval = 1;

        if (_valuesList.isEmpty())
            return;

        for (int i = 0; i < _xValues.length; i += interval)
        {
            Rectangle2D rect = metrics.getStringBounds(_xValues[i], g);
            int x = (int)getX(i);
            int y2 = getHeight() - PADDING + MINOR_AXIS_EXTRA;

            g.setColor(Colors.MINOR_AXIS);
            g.drawLine(x, PADDING, x, y2);

            g.setColor(Colors.TEXT);
            g.drawString(_xValues[i], x - (float)rect.getCenterX(), y2 + (float)rect.getHeight());
        }
    }

    private void drawMinorYAxis(Graphics2D g)
    {
        FontMetrics metrics = g.getFontMetrics(Fonts.MINOR_AXIS);
        double median = (max + min) / 2;
        int interval = (int)Math.abs((max - min) / (getHeight() / 100f));

        if (interval == 0)
            interval = 1;

        if (_valuesList.isEmpty())
            return;

        // draw above mean
        int value = (int)median;
        int y = (int)getY(value);

        while (y >= PADDING)
        {
            String text = String.valueOf(value);
            Rectangle2D rect = metrics.getStringBounds(text, g);
            int x = PADDING - MINOR_AXIS_EXTRA;

            g.setColor(Colors.MINOR_AXIS);
            g.drawLine(x, y, getWidth() - PADDING, y);

            g.setColor(Colors.TEXT);
            g.drawString(text, x - (float)rect.getWidth(), y - (float)rect.getCenterY());

            value += interval;
            y = (int)getY(value);
        }

        // draw below mean
        value = (int)median - interval;
        y = (int)getY(value);

        while (y <= getHeight() - PADDING)
        {
            String text = String.valueOf(value);
            Rectangle2D rect = metrics.getStringBounds(text, g);
            int x = PADDING - MINOR_AXIS_EXTRA;

            g.setColor(Colors.MINOR_AXIS);
            g.drawLine(x, y, getWidth() - PADDING, y);

            g.setColor(Colors.TEXT);
            g.drawString(text, x - (float)rect.getWidth(), y - (float)rect.getCenterY());

            value -= interval;
            y = (int)getY(value);
        }
    }

    private void drawAxis(Graphics2D g)
    {
        g.setColor(Colors.AXIS);
        g.setStroke(new BasicStroke(AXIS_WIDTH));

        g.drawLine(PADDING, PADDING, PADDING, getHeight() - PADDING); // vertical
        // axis

        // constrain axis to chart bounds
        int y = (int)getY(0);
        y = Math.min(Math.max(y, PADDING), getHeight() - PADDING);

        // set axis to bottom when no datasets
        if (_valuesList.isEmpty())
            y = getHeight() - PADDING;

        g.drawLine(PADDING, y, getWidth() - PADDING, y); // horizontal
        // axis
    }

    private void drawLines(Graphics2D g)
    {
        // set clip so line does not draw outside the axis
        g.setClip(PADDING, PADDING, getWidth() - PADDING * 2, getHeight() - PADDING * 2);

        int index = 0;
        for (String name : _valuesList.keySet())
        {
            g.setColor(Colors.get(index));
            g.setStroke(new BasicStroke(LINE_WIDTH, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g.draw(getLinePath(name));

            index++;
        }

        g.setClip(null);
    }

    private Path2D getLinePath(String name)
    {
        double[] values = _valuesList.get(name);

        Path2D path = new Path2D.Float();
        path.moveTo(getX(0), getY(getAnimatedValue(name, 0)));

        for (int i = 1; i < values.length; i++)
            path.lineTo(getX(i), getY(getAnimatedValue(name, i)));

        return path;
    }

    private double getAnimatedValue(String name, int index)
    {
        double mean = (min + max) / 2;
        double aniLeftPercent = 1 - getAniProgress(name);

        double[] values = _valuesList.get(name);

        return values[index] - (values[index] - mean) * aniLeftPercent;
    }

    /**
     * Get x-coord of value based on index of value.
     */
    private float getX(int index)
    {
        int width = getWidth() - PADDING * 2;
        int left = PADDING;

        if (_xValues.length == 1)
            return left;

        return left + width / ((float)_xValues.length - 1) * index;
    }

    /**
     * Get y-coord of value based on min/max values.
     */
    private double getY(double value)
    {
        int height = getHeight() - PADDING * 2;
        float top = PADDING + height / 8f;
        float bottom = getHeight() - PADDING - height / 8f;

        return bottom - (bottom - top) * (value - min) / (max - min);
    }


    public final void addValues(String name, double[] values)
    {
        if (values.length == 0)
            return;

        _valuesList.put(name, values);
        startAnimation(name);

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

    public final void removeValues(String name)
    {
        if (_valuesList.containsKey(name))
        {
            _valuesList.remove(name);
            removeAnimation(name);
        }
    }

    public final void clear()
    {
        _xValues = new String[] { };
        _valuesList.clear();

        removeAllAnimations();

        min = Float.MAX_VALUE;
        max = Float.MIN_VALUE;

        repaint();
    }

    //========================================================================= PROPERTIES
    public final void setTitle(String title)
    {
        _title.setText(title);
        repaint();
    }

    public final void setXAxisText(String text)
    {
        _xAxisText = text;
        repaint();
    }
    public final void setYAxisText(String text)
    {
        _yAxisText = text;
        repaint();
    }

    public final void setXValues(String[] values)
    {
        _xValues = values;
        repaint();
    }

    //========================================================================= EVENTS
    protected final void onAniProgressChanged()
    {
        repaint();
    }
}