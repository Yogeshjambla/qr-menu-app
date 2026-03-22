package com.food.config;

import com.food.entity.Admin;
import com.food.entity.Category;
import com.food.entity.MenuItem;
import com.food.entity.TableQR;
import com.food.repository.AdminRepository;
import com.food.repository.CategoryRepository;
import com.food.repository.MenuItemRepository;
import com.food.repository.TableQRRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private TableQRRepository tableQRRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        initializeAdmin();
        initializeCategories();
        initializeMenuItems();
        initializeTables();
    }

    private void initializeAdmin() {
        if (adminRepository.count() == 0) {
            Admin admin = new Admin();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            adminRepository.save(admin);
            System.out.println("Default admin user created: admin/admin123");
        }
    }

    private void initializeCategories() {
        if (categoryRepository.count() == 0) {
            Category appetizers = new Category();
            appetizers.setName("Appetizers");
            categoryRepository.save(appetizers);

            Category mainCourse = new Category();
            mainCourse.setName("Main Course");
            categoryRepository.save(mainCourse);

            Category desserts = new Category();
            desserts.setName("Desserts");
            categoryRepository.save(desserts);

            Category beverages = new Category();
            beverages.setName("Beverages");
            categoryRepository.save(beverages);

            System.out.println("Default categories created");
        }
    }

    private void initializeMenuItems() {
        if (menuItemRepository.count() == 0) {
            Category appetizers = categoryRepository.findByName("Appetizers").orElse(null);
            Category mainCourse = categoryRepository.findByName("Main Course").orElse(null);
            Category desserts = categoryRepository.findByName("Desserts").orElse(null);
            Category beverages = categoryRepository.findByName("Beverages").orElse(null);

            if (appetizers != null) {
                MenuItem springRolls = new MenuItem();
                springRolls.setName("Spring Rolls");
                springRolls.setDescription("Crispy vegetable spring rolls with sweet chili sauce");
                springRolls.setPrice(6.99);
                springRolls.setCategory(appetizers);
                springRolls.setAvailable(true);
                menuItemRepository.save(springRolls);

                MenuItem salad = new MenuItem();
                salad.setName("Caesar Salad");
                salad.setDescription("Fresh romaine lettuce with parmesan cheese and croutons");
                salad.setPrice(8.99);
                salad.setCategory(appetizers);
                salad.setAvailable(true);
                menuItemRepository.save(salad);
            }

            if (mainCourse != null) {
                MenuItem pasta = new MenuItem();
                pasta.setName("Spaghetti Carbonara");
                pasta.setDescription("Classic Italian pasta with bacon, eggs, and parmesan cheese");
                pasta.setPrice(14.99);
                pasta.setCategory(mainCourse);
                pasta.setAvailable(true);
                menuItemRepository.save(pasta);

                MenuItem steak = new MenuItem();
                steak.setName("Grilled Steak");
                steak.setDescription("Premium beef steak grilled to perfection with herbs");
                steak.setPrice(24.99);
                steak.setCategory(mainCourse);
                steak.setAvailable(true);
                menuItemRepository.save(steak);

                MenuItem salmon = new MenuItem();
                salmon.setName("Grilled Salmon");
                salmon.setDescription("Fresh Atlantic salmon with lemon butter sauce");
                salmon.setPrice(18.99);
                salmon.setCategory(mainCourse);
                salmon.setAvailable(true);
                menuItemRepository.save(salmon);
            }

            if (desserts != null) {
                MenuItem tiramisu = new MenuItem();
                tiramisu.setName("Tiramisu");
                tiramisu.setDescription("Classic Italian dessert with coffee and mascarpone");
                tiramisu.setPrice(6.99);
                tiramisu.setCategory(desserts);
                tiramisu.setAvailable(true);
                menuItemRepository.save(tiramisu);

                MenuItem iceCream = new MenuItem();
                iceCream.setName("Ice Cream Sundae");
                iceCream.setDescription("Vanilla ice cream with chocolate sauce and toppings");
                iceCream.setPrice(4.99);
                iceCream.setCategory(desserts);
                iceCream.setAvailable(true);
                menuItemRepository.save(iceCream);
            }

            if (beverages != null) {
                MenuItem coffee = new MenuItem();
                coffee.setName("Coffee");
                coffee.setDescription("Freshly brewed coffee");
                coffee.setPrice(2.99);
                coffee.setCategory(beverages);
                coffee.setAvailable(true);
                menuItemRepository.save(coffee);

                MenuItem juice = new MenuItem();
                juice.setName("Fresh Orange Juice");
                juice.setDescription("Freshly squeezed orange juice");
                juice.setPrice(3.99);
                juice.setCategory(beverages);
                juice.setAvailable(true);
                menuItemRepository.save(juice);
            }

            System.out.println("Default menu items created");
        }
    }

    private void initializeTables() {
        if (tableQRRepository.count() == 0) {
            TableQR table1 = new TableQR();
            table1.setTableName("Table 1");
            tableQRRepository.save(table1);

            TableQR table2 = new TableQR();
            table2.setTableName("Table 2");
            tableQRRepository.save(table2);

            TableQR table3 = new TableQR();
            table3.setTableName("Table 3");
            tableQRRepository.save(table3);

            TableQR table4 = new TableQR();
            table4.setTableName("Table 4");
            tableQRRepository.save(table4);

            System.out.println("Default tables created");
        }
    }
}
