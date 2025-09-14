public class Attack
{
    private final int row1;
    private final int col1;
    private final int row2;
    private final int col2;
    private final int num;

    public Attack(int r1, int c1, int r2, int c2, int n)
    {
        row1 = r1;
        col1 = c1;
        row2 = r2;
        col2 = c2;
        num = n;
    }

    public int getRow1()
    {
        return row1;
    }

    public int getCol1()
    {
        return col1;
    }

    public int getRow2()
    {
        return row2;
    }

    public int getCol2()
    {
        return col2;
    }

    public int getNum()
    {
        return num;
    }
}