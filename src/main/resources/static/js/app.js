// Global variables
let authToken = null;
let cart = [];
let categories = [];
let menuItems = [];
let tables = [];
let orders = [];

// API Base URL
const API_BASE = '/api';

// Initialize app
document.addEventListener('DOMContentLoaded', function() {
    loadCustomerData();
    setupEventListeners();
});

// Setup event listeners
function setupEventListeners() {
    // Login form
    document.getElementById('loginForm').addEventListener('submit', handleLogin);
    
    // Menu item form
    document.getElementById('menuItemForm').addEventListener('submit', handleAddMenuItem);
    
    // Category form
    document.getElementById('categoryForm').addEventListener('submit', handleAddCategory);
    
    // Table form
    document.getElementById('tableForm').addEventListener('submit', handleAddTable);
}

// Authentication functions
function showLogin() {
    const modal = new bootstrap.Modal(document.getElementById('loginModal'));
    modal.show();
}

async function handleLogin(e) {
    e.preventDefault();
    
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    
    try {
        const response = await fetch(`${API_BASE}/admin/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ username, password })
        });
        
        if (response.ok) {
            const data = await response.json();
            authToken = data.token;
            localStorage.setItem('authToken', authToken);
            
            bootstrap.Modal.getInstance(document.getElementById('loginModal')).hide();
            showAdminView();
            loadAdminData();
        } else {
            const error = await response.json();
            alert('Login failed: ' + error.error);
        }
    } catch (error) {
        alert('Login failed: ' + error.message);
    }
}

function logout() {
    authToken = null;
    localStorage.removeItem('authToken');
    showCustomerView();
}

function getAuthHeaders() {
    return {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${authToken}`
    };
}

// View switching
function showCustomerView() {
    document.getElementById('customerView').classList.remove('d-none');
    document.getElementById('adminView').classList.add('d-none');
}

function showAdminView() {
    document.getElementById('customerView').classList.add('d-none');
    document.getElementById('adminView').classList.remove('d-none');
}

// Customer data loading
async function loadCustomerData() {
    await Promise.all([
        loadCategories(),
        loadMenuItems()
    ]);
    renderCustomerView();
}

async function loadCategories() {
    try {
        const response = await fetch(`${API_BASE}/categories`);
        categories = await response.json();
    } catch (error) {
        console.error('Error loading categories:', error);
    }
}

async function loadMenuItems() {
    try {
        const response = await fetch(`${API_BASE}/menu/available`);
        menuItems = await response.json();
    } catch (error) {
        console.error('Error loading menu items:', error);
    }
}

function renderCustomerView() {
    renderCategories();
    renderMenuItems();
}

function renderCategories() {
    const categoryList = document.getElementById('categoryList');
    categoryList.innerHTML = '';
    
    categories.forEach(category => {
        const categoryCard = document.createElement('div');
        categoryCard.className = 'col-md-4 col-sm-6 mb-3';
        categoryCard.innerHTML = `
            <div class="card category-card h-100" onclick="filterByCategory(${category.id})">
                <div class="card-body">
                    <h5 class="category-title">${category.name}</h5>
                </div>
            </div>
        `;
        categoryList.appendChild(categoryCard);
    });
}

function renderMenuItems(items = menuItems) {
    const menuItemList = document.getElementById('menuItemList');
    menuItemList.innerHTML = '';
    
    items.forEach(item => {
        const menuItemCard = document.createElement('div');
        menuItemCard.className = 'col-md-4 col-sm-6 mb-4';
        menuItemCard.innerHTML = `
            <div class="card menu-item-card h-100 fade-in">
                ${item.imageUrl ? `<img src="${item.imageUrl}" class="card-img-top menu-item-image" alt="${item.name}">` : ''}
                <div class="card-body menu-item-body">
                    <h5 class="menu-item-title">${item.name}</h5>
                    <p class="menu-item-description">${item.description}</p>
                    <div class="d-flex justify-content-between align-items-center">
                        <span class="menu-item-price">$${item.price.toFixed(2)}</span>
                        <button class="btn btn-success btn-sm" onclick="addToCart(${item.id})">
                            <i class="fas fa-plus"></i> Add
                        </button>
                    </div>
                </div>
            </div>
        `;
        menuItemList.appendChild(menuItemCard);
    });
}

function filterByCategory(categoryId) {
    const filteredItems = menuItems.filter(item => item.category && item.category.id === categoryId);
    renderMenuItems(filteredItems);
}

// Cart functions
function addToCart(itemId) {
    const item = menuItems.find(i => i.id === itemId);
    if (item) {
        const existingItem = cart.find(i => i.id === itemId);
        if (existingItem) {
            existingItem.quantity++;
        } else {
            cart.push({ ...item, quantity: 1 });
        }
        updateCart();
    }
}

function removeFromCart(itemId) {
    cart = cart.filter(item => item.id !== itemId);
    updateCart();
}

function updateQuantity(itemId, delta) {
    const item = cart.find(i => i.id === itemId);
    if (item) {
        item.quantity += delta;
        if (item.quantity <= 0) {
            removeFromCart(itemId);
        } else {
            updateCart();
        }
    }
}

function updateCart() {
    const cartCount = document.getElementById('cartCount');
    const cartItems = document.getElementById('cartItems');
    const cartTotal = document.getElementById('cartTotal');
    
    const totalItems = cart.reduce((sum, item) => sum + item.quantity, 0);
    cartCount.textContent = totalItems;
    
    cartItems.innerHTML = '';
    let total = 0;
    
    cart.forEach(item => {
        const itemTotal = item.price * item.quantity;
        total += itemTotal;
        
        const cartItem = document.createElement('div');
        cartItem.className = 'cart-item';
        cartItem.innerHTML = `
            <div class="d-flex justify-content-between align-items-center">
                <div>
                    <div class="cart-item-name">${item.name}</div>
                    <div class="cart-item-price">$${item.price.toFixed(2)}</div>
                </div>
                <div class="cart-item-quantity">
                    <button class="quantity-btn" onclick="updateQuantity(${item.id}, -1)">-</button>
                    <span>${item.quantity}</span>
                    <button class="quantity-btn" onclick="updateQuantity(${item.id}, 1)">+</button>
                </div>
            </div>
        `;
        cartItems.appendChild(cartItem);
    });
    
    cartTotal.textContent = total.toFixed(2);
}

function toggleCart() {
    const cartSidebar = new bootstrap.Offcanvas(document.getElementById('cartSidebar'));
    cartSidebar.show();
}

async function placeOrder() {
    if (cart.length === 0) {
        alert('Your cart is empty!');
        return;
    }
    
    const order = {
        items: cart.map(item => ({ id: item.id, quantity: item.quantity })),
        tableQrCode: 'table-1', // You might want to get this from URL or user input
        status: 'PENDING'
    };
    
    try {
        const response = await fetch(`${API_BASE}/orders`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(order)
        });
        
        if (response.ok) {
            cart = [];
            updateCart();
            alert('Order placed successfully!');
            bootstrap.Offcanvas.getInstance(document.getElementById('cartSidebar')).hide();
        } else {
            alert('Failed to place order');
        }
    } catch (error) {
        alert('Error placing order: ' + error.message);
    }
}

