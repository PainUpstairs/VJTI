import java.util.*;
import java.util.Random;

class time {
    private int arrival_t;
    private int burst_t;
    private int n;
    private int priority;

    public time(int a, int b, int nn) {
        this.arrival_t = a;
        this.burst_t = b;
        this.n = nn;
    }

    public time(int a, int b, int nn, int p) {
        this.arrival_t = a;
        this.burst_t = b;
        this.n = nn;
        this.priority = p;
    }

    // To access the variables
    public int atime() {
        return arrival_t;
    }

    public int btime() {
        return burst_t;
    }

    public int pNo() {
        return n;
    }

    public int pr() {
        return priority;
    }
}

public class Schedulers {

    // To print the numbers aesthetically
    static StringBuilder setw(int n, int z) {
        StringBuilder str = new StringBuilder();
        String blank = " ";
        String val = String.valueOf(z);
        int length = val.length();
        int diff = n - length;

        for (int i = 0; i < diff; i++) {
            str.append(blank);
        }

        str.append(val);

        return str;
    }

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        Random rand = new Random();

        // Gnatt Chart
        StringBuilder GanttC = new StringBuilder();
        GanttC.append("|");

        System.out.print(
                "0 for Non Preemptive SJF \n1 for SJF Preemptive\n2 for Round Robin \n3 for Priority \n4 for FCFS\nEnter: ");
        int mode = input.nextInt();
        System.out.println("");

        // Time Quantum
        int TQ = 0;
        if (mode == 2) {
            System.out.print("Enter Time Quantum: ");
            TQ = input.nextInt();
        }

        System.out.print("Enter the number of processess: ");
        int num = input.nextInt();

        time[] p = new time[num];
        // to keep record of the original data
        time[] f = new time[num];

        int lar_highest = 1;
        // For inputs
        // for Priority
        if (mode == 3) {

            System.out.println("For largest number has the highest priority enter 1 else 0: ");
            lar_highest = input.nextInt();

            for (int i = 0; i < num; i++) {
                int a = rand.nextInt(10);
                int b = rand.nextInt(10) + 1;
                int pri = rand.nextInt(10);
                p[i] = new time(a, b, (i + 1), pri);
                f[i] = new time(a, b, (i + 1), pri);
            }
        }
        // For others
        else {
            for (int i = 0; i < num; i++) {
                int a = rand.nextInt(10);
                int b = rand.nextInt(10) + 1;
                p[i] = new time(a, b, (i + 1));
                f[i] = new time(a, b, (i + 1));
            }
        }

        // Sorting acc to Arrival Time
        for (int i = 0; i < num; i++) {
            for (int j = i; j < num; j++) {
                if (p[i].atime() > p[j].atime()) {
                    int x = 0, y = 0, z = 0;
                    time temp = new time(x, y, z);
                    temp = p[i];
                    p[i] = p[j];
                    p[j] = temp;
                }
            }
        }

        // For Ready Queue
        if (mode != 2) {
            System.out.print("Ready Queue: |");
            for (int i = 0; i < num; i++) {
                System.out.print(" P" + p[i].pNo() + " |");
            }
            System.out.println("");
        }

        // since class variables cant be overwritten we will have to make another
        // array for the burst time value and make it dance with the sorting too
        int[] tb = new int[num];
        for (int i = 0; i < num; i++) {
            tb[i] = p[i].btime();
        }

        // Completion Time
        int[] compl_t = new int[num];
        // To store the first time a process got the cpu
        int[] r = new int[num];

        // to store the first time the process got the CPU ( this is for one-time-use
        // switch)
        boolean[] bol = new boolean[num];

        // Number of completed process
        int NPC = 0;

        // Run Time ( at the moment )
        int RT = p[0].atime();

        int temp_getId = 0;

