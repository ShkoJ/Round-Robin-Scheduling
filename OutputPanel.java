import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class OutputPanel extends JPanel {

    private final AppFrame parentFrame; // Reference to the main frame to switch panels
    private final Object[][] processData;
    private final SimulationResult simulationResult;

    public OutputPanel(AppFrame parentFrame, Object[][] processData, SimulationResult simulationResult) {
        this.parentFrame = parentFrame;
        this.processData = processData;
        this.simulationResult = simulationResult;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(new Color(0xF1F8E9));

        JLabel title = new JLabel("Simulation Results", SwingConstants.CENTER); // Changed title
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(new Color(0x33691E));
        add(title, BorderLayout.NORTH);

        // Process data table
        String[] displayColumnNames = {"Process ID", "Arrival", "Burst", "Completion", "Turnaround", "Waiting"};
        JTable table = new JTable(new DefaultTableModel(processData, displayColumnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        table.getTableHeader().setReorderingAllowed(false);
        JScrollPane scrollPane = new JScrollPane(table);

        // Averages
        String avgStats = String.format(
                "<html><b>Average Turnaround Time:</b> %.2f &nbsp;&nbsp;&nbsp; <b>Average Waiting Time:</b> %.2f</html>",
                simulationResult.getAverageTurnaroundTime(), simulationResult.getAverageWaitingTime()
        );
        JLabel avgLabel = new JLabel(avgStats, SwingConstants.CENTER);
        avgLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        avgLabel.setForeground(new Color(0x000000));

        // Return button
        JButton returnButton = new JButton("Return to Input Form");
        returnButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        returnButton.setBackground(new Color(0x8BC34A));
        returnButton.setForeground(Color.BLACK);
        returnButton.setFocusPainted(false);
        returnButton.addActionListener(e -> parentFrame.showInputFormPanel());

        // Gantt chart section
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout(10, 10));
        centerPanel.setBackground(getBackground());

        JLabel ganttLabel = new JLabel("Gantt Chart", SwingConstants.CENTER);
        ganttLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        ganttLabel.setForeground(new Color(0x33691E)); 

        // Pass the Gantt entries from the simulationResult
        GanttChartPanel ganttChartPanel = new GanttChartPanel(simulationResult.getGanttEntries());
        JScrollPane ganttScroll = new JScrollPane(ganttChartPanel);
        ganttScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        ganttScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        ganttScroll.setBorder(BorderFactory.createEmptyBorder()); // Remove scroll pane border

        centerPanel.add(scrollPane, BorderLayout.NORTH);
        centerPanel.add(ganttLabel, BorderLayout.CENTER);
        centerPanel.add(ganttScroll, BorderLayout.SOUTH); // Position Gantt chart at the bottom of center panel

        add(centerPanel, BorderLayout.CENTER);

        // Bottom (averages and return button)
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setBackground(getBackground());
        avgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        returnButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        bottomPanel.add(avgLabel);
        bottomPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        bottomPanel.add(returnButton);

        add(bottomPanel, BorderLayout.SOUTH);
    }
}