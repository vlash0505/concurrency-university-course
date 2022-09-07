import javax.swing.*;

public class LabPartA extends LabConcurrency1 {

    public void setup() {
        setupPanel();
    }

    private void setupPanel() {
        JFrame win = setupWindow();
        JPanel panel = new JPanel();

        JSlider slider = new JSlider(0,100,0);
        JButton btn = new JButton("Start");
        JTextArea textArea1 = new JTextArea();
        JTextArea textArea2 = new JTextArea();

        panel.add(btn);
        panel.add(slider);
        panel.add(textArea1);
        panel.add(textArea2);

        setupButton(btn, slider, textArea1, textArea2);

        win.setContentPane(panel);
        win.setVisible(true);
    }

    private void setupButton(JButton btn, JSlider slider, JTextArea textArea1, JTextArea textArea2) {
        btn.addActionListener(
                e -> {
                    setupThread(slider, textArea1, FIRST_VALUE).start();
                    setupThread(slider, textArea2, SECOND_VALUE).start();
                    btn.setEnabled(false);
                }
        );
    }

    private Thread setupThread(JSlider slider, JTextArea textArea, int sliderValue) {
        return new Thread(createSliderRunnable(slider, textArea, sliderValue, LabPart.PART_A));
    }

}
