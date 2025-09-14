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

    public void deploy(int r, int c)
    {
        setArmies(r, c, getArmies(r, c) + 1);
        setArmiesDeployed(getArmiesDeployed() + 1);
    }

    public void attack(int r1, int c1, int r2, int c2, Player p, int num, int[][] board)
    {
        switch (p) {
            case Human ignored when getColor() != p.getColor() -> {
                int myArmies = getArmies(r1, c1);
                int enemyArmies = p.getArmies(r2, c2);
                p.setArmies(r2, c2, Math.max(0, enemyArmies - (int) Math.round(getOffenseKillRate() * num)));
                setArmies(r1, c1, getArmies(r1, c1) - Math.min(num, (int) Math.round(p.getDefenseKillRate() * enemyArmies)));
                if (p.getArmies(r2, c2) == 0 && getArmies(r1, c1) > 0) {
                    board[r2][c2] = board[r1][c1];
                    setArmies(r2, c2, getArmies(r1, c1) - (myArmies - num));
                    setArmies(r1, c1, myArmies - num);
                }
            }
            case AI ignored -> {
                int myArmies = getArmies(r1, c1);
                int enemyArmies = p.getArmies(r2, c2);
                p.setArmies(r2, c2, Math.max(0, enemyArmies - (int) Math.round(getOffenseKillRate() * num)));
                setArmies(r1, c1, getArmies(r1, c1) - Math.min(num, (int) Math.round(p.getDefenseKillRate() * enemyArmies)));
                if (p.getArmies(r2, c2) == 0 && getArmies(r1, c1) > 0) {
                    board[r2][c2] = board[r1][c1];
                    setArmies(r2, c2, getArmies(r1, c1) - (myArmies - num));
                    setArmies(r1, c1, myArmies - num);
                }
            }
            case Neutral ignored -> {
                int myArmies = getArmies(r1, c1);
                int enemyArmies = p.getArmies(r2, c2);
                p.setArmies(r2, c2, Math.max(0, enemyArmies - (int) Math.round(getOffenseKillRate() * num)));
                setArmies(r1, c1, getArmies(r1, c1) - Math.min(num, (int) Math.round(p.getDefenseKillRate() * enemyArmies)));
                if (p.getArmies(r2, c2) == 0 && getArmies(r1, c1) > 0) {
                    board[r2][c2] = board[r1][c1];
                    setArmies(r2, c2, getArmies(r1, c1) - (myArmies - num));
                    setArmies(r1, c1, myArmies - num);
                }
            }
            case null, default -> {
                setArmies(r1, c1, getArmies(r1, c1) - num);
                setArmies(r2, c2, getArmies(r2, c2) + num);
            }
        }
    }
}