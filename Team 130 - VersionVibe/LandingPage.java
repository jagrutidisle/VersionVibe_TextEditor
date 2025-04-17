import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class LandingPage {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Landing Page");
        frame.setSize(2000, 2000);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel label = new JLabel(new ImageIcon("logo.jpg"));
        frame.add(label, BorderLayout.CENTER);
        
        frame.setVisible(true);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                frame.setVisible(false);
                
                try {
                    VersionVibe.main(args); 
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 3000); // 3 seconds delay
    }
}
