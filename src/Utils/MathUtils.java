package Utils;

public final class MathUtils
{
    public static int rand(int min, int max)
    {
        return min + (int)(Math.random() * ((max - min) + 1));
    }
}
