import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.List;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

public class MainForm extends JFrame {
    private JList<String> listItemTable;
    private JTextField textFieldEnterID;
    private JButton loadDataToDatabase;
    private JPanel panelControl;
    private JPanel panelChartPrice;
    private JPanel panelData;
    private JPanel panelChartQuantity;
    private DefaultListModel<String> listModel;

    private JLabel supplyPriceLabel;
    private JLabel supplyQuantityLabel;
    private JLabel demandPriceLabel;
    private JLabel demandQuantityLabel;

    public MainForm() {
        setTitle("Item Data Chart");
        setSize(800, 600);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        panelControl = new JPanel();
        panelChartPrice = new JPanel();
        panelData = new JPanel();
        panelChartQuantity = new JPanel();

        panelControl.setLayout(new BorderLayout());
        listModel = new DefaultListModel<>();
        listItemTable = new JList<>(listModel);
        JScrollPane listScrollPane = new JScrollPane(listItemTable);

        textFieldEnterID = new JTextField();
        loadDataToDatabase = new JButton("Load Data");

        panelControl.add(listScrollPane, BorderLayout.CENTER);
        panelControl.add(textFieldEnterID, BorderLayout.NORTH);
        panelControl.add(loadDataToDatabase, BorderLayout.SOUTH);

        supplyPriceLabel = new JLabel("Supply Price: ");
        supplyQuantityLabel = new JLabel("Supply Quantity: ");
        demandPriceLabel = new JLabel("Demand Price: ");
        demandQuantityLabel = new JLabel("Demand Quantity: ");

        panelData.setLayout(new GridLayout(4, 1));
        panelData.add(supplyPriceLabel);
        panelData.add(supplyQuantityLabel);
        panelData.add(demandPriceLabel);
        panelData.add(demandQuantityLabel);

        loadDataToDatabase.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String itemIdText = textFieldEnterID.getText();
                try {
                    int itemId = Integer.parseInt(itemIdText);
                    loadItemData(itemId);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(MainForm.this, "Please enter a valid item ID.");
                }
            }
        });

        listItemTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    String selectedItem = listItemTable.getSelectedValue();
                    if (selectedItem != null) {
                        int itemId = DatabaseManager.getItemIdFromTableName(selectedItem.replace(' ', '_'));
                        textFieldEnterID.setText(String.valueOf(itemId));
                        loadItemData(itemId);
                    }
                }
            }
        });

        setLayout(new GridLayout(2, 2));
        add(panelControl);
        add(panelChartPrice);
        add(panelData);
        add(panelChartQuantity);

        loadItemListFromDatabase();
    }

    private void loadItemData(int itemId) {
        Item item = ConnectorAPI.retrieveItemFromAPI(itemId);
        if (item != null) {
            DatabaseManager.saveItemToDatabase(item);
            if (!listModel.contains(item.getName())) {
                listModel.addElement(item.getName());
            }
            loadChartData(item.getName().replace(' ', '_'));
            updateDataPanel(item);
        } else {
            JOptionPane.showMessageDialog(this, "Item not found in API.");
        }
    }

    private void loadChartData(String tableName) {
        List<ItemRecord> priceData = DatabaseManager.getPriceData(tableName);
        displayChart(priceData);
    }

    private void displayChart(List<ItemRecord> priceData) {
        TimeSeries supplyPriceSeries = new TimeSeries("Supply Price");
        TimeSeries demandPriceSeries = new TimeSeries("Demand Price");
        TimeSeries supplyQuantitySeries = new TimeSeries("Supply Quantity");
        TimeSeries demandQuantitySeries = new TimeSeries("Demand Quantity");

        for (ItemRecord record : priceData) {
            supplyPriceSeries.addOrUpdate(new Second(record.getTimestamp()), record.getSupplyPrice());
            demandPriceSeries.addOrUpdate(new Second(record.getTimestamp()), record.getDemandPrice());
            supplyQuantitySeries.addOrUpdate(new Second(record.getTimestamp()), record.getSupplyQuantity());
            demandQuantitySeries.addOrUpdate(new Second(record.getTimestamp()), record.getDemandQuantity());
        }

        TimeSeriesCollection priceDataset = new TimeSeriesCollection();
        priceDataset.addSeries(supplyPriceSeries);
        priceDataset.addSeries(demandPriceSeries);

        TimeSeriesCollection quantityDataset = new TimeSeriesCollection();
        quantityDataset.addSeries(supplyQuantitySeries);
        quantityDataset.addSeries(demandQuantitySeries);

        JFreeChart priceChart = ChartFactory.createTimeSeriesChart(
                "Item Price Data",
                "Time",
                "Price",
                priceDataset,
                true,
                true,
                false
        );

        JFreeChart quantityChart = ChartFactory.createTimeSeriesChart(
                "Item Quantity Data",
                "Time",
                "Quantity",
                quantityDataset,
                true,
                true,
                false
        );

        XYPlot pricePlot = (XYPlot) priceChart.getPlot();
        DateAxis priceAxis = (DateAxis) pricePlot.getDomainAxis();
        priceAxis.setDateFormatOverride(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

        XYPlot quantityPlot = (XYPlot) quantityChart.getPlot();
        DateAxis quantityAxis = (DateAxis) quantityPlot.getDomainAxis();
        quantityAxis.setDateFormatOverride(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

        ChartPanel priceChartPanel = new ChartPanel(priceChart);
        priceChartPanel.setPreferredSize(new Dimension(400, 300));

        ChartPanel quantityChartPanel = new ChartPanel(quantityChart);
        quantityChartPanel.setPreferredSize(new Dimension(400, 300));

        panelChartPrice.removeAll();
        panelChartPrice.setLayout(new BorderLayout());
        panelChartPrice.add(priceChartPanel, BorderLayout.CENTER);
        panelChartPrice.validate();

        panelChartQuantity.removeAll();
        panelChartQuantity.setLayout(new BorderLayout());
        panelChartQuantity.add(quantityChartPanel, BorderLayout.CENTER);
        panelChartQuantity.validate();
    }

    private void updateDataPanel(Item item) {
        List<ItemRecord> records = DatabaseManager.getPriceData(item.getName().replace(' ', '_'));
        if (!records.isEmpty()) {
            ItemRecord latestRecord = records.get(records.size() - 1);
            supplyPriceLabel.setText("Supply Price: " + latestRecord.getSupplyPrice());
            supplyQuantityLabel.setText("Supply Quantity: " + latestRecord.getSupplyQuantity());
            demandPriceLabel.setText("Demand Price: " + latestRecord.getDemandPrice());
            demandQuantityLabel.setText("Demand Quantity: " + latestRecord.getDemandQuantity());
        }
    }

    private void loadItemListFromDatabase() {
        List<String> itemNames = DatabaseManager.getItemNamesFromDatabase();
        for (String itemName : itemNames) {
            listModel.addElement(itemName);
        }
        System.out.println("Items loaded from database: " + itemNames);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainForm form = new MainForm();
            form.setVisible(true);
        });
    }
}
