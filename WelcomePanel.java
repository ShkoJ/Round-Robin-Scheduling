import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

@SuppressWarnings("serial") 
public class WelcomePanel extends JPanel {

    private final AppFrame parentFrame; // Reference to the main frame to switch panels

    public WelcomePanel(AppFrame parentFrame) {
        this.parentFrame = parentFrame;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(0xE3F2FD));
        setBorder(BorderFactory.createEmptyBorder(40, 50, 15, 50));

        // Title
        JLabel title = new JLabel("Welcome to Round Robin Simulator!", SwingConstants.CENTER);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(new Color(0x1A237E));

        // Sub-heading
        JLabel subtitle = new JLabel("A simple and interactive scheduling simulation", SwingConstants.CENTER);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setFont(new Font("SansSerif", Font.ITALIC, 16));
        subtitle.setForeground(new Color(0x546E7A));
        subtitle.setBorder(BorderFactory.createEmptyBorder(25, 0, 30, 0));

        JLabel description = new JLabel(
                "<html><div style='text-align: center;'>"
                        + "<hr/>"
                		+ "<br/>"
                        + "<b>Features:</b><br>"
                        + "• Enter arrival and burst times<br>"
                        + "• Set quantum value<br>"
                        + "• Visualize Gantt chart<br>"
                        + "• Analyze turnaround and waiting times"
                		+ "<br/><br/>"
                        + "<hr/>"
                        + "</div></html>"
        );
        description.setFont(new Font("SansSerif", Font.PLAIN, 16));
        description.setAlignmentX(Component.CENTER_ALIGNMENT);
        description.setForeground(new Color(40, 40, 80));

        JPanel descriptionPanel = new JPanel();
        descriptionPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        descriptionPanel.add(description);
        descriptionPanel.setBackground(getBackground()); // Inherit panel background

        // Get Started Button
        JButton startButton = new JButton("Get Started!");
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.setFont(new Font("SansSerif", Font.PLAIN, 16));
        startButton.setFocusPainted(false);
        startButton.setBackground(new Color(0x9acff5));
        startButton.setForeground(Color.BLACK);
        startButton.setPreferredSize(new Dimension(140, 40));
        startButton.setMaximumSize(new Dimension(160, 40));
        startButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        startButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                startButton.setBackground(new Color(0x5ab7fa));
            }
            public void mouseExited(MouseEvent evt) {
                startButton.setBackground(new Color(0x6dbdf7));
            }
        });

        startButton.addActionListener(e -> parentFrame.showInputFormPanel());

        // Add a footer
        JLabel footer = new JLabel("Created by Peter, Saleem, and Shko ", SwingConstants.CENTER);
        footer.setFont(new Font("SansSerif", Font.ITALIC, 12));
        footer.setForeground(Color.GRAY);
        footer.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add everything to panel
        add(title);
        add(subtitle);
        add(descriptionPanel);
        add(Box.createVerticalGlue());
        add(Box.createVerticalStrut(15));
        add(startButton);
        add(Box.createVerticalStrut(20));
        add(footer);
    }
}