// Admin functions
async function loadAdminData() {
    if (!authToken) return;
    
    await Promise.all([
        loadAdminCategories(),
        loadAdminMenuItems(),
        loadAdminTables(),
        loadAdminOrders(),
        loadDashboardStats()
    ]);
    renderAdminView();
}

async function loadDashboardStats() {
    try {
        const response = await fetch(`${API_BASE}/admin/dashboard`, {
            headers: getAuthHeaders()
        });
        const stats = await response.json();
        
        document.getElementById('categoryCount').textContent = stats.categoryCount;
        document.getElementById('menuItemCount').textContent = stats.menuItemCount;
        document.getElementById('tableCount').textContent = tables.length;
        document.getElementById('orderCount').textContent = orders.length;
    } catch (error) {
        console.error('Error loading stats:', error);
    }
}

async function loadAdminCategories() {
    try {
        const response = await fetch(`${API_BASE}/categories`);
        categories = await response.json();
    } catch (error) {
        console.error('Error loading categories:', error);
    }
}

async function loadAdminMenuItems() {
    try {
        const response = await fetch(`${API_BASE}/menu`);
        menuItems = await response.json();
    } catch (error) {
        console.error('Error loading menu items:', error);
    }
}

async function loadAdminTables() {
    try {
        const response = await fetch(`${API_BASE}/tables`);
        tables = await response.json();
    } catch (error) {
        console.error('Error loading tables:', error);
    }
}

async function loadAdminOrders() {
    try {
        const response = await fetch(`${API_BASE}/orders/admin`, {
            headers: getAuthHeaders()
        });
        orders = await response.json();
    } catch (error) {
        console.error('Error loading orders:', error);
    }
}

function renderAdminView() {
    renderAdminMenuItems();
    renderAdminCategories();
    renderAdminTables();
    renderAdminOrders();
}

