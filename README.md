VersionVibe – A Text Editor That Versions Differently


Video Link:https://drive.google.com/file/d/1yYC3F9aF015GJeuaEkTYsJPluC_J5_VB/view?usp=sharing
What’s the Idea?

->VersionVibe is a custom-built text editor that integrates version control directly into the app. Unlike traditional version control tools like Git, it uses a tree-based data structure for tracking versions, allowing users to explore document histories, undo changes, and branch into alternate document versions seamlessly. All of this is embedded into a simple Java Swing GUI for easy, intuitive use.

Goals

->The main goal is to make version tracking as easy as typing. VersionVibe simplifies version management by eliminating the need for external tools like Git, offering an integrated, user-friendly interface where every document change is tracked as a version, allowing users to navigate through different versions effortlessly.

Custom Data Structure

->The core of VersionVibe is a custom version tree. Each version of a document is represented as a node containing the document’s content, a unique hash (SHA-1), and pointers to its parent and child versions. This structure supports undo/redo with branching, offering users a non-linear path through their document's history.

Key Features

->Custom Versioning Tree: Undo/redo with branching.

->Save Version: Auto-generated IDs for each version.

->Version Comparison: View changes using the Change Summary dialog.

->Basic Text Editing: Includes Find & Replace, Cut/Copy/Paste tools.

->File Operations: Open, Save, Print, Exit.

Why This Matters

->VersionVibe reimagines text editors by embedding custom version control directly into the application. It's simple, intuitive, and fast—ideal for those who want to manage document versions without relying on external tools like Git.

Built With

->Java (Java Swing for GUI)

->Custom tree-based version control (VersionNode class)

Challenges

->Implementing branching redo functionality.

->Ensuring GUI responsiveness during complex version tracking.

->Designing readable change summaries and balancing memory efficiency with file I/O.

Future Upgrades

->Dark mode

->Visual version tree viewer

->Persistent version history

->Markdown preview

->AI-based change suggestions

Team

->Vrunani Muley, Jagruti Disle & Dhammavi Pilewan
"Crafting software that adapts to user needs through custom data structures."