        do {
            int RQ = 0;
            int min = 0;
            int getID = 0;
            int idleCPU = 0;
            int hp_id = NPC;

            // excluding Round Robin
            if (mode != 2) {
                // To check processess in RQ
                for (int j = 0; j < num; j++) {
                    if (RT >= p[j].atime()) {
                        RQ++;
                    }
                }

                // to check if cpu is idle
                for (int j = NPC; j < num; j++) {
                    if (RT >= p[j].atime()) {
                        idleCPU++;
                    }
                }
                if (idleCPU == 0) {
                    RT = p[NPC].atime();
                }

                // only SJF
                if (mode == 0 || mode == 1) {
                    // to find the minimum burst time
                    min = p[NPC].btime();
                    getID = p[NPC].pNo();
                    for (int j = NPC; j < RQ; j++) {
                        if (min > tb[j]) {
                            min = tb[j];
                            getID = p[j].pNo();
                        }
                    }

                    // For Gantt Chart
                    if (getID != temp_getId) {
                        String s = String.valueOf(getID);
                        GanttC.append(" P" + s + " |");
                        temp_getId = getID;
                    }
                } else if (mode == 4) {
                    RT = RT + tb[NPC];
                    for (int j = 0; j < num; j++) {
                        if (p[NPC].pNo() == f[j].pNo()) {
                            compl_t[j] = RT;
                        }
                    }
                    tb[NPC] = 0;
                }
                // Priority
                else {
                    // to find highest priority
                    if (lar_highest == 1) {
                        int highestP = p[NPC].pr();
                        for (int j = NPC; j < RQ; j++) {
                            if (highestP <= p[j].pr()) {
                                highestP = p[j].pr();
                                getID = p[j].pNo();
                                hp_id = j;
                            }
                        }
                    } else {
                        int highestP = p[NPC].pr();
                        for (int j = NPC; j < RQ; j++) {
                            if (highestP >= p[j].pr()) {
                                highestP = p[j].pr();
                                getID = p[j].pNo();
                                hp_id = j;
                            }
                        }
                    }

                    // For Gantt Chart
                    if (getID != temp_getId) {
                        String s = String.valueOf(getID);
                        GanttC.append(" P" + s + " |");
                        temp_getId = getID;
                    }
                }

                // NON-PRREEMPTIVE //
                if (mode == 0) {
                    for (int j = 0; j < num; j++) {
                        if (getID == p[j].pNo()) {
                            tb[j] = 0;
                        }
                        if (getID == f[j].pNo()) {
                            RT = RT + min;
                            compl_t[j] = RT;
                        }
                    }
                }

                // PREEMPTIVE //
                else if (mode == 1) {
                    // reducing the smallest burst time by 1
                    for (int j = NPC; j < RQ; j++) {
                        if (min == tb[j]) {
                            tb[j]--;
                            RT++;
                            for (int k = 0; k < num; k++) {
                                if (f[k].pNo() == p[j].pNo()) {
                                    if (bol[j] == false) {
                                        r[k] = RT - 1;
                                        bol[j] = true;
                                    }
                                }
                            }
                            break;
                        }
                    }

                    for (int j = NPC; j < num; j++) {
                        if (tb[j] == 0) {

                            // to set the completion time of the process whose burst time is now 0
                            for (int k = 0; k < num; k++) {
                                if (f[k].pNo() == getID) {
                                    compl_t[k] = RT;
                                }
                            }
                        }
                    }
                }
                // priority //

                else if (mode == 3) {
                    // record of first cpu use time by a process
                    for (int j = 0; j < num; j++) {
                        if (p[hp_id].pNo() == f[j].pNo()) {
                            if (bol[hp_id] == false) {
                                r[j] = RT;
                                bol[hp_id] = true;
                            }
                        }
                    }
                    tb[hp_id]--;
                    RT++;

                    // Priority Completion Time
                    if (tb[hp_id] == 0) {
                        for (int j = 0; j < num; j++) {
                            if (p[hp_id].pr() == f[j].pr()) {
                                compl_t[j] = RT;
                            }
                        }
                    }
                }

                for (int j = NPC; j < num; j++) {
                    if (tb[j] == 0) {
                        // to switch the substitute array of burstime
                        int st = tb[j];
                        // boolean switch
                        Boolean bt = bol[j];
                        // to switch the completed process
                        int w = 0, x = 0, y = 0, z = 0;
                        time temp = new time(w, x, y, z);
                        temp = p[j];
                        for (int k = j; k > NPC; k--) {
                            tb[k] = tb[k - 1];
                            bol[k] = bol[k - 1];
                            p[k] = p[k - 1];
                        }
                        p[NPC] = temp;
                        tb[NPC] = st;
                        bol[NPC] = bt;

                        NPC++;
                    }
                }
            }

            // RR //
            else {
                // to check if cpu is idle
                for (int j = NPC; j < num; j++) {
                    if (RT >= p[j].atime()) {
                        idleCPU++;
                    }
                }
                if (idleCPU == 0) {
                    RT = p[NPC].atime();
                }

                // For Response Time
                for (int j = 0; j < num; j++) {
                    if (f[j].pNo() == p[NPC].pNo()) {
                        if (bol[NPC] == false) {
                            r[j] = RT;
                            bol[NPC] = true;
                        }
                    }
                }

                // For Gantt Chart
                getID = p[NPC].pNo();
                if (getID != temp_getId) {
                    String s = String.valueOf(getID);
                    GanttC.append(" P" + s + " |");
                    temp_getId = getID;
                }

                // Process in CPU
                if (tb[NPC] >= TQ) {
                    tb[NPC] = tb[NPC] - TQ;
                    RT = RT + TQ;
                } else {
                    RT = RT + tb[NPC];
                    tb[NPC] = 0;
                }

                // To check processess in RQ after running a process
                for (int j = 0; j < num; j++) {
                    if (RT >= p[j].atime()) {
                        RQ++;
                    }
                }

                // If the said process is completed
                if (tb[NPC] == 0) {
                    for (int k = 0; k < num; k++) {
                        if (f[k].pNo() == p[NPC].pNo()) {
                            compl_t[k] = RT;
                        }
                    }
                    NPC++;
                }
                // If the said process is not completed make it wait in the queue
                else {

                    int x = 0, y = 0, z = 0;
                    time temp = new time(x, y, z);
                    temp = p[NPC];
                    int temp_tb = tb[NPC];
                    boolean temp_bol = bol[NPC];

                    for (int k = NPC; k < (RQ - 1); k++) {
                        tb[k] = tb[k + 1];
                        p[k] = p[k + 1];
                        bol[k] = bol[k + 1];
                    }
                    p[RQ - 1] = temp;
                    tb[RQ - 1] = temp_tb;
                    bol[RQ - 1] = temp_bol;
                }

            }

        } while (NPC < num);

