package Views;

import AniChart.BarChart;
import Utils.MathUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public final class BarChartFrame extends JFrame
{
    public static void main(String[] args)
    {
        new BarChartFrame().setVisible(true);
    }

    private final BarChart _chart = new BarChart();

    private BarChartFrame()
    {
        super("Bar Chart");

        setBounds(100, 100, 400, 300);
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
        _chart.setTitle("Title");
        _chart.setXAxisText("Cost");

        int count = MathUtils.rand(60, 90);
        float[] values = new float[count];

        values[0] = MathUtils.rand(-50, 50);
        for (int i = 1; i < count; i++)
            values[i] = values[i - 1] + MathUtils.rand(-10, 10);

        _chart.setValues(values);
    }
}
