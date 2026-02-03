import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;
import java.util.List;
import java.util.ArrayList;

public class HomeScreen extends JFrame{

    public HomeScreen(){
        setTitle("Recipe Management System");
        setSize(400,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // instance of panel
        ExpandableListPanel eListPanel = new ExpandableListPanel();
        eListPanel.setPreferredSize(new Dimension(380, 400));


        // Buttons
        GoodButton addButton = new GoodButton("Add Recipe");
        GoodButton deleteButton = new GoodButton("Delete Selected Recipes");

        addButton.addActionListener(e -> {
            AddRecipeWindow win = new AddRecipeWindow(recipe -> {
                StringBuilder body = new StringBuilder();

                body.append("<b>Description:</b><br>");
                body.append(recipe.description);
                body.append("<br><br><b>Ingredients:</b><br>");

                for (String ing : recipe.ingredients) {
                    body.append("-  ").append(ing).append("<br>");
                }

                eListPanel.addListItem(recipe.title, body.toString());
            });

            win.setVisible(true);
        });

        deleteButton.addActionListener(e -> {
            eListPanel.deleteSelectedListItem();
        });

        add(eListPanel, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);

        add(buttonPanel, BorderLayout.SOUTH);


        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HomeScreen().setVisible(true));
    }
}

final class ExpandableListPanel extends JPanel{
    private JPanel listContainer;

    private List<ItemWrapper> items = new ArrayList<>();

    //helper inner class for maintaining checkboxes and item data
    private class ItemWrapper{
        JCheckBox checkBox;
        JPanel panel;
        String title;

        ItemWrapper(JCheckBox cb, JPanel p, String t){
            this.checkBox = cb;
            this.panel = p;
            this.title = t;
        }
    }

    public ExpandableListPanel(){
        // BorderLayout so scrollpanel fills the Jpanel
        setLayout(new BorderLayout());

        // panel to hold the list items
        listContainer = new JPanel();
        listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS));
        listContainer.setBackground(Color.WHITE);

        //Wrap all in ScrollBox
        JScrollPane scrollPane = new JScrollPane(listContainer);
        scrollPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        scrollPane.getVerticalScrollBar().setUnitIncrement(4);

        add(scrollPane, BorderLayout.CENTER);     
    }

    public void addListItem(String title, String description){
        JPanel item = new JPanel(new BorderLayout());
        // item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        item.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));

        JCheckBox cb = new JCheckBox();

        GoodButton header = new GoodButton(title);
        header.setHorizontalAlignment(SwingConstants.LEFT);
        header.setFocusPainted(false);

        JEditorPane content = new JEditorPane("text/html",
        "<div style='padding:10px;'>" + description + "</div>");
        content.setEditable(false);
        JScrollPane contentScroll = new JScrollPane(content);
        contentScroll.setPreferredSize(new Dimension(300, 120));
        contentScroll.setVisible(false);

        //wrap all in single panel component
        JPanel fullBar = new JPanel(new BorderLayout());
        fullBar.add(cb, BorderLayout.WEST);
        fullBar.add(header, BorderLayout.NORTH);
        fullBar.add(contentScroll, BorderLayout.CENTER);

        item.add(fullBar, BorderLayout.CENTER);

        header.addActionListener(e -> {
            contentScroll.setVisible(!contentScroll.isVisible());
            revalidate();
            repaint();
        });

        listContainer.add(item);
        items.add(new ItemWrapper(cb, item, title));

        listContainer.revalidate();
        listContainer.repaint();
    }

    public void deleteSelectedListItem(){
        // backward run to avoid index issue when deletion
        for (int i = items.size() - 1; i >= 0; i--) {
            ItemWrapper wrapper = items.get(i);
            if(wrapper.checkBox.isSelected()){
                listContainer.remove(wrapper.panel);
                items.remove(i);
            }
        }

        listContainer.revalidate();
        listContainer.repaint();
    }

    public List<String> getAllTitles(){
        List<String> result = new ArrayList<>();
        for(ItemWrapper w: items){
            result.add(w.title);
        }

        return result;
    }
}

// GoodButton: Better Buttons aesthetically
class GoodButton extends JButton{
    public GoodButton(String text){
        super(text);

        // Modern styling
        setBackground(new Color(63, 81, 181)); 
        setForeground(Color.WHITE);
        setFont(new Font("SansSerif", Font.BOLD, 14));
        setFocusPainted(false);
        setBorderPainted(false);
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
}