package Utils;

public final class MathUtils
{
    private MathUtils()
    {
        throw new AssertionError();
    }

    public static int rand(int min, int max)
    {
        return min + (int)(Math.random() * ((max - min) + 1));
    }

    public static float getMin(float[] values)
    {
        if (values.length == 0)
            return 0;

        float min = values[0];

        for (int i = 1; i < values.length; i++)
            if (values[i] < min)
                min = values[i];

        return min;
    }
    public static float getMax(float[] values)
    {
        if (values.length == 0)
            return 0;

        float max = values[0];

        for (int i = 1; i < values.length; i++)
            if (values[i] > max)
                max = values[i];

        return max;
    }
}
