package Views;

import AniChart.Colors;
import AniChart.LineChart;

import javax.swing.*;

public final class LineChartFrame extends JFrame
{
    public static void main(String[] args)
    {
        new LineChartFrame().setVisible(true);
    }

    private LineChartFrame()
    {
        super("Line Chart");

        setBounds(100, 100, 400, 300);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        LineChart chart = new LineChart();

        chart.setTitle("Title");
        chart.setXValues(new String[] { "1", "2", "3", "4", "5", "6" });
        chart.addDataset("Temperature", Colors.get(0), new double[] { 1, 3, 2, 4, 2, 1 });
        chart.addDataset("Humidity", Colors.get(1), new double[] { 2, 7, 5, 4, 6, 9 });

        add(chart);
    }
}
