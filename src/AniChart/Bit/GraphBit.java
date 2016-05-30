package AniChart.Bit;

import AniChart.Colors;
import AniChart.Fonts;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

public final class GraphBit
{
    //========================================================================= VARIABLE
    private static final int MINOR_X_AXIS_HEIGHT = 10;
    private static final int MINOR_Y_AXIS_WIDTH = 10;
    private static final int MINOR_AXIS_EXTRA = 6;

    private static final float AXIS_STROKE = 1.5f;
    private static final float MINOR_AXIS_STROKE = 1f;

    private static final float PIXELS_PER_INTERVAL = 100;

    private float _x1 = 0;
    private float _y1 = 0;
    private float _x2 = 0;
    private float _y2 = 0;

    private String[] _xValues = new String[] { };

    private float _min = 0;
    private float _max = 0;

    //========================================================================= FUNCTIONS
    public final void draw(Graphics2D g)
    {
        drawMinorAxis(g);
        drawAxis(g);
    }

    private void drawMinorAxis(Graphics2D g)
    {
        // TODO draw text within bounds

        drawMinorXAxis(g);
        drawMinorYAxis(g);
    }

    private void drawMinorXAxis(Graphics2D g)
    {
        if (_xValues.length == 0)
            return;

        FontMetrics metrics = g.getFontMetrics(Fonts.MINOR_AXIS);
        g.setFont(Fonts.MINOR_AXIS);
        g.setStroke(new BasicStroke(MINOR_AXIS_STROKE));

        int midIndex = _xValues.length / 2;
        int interval = (int)Math.max(_xValues.length / (gridWidth() / PIXELS_PER_INTERVAL), 1);
        float x;

        // draw left side
        int index = midIndex;

        while ((x = getX(index)) >= gridX1())
        {
            drawMinorXAxis(g, metrics, _xValues[index], x);
            index -= interval;
        }

        // draw right side
        index = midIndex + interval;

        while ((x = getX(index)) <= _x2)
        {
            drawMinorXAxis(g, metrics, _xValues[index], x);
            index += interval;
        }
    }
    private void drawMinorXAxis(Graphics2D g, FontMetrics metrics, String text, float x)
    {
        Rectangle2D rect = metrics.getStringBounds(text, g);

        g.setColor(Colors.MINOR_AXIS);
        g.draw(new Line2D.Float(x, _y1, x, gridY2() + MINOR_AXIS_EXTRA));

        g.setColor(Colors.TEXT);
        g.drawString(text, x - (float)rect.getCenterX(), gridY2() + MINOR_AXIS_EXTRA - (float)rect.getY());
    }

    private void drawMinorYAxis(Graphics2D g)
    {
        // TODO loops forever when height is 0

        if (_min == _max)
            return;

        FontMetrics metrics = g.getFontMetrics(Fonts.MINOR_AXIS);
        g.setFont(Fonts.MINOR_AXIS);
        g.setStroke(new BasicStroke(MINOR_AXIS_STROKE));

        float median = (_max + _min) / 2;
        int interval = (int)Math.max(Math.abs((_max - _min) / (gridHeight() / PIXELS_PER_INTERVAL)), 1);
        float y;

        // draw above mean
        float value = median;

        while ((y = getY(value)) >= _y1)
        {
            drawMinorYAxis(g, metrics, String.valueOf(Math.round(value)), y);
            value += interval;
        }

        // draw below mean
        value = (int)median - interval;

        while ((y = getY(value)) <= gridY2())
        {
            drawMinorYAxis(g, metrics, String.valueOf(Math.round(value)), y);
            value -= interval;
        }
    }
    private void drawMinorYAxis(Graphics2D g, FontMetrics metrics, String text, float y)
    {
        Rectangle2D rect = metrics.getStringBounds(text, g);
        float x = gridX1() - MINOR_AXIS_EXTRA;

        g.setColor(Colors.MINOR_AXIS);
        g.draw(new Line2D.Float(x, y, _x2, y));

        g.setColor(Colors.TEXT);
        g.drawString(text, x - (float)rect.getWidth(), y - (float)rect.getCenterY());
    }

    private void drawAxis(Graphics2D g)
    {
        g.setColor(Colors.AXIS);
        g.setStroke(new BasicStroke(AXIS_STROKE));

        float x = getX(0);
        g.draw(new Line2D.Float(x, _y1, x, gridY2())); // vertical axis

        // constrain axis to chart bounds
        float y = getY(0);
        y = Math.min(Math.max(y, _y1), gridY2());

        g.draw(new Line2D.Float(gridX1(), y, _x2, y)); // horizontal axis
    }


    public final void clear()
    {
        _xValues = new String[] { };

        _min = 0;
        _max = 0;
    }

    //========================================================================= PROPERTIES
    public final Rectangle2D gridBounds()
    {
        return new Rectangle.Float(gridX1(), gridY1(), gridWidth(), gridHeight());
    }

    private float gridX1()
    {
        return _x1 + MINOR_Y_AXIS_WIDTH;
    }
    private float gridY1()
    {
        return _y1;
    }
    private float gridX2()
    {
        return _x2;
    }
    private float gridY2()
    {
        return _y2 - MINOR_X_AXIS_HEIGHT;
    }
    private float gridWidth()
    {
        return _x2 - gridX1();
    }
    private float gridHeight()
    {
        return gridY2() - _y1;
    }

    /**
     * Get x-coord of value based on index of value.
     */
    public final float getX(int index)
    {
        if (_xValues.length <= 1)
            return gridX1();

        return gridX1() + gridWidth() / (_xValues.length - 1) * index;
    }

    /**
     * Get y-coord of value based on min/max values.
     */
    public final float getY(float value)
    {
        if (_min == _max)
            return _y1 + gridHeight() / 2;

        float top = _y1 + gridHeight() / 8;
        float bottom = gridY2() - gridHeight() / 8;

        return bottom - (bottom - top) * (value - _min) / (_max - _min);
    }

    public final float min()
    {
        return _min;
    }
    public final float max()
    {
        return _max;
    }

    public final void setBounds(float x1, float y1, float x2, float y2)
    {
        _x1 = x1;
        _y1 = y1;
        _x2 = x2;
        _y2 = y2;
    }

    public final void setXValues(String[] values)
    {
        _xValues = values;
    }

    public final void setMinMax(float min, float max)
    {
        _min = min;
        _max = max;
    }
}
