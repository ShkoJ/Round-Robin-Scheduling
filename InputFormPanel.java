import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class InputFormPanel extends JPanel {

    private final AppFrame parentFrame; // Reference to the main frame to switch panels
    private JTable table;
    private JTextField quantumField;
    private DefaultTableModel model;

    public InputFormPanel(AppFrame parentFrame) {
        this.parentFrame = parentFrame; 
        initComponents();
    }

    private void initComponents() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(30, 40, 20, 40));
        setBackground(new Color(0xE3F2FD));

        JLabel title = new JLabel("Enter Processes and Quantum Time", SwingConstants.CENTER);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setForeground(new Color(0x1A237E));
        add(title);
        add(Box.createVerticalStrut(20));

        // Control Panel (Number of processes and Generate Table button)
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        controlPanel.setBackground(getBackground());

        JLabel processLabel = new JLabel("Number of Processes (1-20): ");
        processLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        controlPanel.add(processLabel);

        JSpinner processSpinner = new JSpinner(new SpinnerNumberModel(4, 1, 20, 1));
        controlPanel.add(processSpinner);

        JButton generateButton = new JButton("Generate Table");
        generateButton.setBackground(new Color(0x9acff5));
        generateButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        generateButton.setForeground(Color.BLACK);
        generateButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        generateButton.setFocusPainted(false);
        controlPanel.add(generateButton);

        add(controlPanel);
        add(Box.createVerticalStrut(20));

        // Table setup
        String[] columns = { "Process ID", "Arrival Time", "Burst Time" };
        Object[][] data = {
                { "P1", 0, 5 },
                { "P2", 2, 3 },
                { "P3", 4, 6 },
                { "P4", 6, 4 }
        };

        model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0; // Make only arrival time and burst time editable
            }
        };

        table = new JTable(model);
        table.getTableHeader().setReorderingAllowed(false);
        table.setRowHeight(25);
        table.setFillsViewportHeight(true);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane);
        add(Box.createVerticalStrut(15));

        // Generate button action
        generateButton.addActionListener(e -> {
            int numProcesses = (int) processSpinner.getValue();
            Object[][] newData = new Object[numProcesses][3];
            for (int i = 0; i < numProcesses; i++) {
                newData[i][0] = "P" + (i + 1);
                newData[i][1] = 0;
                newData[i][2] = 0;
            }
            model.setDataVector(newData, columns); // Use setDataVector to regenerate
            table.getTableHeader().setReorderingAllowed(false);
        });

        // Hover effects for generate button
        generateButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                generateButton.setBackground(new Color(0x5ab7fa));
            }
            public void mouseExited(MouseEvent e) {
                generateButton.setBackground(new Color(0x9acff5));
            }
        });

        // Quantum time input
        JPanel quantumPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        quantumPanel.setBackground(getBackground());

        JLabel quantumLabel = new JLabel("Quantum Time: ");
        quantumLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        quantumPanel.add(quantumLabel);

        quantumField = new JTextField(5);
        quantumField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        quantumField.setText("3");
        quantumPanel.add(quantumField);
        add(quantumPanel);
        add(Box.createVerticalStrut(15));

        // Run Simulation button
        JButton runButton = new JButton("Run Simulation");
        runButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        runButton.setFont(new Font("SansSerif", Font.PLAIN, 16));
        runButton.setBackground(new Color(0x9acff5));
        runButton.setForeground(Color.BLACK);
        runButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect for run button
        runButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                runButton.setBackground(new Color(0x5ab7fa));
            }
            public void mouseExited(MouseEvent e) {
                runButton.setBackground(new Color(0x9acff5));
            }
        });

        runButton.addActionListener(e -> runSimulation());
        add(runButton);
    }

    private void runSimulation() {
        try {
            if (table.isEditing()) {
                table.getCellEditor().stopCellEditing();
            }

            int rowCount = model.getRowCount();
            List<RoundRobinScheduler.Process> inputProcesses = new ArrayList<>();

            for (int i = 0; i < rowCount; i++) {
                String arrivalStr = model.getValueAt(i, 1).toString().trim();
                String burstStr = model.getValueAt(i, 2).toString().trim();

                int arrival = Integer.parseInt(arrivalStr);
                int burst = Integer.parseInt(burstStr);

                if (arrival < 0 || burst <= 0) { // Burst time should be positive
                    throw new NumberFormatException("Arrival time must be non-negative and burst time must be positive.");
                }

                inputProcesses.add(new RoundRobinScheduler.Process(i + 1, arrival, burst));
            }

            int quantum = Integer.parseInt(quantumField.getText().trim());
            if (quantum <= 0) {
                throw new NumberFormatException("Quantum time must be a positive integer.");
            }

            // Run the simulation and get the encapsulated results
            SimulationResult result = RoundRobinScheduler.runRR(inputProcesses, quantum);

            // Prepare data for the output table
            Object[][] displayData = new Object[result.getCompletedProcesses().size()][6];
            for (int i = 0; i < result.getCompletedProcesses().size(); i++) {
                RoundRobinScheduler.Process p = result.getCompletedProcesses().get(i);
                displayData[i][0] = p.id;
                displayData[i][1] = p.arrivalTime;
                displayData[i][2] = p.burstTime;
                displayData[i][3] = p.completionTime;
                displayData[i][4] = p.turnaroundTime;
                displayData[i][5] = p.waitingTime;
            }

            parentFrame.showOutputPanel(displayData, result); // Pass the entire result
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Invalid input: " + ex.getMessage(),
                    "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}