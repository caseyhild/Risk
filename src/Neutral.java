public class Neutral extends Player
{
    public Neutral(int color, double defenseKillRate, int neutralArmies, int boardSize)
    {
        super("Neutral", color, 0.0, defenseKillRate, boardSize);
        for(int r = 0; r < boardSize; r++)
        {
            for(int c = 0; c < boardSize; c++)
            {
                setArmies(r, c, neutralArmies);
            }
        }
    }
}