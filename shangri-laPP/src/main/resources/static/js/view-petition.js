// view-petition.js
const API_BASE_URL = 'http://localhost:8080/api';

// Get petition ID from URL parameters
const urlParams = new URLSearchParams(window.location.search);
const petitionId = urlParams.get('id');

if (!petitionId) {
    window.location.href = 'index.html';
}

// Check authentication on page load
document.addEventListener('DOMContentLoaded', () => {
    const token = localStorage.getItem('slpp_token');
    if (token) {
        document.querySelectorAll('.logged-in-only').forEach(el => el.classList.remove('hidden'));
    }
    loadPetitionData();
});

// Load petition data
async function loadPetitionData() {
    try {
        const [petition, signatures] = await Promise.all([
            fetchPetition(),
            fetchSignatures()
        ]);
        displayPetition(petition);
        displaySignatures(signatures);
    } catch (error) {
        showToast('Error loading petition data', 'error');
    }
}

// Fetch petition details
async function fetchPetition() {
    const response = await fetch(`${API_BASE_URL}/petitions/${petitionId}`, {
        headers: {
            'Accept': 'application/json'
        },
        credentials: 'include'
    });
    if (!response.ok) {
        throw new Error('Failed to fetch petition');
    }
    return response.json();
}
// Fetch petition signatures
async function fetchSignatures() {
    const response = await fetch(`${API_BASE_URL}/petitions/${petitionId}/signatures`, {
        headers: {
            'Accept': 'application/json'
        },
        credentials: 'include'
    });
    if (!response.ok) {
        throw new Error('Failed to fetch signatures');
    }
    return response.json();
}

// Display petition details
function displayPetition(petition) {
    document.getElementById('petition-title').textContent = petition.title;
    document.getElementById('petition-author').textContent = petition.petitionerEmail;
    document.getElementById('petition-status').textContent = petition.status;
    document.getElementById('signature-count').textContent = petition.signatureCount;
    document.getElementById('petition-content').textContent = petition.content;

    if (petition.response) {
        document.getElementById('committee-response').classList.remove('hidden');
        document.getElementById('response-content').textContent = petition.response;
    }

    const signButton = document.getElementById('sign-petition');
    if (petition.status === 'open' && localStorage.getItem('slpp_token')) {
        signButton.classList.remove('hidden');
    }
}

// Display signatures
function displaySignatures(signatures) {
    const signaturesList = document.getElementById('signatures-list');
    signaturesList.innerHTML = '';

    signatures.forEach(signature => {
        const div = document.createElement('div');
        div.className = 'border-b py-2';
        div.innerHTML = `
            <div class="flex justify-between items-center">
                <span>${signature.petitionerName}</span>
                <span class="text-gray-500 text-sm">
                    ${new Date(signature.signedAt).toLocaleString()}
                </span>
            </div>
        `;
        signaturesList.appendChild(div);
    });
}

// Handle petition signing
document.getElementById('sign-petition').addEventListener('click', async () => {
    try {
        const response = await fetchWithAuth(`${API_BASE_URL}/petitions/${petitionId}/sign`, {
            method: 'POST'
        });

        if (response.ok) {
            showToast('Petition signed successfully', 'success');
            loadPetitionData();
        } else {
            throw new Error('Failed to sign petition');
        }
    } catch (error) {
        showToast('Error signing petition', 'error');
    }
});

// Helper function for authenticated fetch requests
async function fetchWithAuth(url, options = {}) {
    const token = localStorage.getItem('slpp_token');
    if (!token) {
        throw new Error('No authentication token');
    }

    const headers = {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'Authorization': `Bearer ${token}`,
        'Origin': 'http://localhost:8080'
    };

    const config = {
        ...options,
        headers: headers,
        credentials: 'include',
        mode: 'cors'
    };

    try {
        const response = await fetch(url, config);
        if (response.status === 401) {
            localStorage.removeItem('slpp_token');
            window.location.href = 'index.html';
        }
        return response;
    } catch (error) {
        console.error('Fetch error:', error);
        throw error;
    }
}

// Show toast messages
function showToast(message, type = 'info') {
    const toast = document.createElement('div');
    toast.className = `fixed bottom-4 right-4 px-6 py-3 rounded-lg ${
        type === 'error' ? 'bg-red-500' : 'bg-green-500'
    } text-white`;
    toast.textContent = message;
    document.body.appendChild(toast);
    setTimeout(() => toast.remove(), 3000);
}

// Handle logout
document.getElementById('logout').addEventListener('click', (e) => {
    e.preventDefault();
    localStorage.removeItem('slpp_token');
    window.location.href = 'index.html';
});