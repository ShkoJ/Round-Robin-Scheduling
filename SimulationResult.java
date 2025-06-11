import java.util.List;

public class SimulationResult {
    private final List<RoundRobinScheduler.Process> completedProcesses;
    private final List<RoundRobinScheduler.GanttEntry> ganttEntries;
    private final double averageTurnaroundTime;
    private final double averageWaitingTime;

    public SimulationResult(List<RoundRobinScheduler.Process> completedProcesses,
                            List<RoundRobinScheduler.GanttEntry> ganttEntries,
                            double averageTurnaroundTime, double averageWaitingTime) {
        this.completedProcesses = completedProcesses;
        this.ganttEntries = ganttEntries;
        this.averageTurnaroundTime = averageTurnaroundTime;
        this.averageWaitingTime = averageWaitingTime;
    }

    public List<RoundRobinScheduler.Process> getCompletedProcesses() {
        return completedProcesses;
    }

    public List<RoundRobinScheduler.GanttEntry> getGanttEntries() {
        return ganttEntries;
    }

    public double getAverageTurnaroundTime() {
        return averageTurnaroundTime;
    }

    public double getAverageWaitingTime() {
        return averageWaitingTime;
    }
}