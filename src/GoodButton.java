import javax.swing.*;
import java.awt.*;

public class GoodButton extends JButton{
    public GoodButton(String text){
        super(text);

        // Modern styling
        setBackground(new Color(63, 81, 181)); // Material Blue
        setForeground(Color.WHITE);
        setFont(new Font("SansSerif", Font.BOLD, 14));
        setFocusPainted(false);
        // setBorderPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add padding
        setMargin(new Insets(10, 20, 10, 20));
        
        // Simple Hover Effect
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                setBackground(new Color(48, 63, 159)); // Darker on hover
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                setBackground(new Color(63, 81, 181)); // Back to original
            }
        });
    }
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2.setColor(getBackground());
        // Draw a rounded rectangle (x, y, width, height, arcWidth, arcHeight)
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
        
        g2.dispose();
        super.paintComponent(g); // This draws the text/icon
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("For Button");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600,200);
        frame.setLayout(new FlowLayout());

        GoodButton gb = new GoodButton("Click here!!");
        gb.setSize(100,50);

        frame.add(gb);
        // frame.pack();

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
