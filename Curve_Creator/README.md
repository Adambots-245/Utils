# README
To run the application, simply execute CurveCreator.java
Click to create points, or click on existing points to select/drag them around
The coordinates of the currently selected point are displayed in the top right, and can be manually modified by entering a new value in the field and hitting enter
For Ex., To create an input deadzone for all inputs < 0.3 simply add a point at (0.3, 0)

After creating a curve, hit the 'Generate Array' button to print the array to the console, which can then be copy and pasted into the main codebase (Gamepad -> Buttons.java)
If you wish to modify an already existing curve, simple copy and paste the array into the text field below the 'Import Array' button and then hit the button, if formatted improperly, it will alert you.