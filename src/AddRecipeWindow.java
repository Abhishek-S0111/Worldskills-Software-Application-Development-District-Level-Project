import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import javax.swing.border.EmptyBorder;

public class AddRecipeWindow extends JFrame{
    private RecipeListener listener;

    public AddRecipeWindow(RecipeListener listener){
        this.listener = listener;

        setTitle("Add new Recipe");
        setSize(400,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel recipeTitleLabel = new JLabel("Enter Recipe Name:");
        JTextField recipeTitleInput = new JTextField();
        recipeTitleInput.setPreferredSize(new Dimension(100,30));

        JPanel titleContainer = new JPanel();
        titleContainer.setLayout(new FlowLayout());
        titleContainer.add(recipeTitleLabel);
        titleContainer.add(recipeTitleInput);

        JLabel descLabel = new JLabel("Enter Description:");
        JTextArea descInput = new JTextArea();
        descInput.setPreferredSize(new Dimension(350, 250));

        JPanel descContainer = new JPanel();
        descContainer.setLayout(new FlowLayout());
        descContainer.add(descLabel);
        descContainer.add(descInput);

        JLabel ingredientsLabel = new JLabel("Ingredients");
        ExpandableIngredientsPanel ingredientList = new ExpandableIngredientsPanel();
        

        JPanel ingredientsContainer = new JPanel();

        GoodButton addIngredient = new GoodButton("Add Ingredients");
        GoodButton removeIngredient = new GoodButton("Remove Ingredients");

        addIngredient.addActionListener(e -> {
            ingredientList.addListItem();
        });

        removeIngredient.addActionListener(e -> {
            ingredientList.deleteSelectedListItem();
        });

        JPanel buttonBar = new JPanel();
        buttonBar.setLayout(new FlowLayout());
        buttonBar.add(addIngredient);
        buttonBar.add(removeIngredient);
        
        ingredientsContainer.setLayout(new BoxLayout(ingredientsContainer, BoxLayout.Y_AXIS));
        ingredientsContainer.add(ingredientsLabel);
        ingredientsContainer.add(ingredientList);
        ingredientsContainer.add(buttonBar);

        ingredientsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        ingredientList.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonBar.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel centerWrapper = new JPanel();
        centerWrapper.setLayout(
            new BoxLayout(centerWrapper, BoxLayout.Y_AXIS)
        );

        centerWrapper.add(descContainer);
        centerWrapper.add(ingredientsContainer);

        GoodButton saveButton = new GoodButton("Save Recipe");
        saveButton.addActionListener(e -> {
            String title = recipeTitleInput.getText();
            String desc = descInput.getText();

            List<String> ingredients = ingredientList.getAllTitles();

            Recipe r = new Recipe(title, desc, ingredients);

            listener.onRecipeAdded(r);
            dispose();
        });

        add(titleContainer, BorderLayout.NORTH);
        add(centerWrapper, BorderLayout.CENTER);
        add(saveButton, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
    }
}

class ExpandableIngredientsPanel extends JPanel{
    private JPanel listContainer;

    private List<ItemWrapper> items = new ArrayList<>();

    //helper inner class for maintaining checkboxes and item data
    private class ItemWrapper {
    JCheckBox checkBox;
    JPanel panel;
    JTextField nameField;
    JTextField qtyField;

    ItemWrapper(JCheckBox cb, JPanel p, JTextField n, JTextField q) {
        checkBox = cb;
        panel = p;
        nameField = n;
        qtyField = q;
    }
}


    public ExpandableIngredientsPanel(){
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

    public void addListItem() {

        JPanel item = new JPanel(new BorderLayout());
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        item.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));

        JCheckBox cb = new JCheckBox();

        JTextField nameField = new JTextField(10);
        JTextField qtyField  = new JTextField(6);

        JPanel fields = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fields.add(new JLabel("Item:"));
        fields.add(nameField);
        fields.add(new JLabel("Qty:"));
        fields.add(qtyField);

        JPanel fullBar = new JPanel(new BorderLayout());
        fullBar.add(cb, BorderLayout.WEST);
        fullBar.add(fields, BorderLayout.CENTER);

        item.add(fullBar, BorderLayout.CENTER);

        listContainer.add(item);
        items.add(new ItemWrapper(cb, item, nameField, qtyField));

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

    public List<String> getAllTitles() {
        List<String> result = new ArrayList<>();

        for (ItemWrapper w : items) {

            String name = w.nameField.getText().trim();
            String qty  = w.qtyField.getText().trim();

            if (!name.isEmpty()) {
                if (qty.isEmpty()) {
                    result.add(name);
                } else {
                    result.add(name + " : " + qty);
                }
            }
        }

        return result;
    }


    public List<String> getAllIngredients() {
        List<String> result = new ArrayList<>();

        for (ItemWrapper w : items) {

            String name = w.nameField.getText().trim();
            String qty  = w.qtyField.getText().trim();

            if (!name.isEmpty()) {
                if (qty.isEmpty())
                    result.add(name);
                else
                    result.add(name + " (" + qty + ")");
            }
        }

        return result;
    }

}

class Recipe {
    String title;
    String description;
    List<String> ingredients;

    Recipe(String t, String d, List<String> i) {
        title = t;
        description = d;
        ingredients = i;
    }
}

interface RecipeListener {
    void onRecipeAdded(Recipe recipe);
}