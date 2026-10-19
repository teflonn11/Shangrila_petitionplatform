// dashboard.js
const API_BASE_URL = 'http://localhost:8080/api';

document.addEventListener('DOMContentLoaded', () => {
    // Get token once at the start
    const token = localStorage.getItem('slpp_token');
    if (!token) {
        window.location.href = 'index.html';
        return;
    }

    // Helper function for authenticated fetch requests
    async function fetchWithAuth(url, options = {}) {
        const headers = {
            'Content-Type': 'application/json',
            'Accept': 'application/json',
            'Authorization': `Bearer ${token}`,
            ...options.headers
        };

        const response = await fetch(url, {
            ...options,
            headers,
            credentials: 'include'
        });

        if (response.status === 401) {
            localStorage.removeItem('slpp_token');
            window.location.href = 'index.html';
            return;
        }
        return response;
    }

    // Updated petition form submission in dashboard.js
    const petitionForm = document.getElementById('petition-form');
    if (petitionForm) {
        petitionForm.addEventListener('submit', async (e) => {
            e.preventDefault();

            const formData = {
                title: document.getElementById('title').value,
                content: document.getElementById('content').value
            };

            console.log('Sending petition data:', formData); // Debug log

            try {
                const response = await fetchWithAuth(`${API_BASE_URL}/petitions`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'Accept': 'application/json',
                        'Authorization': `Bearer ${token}`
                    },
                    body: JSON.stringify(formData)
                });

                // Debug log for response
                const responseData = await response.json();
                console.log('Response:', responseData);

                if (!response.ok) {
                    throw new Error(responseData.message || 'Failed to create petition');
                }

                showToast('Petition created successfully', 'success');
                petitionForm.reset();
                loadDashboardData();
            } catch (error) {
                console.error('Full error details:', error); // Debug log
                showToast('Error creating petition: ' + error.message, 'error');
            }
        });
    }

    // Load dashboard data
    async function loadDashboardData() {
        try {
            const [statistics, petitions] = await Promise.all([
                fetchStatistics(),
                fetchMyPetitions()
            ]);
            updateStatistics(statistics);
            displayPetitions(petitions);
        } catch (error) {
            showToast('Error loading dashboard data', 'error');
        }
    }

    // Fetch statistics
    async function fetchStatistics() {
        const response = await fetchWithAuth(`${API_BASE_URL}/petitioners/statistics`);
        if (!response.ok) {
            throw new Error('Failed to fetch statistics');
        }
        return response.json();
    }

    // Fetch petitions
    async function fetchMyPetitions() {
        const response = await fetchWithAuth(`${API_BASE_URL}/petitioners/petitions`);
        if (!response.ok) {
            throw new Error('Failed to fetch petitions');
        }
        return response.json();
    }

    // Update statistics display
    function updateStatistics(stats) {
        document.getElementById('petition-count').textContent = stats.totalPetitions || 0;
        document.getElementById('signature-count').textContent = stats.totalSignatures || 0;
        document.getElementById('active-count').textContent = stats.activePetitions || 0;
    }

    // Display petitions
    function displayPetitions(petitions) {
        const petitionsList = document.getElementById('petitions-list');
        if (!petitionsList) return;

        petitionsList.innerHTML = '';
        petitions.forEach(petition => {
            const element = createPetitionElement(petition);
            petitionsList.appendChild(element);
        });
    }

    // Create petition element
    function createPetitionElement(petition) {
        const div = document.createElement('div');
        div.className = 'bg-white p-6 rounded-lg shadow mb-4';
        div.innerHTML = `
            <div class="flex justify-between items-start">
                <div>
                    <h3 class="text-xl font-semibold mb-2">${petition.title}</h3>
                    <p class="text-gray-600 mb-4">${petition.content.substring(0, 200)}...</p>
                    <div class="flex space-x-4 text-sm text-gray-500">
                        <span>Signatures: ${petition.signatureCount}</span>
                        <span>Status: ${petition.status}</span>
                        <span>Created: ${new Date(petition.createdAt).toLocaleDateString()}</span>
                    </div>
                </div>
            </div>
        `;
        return div;
    }

    // Navigation handlers
    document.getElementById('create-petition')?.addEventListener('click', (e) => {
        e.preventDefault();
        showSection('create-petition-section');
    });

    document.getElementById('my-petitions')?.addEventListener('click', (e) => {
        e.preventDefault();
        showSection('my-petitions-section');
    });

    document.getElementById('logout')?.addEventListener('click', (e) => {
        e.preventDefault();
        localStorage.removeItem('slpp_token');
        window.location.href = 'index.html';
    });

    // Show/hide sections
    function showSection(sectionId) {
        const sections = ['create-petition-section', 'my-petitions-section'];
        sections.forEach(id => {
            const section = document.getElementById(id);
            if (section) {
                section.classList.toggle('hidden', id !== sectionId);
            }
        });
    }

    // Show toast message
    function showToast(message, type = 'info') {
        const toast = document.createElement('div');
        toast.className = `fixed bottom-4 right-4 px-6 py-3 rounded-lg ${
            type === 'error' ? 'bg-red-500' : 'bg-green-500'
        } text-white z-50`;
        toast.textContent = message;
        document.body.appendChild(toast);
        setTimeout(() => toast.remove(), 3000);
    }

    // Load initial data
    loadDashboardData();
});





















