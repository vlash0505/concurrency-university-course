package ui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;

import java.awt.BorderLayout;
import java.awt.Dimension;

public class Frame extends JFrame {
    private static final int FRAME_WIDTH = 720;
    private static final int CONTROL_PANEL_HEIGHT = 75;
    private GridPanel grid;
    private JPanel controlPanel;

    public void setup() {
        JPanel allContent = new JPanel(new BorderLayout());
        this.grid = new GridPanel();
        this.controlPanel = new JPanel(new BorderLayout());

        startButtonInit();

        controlPanel.setPreferredSize(new Dimension(FRAME_WIDTH, CONTROL_PANEL_HEIGHT));

        allContent.add(grid,BorderLayout.CENTER);
        allContent.add(controlPanel,BorderLayout.SOUTH);

        this.add(allContent);
        this.setResizable(false);
        this.setVisible(true);
        this.setTitle("Winnie-the-pooh problem");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
    }

    public void startButtonInit() {
        JButton startButton = new JButton("Start");
        startButton.addActionListener(e -> grid.performSearchAnimation());
        controlPanel.add(startButton);
    }
}