        // Ready Queue of RR
        if (mode == 2) {
            System.out.println("Ready Queue: " + GanttC);
        }

        // Gant chart
        if (mode == 4) {
            System.out.print("Gantt Chart: |");
            for (int j = 0; j < num; j++) {
                System.out.print(" P" + p[j].pNo() + " |");
            }
            System.out.println("");
        } else {
            System.out.println("Gantt Chart: " + GanttC);
        }

        // TAT
        int[] tat = new int[num];
        // wait time
        int[] wt = new int[num];
        for (int j = 0; j < num; j++) {
            tat[j] = compl_t[j] - f[j].atime();
            wt[j] = tat[j] - f[j].btime();
        }

        // NON-PEEMPTIVE
        if (mode == 0 || mode == 4) {
            System.out.println("Pro.ID\tAT\tBT\tCT\tTAT\tWT");

            for (int z = 0; z < num; z++) {
                System.out.println("P" + (z + 1) + "\t" + setw(2, f[z].atime()) + "\t" + setw(2, f[z].btime()) + "\t"
                        + setw(2, compl_t[z]) + "\t"
                        + setw(2, tat[z]) + "\t" + setw(2, wt[z]) + "\t");
            }
        }
        // PREEMPTIVE + Round robin + Priority
        else {
            // response time
            int[] resp = new int[num];

            for (int j = 0; j < num; j++) {
                resp[j] = r[j] - f[j].atime();
            }
            // Priority
            if (mode == 3) {
                System.out.println("Pro.ID\tAT\tBT\tPri\tCT\tTAT\tWT\tRT");

                for (int z = 0; z < num; z++) {
                    System.out.println("P" + (z + 1) + "\t" + setw(2, f[z].atime()) + "\t" + setw(2, f[z].btime())
                            + "\t" + setw(2, f[z].pr())
                            + "\t" + setw(2, compl_t[z]) + "\t"
                            + setw(2, tat[z]) + "\t" + setw(2, wt[z]) + "\t" + setw(2, resp[z]));
                }
            } else {
                System.out.println("Pro.ID\tAT\tBT\tCT\tTAT\tWT\tRT");

                for (int z = 0; z < num; z++) {
                    System.out.println(
                            "P" + (z + 1) + "\t" + setw(2, f[z].atime()) + "\t" + setw(2, f[z].btime()) + "\t"
                                    + setw(2, compl_t[z]) + "\t"
                                    + setw(2, tat[z]) + "\t" + setw(2, wt[z]) + "\t" + setw(2, resp[z]));
                }
            }
        }
        System.out.println("");
        input.close();
    }
}
