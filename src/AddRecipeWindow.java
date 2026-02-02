import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import javax.swing.border.EmptyBorder;

public class AddRecipeWindow extends JFrame{

    public AddRecipeWindow(){
        setTitle("Add new Recipe");
        setSize(400,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel recipeTitleLabel = new JLabel("Enter Recipe Name");
        JTextField recipeTitleInput = new JTextField();

        JLabel descLabel = new JLabel("Enter Description:");
        JTextArea descInput = new JTextArea();

        JLabel ingredientsLabel = new JLabel("Ingredients");
        JList ingredienList = new JList<>();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AddRecipeWindow().setVisible(true));
    }
}

class ExpandableListPanel extends JPanel{
    private JPanel listContainer;

    private List<ItemWrapper> items = new ArrayList<>();

    //helper inner class for maintaining checkboxes and item data
    private class ItemWrapper{
        JCheckBox checkBox;
        JPanel panel;

        ItemWrapper(JCheckBox cb, JPanel p){
            this.checkBox = cb;
            this.panel = p;
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
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        item.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));

        JCheckBox cb = new JCheckBox();

        GoodButton header = new GoodButton(title);
        header.setHorizontalAlignment(SwingConstants.LEFT);
        header.setFocusPainted(false);

        JLabel content = new JLabel("<html><div style='padding:10px;'>" + description + "</div></html>");
        content.setVisible(false);

        //wrap all in single panel component
        JPanel fullBar = new JPanel(new BorderLayout());
        fullBar.add(cb, BorderLayout.WEST);
        fullBar.add(header, BorderLayout.NORTH);
        fullBar.add(content, BorderLayout.SOUTH);

        item.add(fullBar, BorderLayout.NORTH);

        header.addActionListener(e -> {
            content.setVisible(!content.isVisible());
            revalidate();
            repaint();
        });

        listContainer.add(item);
        items.add(new ItemWrapper(cb, item));

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
}