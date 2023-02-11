import javax.lang.model.element.Element;

public class PathFlipper {

    // static final String input = "1.7042065992654707,-3.0200039768061373,-0.5051282994411175,0.0,true,false,";
    // static final String input2 = "1.7042065992654707,-3.0200039768061373,-0.5051282994411175,0.0,true,false,";
    // static final String input3 = "4.469119396206326,-2.9801254268502593,0.558299699382288,0.1196356498676332,true,false,";
    // static final String input4 = "7.0878108433089615,-2.953539726879674,-0.026585699970585353,-1.9939274977938855,true,false,";
    static final String input = "1.7042065992654707,-3.0200039768061373,-0.5051282994411175,0.0,true,false,4.469119396206326,-2.9801254268502593,0.558299699382288,0.1196356498676332,true,false,7.0878108433089615,-2.953539726879674,-0.026585699970585353,-1.9939274977938855,true,false,";
    static final double width = 16.5417;

    public static void main (String Args[]) {
        String[] elements = input.split(",");
        for (int i = 0; i < elements.length; i += 6) {
            elements[i] = String.valueOf(-Double.parseDouble(elements[i])+width);
            elements[i+2] = String.valueOf(-Double.parseDouble(elements[i+2]));
        }

        String output = "";
        for (int i = 0; i < elements.length; i++) {
            output = output+elements[i]+",";
        }
        System.out.println(output);
    }
}
