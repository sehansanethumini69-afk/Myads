const API_URL = 'http://localhost:8080/api';
const TOKEN_KEY = 'myads_token';
const USER_KEY = 'myads_user';

// Check if user is logged in
function isLoggedIn() {
    return !!localStorage.getItem(TOKEN_KEY);
}

// Get current token
function getToken() {
    return localStorage.getItem(TOKEN_KEY);
}

// Get current user
function getCurrentUser() {
    const user = localStorage.getItem(USER_KEY);
    return user ? JSON.parse(user) : null;
}

// Check if user is admin
function isAdmin() {
    const user = getCurrentUser();
    return user && user.role === 'ADMIN';
}

// Register user
async function register(name, email, password, phoneNumber) {
    try {
        const response = await fetch(`${API_URL}/auth/register`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ name, email, password, phoneNumber })
        });

        const data = await response.json();
        
        if (!response.ok) {
            throw new Error(data.message || 'Registration failed');
        }

        return { success: true, message: data.message };
    } catch (error) {
        return { success: false, message: error.message };
    }
}

// Login user
async function login(email, password) {
    try {
        const response = await fetch(`${API_URL}/auth/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ email, password })
        });

        const data = await response.json();
        
        if (!response.ok) {
            throw new Error(data.message || 'Login failed');
        }

        // Store token and user info
        localStorage.setItem(TOKEN_KEY, data.token);
        
        // Decode token to get user info (simplified)
        const payload = JSON.parse(atob(data.token.split('.')[1]));
        localStorage.setItem(USER_KEY, JSON.stringify({
            email: payload.sub,
            role: payload.role || 'USER'
        }));

        return { success: true, token: data.token };
    } catch (error) {
        return { success: false, message: error.message };
    }
}

// Logout user
function logout() {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(USER_KEY);
    window.location.href = 'login.html';
}

// Update UI based on auth state
function updateAuthUI() {
    const authLinks = document.getElementById('authLinks');
    if (!authLinks) return;

    if (isLoggedIn()) {
        authLinks.innerHTML = `
            <a href="my-ads.html" class="nav-link">My Ads</a>
            <a href="profile.html" class="nav-link">Profile</a>
            <button onclick="logout()" class="btn btn-outline">Logout</button>
        `;
    } else {
        authLinks.innerHTML = `
            <a href="login.html" class="nav-link">Login</a>
            <a href="register.html" class="btn btn-primary">Register</a>
        `;
    }
}

// Protect route (redirect if not logged in)
function requireAuth() {
    if (!isLoggedIn()) {
        window.location.href = 'login.html?redirect=' + encodeURIComponent(window.location.pathname);
        return false;
    }
    return true;
}

// Protect admin route
function requireAdmin() {
    if (!isLoggedIn() || !isAdmin()) {
        window.location.href = 'index.html';
        return false;
    }
    return true;
}

// Initialize auth on page load
document.addEventListener('DOMContentLoaded', updateAuthUI);