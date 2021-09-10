import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
        System.out.println("Button pressed: " + e.getActionCommand());
        ((JButton) e.getSource()).setEnabled(false);
    }
}
