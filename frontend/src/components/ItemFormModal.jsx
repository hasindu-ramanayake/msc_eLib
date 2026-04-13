import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';

/**
 * ItemFormModal Component
 * Modal used to Create or Update a library item.
 */
const ItemFormModal = ({ item, isOpen, onClose, onSuccess }) => {
    const { user } = useAuth();
    const [formData, setFormData] = useState({
        title: '',
        author: '',
        isbn: '',
        totalStock: 0,
        genre: 'FICTION',
        publicationDate: new Date().toISOString().split('T')[0]
    });
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const genres = [
        'FICTION',
        'NON_FICTION',
        'SCIENCE',
        'HISTORY',
        'FANTASY',
        'TECHNOLOGY'
    ];

    useEffect(() => {
        if (item) {
            setFormData({
                title: item.title || '',
                author: item.author || '',
                isbn: item.isbn || '',
                totalStock: item.totalStock || 0,
                genre: item.genre || 'FICTION',
                publicationDate: item.publicationDate ? new Date(item.publicationDate).toISOString().split('T')[0] : new Date().toISOString().split('T')[0]
            });
        }
    }, [item]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: name === 'totalStock' ? parseInt(value) || 0 : value
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError(null);

        try {
            const baseUrl = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8765';
            const url = item 
                ? `${baseUrl}/api/v1/item/${item.itemId}`
                : `${baseUrl}/api/v1/item`;
            
            const method = item ? 'PUT' : 'POST';

            const response = await fetch(url, {
                method,
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${user.token}`
                },
                body: JSON.stringify(formData)
            });

            if (response.ok) {
                onSuccess();
            } else {
                const errorData = await response.json();
                setError(errorData.message || `Failed to ${item ? 'update' : 'create'} item.`);
            }
        } catch (err) {
            setError('An error occurred. Please check your connection.');
        } finally {
            setLoading(false);
        }
    };

    if (!isOpen) return null;

    return (
        <div className="fixed inset-0 z-[60] overflow-y-auto">
            <div className="flex items-center justify-center min-h-screen pt-4 px-4 pb-20 text-center sm:block sm:p-0">
                {/* Overlay */}
                <div onClick={onClose} className="fixed inset-0 transition-opacity" aria-hidden="true">
                    <div className="absolute inset-0 bg-gray-500 opacity-75 backdrop-blur-sm"></div>
                </div>

                {/* Modal Container */}
                <span className="hidden sm:inline-block sm:align-middle sm:h-screen" aria-hidden="true">&#8203;</span>
                <div className="inline-block align-middle bg-white rounded-2xl text-left overflow-hidden shadow-2xl transform transition-all sm:my-8 sm:align-middle sm:max-w-lg sm:w-full border border-gray-100">
                    <div className="bg-white px-4 pt-5 pb-4 sm:p-6 sm:pb-4">
                        <div className="sm:flex sm:items-start">
                            <div className="mt-3 text-center sm:mt-0 sm:ml-4 sm:text-left w-full">
                                <h3 className="text-2xl leading-6 font-extrabold text-gray-900 mb-6 tracking-tight">
                                    {item ? 'Edit Item' : 'Add New Item'}
                                </h3>
                                
                                {error && (
                                    <div className="mb-4 p-3 bg-red-50 text-red-700 text-sm rounded-lg border border-red-100">
                                        {error}
                                    </div>
                                )}

                                <form onSubmit={handleSubmit} className="space-y-4">
                                    <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
                                        <div className="sm:col-span-2">
                                            <label className="block text-xs font-bold text-gray-500 uppercase tracking-widest mb-1">Title</label>
                                            <input
                                                type="text"
                                                name="title"
                                                required
                                                value={formData.title}
                                                onChange={handleChange}
                                                className="block w-full px-4 py-3 bg-gray-50 border border-gray-200 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none transition-all"
                                                placeholder="e.g. The Great Gatsby"
                                            />
                                        </div>
                                        
                                        <div>
                                            <label className="block text-xs font-bold text-gray-500 uppercase tracking-widest mb-1">Author</label>
                                            <input
                                                type="text"
                                                name="author"
                                                required
                                                value={formData.author}
                                                onChange={handleChange}
                                                className="block w-full px-4 py-3 bg-gray-50 border border-gray-200 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none transition-all"
                                                placeholder="e.g. F. Scott Fitzgerald"
                                            />
                                        </div>

                                        <div>
                                            <label className="block text-xs font-bold text-gray-500 uppercase tracking-widest mb-1">ISBN</label>
                                            <input
                                                type="text"
                                                name="isbn"
                                                required
                                                value={formData.isbn}
                                                onChange={handleChange}
                                                className="block w-full px-4 py-3 bg-gray-50 border border-gray-200 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none transition-all"
                                                placeholder="e.g. 978-3-16-148410-0"
                                            />
                                        </div>

                                        <div>
                                            <label className="block text-xs font-bold text-gray-500 uppercase tracking-widest mb-1">Genre</label>
                                            <select
                                                name="genre"
                                                value={formData.genre}
                                                onChange={handleChange}
                                                className="block w-full px-4 py-3 bg-gray-50 border border-gray-200 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none transition-all"
                                            >
                                                {genres.map(g => (
                                                    <option key={g} value={g}>{g}</option>
                                                ))}
                                            </select>
                                        </div>

                                        <div>
                                            <label className="block text-xs font-bold text-gray-500 uppercase tracking-widest mb-1">Total Stock</label>
                                            <input
                                                type="number"
                                                name="totalStock"
                                                min="0"
                                                required
                                                value={formData.totalStock}
                                                onChange={handleChange}
                                                className="block w-full px-4 py-3 bg-gray-50 border border-gray-200 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none transition-all"
                                            />
                                        </div>

                                        <div className="sm:col-span-2">
                                            <label className="block text-xs font-bold text-gray-500 uppercase tracking-widest mb-1">Publication Date</label>
                                            <input
                                                type="date"
                                                name="publicationDate"
                                                required
                                                value={formData.publicationDate}
                                                onChange={handleChange}
                                                className="block w-full px-4 py-3 bg-gray-50 border border-gray-200 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none transition-all"
                                            />
                                        </div>
                                    </div>

                                    <div className="flex justify-end space-x-3 mt-8">
                                        <button
                                            type="button"
                                            onClick={onClose}
                                            className="px-6 py-3 bg-gray-100 text-gray-600 rounded-xl font-bold hover:bg-gray-200 transition-all focus:outline-none"
                                        >
                                            Cancel
                                        </button>
                                        <button
                                            type="submit"
                                            disabled={loading}
                                            className={`px-8 py-3 bg-blue-600 text-white rounded-xl font-bold shadow-lg hover:bg-blue-700 focus:ring-4 focus:ring-blue-200 transition-all ${
                                                loading ? 'opacity-50 cursor-not-allowed' : ''
                                            }`}
                                        >
                                            {loading ? 'Saving...' : item ? 'Update Item' : 'Create Item'}
                                        </button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default ItemFormModal;
