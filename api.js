const API_URL = 'http://localhost:8080/api';

// Helper function for authenticated requests
async function authFetch(url, options = {}) {
    const token = getToken();
    
    const defaultOptions = {
        headers: {
            'Authorization': `Bearer ${token}`
        }
    };

    // Merge options
    const mergedOptions = {
        ...defaultOptions,
        ...options,
        headers: {
            ...defaultOptions.headers,
            ...options.headers
        }
    };

    const response = await fetch(url, mergedOptions);
    
    if (response.status === 401) {
        logout();
        throw new Error('Session expired. Please login again.');
    }

    return response;
}

// Get all approved ads
async function getApprovedAds() {
    const response = await fetch(`${API_URL}/ads`);
    return await response.json();
}

// Search ads
async function searchAds(query) {
    const response = await fetch(`${API_URL}/ads/search?query=${encodeURIComponent(query)}`);
    return await response.json();
}

// Create new ad
async function createAd(formData) {
    const response = await authFetch(`${API_URL}/ads`, {
        method: 'POST',
        body: formData
    });
    
    if (!response.ok) {
        const error = await response.json();
        throw new Error(error.message || 'Failed to create ad');
    }
    
    return await response.json();
}

// Get my ads
async function getMyAds() {
    const response = await authFetch(`${API_URL}/ads/my-ads`);
    return await response.json();
}

// Get pending ads (admin)
async function getPendingAds() {
    const response = await authFetch(`${API_URL}/admin/pending`);
    return await response.json();
}

// Approve ad (admin)
async function approveAd(adId) {
    const response = await authFetch(`${API_URL}/admin/approve/${adId}`, {
        method: 'POST'
    });
    return await response.json();
}

// Reject ad (admin)
async function rejectAd(adId) {
    const response = await authFetch(`${API_URL}/admin/reject/${adId}`, {
        method: 'POST'
    });
    return await response.json();
}

// Get user profile
async function getProfile() {
    const response = await authFetch(`${API_URL}/user/profile`);
    return await response.json();
}

// Update profile
async function updateProfile(name, phoneNumber) {
    const formData = new FormData();
    formData.append('name', name);
    formData.append('phoneNumber', phoneNumber);

    const response = await authFetch(`${API_URL}/user/profile`, {
        method: 'PUT',
        body: formData
    });
    return await response.json();
}