package net.mindoth.runicitems.particle;

import javax.annotation.Nonnull;
import java.util.Random;

public class ParticleColor {

    public static ParticleColor defaultParticleColor() {
        return new ParticleColor(255, 25, 180, 0.3F);
    }

    public static ParticleColor.IntWrapper defaultParticleColorWrapper() {
        return new ParticleColor.IntWrapper(255, 25, 180, 0.3F);
    }

    private final float r;
    private final float g;
    private final float b;
    private final int color;
    private final float scale;

    public ParticleColor(int r, int g, int b, float scale) {
        this.r = r / 255F;
        this.g = g / 255F;
        this.b = b / 255F;
        this.color = (r << 16) | (g << 8) | b;
        this.scale = scale;
    }

    public ParticleColor(double red, double green, double blue, double scale) {
        this((int)red, (int)green, (int)blue, (float)scale);
    }

    /*public static ParticleColor makeRandomColor(int r, int g, int b, Random random){
        return new ParticleColor(random.nextInt(r), random.nextInt(g), random.nextInt(b));
    }*/

    public ParticleColor(float r, float g, float b, float scale) {
        this((int)r, (int)g, (int)b, (float)scale);
    }


    public static ParticleColor fromInt(int color, float scale){
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = (color >> 0) & 0xFF;
        return new ParticleColor(r,g,b,scale);
    }

    public float getRed(){return r;}

    public float getGreen() {
        return g;
    }

    public float getBlue() {
        return b;
    }

    public int getColor() {
        return color;
    }

    public float getScale() {
        return scale;
    }

    public String serialize(){
        return "" + this.r + "," + this.g + "," + this.b + "," + scale;
    }

    public IntWrapper toWrapper(){
        return new IntWrapper(this);
    }

    public static ParticleColor deserialize(String string){
        if ( string == null || string.isEmpty() ) return defaultParticleColor();
        String[] arr = string.split(",");
        return new ParticleColor(Integer.parseInt(arr[0].trim()), Integer.parseInt(arr[1].trim()), Integer.parseInt(arr[2].trim()), Float.parseFloat(arr[3].trim()));
    }

    public static class IntWrapper {
        public int r;
        public int g;
        public int b;
        public float scale;

        public IntWrapper(int r, int g, int b, float scale) {
            this.r = r;
            this.g = g;
            this.b = b;
            this.scale = scale;
        }

        public IntWrapper(ParticleColor color) {
            this.r = (int) (color.getRed() * 255.0);
            this.g = (int) (color.getGreen() * 255.0);
            this.b = (int) (color.getBlue() * 255.0);
            this.scale = color.getScale();
        }

        public ParticleColor toParticleColor(){
            return new ParticleColor(r,g,b,scale);
        }

        public String serialize(){
            return "" + this.r + "," + this.g + "," + this.b + "," + this.scale;
        }

        public void makeVisible(){
            if (r + g + b < 20){
                b += 10;
                g += 10;
                r += 10;
            }
        }

        public static @Nonnull ParticleColor.IntWrapper deserialize(String string){
            ParticleColor.IntWrapper color = defaultParticleColorWrapper();
            if(string == null || string.isEmpty())
                return color;

            try{
                String[] arr = string.split(",");
                color = new ParticleColor.IntWrapper(Integer.parseInt(arr[0].trim()), Integer.parseInt(arr[1].trim()), Integer.parseInt(arr[2].trim()), Float.parseFloat(arr[3].trim()));
                return color;
            }catch (Exception ignored){ }
            return color;
        }
    }
}
