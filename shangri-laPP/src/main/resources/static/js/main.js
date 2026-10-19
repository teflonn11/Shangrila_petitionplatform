// Constants
const API_BASE_URL = 'http://localhost:8080/api';
const TOKEN_KEY = 'slpp_token';

// DOM Elements
const navLinks = document.getElementById('nav-links');
const sections = {
    home: document.getElementById('home-section'),
    login: document.getElementById('login-section'),
    register: document.getElementById('register-section'),
    dashboard: document.getElementById('dashboard-section')
};

// Navigation Functions
function showSection(sectionId) {
    Object.values(sections).forEach(section => section.classList.add('hidden'));
    sections[sectionId].classList.remove('hidden');
}

// Event Listeners
document.getElementById('home-link').addEventListener('click', (e) => {
    e.preventDefault();
    showSection('home');
});

document.getElementById('login-link').addEventListener('click', (e) => {
    e.preventDefault();
    showSection('login');
});

document.getElementById('register-link').addEventListener('click', (e) => {
    e.preventDefault();
    showSection('register');
});

// Authentication Functions
async function login(email, password) {
    try {
        const response = await fetch(`${API_BASE_URL}/auth/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            credentials: 'include',
            body: JSON.stringify({ email, password })
        });

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.message || 'Login failed');
        }

        const data = await response.json();
        localStorage.setItem('slpp_token', data.token);
        window.location.href = '/dashboard.html';
    } catch (error) {
        showToast(error.message, 'error');
    }
}


async function register(formData) {
    try {
        const response = await fetch(`${API_BASE_URL}/petitioners/register`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            credentials: 'include',
            body: JSON.stringify(formData)
        });

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.message || 'Registration failed');
        }

        showToast('Registration successful. Please login.', 'success');
        showSection('login');
    } catch (error) {
        showToast(error.message, 'error');
    }
}

// Form Submissions
document.getElementById('login-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    const email = document.getElementById('login-email').value;
    const password = document.getElementById('login-password').value;
    await login(email, password);
});

document.getElementById('register-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    const formData = {
        email: document.getElementById('register-email').value,
        fullName: document.getElementById('register-name').value,
        dateOfBirth: document.getElementById('register-dob').value,
        password: document.getElementById('register-password').value,
        bioId: document.getElementById('register-bioid').value
    };
    await register(formData);
});

// Petition Functions
async function loadPetitions() {
    try {
        const response = await fetch(`${API_BASE_URL}/petitions`, {
            headers: {
                'Accept': 'application/json'
            },
            credentials: 'include'
        });

        if (!response.ok) {
            throw new Error('Failed to load petitions');
        }

        const petitions = await response.json();
        displayPetitions(petitions);
    } catch (error) {
        showToast(error.message, 'error');
    }
}

// Helper function for authenticated fetch requests
async function fetchWithAuth(url, options = {}) {
    const token = localStorage.getItem(TOKEN_KEY);
    if (!token) {
        throw new Error('No authentication token');
    }

    const headers = {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'Authorization': `Bearer ${token}`,
        ...options.headers
    };

    const config = {
        ...options,
        headers: headers,
        credentials: 'include'
    };

    const response = await fetch(url, config);
    if (response.status === 401) {
        localStorage.removeItem(TOKEN_KEY);
        window.location.href = 'index.html';
    }
    return response;
}

// Update navigation based on authentication status
// function updateNavigation(isAuthenticated) {
//     const loginLink = document.getElementById('login-link');
//     const registerLink = document.getElementById('register-link');
//     const dashboardLink = document.getElementById('dashboard-link');
//     const logoutLink = document.getElementById('logout-link');
//
//     if (isAuthenticated) {
//         loginLink.classList.add('hidden');
//         registerLink.classList.add('hidden');
//         dashboardLink.classList.remove('hidden');
//         logoutLink.classList.remove('hidden');
//     } else {
//         loginLink.classList.remove('hidden');
//         registerLink.classList.remove('hidden');
//         dashboardLink.classList.add('hidden');
//         logoutLink.classList.add('hidden');
//     }
// }

// Show toast messages
function showToast(message, type = 'info') {
    const toast = document.createElement('div');
    toast.className = `fixed bottom-4 right-4 px-6 py-3 rounded-lg ${
        type === 'error' ? 'bg-red-500' : 'bg-green-500'
    } text-white z-50`;
    toast.textContent = message;
    document.body.appendChild(toast);
    setTimeout(() => toast.remove(), 3000);
}

// // Check authentication status on page load
// document.addEventListener('DOMContentLoaded', () => {
//     const token = localStorage.getItem(TOKEN_KEY);
//     updateNavigation(!!token);
//     loadPetitions();
// });