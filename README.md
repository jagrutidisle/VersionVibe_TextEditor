VersionVibe – A Text Editor That Versions Differently

Submission for Buffer 6.0
Theme: Custom Data Structures in Real-Life Applications
Group No. 130
Video link:https://drive.google.com/file/d/1yYC3F9aF015GJeuaEkTYsJPluC_J5_VB/view?usp=sharing
What’s the Idea?
VersionVibe is not just a text editor—it’s a version-aware editor with its own custom-built version control system! Think Git, but handcrafted from scratch and baked directly into a friendly Java GUI. Built for the "Custom Data Structures" theme, this project showcases how a tree-based structure can be applied to real-world scenarios like document editing and version tracking. Each version of the document is a node, and every edit becomes a new branch in your document's evolution.

Goals :-
“What if your text editor could remember every version you saved, and you could explore alternate timelines of your document?”
That’s what VersionVibe is all about:
Track your changes like Git, but with fewer headaches.
Branch into alternate futures with redo.
Undo and Redo with context—choose your path.
Visualize versioning through meaningful dialogs.
All wrapped in a clean Java Swing GUI.

Custom Data Structure – 
At the core of this editor is a custom version tree:
VersionNode {
    String content;
    String hash; // for version ID
    VersionNode parent;
    List<VersionNode> children;
}
Every version saved becomes a node.
Undo moves up the tree.
Redo moves to any child node (yes, branching!).
We used SHA-1 hashing to identify each version uniquely.
This isn't borrowed from a library—it’s fully custom, built from scratch to manage the entire versioning flow in memory.

Key Features :-
Custom Versioning Tree to handle undo/redo with multiple branches
Save Version (Ctrl+S) with auto-generated ID
Undo & Redo Navigation across versions
Find & Replace, Cut/Copy/Paste tools
File operations: Open, Save, Print, Exit
Change Summary dialog to compare versions
Tree-structured in-memory history (custom built, no Git or Libs)

Demo Flow :-
Start the app, type something cool.
Save a version (Ctrl+S) – boom! That’s a node.
Make more edits, save again – another node.
Undo – back in time!
Redo – but wait! You can choose from multiple branches?! 
View what changed. Explore different futures of your document.
No dependencies needed – pure Java.

File Structure :-
VersionVibe.java  // the one file to rule them all
├── VersionNode   // inner class – tree magic lives here
└── Saved Versions (Optional) → version_<id>.txt

Why This Matters :-
VersionVibe rethinks how editors can be smart about history. We didn’t want to rely on Git or external tools. Instead, we modeled a version control system from scratch, purely in Java, using a self-crafted data structure.
That’s what this competition is about: solving real problems with custom logic.

Built With :-
Java (Java Swing for GUI)
Custom tree-based structure (VersionNode class)
Pure determination
10% StackOverflow, 90% Debugging 

Challenges We Faced :-
Designing redo with branching logic (not just linear redo)
Keeping the GUI responsive while syncing version logic
Making change summaries readable and useful
Balancing file I/O and in-memory history (we chose in-memory for speed)

Future Upgrades :-
Dark mode
Visual version tree viewer
Persistent version history (even after app restarts)
Markdown preview
 AI-based change suggestions? 

Team Info :-
Vrunani Muley,Jagruti Disle & Dhammavi Pilewan
"We believe code should not just work, it should think. That’s what custom data structures let us do—build software that adapts to our minds, not the other way around."
