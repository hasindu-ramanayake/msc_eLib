import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';
import Header from './Header';

/**
 * EditProfile Component
 * Allows users to update their personal information and address.
 * Features a modern, responsive form with real-time feedback.
 */
const EditProfile = () => {
    const { user, login } = useAuth();
    const navigate = useNavigate();
    const [loading, setLoading] = useState(true);
    const [saving, setSaving] = useState(false);
    const [message, setMessage] = useState({ type: '', text: '' });

    const [formData, setFormData] = useState({
        firstName: '',
        lastName: '',
        email: '',
        address: {
            addressLine1: '',
            addressLine2: '',
            eircode: '',
            county: ''
        }
    });

    useEffect(() => {
        const fetchProfile = async () => {
            if (!user?.token) return;

            try {
                const response = await fetch('http://localhost:8765/api/v1/users/me', {
                    headers: {
                        'Authorization': `Bearer ${user.token}`
                    }
                });

                if (response.ok) {
                    const data = await response.json();
                    setFormData({
                        firstName: data.firstName || '',
                        lastName: data.lastName || '',
                        email: data.email || '',
                        address: {
                            addressLine1: data.address?.addressLine1 || '',
                            addressLine2: data.address?.addressLine2 || '',
                            eircode: data.address?.eircode || '',
                            county: data.address?.county || ''
                        }
                    });
                } else {
                    setMessage({ type: 'error', text: 'Failed to load profile details.' });
                }
            } catch (error) {
                setMessage({ type: 'error', text: 'An error occurred while fetching your profile.' });
            } finally {
                setLoading(false);
            }
        };

        fetchProfile();
    }, [user?.token]);

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        if (name.includes('.')) {
            const [parent, child] = name.split('.');
            setFormData(prev => ({
                ...prev,
                [parent]: {
                    ...prev[parent],
                    [child]: value
                }
            }));
        } else {
            setFormData(prev => ({ ...prev, [name]: value }));
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setSaving(true);
        setMessage({ type: '', text: '' });

        try {
            const response = await fetch('http://localhost:8765/api/v1/users/edit-profile', {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${user.token}`
                },
                body: JSON.stringify(formData)
            });

            if (response.ok) {
                const updatedUser = await response.json();
                
                // Update the local AuthContext so the Header name updates immediately
                const newUserContext = {
                    ...user,
                    firstName: updatedUser.firstName,
                    lastName: updatedUser.lastName
                };
                login(newUserContext);

                setMessage({ type: 'success', text: 'Profile updated successfully!' });
                
                // Scroll to top to show success message
                window.scrollTo({ top: 0, behavior: 'smooth' });
            } else {
                const errorData = await response.json();
                setMessage({ type: 'error', text: errorData.message || 'Failed to update profile.' });
            }
        } catch (error) {
            setMessage({ type: 'error', text: 'An error occurred. Please try again later.' });
        } finally {
            setSaving(false);
        }
    };

    if (loading) {
        return (
            <div className="min-h-screen bg-gray-50 flex items-center justify-center">
                <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
            </div>
        );
    }

    return (
        <div className="min-h-screen bg-gray-50 pb-12">
            <Header />
            
            <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 mt-10">
                <div className="bg-white rounded-2xl shadow-xl overflow-hidden">
                    {/* Hero Section */}
                    <div className="bg-gradient-to-r from-blue-600 to-indigo-700 px-8 py-10">
                        <h1 className="text-3xl font-extrabold text-white tracking-tight">Edit Your Profile</h1>
                        <p className="mt-2 text-blue-100 italic">Keep your personal and delivery details up to date.</p>
                    </div>

                    <form onSubmit={handleSubmit} className="p-8 space-y-8">
                        {message.text && (
                            <div className={`p-4 rounded-lg flex items-center ${
                                message.type === 'success' ? 'bg-green-50 text-green-800 border border-green-200' : 'bg-red-50 text-red-800 border border-red-200'
                            } animate-pulse`}>
                                <span className="mr-3">{message.type === 'success' ? '✅' : '❌'}</span>
                                {message.text}
                            </div>
                        )}

                        {/* Basic Information Group */}
                        <div>
                            <h3 className="text-lg font-semibold text-gray-900 border-b pb-2 mb-6">Basic Information</h3>
                            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                                <div>
                                    <label className="block text-sm font-medium text-gray-700 mb-1">First Name</label>
                                    <input
                                        type="text"
                                        name="firstName"
                                        value={formData.firstName}
                                        onChange={handleInputChange}
                                        className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition-all outline-none"
                                        required
                                    />
                                </div>
                                <div>
                                    <label className="block text-sm font-medium text-gray-700 mb-1">Last Name</label>
                                    <input
                                        type="text"
                                        name="lastName"
                                        value={formData.lastName}
                                        onChange={handleInputChange}
                                        className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition-all outline-none"
                                        required
                                    />
                                </div>
                                <div className="md:col-span-2">
                                    <label className="block text-sm font-medium text-gray-700 mb-1">Email (Read-only)</label>
                                    <input
                                        type="email"
                                        value={formData.email}
                                        className="w-full px-4 py-2 border border-gray-200 bg-gray-100 rounded-lg text-gray-500 cursor-not-allowed outline-none"
                                        disabled
                                    />
                                </div>
                            </div>
                        </div>

                        {/* Address Information Group */}
                        <div>
                            <h3 className="text-lg font-semibold text-gray-900 border-b pb-2 mb-6">Address & Shipping</h3>
                            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                                <div className="md:col-span-2">
                                    <label className="block text-sm font-medium text-gray-700 mb-1">Address Line 1</label>
                                    <input
                                        type="text"
                                        name="address.addressLine1"
                                        value={formData.address.addressLine1}
                                        onChange={handleInputChange}
                                        placeholder="Street address, P.O. box, etc."
                                        className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition-all outline-none"
                                    />
                                </div>
                                <div className="md:col-span-2">
                                    <label className="block text-sm font-medium text-gray-700 mb-1">Address Line 2 (Optional)</label>
                                    <input
                                        type="text"
                                        name="address.addressLine2"
                                        value={formData.address.addressLine2}
                                        onChange={handleInputChange}
                                        placeholder="Apartment, suite, unit, building, floor, etc."
                                        className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition-all outline-none"
                                    />
                                </div>
                                <div>
                                    <label className="block text-sm font-medium text-gray-700 mb-1">Eircode / ZIP Code</label>
                                    <input
                                        type="text"
                                        name="address.eircode"
                                        value={formData.address.eircode}
                                        onChange={handleInputChange}
                                        className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition-all outline-none"
                                    />
                                </div>
                                <div>
                                    <label className="block text-sm font-medium text-gray-700 mb-1">County / Region</label>
                                    <input
                                        type="text"
                                        name="address.county"
                                        value={formData.address.county}
                                        onChange={handleInputChange}
                                        className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition-all outline-none"
                                    />
                                </div>
                            </div>
                        </div>

                        {/* Address Information Group */}
                        <div className="flex items-center justify-end space-x-4 pt-6 border-t mt-10">
                            <button
                                type="button"
                                onClick={() => navigate('/')}
                                className="px-6 py-2.5 text-sm font-medium text-gray-700 bg-white border border-gray-300 rounded-lg hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-gray-500 transition-all"
                            >
                                Cancel
                            </button>
                            <button
                                type="submit"
                                disabled={saving}
                                className={`px-8 py-2.5 text-sm font-medium text-white bg-blue-600 rounded-lg hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 transition-all shadow-md ${
                                    saving ? 'opacity-50 cursor-not-allowed' : ''
                                }`}
                            >
                                {saving ? (
                                    <span className="flex items-center">
                                        <svg className="animate-spin -ml-1 mr-3 h-5 w-5 text-white" fill="none" viewBox="0 0 24 24">
                                            <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"></circle>
                                            <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                                        </svg>
                                        Saving...
                                    </span>
                                ) : 'Save All Changes'}
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    );
};

export default EditProfile;
