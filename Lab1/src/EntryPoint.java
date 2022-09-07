public class EntryPoint {

    public static void main(String[] args) {
        LabConcurrency1 taskA = new LabPartA();
        //LabConcurrency1 taskB = new LabPartB();

        taskA.setup();
        //taskB.setup();
    }

}
