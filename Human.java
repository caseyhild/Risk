public class Human extends Player
{
    private int armiesPerTurn;
    private int armiesDeployed;

    public Human(String name, int color, double offenseKillRate, double defenseKillRate, int apt, int boardSize)
    {
        super(name, color, offenseKillRate, defenseKillRate, boardSize);
        armiesPerTurn = apt;
        armiesDeployed = 0;
    }

    public int getArmiesPerTurn()
    {
        return armiesPerTurn;
    }

    public void setArmiesPerTurn(int apt)
    {
        armiesPerTurn = apt;
    }

    public int getArmiesDeployed()
    {
        return armiesDeployed;
    }

    public void setArmiesDeployed(int ad)
    {
        armiesDeployed = ad;
    }

    public void deploy(int r, int c, int num)
    {
        setArmies(r, c, getArmies(r, c) + 1);
        setArmiesDeployed(getArmiesDeployed() + 1);
    }

    public void attack(int r1, int c1, int r2, int c2, Player p, int num, int[][] board)
    {
        if(p instanceof Human && getColor() != ((Human) p).getColor())
        {
            int myArmies = getArmies(r1, c1);
            int enemyArmies = ((Human) p).getArmies(r2, c2);
            ((Human) p).setArmies(r2, c2, Math.max(0, enemyArmies - (int) Math.round(getOffenseKillRate() * num)));
            setArmies(r1, c1, getArmies(r1, c1) - Math.min(num, (int) Math.round(((Human) p).getDefenseKillRate() * enemyArmies)));
            if(((Human) p).getArmies(r2, c2) == 0 && getArmies(r1, c1) > 0)
            {
                board[r2][c2] = board[r1][c1];
                setArmies(r2, c2, getArmies(r1, c1) - (myArmies - num));
                setArmies(r1, c1, myArmies - num);
            }
        }
        else if(p instanceof AI)
        {
            int myArmies = getArmies(r1, c1);
            int enemyArmies = ((AI) p).getArmies(r2, c2);
            ((AI) p).setArmies(r2, c2, Math.max(0, enemyArmies - (int) Math.round(getOffenseKillRate() * num)));
            setArmies(r1, c1, getArmies(r1, c1) - Math.min(num, (int) Math.round(((AI) p).getDefenseKillRate() * enemyArmies)));
            if(((AI) p).getArmies(r2, c2) == 0 && getArmies(r1, c1) > 0)
            {
                board[r2][c2] = board[r1][c1];
                setArmies(r2, c2, getArmies(r1, c1) - (myArmies - num));
                setArmies(r1, c1, myArmies - num);
            }
        }
        else if(p instanceof Neutral)
        {
            int myArmies = getArmies(r1, c1);
            int enemyArmies = ((Neutral) p).getArmies(r2, c2);
            ((Neutral) p).setArmies(r2, c2, Math.max(0, enemyArmies - (int) Math.round(getOffenseKillRate() * num)));
            setArmies(r1, c1, getArmies(r1, c1) - Math.min(num, (int) Math.round(((Neutral) p).getDefenseKillRate() * enemyArmies)));
            if(((Neutral) p).getArmies(r2, c2) == 0 && getArmies(r1, c1) > 0)
            {
                board[r2][c2] = board[r1][c1];
                setArmies(r2, c2, getArmies(r1, c1) - (myArmies - num));
                setArmies(r1, c1, myArmies - num);
            }
        }
        else
        {
            setArmies(r1, c1, getArmies(r1, c1) - num);
            setArmies(r2, c2, getArmies(r2, c2) + num);
        }
    }
}