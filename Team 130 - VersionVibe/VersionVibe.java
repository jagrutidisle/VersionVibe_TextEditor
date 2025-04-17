import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;

// Main class - gives version control vibes to a simple text editor
public class VersionVibe {
    private JFrame frame;
    private JTextArea textArea;
    private VersionNode currentVersion;
    private Map<String, VersionNode> versionMap = new HashMap<>();
    private JFileChooser fileChooser = new JFileChooser();
    private int fontSize = 20;  // Default font size
    private Color lightBackground = Color.WHITE;
    private Color darkBackground = Color.DARK_GRAY;
    private Color lightText = Color.BLACK;
    private Color darkText = Color.WHITE;
    private Color lightButtonBackground = new Color(238, 238, 238);
    private Color darkButtonBackground = new Color(50, 50, 50);
    
    public static void main(String[] args) {
        // just starting the app on the Event Dispatch Thread
        SwingUtilities.invokeLater(VersionVibe::new);
    }

    // Constructor - sets up the UI and buttons
    public VersionVibe() {
        frame = new JFrame("Versioned Text Editor");
        textArea = new JTextArea(20, 60);
        textArea.setFont(new Font("Arial", Font.PLAIN, fontSize)); // Set default font size
        JScrollPane scrollPane = new JScrollPane(textArea);

        JButton undoButton = new JButton("Undo");
        JButton redoButton = new JButton("Redo");
        JButton saveVersionButton = new JButton("Save Version"); // save current state/version

        // root version = empty text area
        VersionNode root = new VersionNode("", null);
        currentVersion = root;
        versionMap.put(root.hash, root);frame.pack();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        // Ctrl+S shortcut to save version
        textArea.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK), "saveShortcut");
        textArea.getActionMap().put("saveShortcut", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                saveVersion();
            }
        });

        // Button actions
        undoButton.addActionListener(e -> undo());
        redoButton.addActionListener(e -> showRedoDialog());
        saveVersionButton.addActionListener(e -> saveVersion());

        JPanel panel = new JPanel();
        panel.add(undoButton);
        panel.add(redoButton);
        panel.add(saveVersionButton);

        // Menu bar setup
        frame.setJMenuBar(createMenuBar());

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(panel, BorderLayout.SOUTH);

        

    }

    // Creating the File, Edit, and View menu
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        // JMenuItem newTab = new JMenuItem("New Tab");
        JMenuItem open = new JMenuItem("Open");
        JMenuItem save = new JMenuItem("Save");
        JMenuItem print = new JMenuItem("Print");
        // JMenuItem close = new JMenuItem("Close Tab");
        JMenuItem exit = new JMenuItem("Exit");

        // newTab.addActionListener(e -> textArea.setText(""));
        open.addActionListener(e -> openFile());
        save.addActionListener(e -> saveToFile());
        print.addActionListener(e -> {
            try {
                textArea.print();
            } catch (Exception ex) {
                ex.printStackTrace(); // very raw, but gets the job done
            }
        });
        // close.addActionListener(e -> textArea.setText(""));
        exit.addActionListener(e -> System.exit(0));

        // fileMenu.add(newTab);
        fileMenu.add(open);
        fileMenu.add(save);
        fileMenu.add(print);
        // fileMenu.add(close);
        fileMenu.add(exit);

        JMenu editMenu = new JMenu("Edit");
        JMenuItem cut = new JMenuItem("Cut");
        JMenuItem copy = new JMenuItem("Copy");
        JMenuItem paste = new JMenuItem("Paste");
        JMenuItem delete = new JMenuItem("Delete");
        JMenuItem find = new JMenuItem("Find");
        JMenuItem replace = new JMenuItem("Replace");

        cut.addActionListener(e -> textArea.cut());
        copy.addActionListener(e -> textArea.copy());
        paste.addActionListener(e -> textArea.paste());
        delete.addActionListener(e -> textArea.replaceSelection(""));
        find.addActionListener(e -> findText());
        replace.addActionListener(e -> replaceText());

        editMenu.add(cut);
        editMenu.add(copy);
        editMenu.add(paste);
        editMenu.add(delete);
        editMenu.add(find);
        editMenu.add(replace);

        JMenu viewMenu = new JMenu("View");
        JMenuItem zoomIn = new JMenuItem("Zoom In");
        JMenuItem zoomOut = new JMenuItem("Zoom Out");

        zoomIn.addActionListener(e -> zoomIn());
        zoomOut.addActionListener(e -> zoomOut());
        JMenuItem darkModeToggle = new JMenuItem("Toggle Dark Mode");
        darkModeToggle.addActionListener(e -> toggleDarkMode());
        viewMenu.add(darkModeToggle);
        viewMenu.add(zoomIn);
        viewMenu.add(zoomOut);

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(viewMenu); // Added View menu

        return menuBar;
    }

    // Zoom In functionality
    private void zoomIn() {
        fontSize += 2;  // Increase font size by 2
        textArea.setFont(new Font("Arial", Font.PLAIN, fontSize));
    }
