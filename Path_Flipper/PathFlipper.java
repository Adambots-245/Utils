package Path_Flipper;
public class PathFlipper {
    static final String input = "";
    static final double width = 16.5417;

    public static void main (String Args[]) {
        String[] elements = input.split(",");
        for (int i = 0; i < elements.length; i += 6) {
            elements[i] = String.valueOf(-Double.parseDouble(elements[i])+width);
            elements[i+2] = String.valueOf(-Double.parseDouble(elements[i+2]));
        }

        System.out.println("X,Y,Tangent X,Tangent Y,Fixed Theta,Reversed,Name");
        String output = "";
        for (int i = 0; i < elements.length; i++) {
            if (i % 6 == 0 && i != 0) {
                output += "\n";
            }
            output = output+elements[i]+",";
        }
        System.out.println(output);
    }
}
