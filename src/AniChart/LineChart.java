package AniChart;

import AniChart.Bit.GraphBit;
import AniChart.Bit.TitleBit;
import AniChart.Bit.XAxisTextBit;
import Utils.MathUtils;

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.LinkedHashMap;

public final class LineChart extends AnimatedPanel
{
    //========================================================================= VARIABLES
    private static final int PADDING = 40; // TODO reduce later after factoring axis size

    private static final float LINE_WIDTH = 2f;

    private final TitleBit _title = new TitleBit();
    private final XAxisTextBit _xAxisText = new XAxisTextBit();
    private String _yAxisText = "";
    private final GraphBit _graph = new GraphBit();

    // linked hash map to preserve insertion order
    private final LinkedHashMap<String, float[]> _valuesList = new LinkedHashMap<>();

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

        drawLines(g);
    }

    private void drawText(Graphics2D g)
    {
        _title.draw(g, getWidth() / 2f, PADDING, true);
        _xAxisText.draw(g, getWidth() / 2f, getHeight() - PADDING - _xAxisText.height(), true);

        drawAxisText(g);
    }

    private void drawAxisText(Graphics2D g)
    {
        FontMetrics metrics = g.getFontMetrics(Fonts.AXIS);
        g.setFont(Fonts.AXIS);

        // y-axis
        Rectangle2D rect = metrics.getStringBounds(_yAxisText, g);

        float rad = 90 * (float)Math.PI / 180;

        float x = PADDING / 5f;
        float y = getHeight() / 2f;

        g.rotate(-rad, x, y);
        g.drawString(_yAxisText, PADDING / 3f - (float)rect.getCenterX(), getHeight() / 2f - (float)rect.getCenterY());
        g.rotate(rad, x, y);
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
        float[] values = _valuesList.get(name);

        Path2D path = new Path2D.Float();
        path.moveTo(_graph.getX(0), _graph.getY(getAnimatedValue(name, 0)));

        for (int i = 1; i < values.length; i++)
            path.lineTo(_graph.getX(i), _graph.getY(getAnimatedValue(name, i)));

        return path;
    }

    private float getAnimatedValue(String name, int index)
    {
        float mean = (_graph.min() + _graph.max()) / 2;
        float aniLeftPercent = 1 - getAniProgress(name);

        float[] values = _valuesList.get(name);

        return values[index] - (values[index] - mean) * aniLeftPercent;
    }


    public final void addValues(String name, float[] values)
    {
        if (values.length == 0)
            return;

        startAnimation(name);

        _valuesList.put(name, values);

        float min = Math.min(MathUtils.getMin(values), _graph.min());
        float max = Math.max(MathUtils.getMax(values), _graph.max());

        _graph.setMinMax(min, max);

        repaint();
    }

    public final void removeValues(String name)
    {
        // TODO recalculate min, max

        if (_valuesList.containsKey(name))
        {
            _valuesList.remove(name);
            removeAnimation(name);

            repaint();
        }
    }

    public final void clear()
    {
        _graph.clear();
        _valuesList.clear();
        removeAllAnimations();

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
        _xAxisText.setText(text);
        repaint();
    }
    public final void setYAxisText(String text)
    {
        _yAxisText = text;
        repaint();
    }

    public final void setXValues(String[] values)
    {
        _graph.setXValues(values);
        repaint();
    }

    //========================================================================= EVENTS
    protected final void onAniProgressChanged()
    {
        repaint();
    }
}