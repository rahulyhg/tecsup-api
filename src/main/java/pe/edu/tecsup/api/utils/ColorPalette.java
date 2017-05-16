package pe.edu.tecsup.api.utils;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by ebenites on 15/05/2017.
 */
public class ColorPalette {

    private static final List<String> colors = new ArrayList<>();
    static{
        colors.add("#F57F68");
        colors.add("#87D288");
        colors.add("#F8B552");
        colors.add("#59DBE0");
        colors.add("#512DA8");
        colors.add("#E64A19");
        colors.add("#448AFF");
        colors.add("#E040FB");
        colors.add("#63D64A");
        colors.add("#000000");

    }

    private Map<Integer, String> curseColors;
    private int counter = 0;
    private final Random random;

    public ColorPalette(){
        curseColors = new HashMap<>();
        random = new Random(System.currentTimeMillis());
    }

    public String getColor(Integer codcurso){
        if(curseColors.get(codcurso) == null){
            curseColors.put(codcurso, colors.get(counter++));
            if(colors.size() == counter) counter = 0;
        }
        return curseColors.get(codcurso);
    }

    public String getRandomColor(Integer codcurso){
        if(curseColors.get(codcurso) == null){

            final int red = (255 + random.nextInt(256)) / 2;
            final int green = (255 + random.nextInt(256)) / 2;
            final int blue = (255 + random.nextInt(256)) / 2;

            Color color = new Color(red, green, blue);
            String colorHex = String.format("#%06X", (0xFFFFFF & color.getRGB()));

            curseColors.put(codcurso, colorHex);
        }
        return curseColors.get(codcurso);
    }

}
