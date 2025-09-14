public class Vector
{
    private double x;
    private double y;
    
    public Vector()
    {
        x = 0; 
        y = 0;
    }
    
    public Vector(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    public double getX()
    {
        return x;
    }

    public double getY()
    {
        return y;
    }

    public static Vector sub(Vector v1, Vector v2)
    {
        Vector v = new Vector(0, 0);
        v.x = v1.x - v2.x;
        v.y = v1.y - v2.y;
        return v;
    }
}
