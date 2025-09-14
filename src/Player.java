public class Player
{
    private final double offenseKillRate;
    private final double defenseKillRate;
    private final int color;
    private final int[][] armies;
    private final String name;
    
    public Player(String name, int color, double offenseKillRate, double defenseKillRate, int boardSize)
    {
        this.offenseKillRate = offenseKillRate;
        this.defenseKillRate = defenseKillRate;
        this.color = color;
        this.name = name;
        armies = new int[boardSize][boardSize];
        for(int r = 0; r < boardSize; r++)
        {
            for(int c = 0; c < boardSize; c++)
            {
                setArmies(r, c, 0);
            }
        }
    }
    
    public double getOffenseKillRate()
    {
        return offenseKillRate;
    }
    
    public double getDefenseKillRate()
    {
        return defenseKillRate;
    }
    
    public int getArmies(int row, int col)
    {
        return armies[row][col];
    }
    
    public void setArmies(int row, int col, int num)
    {
        armies[row][col] = num;
    }
    
    public int getColor()
    {
        return color;
    }
    
    public String getName()
    {
        return name;
    }
}