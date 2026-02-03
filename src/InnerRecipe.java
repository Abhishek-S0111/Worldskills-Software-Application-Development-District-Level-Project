import java.util.List;

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