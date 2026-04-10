import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';
import Header from './Header';

/**
 * AdminPage Component
 * Provides administrators with a dashboard to manage users.
 * Allows viewing all registered users and deleting them.
 */
const AdminPage = () => {
    const { user } = useAuth();
    const navigate = useNavigate();
    const [users, setUsers] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [deleteLoading, setDeleteLoading] = useState(null);

    useEffect(() => {
        console.log('[AdminPage] Mount. User data:', user);
        // Redirect non-admins if they somehow bypass the route protection
        if (user?.role !== 'ADMIN') {
            console.warn('[AdminPage] Non-admin access detected. Redirecting...');
            navigate('/');
            return;
        }

        fetchUsers();
    }, [user, navigate]);

    const fetchUsers = async () => {
        console.log('[AdminPage] Fetching users...');
        setLoading(true);
        try {
            const response = await fetch('http://localhost:8765/api/v1/users', {
                headers: {
                    'Authorization': `Bearer ${user.token}`
                }
            });

            console.log('[AdminPage] Fetch response status:', response.status);

            if (response.ok) {
                const data = await response.json();
                console.log('[AdminPage] Data received:', data);
                if (Array.isArray(data)) {
                    setUsers(data);
                } else {
                    console.error('[AdminPage] Received data is not an array:', data);
                    setError('Unexpected data format from server.');
                }
            } else {
                const text = await response.text();
                console.error('[AdminPage] Fetch error text:', text);
                try {
                    const errorData = JSON.parse(text);
                    setError(errorData.message || 'Failed to fetch users');
                } catch (e) {
                    setError(`Error ${response.status}: ${text || 'Unknown error'}`);
                }
            }
        } catch (err) {
            console.error('[AdminPage] Connection error:', err);
            setError('An error occurred while connecting to the server.');
        } finally {
            setLoading(false);
        }
    };

    const handleDeleteUser = async (userId, userEmail) => {
        if (!window.confirm(`Are you sure you want to delete user ${userEmail}? This action cannot be undone.`)) {
            return;
        }

        setDeleteLoading(userId);
        try {
            const response = await fetch(`http://localhost:8765/api/v1/users/${userId}`, {
                method: 'DELETE',
                headers: {
                    'Authorization': `Bearer ${user.token}`
                }
            });

            if (response.ok) {
                setUsers(users.filter(u => u.id !== userId));
            } else {
                const errorData = await response.json();
                alert(errorData.message || 'Failed to delete user');
            }
        } catch (err) {
            alert('An error occurred while attempting to delete the user.');
        } finally {
            setDeleteLoading(null);
        }
    };

    if (loading) {
        return (
            <div className="min-h-screen bg-gray-50 flex items-center justify-center">
                <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
            </div>
        );
    }

    // Final check to catch any weird rendering issue
    try {
        return (
            <div className="min-h-screen bg-gray-50 pb-12 pt-24">
                <Header />

                <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                    <div className="flex flex-col md:flex-row md:items-center justify-between mb-8">
                        <div>
                            <h1 className="text-3xl font-extrabold text-gray-900 tracking-tight">User Management</h1>
                            <p className="mt-1 text-gray-500">View and manage all registered users in the system.</p>
                        </div>
                        <div className="mt-4 md:mt-0">
                            <button
                                onClick={fetchUsers}
                                className="inline-flex items-center px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 transition-colors"
                            >
                                Refresh List
                            </button>
                        </div>
                    </div>

                    {error && (
                        <div className="mb-6 p-4 bg-red-50 border border-red-200 text-red-700 rounded-lg flex items-center">
                            <svg className="w-5 h-5 mr-3" fill="currentColor" viewBox="0 0 20 20">
                                <path fillRule="evenodd" d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7 4a1 1 0 11-2 0 1 1 0 012 0zm-1-9a1 1 0 00-1 1v4a1 1 0 102 0V6a1 1 0 00-1-1z" clipRule="evenodd" />
                            </svg>
                            {error}
                        </div>
                    )}

                    <div className="bg-white shadow-xl rounded-2xl overflow-hidden border border-gray-100">
                        <div className="overflow-x-auto">
                            <table className="min-w-full divide-y divide-gray-200">
                                <thead className="bg-gray-50">
                                <tr>
                                    <th scope="col" className="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">User</th>
                                    <th scope="col" className="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">Role</th>
                                    <th scope="col" className="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">Location</th>
                                    <th scope="col" className="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">Notifications</th>
                                    <th scope="col" className="px-6 py-4 text-right text-xs font-semibold text-gray-500 uppercase tracking-wider">Actions</th>
                                </tr>
                                </thead>
                                <tbody className="bg-white divide-y divide-gray-200">
                                {users.length === 0 ? (
                                    <tr>
                                        <td colSpan="5" className="px-6 py-10 text-center text-gray-500 italic">
                                            No users found.
                                        </td>
                                    </tr>
                                ) : (
                                    users.map((u) => (
                                        <tr key={u.id} className="hover:bg-blue-50/30 transition-colors duration-150">
                                            <td className="px-6 py-4 whitespace-nowrap">
                                                <div className="flex items-center">
                                                    <div className="flex-shrink-0 h-10 w-10 rounded-full bg-gradient-to-br from-blue-100 to-indigo-100 flex items-center justify-center text-blue-700 font-bold shadow-inner">
                                                        {u.firstName ? u.firstName[0] : ''}
                                                    </div>
                                                    <div className="ml-4">
                                                        <div className="text-sm font-bold text-gray-900">{u.firstName} {u.lastName}</div>
                                                        <div className="text-sm text-gray-500">{u.email}</div>
                                                    </div>
                                                </div>
                                            </td>
                                            <td className="px-6 py-4 whitespace-nowrap">
                          <span className={`px-3 py-1 inline-flex text-xs leading-5 font-semibold rounded-full ${
                              u.role === 'ADMIN'
                                  ? 'bg-purple-100 text-purple-800'
                                  : u.role === 'STAFF'
                                      ? 'bg-blue-100 text-blue-800'
                                      : 'bg-green-100 text-green-800'
                          }`}>
                            {u.role}
                          </span>
                                            </td>
                                            <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                                {u.address ? `${u.address.county || 'N/A'}` : 'N/A'}
                                            </td>
                                            <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                                <div className="flex space-x-1">
                                                    {u.notificationPreferences && u.notificationPreferences.length > 0 ? (
                                                        u.notificationPreferences.map(pref => (
                                                            <span key={pref} className="px-2 py-0.5 bg-gray-100 text-gray-600 rounded text-[10px] font-bold uppercase transition-all hover:bg-gray-200">
                                  {pref}
                                </span>
                                                        ))
                                                    ) : (
                                                        <span className="text-gray-400 italic">None</span>
                                                    )}
                                                </div>
                                            </td>
                                            <td className="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                                                <button
                                                    onClick={() => handleDeleteUser(u.id, u.email)}
                                                    disabled={deleteLoading === u.id || u.id === user.id}
                                                    title={u.id === user.id ? "You cannot delete your own account" : ""}
                                                    className={`text-red-600 hover:text-red-900 bg-red-50 hover:bg-red-100 px-4 py-2 rounded-lg transition-all ${
                                                        (deleteLoading === u.id || u.id === user.id) ? 'opacity-50 cursor-not-allowed' : ''
                                                    }`}
                                                >
                                                    {deleteLoading === u.id ? (
                                                        <span className="flex items-center">
                                <svg className="animate-spin h-4 w-4 mr-2" fill="none" viewBox="0 0 24 24">
                                  <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"></circle>
                                  <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                                </svg>
                                Deleting...
                              </span>
                                                    ) : 'Delete'}
                                                </button>
                                            </td>
                                        </tr>
                                    ))
                                )}
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        );
    } catch (err) {
        console.error('[AdminPage] Render error:', err);
        return <div className="p-24 text-red-600 font-bold">A localized rendering error occurred: {err.message}</div>;
    }
};

export default AdminPage;
