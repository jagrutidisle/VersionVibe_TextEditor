import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class LandingPage {
    public static void main(String[] args) {
        // Creating a frame for the landing page
        JFrame frame = new JFrame("Landing Page");
        frame.setSize(2000, 2000);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Adding an image or label to the frame (you can change the image path here)
        JLabel label = new JLabel(new ImageIcon("logo.jpg"));
        frame.add(label, BorderLayout.CENTER);
        
        frame.setVisible(true);

        // Timer to wait for 3 seconds
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Close the landing page and open the VersionVibe
                frame.setVisible(false); // Hide the landing page
                
                // Open VersionVibe (assuming VersionVibe has a main method)
                try {
                    VersionVibe.main(args); // Calls main method of VersionVibe
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 3000); // 3 seconds delay
    }
}