// Dark mode flag
private boolean isDarkMode = false;private void toggleDarkMode() {
    isDarkMode = !isDarkMode; // Toggle the mode

    // Update the menu bar background and menu item text color
    JMenuBar menuBar = frame.getJMenuBar();
    menuBar.setBackground(isDarkMode ? Color.BLACK : lightBackground); // Black background for menu bar in dark mode

    for (int i = 0; i < menuBar.getMenuCount(); i++) {
        JMenu menu = menuBar.getMenu(i);
        menu.setForeground(isDarkMode ? Color.WHITE : Color.BLACK); // White text for menu items in dark mode
        menu.setBackground(isDarkMode ? Color.BLACK : lightBackground); // Menu background black in dark mode
        for (int j = 0; j < menu.getItemCount(); j++) {
            JMenuItem item = menu.getItem(j);
            item.setForeground(isDarkMode ? Color.WHITE : Color.BLACK); // White text for menu items in dark mode
            item.setBackground(isDarkMode ? Color.BLACK : lightBackground); // Menu item background black in dark mode
        }
    }

    // Update colors for text area (leave as it was before, only change if required)
    textArea.setBackground(isDarkMode ? darkBackground : lightBackground);
    textArea.setForeground(isDarkMode ? Color.WHITE : Color.BLACK); // Black text in light mode, white text in dark mode

    // Update button background and text color
    for (Component component : frame.getComponents()) {
        if (component instanceof JButton) {
            JButton button = (JButton) component;
            button.setBackground(isDarkMode ? darkButtonBackground : lightButtonBackground);
            button.setForeground(isDarkMode ? Color.WHITE : Color.BLACK); // Black text in light mode
        }
    }

    // Refresh the UI
    frame.repaint();
}




    // Zoom Out functionality
    private void zoomOut() {
        fontSize = Math.max(8, fontSize - 2);  // Decrease font size by 2 (min 8px)
        textArea.setFont(new Font("Arial", Font.PLAIN, fontSize));
    }

    // Save a version of the text
    private void saveVersion() {
        String content = textArea.getText();
        if (!content.equals(currentVersion.content)) {
            VersionNode newVersion = new VersionNode(content, currentVersion);
            currentVersion.children.add(newVersion);
            currentVersion = newVersion;
            versionMap.put(newVersion.hash, newVersion);
            saveToFile(newVersion); // also save to file
            JOptionPane.showMessageDialog(frame, "Version saved: " + newVersion.hash);
        }
    }

    // Save to versioned file (just raw text with version ID)
    private void saveToFile(VersionNode version) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("version_" + version.hash + ".txt"))) {
            writer.write("Version: " + version.hash + "\n");
            writer.write("Content:\n" + version.content);
            writer.flush();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error saving version to file.");
        }
    }

    // Go back to previous version
    private void undo() {
        if (currentVersion.parent != null) {
            currentVersion = currentVersion.parent;
            textArea.setText(currentVersion.content);
        } else {
            JOptionPane.showMessageDialog(frame, "No previous version.");
        }
    }

    // Show future versions (branches) and select one
    private void showRedoDialog() {
        if (currentVersion.children.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No future versions.");
            return;
        }

        JDialog dialog = new JDialog(frame, "Select Redo Version", true);
        dialog.setLayout(new BorderLayout());

        DefaultListModel<VersionNode> listModel = new DefaultListModel<>();
        for (VersionNode child : currentVersion.children) {
            listModel.addElement(child);
        }

        JList<VersionNode> versionList = new JList<>(listModel);
        versionList.setCellRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                VersionNode node = (VersionNode) value;
                String display = node.toString() + " | " + getChangeSummary(currentVersion.content, node.content);
                return super.getListCellRendererComponent(list, display, index, isSelected, cellHasFocus);
            }
        });

        JButton selectButton = new JButton("Select");
        selectButton.addActionListener(e -> {
            VersionNode selected = versionList.getSelectedValue();
            if (selected != null) {
                currentVersion = selected;
                textArea.setText(selected.content);
                dialog.dispose();
            }
        });

        dialog.add(new JScrollPane(versionList), BorderLayout.CENTER);
        dialog.add(selectButton, BorderLayout.SOUTH);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    // Save to regular file (not versioned)
    private void saveToFile() {
        if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(textArea.getText());

                JOptionPane.showMessageDialog(frame, "File saved successfully.");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(frame, "Error saving file.");
            }
        }
    }

    // Open a file and display its contents
    private void openFile() {
        if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (Scanner scanner = new Scanner(file)) {
                StringBuilder content = new StringBuilder();
                while (scanner.hasNextLine()) {
                    content.append(scanner.nextLine()).append("\n");
                }
                textArea.setText(content.toString());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(frame, "Error opening file.");
            }
        }
    }

    // Simple find dialog
    private void findText() {
        String input = JOptionPane.showInputDialog(frame, "Enter text to find:");
        if (input != null && !input.isEmpty()) {
            String content = textArea.getText();
            int index = content.indexOf(input);
            if (index >= 0) {
                textArea.setCaretPosition(index);
                textArea.select(index, index + input.length());
                textArea.grabFocus();
            } else {
                JOptionPane.showMessageDialog(frame, "Text not found.");
            }
        }
    }

    // Replace text using dialogs
    private void replaceText() {
        String find = JOptionPane.showInputDialog(frame, "Find:");
        if (find != null) {
            String replace = JOptionPane.showInputDialog(frame, "Replace with:");
            if (replace != null) {
                textArea.setText(textArea.getText().replace(find, replace));
            }
        }
    }

    // This function shows how the new version differs from the old one (word-level)
    private String getChangeSummary(String oldContent, String newContent) {
        String[] oldWords = oldContent.split("\\s+");
        String[] newWords = newContent.split("\\s+");
        StringBuilder summary = new StringBuilder();
        for (int i = 0; i < Math.min(oldWords.length, newWords.length); i++) {
            if (!oldWords[i].equals(newWords[i])) {
                summary.append(oldWords[i]).append(" -> ").append(newWords[i]).append(", ");
            }
        }
        return summary.length() > 0 ? summary.toString() : "No changes";
    }

    // Inner class to represent versions
    private static class VersionNode {
        String content;
        VersionNode parent;
        List<VersionNode> children = new ArrayList<>();
        String hash = UUID.randomUUID().toString();

        VersionNode(String content, VersionNode parent) {
            this.content = content;
            this.parent = parent;
        }

        @Override
        public String toString() {
            return "Version " + hash.substring(0, 6);
        }
    }
}
