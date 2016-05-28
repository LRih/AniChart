package Views;

import AniChart.LineChart;
import Utils.MathUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public final class LineChartFrame extends JFrame
{
    public static void main(String[] args)
    {
        new LineChartFrame().setVisible(true);
    }

    private final LineChart _chart = new LineChart();

    private LineChartFrame()
    {
        super("Line Chart");

        setBounds(100, 100, 600, 500);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        BorderLayout layout = new BorderLayout();
        setLayout(layout);

        add(_chart);
        add(createButton(), BorderLayout.SOUTH);
    }

    private JButton createButton()
    {
        JButton btn = new JButton("Reset");

        btn.addActionListener(new ActionListener()
        {
            public final void actionPerformed(ActionEvent e)
            {
                resetChart();
            }
        });

        return btn;
    }

    private void resetChart()
    {
        _chart.clear();

        _chart.setTitle("Title");

        int count = MathUtils.rand(60, 90);
        String[] xValues = new String[count];

        for (int i = 0; i < count; i++)
            xValues[i] = String.valueOf(i + 1);

        _chart.setXValues(xValues);

        int lastChange = MathUtils.rand(-5, 5);
        double[] values = new double[count];
        values[0] = MathUtils.rand(-50, 50);

        for (int i = 1; i < count; i++)
        {
            int change = MathUtils.rand(lastChange - 1, lastChange + 1);
            values[i] = values[i - 1] + change;
            lastChange = change;
        }

        _chart.addValues("Temperature", values);


        values = new double[count];
        values[0] = MathUtils.rand(-50, 50);

        for (int i = 1; i < count; i++)
            values[i] = values[i - 1] + MathUtils.rand(-10, 10);

        _chart.addValues("Humidity", values);
    }
}
