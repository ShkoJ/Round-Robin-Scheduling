import java.awt.CardLayout;
import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

@SuppressWarnings("serial")
public class AppFrame extends JFrame {

    private JPanel cardPanel; // Will hold all the cards
    private CardLayout cardLayout; // Responsible for switching between the different panels within cardPanel

    public static final String WELCOME_PANEL = "WelcomePanel";
    public static final String INPUT_FORM_PANEL = "InputFormPanel";
    public static final String OUTPUT_PANEL = "OutputPanel";

    public AppFrame() {
        super("Round Robin Scheduler");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500); // Initial size to accommodate output more comfortably
        setLocationRelativeTo(null); // Center the window

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Create instances of your new panels
        WelcomePanel welcomePanel = new WelcomePanel(this);
        InputFormPanel inputFormPanel = new InputFormPanel(this);
        // OutputPanel is created dynamically when needed (since it requires data)

        cardPanel.add(welcomePanel, WELCOME_PANEL);
        cardPanel.add(inputFormPanel, INPUT_FORM_PANEL);

        add(cardPanel);

        // Show the welcome screen initially
        cardLayout.show(cardPanel, WELCOME_PANEL);
    }

    public void showWelcomeScreen() {
        cardLayout.show(cardPanel, WELCOME_PANEL);
        setSize(550, 400); // Adjust size for welcome screen
        revalidate();
        repaint();
    }

    public void showInputFormPanel() { 
        cardLayout.show(cardPanel, INPUT_FORM_PANEL);
        setSize(600, 450); // Adjust size for input form
        setLocationRelativeTo(null); // Center the window
        revalidate();
        repaint();
    }

    public void showOutputPanel(Object[][] processData, SimulationResult result) {
        // Remove previous output panel if it exists, to ensure a fresh one
        Component[] components = cardPanel.getComponents();
        for (Component comp : components) {
            if (comp.getName() != null && comp.getName().equals(OUTPUT_PANEL)) {
                cardPanel.remove(comp);
                break;
            }
        }
        OutputPanel outputPanel = new OutputPanel(this, processData, result);
        outputPanel.setName(OUTPUT_PANEL); // Set a name to identify it later
        cardPanel.add(outputPanel, OUTPUT_PANEL);
        cardLayout.show(cardPanel, OUTPUT_PANEL);
        setSize(900, 750); // Adjust size for output screen
        setLocationRelativeTo(null); // Center the window

        revalidate(); 
        repaint();
    }

    // Main method to run the application
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
            // Fallback to default if Nimbus isn't available
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        SwingUtilities.invokeLater(() -> new AppFrame().setVisible(true));
    }
}