function renderAdminMenuItems() {
    const adminMenuItems = document.getElementById('adminMenuItems');
    adminMenuItems.innerHTML = `
        <div class="table-responsive">
            <table class="table admin-table">
                <thead>
                    <tr>
                        <th>Name</th>
                        <th>Category</th>
                        <th>Price</th>
                        <th>Available</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    ${menuItems.map(item => `
                        <tr>
                            <td>${item.name}</td>
                            <td>${item.category ? item.category.name : 'N/A'}</td>
                            <td>$${item.price.toFixed(2)}</td>
                            <td>
                                <span class="badge ${item.available ? 'bg-success' : 'bg-danger'}">
                                    ${item.available ? 'Available' : 'Unavailable'}
                                </span>
                            </td>
                            <td>
                                <div class="action-buttons">
                                    <button class="btn btn-sm btn-warning" onclick="toggleMenuItemAvailability(${item.id})">
                                        <i class="fas fa-toggle-${item.available ? 'on' : 'off'}"></i>
                                    </button>
                                    <button class="btn btn-sm btn-danger" onclick="deleteMenuItem(${item.id})">
                                        <i class="fas fa-trash"></i>
                                    </button>
                                </div>
                            </td>
                        </tr>
                    `).join('')}
                </tbody>
            </table>
        </div>
    `;
}

function renderAdminCategories() {
    const adminCategories = document.getElementById('adminCategories');
    adminCategories.innerHTML = `
        <div class="table-responsive">
            <table class="table admin-table">
                <thead>
                    <tr>
                        <th>Name</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    ${categories.map(category => `
                        <tr>
                            <td>${category.name}</td>
                            <td>
                                <div class="action-buttons">
                                    <button class="btn btn-sm btn-danger" onclick="deleteCategory(${category.id})">
                                        <i class="fas fa-trash"></i>
                                    </button>
                                </div>
                            </td>
                        </tr>
                    `).join('')}
                </tbody>
            </table>
        </div>
    `;
}

function renderAdminTables() {
    const adminTables = document.getElementById('adminTables');
    adminTables.innerHTML = `
        <div class="row">
            ${tables.map(table => `
                <div class="col-md-4 mb-3">
                    <div class="card table-card">
                        <div class="card-body">
                            <h5 class="table-name">${table.tableName}</h5>
                            <p class="qr-status">
                                ${table.qrCode ? 'QR Code Generated' : 'No QR Code'}
                            </p>
                            ${table.qrCode ? `
                                <div class="qr-code-container">
                                    <img src="${table.qrCode}" alt="QR Code" class="qr-code-image">
                                </div>
                            ` : ''}
                            <div class="mt-3">
                                <button class="btn btn-sm btn-primary" onclick="generateQRCode(${table.id})">
                                    <i class="fas fa-qrcode"></i> Generate QR
                                </button>
                                <button class="btn btn-sm btn-danger" onclick="deleteTable(${table.id})">
                                    <i class="fas fa-trash"></i>
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            `).join('')}
        </div>
    `;
}

function renderAdminOrders() {
    const adminOrders = document.getElementById('adminOrders');
    adminOrders.innerHTML = `
        <div class="table-responsive">
            <table class="table admin-table">
                <thead>
                    <tr>
                        <th>Order ID</th>
                        <th>Table</th>
                        <th>Status</th>
                        <th>Items</th>
                        <th>Total</th>
                    </tr>
                </thead>
                <tbody>
                    ${orders.map(order => `
                        <tr>
                            <td>#${order.id}</td>
                            <td>${order.tableQrCode}</td>
                            <td>
                                <span class="order-status status-${order.status.toLowerCase()}">
                                    ${order.status}
                                </span>
                            </td>
                            <td>${order.items ? order.items.length : 0} items</td>
                            <td>$${order.total || '0.00'}</td>
                        </tr>
                    `).join('')}
                </tbody>
            </table>
        </div>
    `;
}

// Modal functions
function showAddMenuItem() {
    const modal = new bootstrap.Modal(document.getElementById('menuItemModal'));
    modal.show();
    loadCategoryOptions();
}

function showAddCategory() {
    const modal = new bootstrap.Modal(document.getElementById('categoryModal'));
    modal.show();
}

function showAddTable() {
    const modal = new bootstrap.Modal(document.getElementById('tableModal'));
    modal.show();
}

function loadCategoryOptions() {
    const categorySelect = document.getElementById('itemCategory');
    categorySelect.innerHTML = categories.map(cat => 
        `<option value="${cat.id}">${cat.name}</option>`
    ).join('');
}

