# codealpha_tasks
A Java Swing application for tracking student grades and managing student grades, configured for Eclipse IDE.
1. **Import the project**:
   - File → Import → General → Existing Projects into Workspace
   - Select the project folder
   - Click "Finish"

2. **Verify Java version**:
   - Right-click project → Properties → Java Build Path
   - Ensure JRE System Library is Java 8 or higher

3. **Set up run configuration**:
   - Right-click project → Run As → Java Application
   - If prompted, select "StudentGradeTrackerGUI" as main class

## Project Structure in Eclipse
StudentGradeTracker (Eclipse Project)
├── src/
│ └── studentgradetracker/
│ ├── StudentGradeTrackerGUI.java (main class)
│ └── Student.java (model class)
├── JRE System Library [JavaSE-1.8]
└── (other Eclipse metadata files)


## Troubleshooting

If you get "javax.swing not accessible" error:
1. Right-click project → Properties → Java Build Path
2. Remove and re-add the JRE System Library
3. For Java 9+, add `module-info.java` with:
   ```java
   module studentgradetracker {
       requires java.desktop;
   }

   ## Key Eclipse Configuration Tips

1. **Java Version Compliance**:
   - Right-click project → Properties → Java Compiler
   - Ensure "Compiler compliance level" matches your JRE version

2. **Adding Resources**:
   - For icons/images: Right-click project → New → Folder → Name: "resources"
   - Mark as "Source Folder" (right-click → Build Path → Use as Source Folder)

3. **Debugging**:
   - Set breakpoints by clicking left margin
   - Run → Debug (F11) to start debugging
   - Use F5 (step into), F6 (step over) during debugging

This Eclipse-specific setup ensures your Swing application runs properly within the IDE environment while maintaining all the functionality of your grade tracking system.
