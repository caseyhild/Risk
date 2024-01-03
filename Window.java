import javax.swing.*;
public class Window extends JPanel 
{
    private static int width = 800;
    private static int height = 600;
    
    public static void main(String[] args)
    {
        java.awt.EventQueue.invokeLater(new Runnable() 
            {
                public void run()
                {
                    Window frame = new Window();
                    frame.setVisible(true);
                }
            }
        );
        Risk r = new Risk();
        r.setScreenSize(width, height);
        JFrame jf = new JFrame();
        jf.setTitle("Risk");
        jf.setSize(width + 6, height + 29);
        jf.setVisible(true);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setResizable(false);
        jf.setLocationRelativeTo(null);
        jf.add(r);
        jf.setVisible(true);
    }
}