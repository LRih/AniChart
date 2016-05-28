package AniChart.Bit;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.util.ArrayList;
import java.util.List;

public final class PieBit
{
    //========================================================================= VARIABLE
    private final List<Float> _values = new ArrayList<>();
    private final List<Color> _colors = new ArrayList<>();

    private float _total = 0;

    //========================================================================= FUNCTIONS
    public final void draw(Graphics2D g, float x, float y, float width, float height, float aniProgress)
    {
        // no values to draw
        if (_values.isEmpty())
            return;

        float size = Math.min(width, height);
        float cx = x + (width - size) / 2;
        float cy = y + (height - size) / 2;

        float totalAngle = 360 * aniProgress;
        float angle = 0;

        for (int i = 0; i < _values.size(); i++)
        {
            float sweep = _values.get(i) / _total * totalAngle;
            drawWedge(g, cx, cy, size, angle, sweep, _colors.get(i));
            angle += sweep;
        }
    }

    private void drawWedge(Graphics2D g, float x, float y, float size, float angle, float sweep, Color color)
    {
        g.setColor(color);

        Arc2D.Double arc = new Arc2D.Double(x, y, size, size, angle, sweep, Arc2D.PIE);
        g.fill(arc);
    }

    public final void addValue(float value, Color color)
    {
        _values.add(value);
        _colors.add(color);

        _total += value;
    }

    public final void clear()
    {
        _values.clear();
        _colors.clear();

        _total = 0;
    }

    //========================================================================= PROPERTIES
    public final int size()
    {
        return _values.size();
    }
}