// Form handlers
async function handleAddMenuItem(e) {
    e.preventDefault();
    
    const formData = {
        name: document.getElementById('itemName').value,
        description: document.getElementById('itemDescription').value,
        price: parseFloat(document.getElementById('itemPrice').value),
        categoryId: parseInt(document.getElementById('itemCategory').value),
        available: document.getElementById('itemAvailable').checked
    };
    
    try {
        const response = await fetch(`${API_BASE}/menu/admin`, {
            method: 'POST',
            headers: getAuthHeaders(),
            body: JSON.stringify(formData)
        });
        
        if (response.ok) {
            const item = await response.json();
            
            // Upload image if provided
            const imageFile = document.getElementById('itemImage').files[0];
            if (imageFile) {
                const imageFormData = new FormData();
                imageFormData.append('file', imageFile);
                
                await fetch(`${API_BASE}/menu/admin/${item.id}/image`, {
                    method: 'POST',
                    headers: {
                        'Authorization': `Bearer ${authToken}`
                    },
                    body: imageFormData
                });
            }
            
            bootstrap.Modal.getInstance(document.getElementById('menuItemModal')).hide();
            document.getElementById('menuItemForm').reset();
            loadAdminMenuItems();
            renderAdminMenuItems();
        } else {
            alert('Failed to add menu item');
        }
    } catch (error) {
        alert('Error adding menu item: ' + error.message);
    }
}

async function handleAddCategory(e) {
    e.preventDefault();
    
    const formData = {
        name: document.getElementById('categoryName').value
    };
    
    try {
        const response = await fetch(`${API_BASE}/categories/admin`, {
            method: 'POST',
            headers: getAuthHeaders(),
            body: JSON.stringify(formData)
        });
        
        if (response.ok) {
            bootstrap.Modal.getInstance(document.getElementById('categoryModal')).hide();
            document.getElementById('categoryForm').reset();
            loadAdminCategories();
            renderAdminCategories();
        } else {
            alert('Failed to add category');
        }
    } catch (error) {
        alert('Error adding category: ' + error.message);
    }
}

async function handleAddTable(e) {
    e.preventDefault();
    
    const formData = {
        tableName: document.getElementById('tableName').value
    };
    
    try {
        const response = await fetch(`${API_BASE}/tables/admin`, {
            method: 'POST',
            headers: getAuthHeaders(),
            body: JSON.stringify(formData)
        });
        
        if (response.ok) {
            bootstrap.Modal.getInstance(document.getElementById('tableModal')).hide();
            document.getElementById('tableForm').reset();
            loadAdminTables();
            renderAdminTables();
        } else {
            alert('Failed to add table');
        }
    } catch (error) {
        alert('Error adding table: ' + error.message);
    }
}

// Action functions
async function toggleMenuItemAvailability(itemId) {
    try {
        const item = menuItems.find(i => i.id === itemId);
        const response = await fetch(`${API_BASE}/menu/admin/${itemId}/availability?status=${!item.available}`, {
            method: 'PATCH',
            headers: getAuthHeaders()
        });
        
        if (response.ok) {
            loadAdminMenuItems();
            renderAdminMenuItems();
        } else {
            alert('Failed to update availability');
        }
    } catch (error) {
        alert('Error updating availability: ' + error.message);
    }
}

async function deleteMenuItem(itemId) {
    if (!confirm('Are you sure you want to delete this menu item?')) return;
    
    try {
        const response = await fetch(`${API_BASE}/menu/admin/${itemId}`, {
            method: 'DELETE',
            headers: getAuthHeaders()
        });
        
        if (response.ok) {
            loadAdminMenuItems();
            renderAdminMenuItems();
        } else {
            alert('Failed to delete menu item');
        }
    } catch (error) {
        alert('Error deleting menu item: ' + error.message);
    }
}

async function deleteCategory(categoryId) {
    if (!confirm('Are you sure you want to delete this category?')) return;
    
    try {
        const response = await fetch(`${API_BASE}/categories/admin/${categoryId}`, {
            method: 'DELETE',
            headers: getAuthHeaders()
        });
        
        if (response.ok) {
            loadAdminCategories();
            renderAdminCategories();
        } else {
            alert('Failed to delete category');
        }
    } catch (error) {
        alert('Error deleting category: ' + error.message);
    }
}

async function deleteTable(tableId) {
    if (!confirm('Are you sure you want to delete this table?')) return;
    
    try {
        const response = await fetch(`${API_BASE}/tables/admin/${tableId}`, {
            method: 'DELETE',
            headers: getAuthHeaders()
        });
        
        if (response.ok) {
            loadAdminTables();
            renderAdminTables();
        } else {
            alert('Failed to delete table');
        }
    } catch (error) {
        alert('Error deleting table: ' + error.message);
    }
}

async function generateQRCode(tableId) {
    try {
        const response = await fetch(`${API_BASE}/tables/admin/generateQR/${tableId}`, {
            method: 'POST',
            headers: getAuthHeaders()
        });
        
        if (response.ok) {
            loadAdminTables();
            renderAdminTables();
        } else {
            alert('Failed to generate QR code');
        }
    } catch (error) {
        alert('Error generating QR code: ' + error.message);
    }
}

// Check for existing auth token on page load
window.addEventListener('load', function() {
    const savedToken = localStorage.getItem('authToken');
    if (savedToken) {
        authToken = savedToken;
        showAdminView();
        loadAdminData();
    }
});
