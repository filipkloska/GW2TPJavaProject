import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

public class MainForm extends JFrame {
    private JPanel mainPanel;
    private JButton loadDataButton;
    private JPanel chartPanel;

    public MainForm() {
        setContentPane(mainPanel);
        setTitle("Item Data Chart");
        setSize(800, 600);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        loadDataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayChart();
            }
        });
    }

    private void displayChart() {
        List<ItemRecord> priceData = DatabaseManager.getPriceData("glob_of_ectoplasm");

        TimeSeries supplySeries = new TimeSeries("Supply Price");
        TimeSeries demandSeries = new TimeSeries("Demand Price");

        for (ItemRecord record : priceData) {
            supplySeries.addOrUpdate(new Second(record.getTimestamp()), record.getSupplyPrice());
            demandSeries.addOrUpdate(new Second(record.getTimestamp()), record.getDemandPrice());
        }

        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(supplySeries);
        dataset.addSeries(demandSeries);

        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                "Item Price Data",
                "Time",
                "Price",
                dataset,
                true,
                true,
                false
        );

        XYPlot plot = (XYPlot) chart.getPlot();
        DateAxis axis = (DateAxis) plot.getDomainAxis();
        axis.setDateFormatOverride(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 600));
        this.chartPanel.removeAll();
        this.chartPanel.setLayout(new BorderLayout());
        this.chartPanel.add(chartPanel, BorderLayout.CENTER);
        this.chartPanel.validate();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainForm form = new MainForm();
            form.setVisible(true);
        });
    }
}
