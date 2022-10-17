package port;

public class ShipRunnable implements Runnable {
    private ShipTask task;
    private Terminal terminal;

    public void setTerminal(Terminal terminal) {
        this.terminal = terminal;
    }

    public void setTask(ShipTask task) {
        this.task = task;
    }

    @Override
    public void run() {
        while (terminal.isClosed() || task == ShipTask.RECEIVE && terminal.isEmpty()) {
            System.out.println(Thread.currentThread().getName() + " waiting for the jetty to open " + terminal.isEmpty());
            pauseWork();
        }

        if (task == ShipTask.PASS) {
            terminal.receiveContainer();
        } else {
            terminal.loadShipContainer();
        }
    }

    public void pauseWork() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
