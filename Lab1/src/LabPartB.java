import javax.swing.*;
import java.awt.event.ActionListener;

public class LabPartB extends LabConcurrency1 {

    public  void setup() {
        JFrame win = setupWindow();
        JPanel panel = new JPanel();

        JSlider slider1 = new JSlider(0,100,0);
        JSlider slider2 = new JSlider(0,100,0);
        JButton btnStart1 = new JButton("Start1");
        JButton btnStart2 = new JButton("Start2");

        JButton btnStop1 = new JButton("Stop1");
        JButton btnStop2 = new JButton("Stop2");

        JTextArea textArea1 = new JTextArea();
        JTextArea textArea2 = new JTextArea();
        textArea1.setSize(10, 10);
        textArea2.setSize(10, 10);

        panel.add(btnStart1);
        panel.add(btnStart2);
        panel.add(slider1);
        panel.add(slider2);
        panel.add(textArea1);
        panel.add(textArea2);
        panel.add(btnStop1);
        panel.add(btnStop2);

        setupStartButton(btnStart1, slider1, textArea1, btnStop2);
        setupStartButton(btnStart2, slider2, textArea2, btnStop1);

        setupStopButton(btnStop1, btnStop2);
        setupStopButton(btnStop2, btnStop1);

        win.setContentPane(panel);
        win.setVisible(true);
    }

    private void setupStartButton(JButton btnStart, JSlider slider, JTextArea textArea, JButton buttonToBlock) {
        btnStart.addActionListener(
                createStartButtonActionListener(slider, textArea, buttonToBlock)
        );
    }

    private void setupStopButton(JButton btnStop, JButton blockedButton) {
        btnStop.addActionListener(
                createStopButtonActionListener(blockedButton)
        );
    }

    private ActionListener createStartButtonActionListener(JSlider slider, JTextArea textArea, JButton buttonToBlock) {
        return e -> {
            if (semaphore == 0) {
                return;
            }
            semaphore = 0;
            new Thread(createSliderRunnable(slider, textArea, FIRST_VALUE, LabPart.PART_B)).start();
            buttonToBlock.setEnabled(false);
        };
    }

    private ActionListener createStopButtonActionListener(JButton blockedButton) {
        return e -> {
            blockedButton.setEnabled(true);
            semaphore = 1;
        };
    }
}
