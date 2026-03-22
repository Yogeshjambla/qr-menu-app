# Food Menu QR Code System

A complete full-stack restaurant management system with QR code menu functionality built with Spring Boot and modern web technologies.

## Features

### 🍽️ Customer Features
- **Digital Menu**: Browse menu items by category
- **QR Code Access**: Scan QR codes to access the menu
- **Shopping Cart**: Add items to cart and place orders
- **Responsive Design**: Works on all devices

### 👨‍💼 Admin Features
- **Dashboard**: Overview of restaurant statistics
- **Menu Management**: Add, edit, delete menu items
- **Category Management**: Organize menu items by categories
- **Table Management**: Create tables and generate QR codes
- **Order Management**: View and manage customer orders
- **Image Upload**: Add images to menu items
- **JWT Authentication**: Secure admin access

### 🔧 Technical Features
- **Spring Boot 3.4.5**: Modern Java backend
- **Spring Security**: JWT-based authentication
- **JPA/Hibernate**: Database management
- **MySQL**: Database storage
- **QR Code Generation**: ZXing library
- **File Upload**: Image handling
- **Bootstrap 5**: Modern UI framework
- **Responsive Design**: Mobile-friendly interface

## Quick Start

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher
- MySQL 8.0 or higher

### Database Setup
1. Create a MySQL database named `foodmenu`
2. Update database credentials in `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/foodmenu?createDatabaseIfNotExist=true
   spring.datasource.username=root
   spring.datasource.password=your_password
   ```

### Running the Application

1. **Clone the repository** (if applicable)
2. **Navigate to the project directory**
3. **Build and run the application**:
   ```bash
   ./mvnw.cmd clean spring-boot:run
   ```
   (on Windows use `mvnw.cmd`, on Linux/Mac use `mvnw`)

4. **Access the application**:
   - **Customer Interface**: http://localhost:8080
   - **Admin Login**: http://localhost:8080 (click "Admin Login")

### Default Credentials
- **Username**: admin
- **Password**: admin123

## API Endpoints

### Authentication
- `POST /api/admin/login` - Admin login
- `POST /api/admin/register` - Register new admin

### Menu Management
- `GET /api/menu` - Get all menu items
- `GET /api/menu/available` - Get available menu items
- `POST /api/menu/admin` - Add menu item (admin only)
- `PUT /api/menu/admin/{id}` - Update menu item (admin only)
- `DELETE /api/menu/admin/{id}` - Delete menu item (admin only)
- `POST /api/menu/admin/{id}/image` - Upload menu item image (admin only)

### Category Management
- `GET /api/categories` - Get all categories
- `POST /api/categories/admin` - Add category (admin only)
- `DELETE /api/categories/admin/{id}` - Delete category (admin only)

### Table Management
- `GET /api/tables` - Get all tables
- `POST /api/tables/admin` - Add table (admin only)
- `POST /api/tables/admin/generateQR/{id}` - Generate QR code for table (admin only)
- `DELETE /api/tables/admin/{id}` - Delete table (admin only)

### Order Management
- `POST /api/orders` - Place order
- `GET /api/orders/admin` - Get all orders (admin only)

### Dashboard
- `GET /api/admin/dashboard` - Get dashboard statistics (admin only)

## Project Structure

```
src/
├── main/
│   ├── java/com/food/
│   │   ├── config/          # Security and configuration
│   │   ├── controller/      # REST controllers
│   │   ├── entity/          # JPA entities
│   │   ├── repository/      # JPA repositories
│   │   ├── security/        # JWT security components
│   │   └── service/         # Business logic
│   └── resources/
│       ├── static/          # Frontend assets
│       │   ├── css/         # Stylesheets
│       │   ├── js/          # JavaScript
│       │   └── index.html   # Main frontend page
│       └── application.properties  # Configuration
```

## Configuration

### Application Properties
Key configuration options in `application.properties`:

```properties
# Database
spring.datasource.url=jdbc:mysql://localhost:3306/foodmenu
spring.datasource.username=root
spring.datasource.password=12345

# File Upload
app.upload.directory=./uploads
app.qr.code.directory=./qrcodes
app.base.url=http://localhost:8080

# File upload size limits
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
```

## Data Initialization

The application automatically creates sample data on first startup:
- Default admin user (admin/admin123)
- Sample categories (Appetizers, Main Course, Desserts, Beverages)
- Sample menu items
- Sample tables

## Security

- JWT-based authentication for admin users
- Role-based access control
- Public access to customer-facing endpoints
- File upload security measures
- CORS configuration for frontend integration

## File Storage

- **Menu Item Images**: Stored in `./uploads/menu-items/`
- **QR Codes**: Stored in `./qrcodes/`
- **Access URLs**: 
  - Images: `http://localhost:8080/uploads/menu-items/{filename}`
  - QR Codes: `http://localhost:8080/qrcodes/{filename}`

## Development

### Adding New Features
1. Create/update entities in `entity/` package
2. Add repositories in `repository/` package
3. Implement services in `service/` package
4. Add controllers in `controller/` package
5. Update frontend in `static/` directory

### Database Changes
- JPA auto-DDL is enabled (`spring.jpa.hibernate.ddl-auto=update`)
- For production, consider using Flyway or Liquibase for migrations

### Testing
- Run tests with: `./mvnw test`
- Integration tests can be added in `src/test/java/`

## Production Deployment

### Database Considerations
- Use a production MySQL instance
- Configure connection pooling
- Set up proper backups

### Security Considerations
- Change default admin password
- Use HTTPS in production
- Configure proper CORS settings
- Set up firewall rules

### Performance Considerations
- Enable database connection pooling
- Configure static file caching
- Use CDN for static assets
- Consider Redis for session storage

## Troubleshooting

### Common Issues
1. **Database Connection Error**: Check MySQL service and credentials
2. **File Upload Issues**: Ensure upload directories exist and have proper permissions
3. **QR Code Generation**: Check ZXing library dependencies
4. **Authentication Issues**: Verify JWT configuration

### Logs
Check application logs for detailed error information:
```bash
tail -f logs/application.log
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For support and questions:
- Create an issue in the repository
- Check the troubleshooting section
- Review the API documentation

---

**Built with ❤️ using Spring Boot and modern web technologies**
