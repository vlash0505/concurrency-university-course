import javax.swing.*;

public abstract class LabConcurrency1 {

    //semaphore; 0 - can pass, 1 - can't
    protected int semaphore = 0;
    public static int FIRST_VALUE = 90;
    public static int SECOND_VALUE = 10;

    public abstract void setup();

    public JFrame setupWindow() {
        JFrame win = new JFrame();
        win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        win.setSize(400,500);

        return win;
    }

    public Runnable createSliderRunnable(JSlider slider, JTextArea textArea, int sliderValue, LabPart labPart) {
        return () -> {
            synchronized(slider) {
                while(slider.getValue() != sliderValue) {
                    if (labPart == LabPart.PART_B && semaphore == 1) {
                        break;
                    }
                    final boolean isLess = slider.getValue() < sliderValue;
                    slider.setValue(isLess ? slider.getValue() + 1 : slider.getValue() - 1);
                    textArea.setText(String.valueOf(slider.getValue()));
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                semaphore = 1;
            }
        };
    }
}
