import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.List;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GanttChartPanel extends JPanel {
    private final List<RoundRobinScheduler.GanttEntry> ganttEntries;
    private final int blockWidth = 40; // Width per time unit (scales duration)
    private final int BAR_HEIGHT = 40; // Height of each Gantt bar
    private final int VERTICAL_PADDING_TOP = 20; // Padding above the bars
    private final int VERTICAL_PADDING_BOTTOM = 30; // Padding below the bars (for time markers)
    private final int HORIZONTAL_PADDING_LEFT = 20; // Padding on the left for the chart
    private final int HORIZONTAL_PADDING_RIGHT = 20; // Padding on the right for the chart

    public GanttChartPanel(List<RoundRobinScheduler.GanttEntry> ganttEntries) {
        this.ganttEntries = ganttEntries;
        setBackground(new Color(0xE8F5E9)); // Light green background
    }

    @Override
    public Dimension getPreferredSize() {
        if (ganttEntries == null || ganttEntries.isEmpty()) {
            return new Dimension(800, BAR_HEIGHT + VERTICAL_PADDING_TOP + VERTICAL_PADDING_BOTTOM); // Default size
        }

        // Find the total time elapsed (end time of the last entry)
        int totalTime = 0;
        if (!ganttEntries.isEmpty()) {
            // Ensure processes are sorted by end time for calculating total duration
            // This is already handled by the scheduler's logic ensuring sequential blocks
            totalTime = ganttEntries.get(ganttEntries.size() - 1).getEndTime();
        }

        // Calculate the required width based on total time, blockWidth, and horizontal padding
        int requiredWidth = totalTime * blockWidth + HORIZONTAL_PADDING_LEFT + HORIZONTAL_PADDING_RIGHT;
        int preferredHeight = BAR_HEIGHT + VERTICAL_PADDING_TOP + VERTICAL_PADDING_BOTTOM;

        // Ensure a minimum width to avoid excessively small charts, even if total time is small
        return new Dimension(Math.max(600, requiredWidth), preferredHeight);
    }

    @Override
    protected void paintComponent(Graphics g) { 
        super.paintComponent(g);

        if (ganttEntries == null || ganttEntries.isEmpty()) {
            g.setColor(Color.GRAY);
            g.setFont(new Font("SansSerif", Font.PLAIN, 14));
            String msg = "Gantt Chart will appear here after simulation.";
            FontMetrics fm = g.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(msg)) / 2;
            int y = (getHeight() / 2) + (fm.getAscent() / 2);
            g.drawString(msg, x, y);
            return;
        }

        int currentX = HORIZONTAL_PADDING_LEFT; // Start drawing from the left padding
        int barY = VERTICAL_PADDING_TOP;      // Y position for the top of the bars

        g.setFont(new Font("SansSerif", Font.PLAIN, 12)); // Font for time markers
        FontMetrics timeMetrics = g.getFontMetrics();

        for (RoundRobinScheduler.GanttEntry entry : ganttEntries) {
            int duration = entry.getEndTime() - entry.getStartTime();
            int width = duration * blockWidth;

            // Draw the box
            g.setColor(new Color(0xAED581)); // light green
            g.fillRect(currentX, barY, width, BAR_HEIGHT);
            g.setColor(Color.BLACK);
            g.drawRect(currentX, barY, width, BAR_HEIGHT);

            // Draw the process ID in the middle
            g.setFont(new Font("SansSerif", Font.BOLD, 14));
            FontMetrics processIdMetrics = g.getFontMetrics();
            int textWidth = processIdMetrics.stringWidth(entry.getProcessId());
            g.drawString(entry.getProcessId(), currentX + (width - textWidth) / 2, barY + BAR_HEIGHT / 2 + (processIdMetrics.getAscent() / 2) - 2);

            // Draw start time marker (below the bar, considering padding)
            String startTimeStr = String.valueOf(entry.getStartTime());
            int startTimeWidth = timeMetrics.stringWidth(startTimeStr);
            // Position startTime: slightly to the left of the bar's start (currentX)
            g.drawString(startTimeStr, currentX - (startTimeWidth / 2), barY + BAR_HEIGHT + timeMetrics.getHeight());

            currentX += width; // Move currentX for the next block
        }

        // Draw the last end time marker (below the last bar's end)
        RoundRobinScheduler.GanttEntry last = ganttEntries.get(ganttEntries.size() - 1);
        String endTimeStr = String.valueOf(last.getEndTime());
        int endTimeWidth = timeMetrics.stringWidth(endTimeStr);
        // Position endTime: slightly to the left of currentX (which is the end of the last bar)
        g.drawString(endTimeStr, currentX - (endTimeWidth / 2), barY + BAR_HEIGHT + timeMetrics.getHeight());
    }
}