// // dashboard.js
// const API_BASE_URL = 'http://localhost:8080/api';
//
// // Check authentication on page load
// document.addEventListener('DOMContentLoaded', () => {
//     const token = localStorage.getItem('slpp_token');
//     if (!token) {
//         window.location.href = '/index.html';
//         return;
//     }
//     loadDashboardData();
// });
//
// // Load dashboard statistics and petitions
// async function loadDashboardData() {
//     try {
//         const [statistics, petitions] = await Promise.all([
//             fetchStatistics(),
//             fetchMyPetitions()
//         ]);
//         updateStatistics(statistics);
//         displayPetitions(petitions);
//     } catch (error) {
//         showToast('Error loading dashboard data', 'error');
//     }
// }
//
// // Fetch user's statistics
// async function fetchStatistics() {
//     const response = await fetchWithAuth(`${API_BASE_URL}/petitioners/statistics`);
//     if (!response.ok) {
//         throw new Error('Failed to fetch statistics');
//     }
//     return response.json();
// }
// // Fetch user's petitions
// async function fetchMyPetitions() {
//     const response = await fetchWithAuth(`${API_BASE_URL}/petitioners/petitions`);
//     if (!response.ok) {
//         throw new Error('Failed to fetch petitions');
//     }
//     return response.json();
// }
//
// // Update statistics display
// function updateStatistics(stats) {
//     document.getElementById('petition-count').textContent = stats.totalPetitions;
//     document.getElementById('signature-count').textContent = stats.totalSignatures;
//     document.getElementById('active-count').textContent = stats.activePetitions;
// }
//
// // Display petitions in the list
// function displayPetitions(petitions) {
//     const petitionsList = document.getElementById('petitions-list');
//     petitionsList.innerHTML = '';
//
//     petitions.forEach(petition => {
//         const petitionElement = createPetitionElement(petition);
//         petitionsList.appendChild(petitionElement);
//     });
// }
//
// // Create petition list item
// function createPetitionElement(petition) {
//     const div = document.createElement('div');
//     div.className = 'bg-white p-6 rounded-lg shadow mb-4';
//     div.innerHTML = `
//         <div class="flex justify-between items-start">
//             <div>
//                 <h3 class="text-xl font-semibold mb-2">${petition.title}</h3>
//                 <p class="text-gray-600 mb-4">${petition.content.substring(0, 200)}...</p>
//                 <div class="flex space-x-4 text-sm text-gray-500">
//                     <span>Signatures: ${petition.signatureCount}</span>
//                     <span>Status: ${petition.status}</span>
//                     <span>Created: ${new Date(petition.createdAt).toLocaleDateString()}</span>
//                 </div>
//             </div>
//             <a href="view-petition.html?id=${petition.id}"
//                class="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700">
//                 View Details
//             </a>
//         </div>
//     `;
//     return div;
// }
//
// // Handle petition form submission
// document.getElementById('petition-form').addEventListener('submit', async (e) => {
//     e.preventDefault();
//     const formData = {
//         title: document.getElementById('title').value,
//         content: document.getElementById('content').value
//     };
//
//     try {
//         const response = await fetchWithAuth(`${API_BASE_URL}/petitions`, {
//             method: 'POST',
//             headers: {
//                 'Content-Type': 'application/json'
//             },
//             body: JSON.stringify(formData)
//         });
//
//         if (response.ok) {
//             showToast('Petition created successfully', 'success');
//             document.getElementById('petition-form').reset();
//             loadDashboardData();
//         } else {
//             throw new Error('Failed to create petition');
//         }
//     } catch (error) {
//         showToast('Error creating petition', 'error');
//     }
// });
//
// // Helper function for authenticated fetch requests
// async function fetchWithAuth(url, options = {}) {
//     const token = localStorage.getItem('slpp_token');
//     const headers = {
//         ...options.headers,
//         'Authorization': `Bearer ${token}`,
//         'Content-Type': 'application/json'
//     };
//
//     const response = await fetch(url, {
//         ...options,
//         headers
//     });
//
//     if (response.status === 401) {
//         localStorage.removeItem('slpp_token');
//         window.location.href = '/index.html';
//         return;
//     }
//
//     return response;
// }
//
// // Show toast messages
// function showToast(message, type = 'info') {
//     const toast = document.createElement('div');
//     toast.className = `fixed bottom-4 right-4 px-6 py-3 rounded-lg ${
//         type === 'error' ? 'bg-red-500' : 'bg-green-500'
//     } text-white`;
//     toast.textContent = message;
//     document.body.appendChild(toast);
//     setTimeout(() => toast.remove(), 3000);
// }
//
// // Navigation handlers
// document.getElementById('create-petition').addEventListener('click', (e) => {
//     e.preventDefault();
//     document.getElementById('create-petition-section').classList.remove('hidden');
//     document.getElementById('my-petitions-section').classList.add('hidden');
// });
//
// document.getElementById('my-petitions').addEventListener('click', (e) => {
//     e.preventDefault();
//     document.getElementById('create-petition-section').classList.add('hidden');
//     document.getElementById('my-petitions-section').classList.remove('hidden');
// });
//
//
//
// document.getElementById('logout').addEventListener('click', (e) => {
//     e.preventDefault();
//     localStorage.removeItem('slpp_token');
//     window.location.href = 'index.html';
// });
//
// // Handle petition form submission
// const petitionForm = document.getElementById('petition-form');
// if (petitionForm) {
//     petitionForm.addEventListener('submit', async (e) => {
//         e.preventDefault();
//
//         // Log the form data to check what's being sent
//         console.log('Form Data:', {
//             title: document.getElementById('title').value,
//             content: document.getElementById('content').value
//         });
//
//         try {
//             const response = await fetch(`${API_BASE_URL}/api/petitions`, {
//                 method: 'POST',
//                 headers: {
//                     'Content-Type': 'application/json',
//                     'Authorization': `Bearer ${token}`
//                 },
//                 body: JSON.stringify({
//                     title: document.getElementById('title').value,
//                     content: document.getElementById('content').value
//                 })
//             });
//
//             if (!response.ok) {
//                 const errorData = await response.json();
//                 throw new Error(errorData.message || 'Failed to create petition');
//             }
//
//             const result = await response.json();
//             console.log('Success:', result);
//             showToast('Petition created successfully', 'success');
//             petitionForm.reset();
//             loadPetitions();
//         } catch (error) {
//             console.error('Error:', error);
//             showToast(error.message, 'error');
//         }
//     });
// }
//
