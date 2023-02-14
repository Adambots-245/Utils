import javax.lang.model.element.Element;

public class PathFlipper {
    static final String input = "14.837493400734528,-3.0200039768061373,0.5051282994411175,0.0,true,false,12.072580603793673,-2.9801254268502593,-0.558299699382288,0.1196356498676332,true,false,9.453889156691037,-2.953539726879674,0.026585699970585353,-1.9939274977938855,true,false,";
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
