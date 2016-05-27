package Views;

import AniChart.PieChart;
import Utils.MathUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public final class PieChartFrame extends JFrame
{
    public static void main(String[] args)
    {
        new PieChartFrame().setVisible(true);
    }

    private PieChart _chart = new PieChart();

    private PieChartFrame()
    {
        super("Pie Chart");

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

        int count = MathUtils.rand(2, 8);
        float[] values = new float[count];

        for (int i = 0; i < count; i++)
            values[i] = MathUtils.rand(1, 10);

        _chart.setValues(values);
    }
}
