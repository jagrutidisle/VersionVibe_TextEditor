VersionVibe – A Text Editor That Versions Differently
Submission for Buffer 6.0
Theme: Custom Data Structures in Real-Life Applications
Group No. 130
What’s the Idea?
VersionVibe goes beyond a typical text editor—it integrates a custom-built version control system directly into a user-friendly Java GUI. Unlike Git, this system is handcrafted from scratch, emphasizing ease of use and integration with document editing.

Goals
VersionVibe aims to revolutionize text editing by allowing users to explore alternate timelines of their documents through a version-aware interface. It simplifies version tracking and management compared to traditional tools like Git.

Custom Data Structure – How We Built the Magic
At its core is a custom version tree:
class VersionNode {
    String content;
    String hash; // Version ID using SHA-1 hashing
    VersionNode parent;
    List<VersionNode> children;
}
Each edit creates a new node in the tree, enabling robust undo and redo functionalities with branching support.

Key Features

Custom Versioning Tree for undo/redo with branching

Save Version (Ctrl+S) with auto-generated IDs

Visual undo and redo with context navigation

Find & Replace, Cut/Copy/Paste tools

File operations: Open, Save, Print, Exit

Change Summary dialog for version comparison

Demo Flow

Start VersionVibe, create or open a document.

Save versions (Ctrl+S) to create nodes in the version tree.

Navigate through edit history with undo and redo, exploring multiple branches.

Compare versions using the Change Summary dialog.

File Structure

VersionVibe.java
├── VersionNode.java
├──LandingPage.java
├──logo.jpg
Why This Matters
VersionVibe reimagines text editors by embedding a custom version control system directly within the application. It prioritizes simplicity and performance by avoiding external dependencies like Git, focusing on a self-contained, Java-based solution.

Built With

Java (Java Swing for GUI)

Custom tree-based structure (VersionNode class)

Pure determination and extensive debugging

Challenges We Faced

Implementing redo with branching logic

Ensuring responsive GUI with complex version logic

Designing readable change summaries

Balancing file I/O and in-memory operations for efficiency

Future Upgrades

Dark mode

Visual version tree viewer

Persistent version history across app restarts

Markdown preview

AI-based change suggestions

Team Info

Vrunani Muley, Jagruti Disle & Dhammavi Pilewan
"We believe in crafting software that adapts to user needs through custom data structures, not the other way around."

