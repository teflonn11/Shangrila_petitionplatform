// admin-dashboard.js
const API_BASE_URL = 'http://localhost:8080/api';

// Check authentication on page load
document.addEventListener('DOMContentLoaded', () => {
    const token = localStorage.getItem('slpp_token');
    if (!token) {
        window.location.href = 'index.html';
        return;
    }
    loadDashboardData();
});

// Load dashboard data
async function loadDashboardData() {
    try {
        const [statistics, pendingPetitions] = await Promise.all([
            fetchStatistics(),
            fetchPendingPetitions()
        ]);
        updateStatistics(statistics);
        displayPendingPetitions(pendingPetitions);
    } catch (error) {
        showToast('Error loading dashboard data', 'error');
    }
}

// Fetch committee statistics
async function fetchStatistics() {
    const response = await fetchWithAuth(`${API_BASE_URL}/committee/statistics`);
    return response.json();
}

// Fetch pending petitions
async function fetchPendingPetitions() {
    const response = await fetchWithAuth(`${API_BASE_URL}/committee/pending`);
    if (!response.ok) {
        const error = await response.json();
        throw new Error(error.message || 'Failed to fetch pending petitions');
    }
    return response.json();
}

// Update statistics display
function updateStatistics(stats) {
    document.getElementById('total-petitions').textContent = stats.totalPetitions;
    document.getElementById('pending-count').textContent = stats.pendingCount;
    document.getElementById('current-threshold').textContent = stats.currentThreshold;
    document.getElementById('closed-count').textContent = stats.closedPetitions;
}

// Display pending petitions
function displayPendingPetitions(petitions) {
    const petitionsList = document.getElementById('petitions-list');
    petitionsList.innerHTML = '';

    petitions.forEach(petition => {
        const petitionElement = createPetitionElement(petition);
        petitionsList.appendChild(petitionElement);
    });
}

// Create petition element
function createPetitionElement(petition) {
    const div = document.createElement('div');
    div.className = 'bg-white p-6 rounded-lg shadow mb-4';
    div.innerHTML = `
        <div class="flex justify-between items-start">
            <div class="flex-1">
                <h3 class="text-xl font-semibold mb-2">${petition.title}</h3>
                <p class="text-gray-600 mb-4">${petition.content.substring(0, 200)}...</p>
                <div class="flex space-x-4 text-sm text-gray-500">
                    <span>Signatures: ${petition.signatureCount}</span>
                    <span>Created: ${new Date(petition.createdAt).toLocaleDateString()}</span>
                    <span>By: ${petition.petitionerEmail}</span>
                </div>
            </div>
            <button onclick="openResponseModal('${petition.id}')"
                    class="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700">
                Write Response
            </button>
        </div>
    `;
    return div;
}

// Handle threshold form submission
document.getElementById('threshold-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    const threshold = document.getElementById('signature-threshold').value;

    try {
        const response = await fetchWithAuth(`${API_BASE_URL}/committee/threshold`, {
            method: 'POST',
            body: JSON.stringify({ threshold: parseInt(threshold) })
        });

        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.message || 'Failed to update threshold');
        }

        showToast('Threshold updated successfully', 'success');
        loadDashboardData();
    } catch (error) {
        showToast(error.message, 'error');
    }
});

// Response modal functions
function openResponseModal(petitionId) {
    document.getElementById('petition-id').value = petitionId;
    document.getElementById('response-modal').classList.remove('hidden');
}

function closeResponseModal() {
    document.getElementById('response-modal').classList.add('hidden');
    document.getElementById('response-form').reset();
}

// Handle response form submission
document.getElementById('response-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    const petitionId = document.getElementById('petition-id').value;
    const response = document.getElementById('response').value;

    try {
        const apiResponse = await fetchWithAuth(
            `${API_BASE_URL}/committee/petitions/${petitionId}/response`,
            {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ response })
            }
        );

        if (apiResponse.ok) {
            showToast('Response submitted successfully', 'success');
            closeResponseModal();
            loadDashboardData();
        } else {
            throw new Error('Failed to submit response');
        }
    } catch (error) {
        showToast('Error submitting response', 'error');
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

// Navigation handlers
document.getElementById('logout').addEventListener('click', (e) => {
    e.preventDefault();
    localStorage.removeItem('slpp_token');
    window.location.href = 'index.html';
});