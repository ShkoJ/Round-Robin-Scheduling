import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class RoundRobinScheduler {

    // An inner class representing the individual processes
    public static class Process {
        public String id;
        public int arrivalTime;
        public int burstTime;
        public int remainingTime; // How much burst time is remaining
        public int completionTime;
        public int turnaroundTime; // CT - AT
        public int waitingTime; // TAT - BT

        public Process(int pId, int arrivalTime, int burstTime) {
            this.id = "P" + pId;
            this.arrivalTime = arrivalTime;
            this.burstTime = burstTime;
            this.remainingTime = burstTime; // Initially, full burstTime length is remaining
        }

        // Add getters
        public String getId() { return id; }
        public int getArrivalTime() { return arrivalTime; }
        public int getBurstTime() { return burstTime; }
        public int getRemainingTime() { return remainingTime; }
        public int getCompletionTime() { return completionTime; }
        public int getTurnaroundTime() { return turnaroundTime; }
        public int getWaitingTime() { return waitingTime; }
    }

    // An inner class to keep track of process execution order for the Gantt chart
    public static class GanttEntry {
        public String processId;
        public int startTime;
        public int endTime;

        public GanttEntry(String processId, int startTime, int endTime) {
            this.processId = processId;
            this.startTime = startTime;
            this.endTime = endTime;
        }

        // Add getters
        public String getProcessId() { return processId; }
        public int getStartTime() { return startTime; }
        public int getEndTime() { return endTime; }
    }

    // Method that returns a SimulationResult object containing all the calculations and Gantt chart
    public static SimulationResult runRR(List<Process> inputProcesses, int quantumTime) {

        List<GanttEntry> currentGanttChart = new ArrayList<>(); // List to keep track of Gantt chart entries
        List<Process> completed = new ArrayList<>(); // Will contain all the processes that are done executing
        Queue<Process> readyQueue = new LinkedList<>(); // Represents the queue of processes waiting to be executed

        // Make a deep copy of input processes to avoid modifying the original list
        List<Process> processesToSchedule = new ArrayList<>();
        for (Process p : inputProcesses) {
            processesToSchedule.add(new Process(Integer.parseInt(p.id.substring(1)), p.arrivalTime, p.burstTime));
        }

        // Sort processes by arrival time initially
        processesToSchedule.sort(Comparator.comparingInt(p -> p.arrivalTime));

        int time = 0; // Current system time
        int index = 0; // Index for processesToSchedule

        // Loop until all processes are in the completed list
        while (completed.size() < processesToSchedule.size()) {

            // Add new arrivals to the ready queue
            while (index < processesToSchedule.size() && processesToSchedule.get(index).arrivalTime <= time) {
                readyQueue.add(processesToSchedule.get(index));
                index++;
            }

            // If no processes got added, none are ready to enter the queue, leading to idle CPU
            if (readyQueue.isEmpty()) {
                time++; // Idle CPU
                continue;
            }

            Process current = readyQueue.poll(); // Round Robin: grab from front of the queue

            // Run the process for quantum or less
            int runTime = Math.min(current.remainingTime, quantumTime);

            // Add instance to the Gantt chart list
            currentGanttChart.add(new GanttEntry(current.id, time, time + runTime));

            // Increment the time-line and decrement remainingTime by the runTime
            time += runTime;
            current.remainingTime -= runTime;

            // Add newly arrived processes during this run time
            while (index < processesToSchedule.size() && processesToSchedule.get(index).arrivalTime <= time) {
                readyQueue.add(processesToSchedule.get(index));
                index++;
            }

            // If done, calculate the desired fields
            if (current.remainingTime == 0) {
                current.completionTime = time;
                current.turnaroundTime = current.completionTime - current.arrivalTime;
                current.waitingTime = current.turnaroundTime - current.burstTime;
                completed.add(current);
            } else {
                // Not done yet, put it back in the queue
                readyQueue.add(current);
            }
        }

        // Calculate averages for the SimulationResult
        double totalTAT = 0;
        double totalWT = 0;
        for (Process p : completed) {
            totalTAT += p.turnaroundTime;
            totalWT += p.waitingTime; 
        }
        double avgTAT = completed.isEmpty() ? 0 : totalTAT / completed.size();
        double avgWT = completed.isEmpty() ? 0 : totalWT / completed.size();

        // Return all results encapsulated in SimulationResult
        return new SimulationResult(completed, currentGanttChart, avgTAT, avgWT); 
    }
}