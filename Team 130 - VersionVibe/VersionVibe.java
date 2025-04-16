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
    private int fontSize = 30;  // Default font size

    public static void main(String[] args) {
        SwingUtilities.invokeLater(VersionVibe::new);
    }

    // Constructor - sets up the UI and buttons
    public VersionVibe() {
        frame = new JFrame("VersionVibe");
        textArea = new JTextArea(20, 60);
        textArea.setFont(new Font("Arial", Font.PLAIN, fontSize));
        JScrollPane scrollPane = new JScrollPane(textArea);

        JButton undoButton = new JButton("Undo");
        JButton redoButton = new JButton("Redo");
        JButton saveVersionButton = new JButton("Save Version");

        VersionNode root = new VersionNode("", null);
        currentVersion = root;
        versionMap.put(root.hash, root);

        textArea.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK), "saveShortcut");
        textArea.getActionMap().put("saveShortcut", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                saveVersion();
            }
        });

        undoButton.addActionListener(e -> undo());
        redoButton.addActionListener(e -> showRedoDialog());
        saveVersionButton.addActionListener(e -> saveVersion());

        JPanel panel = new JPanel();
        panel.add(undoButton);
        panel.add(redoButton);
        panel.add(saveVersionButton);

        frame.setJMenuBar(createMenuBar());
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(panel, BorderLayout.SOUTH);

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem open = new JMenuItem("Open");
        JMenuItem save = new JMenuItem("Save");
        JMenuItem print = new JMenuItem("Print");
        JMenuItem exit = new JMenuItem("Exit");

        open.addActionListener(e -> openFile());
        save.addActionListener(e -> saveToFile());
        print.addActionListener(e -> {
            try {
                textArea.print();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        exit.addActionListener(e -> System.exit(0));

        fileMenu.add(open);
        fileMenu.add(save);
        fileMenu.add(print);
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

        viewMenu.add(zoomIn);
        viewMenu.add(zoomOut);

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(viewMenu);

        return menuBar;
    }

    private void zoomIn() {
        fontSize += 2;
        textArea.setFont(new Font("Arial", Font.PLAIN, fontSize));
    }

    private void zoomOut() {
        fontSize = Math.max(8, fontSize - 2);
        textArea.setFont(new Font("Arial", Font.PLAIN, fontSize));
    }

    private void saveVersion() {
        String content = textArea.getText();
        if (!content.equals(currentVersion.content)) {
            VersionNode newVersion = new VersionNode(content, currentVersion);
            currentVersion.children.add(newVersion);
            currentVersion = newVersion;
            versionMap.put(newVersion.hash, newVersion);
            saveToFile(newVersion);
            JOptionPane.showMessageDialog(frame, "Version saved: " + newVersion.hash);
        }
    }

    private void saveToFile(VersionNode version) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("version_" + version.hash + ".txt"))) {
            writer.write("Version: " + version.hash + "\n");
            writer.write("Content:\n" + version.content);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error saving version to file.");
        }
    }

    private void undo() {
        if (currentVersion.parent != null) {
            currentVersion = currentVersion.parent;
            textArea.setText(currentVersion.content);
        } else {
            JOptionPane.showMessageDialog(frame, "No previous version.");
        }
    }

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

    private void replaceText() {
        String find = JOptionPane.showInputDialog(frame, "Find:");
        if (find != null) {
            String replace = JOptionPane.showInputDialog(frame, "Replace with:");
            if (replace != null) {
                textArea.setText(textArea.getText().replace(find, replace));
            }
        }
    }

    private String getChangeSummary(String oldContent, String newContent) {
        Set<String> oldWords = new HashSet<>(Arrays.asList(oldContent.split("\\s+")));
        Set<String> newWords = new HashSet<>(Arrays.asList(newContent.split("\\s+")));

        Set<String> added = new HashSet<>(newWords);
        added.removeAll(oldWords);

        Set<String> removed = new HashSet<>(oldWords);
        removed.removeAll(newWords);

        return "Added: " + added.size() + ", Removed: " + removed.size();
    }

    // Inner class representing a version node
    static class VersionNode {
        String content;
        VersionNode parent;
        List<VersionNode> children = new ArrayList<>();
        String hash;

        VersionNode(String content, VersionNode parent) {
            this.content = content;
            this.parent = parent;
            this.hash = UUID.randomUUID().toString().substring(0, 8); // Short hash
        }

        public String toString() {
            return "v_" + hash;
        }
    }
}
