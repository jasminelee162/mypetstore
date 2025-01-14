package csu.web.mypetstore.persistence;

import java.util.List;
import csu.web.mypetstore.domain.Category;
public interface CategoryDao {
    List<Category> getCategoryList();

    Category getCategory(String categoryId);
}
