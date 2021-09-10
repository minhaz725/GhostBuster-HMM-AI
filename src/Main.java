import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class Main {
    static boolean catchFlag = false;
    static int attemptCount = 0;
    public static void replace(JFrame frame,Board board,JPanel containerPanel,JPanel buttonPanel,int M,int N,ActionListener listener){
        System.out.println("In replace()!");
        frame.remove(containerPanel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        containerPanel = new JPanel();
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(M, N));

        board.updateDistanceMatrix();
        board.updateProbMatrix();
        board.printProbMatrix();
        board.setGridProb(frame,containerPanel,buttonPanel,listener);
        buttonPanel.setPreferredSize(new Dimension(800, 600));

        board.moveGhost();
        System.out.println(board.getGhost_x()+" "+board.getGhost_y());

        JButton advanceTimeBtn = new JButton("Advance Time");
        JButton catchBtn = new JButton("Catch Ghost");
        JButton exitBtn = new JButton("Exit");
        JPanel finalButtonPanel = buttonPanel;
        JPanel finalContainerPanel = containerPanel;
        advanceTimeBtn.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                System.out.println("Advance Time Button clicked!");
                catchFlag = false;
                replace(frame,board, finalContainerPanel, finalButtonPanel,M,N,listener);
            }
        });
        advanceTimeBtn.setBounds(50,100,95,60);
        advanceTimeBtn.setFont(advanceTimeBtn.getFont().deriveFont(Font.BOLD, 20));
        catchBtn.setBounds(50,100,95,60);
        catchBtn.setFont(advanceTimeBtn.getFont().deriveFont(Font.BOLD, 19));
        exitBtn.setBounds(50,100,95,60);
        exitBtn.setFont(advanceTimeBtn.getFont().deriveFont(Font.BOLD, 19));

        catchBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Catch Button Clicked!");
                catchFlag = true;
            }
        });
        exitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Exit Button Clicked!");
                frame.dispose();
            }
        });
        Box box = Box.createVerticalBox();
        box.add(advanceTimeBtn);
        box.add(Box.createVerticalStrut(20));
        box.add(catchBtn);
        box.add(Box.createVerticalStrut(20));
        box.add(exitBtn);
        containerPanel.add(box);
        containerPanel.add(buttonPanel);
        containerPanel.revalidate();
        containerPanel.repaint();


        frame.getContentPane().add(containerPanel);
        SwingUtilities.updateComponentTreeUI(frame);
        frame.invalidate();
        frame.validate();
        frame.repaint();
        frame.setTitle("Ghost Hunter");
        frame.pack();
        frame.setVisible(true);
        System.out.println("Frame refreshed!");

    }
    public static void main(String[] args) throws Exception {
        int M,N;
        M=9;
        N=9;
        Board board = new Board(M,N);
        board.printProbMatrix();

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel buttonPanel = new JPanel();
        JPanel containerPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(M, N));
        ActionListener listener = new MyListener();
        board.setGridProb(frame,containerPanel,buttonPanel,listener);

        buttonPanel.setPreferredSize(new Dimension(800, 600));

        JButton advanceTimeBtn = new JButton("Advance Time");
        JButton catchBtn = new JButton("Catch Ghost");
        JButton exitBtn = new JButton("Exit");

        advanceTimeBtn.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                System.out.println("Advance Time Button clicked!");
                catchFlag = false;
                replace(frame,board,containerPanel,buttonPanel,M,N,listener);
            }
        });
        catchBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Catch Button Clicked!");
                catchFlag = true;
            }
        });
        exitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Exit Button Clicked!");
                frame.dispose();
            }
        });
        advanceTimeBtn.setBounds(50,100,95,60);
        advanceTimeBtn.setFont(advanceTimeBtn.getFont().deriveFont(Font.BOLD, 20));
        catchBtn.setBounds(50,100,95,60);
        catchBtn.setFont(advanceTimeBtn.getFont().deriveFont(Font.BOLD, 19));
        exitBtn.setBounds(50,100,95,60);
        exitBtn.setFont(advanceTimeBtn.getFont().deriveFont(Font.BOLD, 19));


        Box box = Box.createVerticalBox();
        //box.add(attemptLbl);
        box.add(Box.createVerticalStrut(20));
        box.add(advanceTimeBtn);
        box.add(Box.createVerticalStrut(20));
        box.add(catchBtn);
        box.add(Box.createVerticalStrut(20));
        box.add(exitBtn);
        containerPanel.add(box);
        containerPanel.add(buttonPanel);

        frame.getContentPane().add(containerPanel);
        frame.setTitle("Ghost Hunter");
        frame.pack();
        frame.setVisible(true);
    }
}