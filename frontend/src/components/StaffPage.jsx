import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import Header from './Header';

/**
 * StaffPage Component
 * Provides a dashboard to manage items (books, games, movies) in the library.
 * Accessible without requiring the user to log in.
 */
const StaffPage = () => {
    const navigate = useNavigate();
    const [activeTab, setActiveTab] = useState('books'); // Default to 'books' tab
    const [items, setItems] = useState([]);
    const [searchQuery, setSearchQuery] = useState('');
    const [selectedItemId, setSelectedItemId] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [showConfirmSave, setShowConfirmSave] = useState(false);
    const [editItem, setEditItem] = useState(null);
    const [showModal, setShowModal] = useState(false);


    const API_BASE = "http://localhost:8765";

    const mockData = {
        books: [],
        events: [
            { id: 1, title: 'Event 1', location: 'Main Hall', date: 'Oct 20, 2026', description: 'Event description 1' },
            { id: 2, title: 'Event 2', location: 'Tech Lab 2', date: 'Oct 26, 2026', description: 'Event description 2' },
            { id: 3, title: 'Event 3', location: 'Reading Room B', date: 'Oct 28, 2026', description: 'Event description 3' },
        ],
        games: [
            { id: 1, title: 'Chess', category: 'Game', language: 'English', publishedYear: 2020 },
            { id: 2, title: 'Monopoly', category: 'Game', language: 'English', publishedYear: 2021 },
        ],
        movies: [
            { id: 1, title: 'The Matrix', category: 'Movie', language: 'English', publishedYear: 1999 },
            { id: 2, title: 'Inception', category: 'Movie', language: 'English', publishedYear: 2010 },
        ],
    };

    // Function to handle tab change
    const handleTabClick = (tab) => {
        setActiveTab(tab);
        fetchItems(tab);
    };

    // Function to format date
    const formatDate = (date) => {
        return new Date(date).toLocaleDateString('en-US', { month: 'short', day: 'numeric', year: 'numeric' });
    };

    useEffect(() => {
        fetchItems(activeTab);
    }, []);

    const fetchItems = async (tab) => {
        setLoading(true);
        setError(null);

        try {
            // BOOKS come from backend
            if (tab === 'books') {
                const response = await fetch(`${API_BASE}/api/v1/item`, {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json'
                    }
                });

                if (!response.ok) throw new Error("Failed to fetch books");

                const data = await response.json();

                const normalized = data.map(item => ({
                    ...item,
                    isbn: item.isbn13 || ''
                }));

                setItems(normalized);
            }
            else {
                // EVENTS / GAMES / MOVIES still mock data
                setTimeout(() => {
                    setItems(mockData[tab] || []);
                }, 300);
            }

        } catch (err) {
            console.error(err);
            setError("Failed to load items");
        } finally {
            setLoading(false);
        }
    };


    // Handle deleting an item
    const handleDeleteItem = async (id) => {
        if (!window.confirm("Delete this item?")) return;

        if (activeTab === 'books') {
            await fetch(`${API_BASE}/api/v1/item/${id}`, {
                method: 'DELETE'
            });
        }

        setItems(items.filter(i => i.id !== id));
    };

    const handleSaveClick = () => {
        setShowConfirmSave(true);
    };

    const confirmSave = async () => {
        try {
            // NEW ITEM (CREATE)
            if (!items.find(i => i.id === editItem.id)) {
                const response = await fetch(`${API_BASE}/api/v1/item`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(editItem)
                });

                if (!response.ok) throw new Error("Failed to create item");

                const savedItem = await response.json();

                setItems(prev => [...prev, savedItem]);
            }

            // EXISTING ITEM (UPDATE)
            else {
                const response = await fetch(`${API_BASE}/api/v1/item/${editItem.id}`, {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(editItem)
                });

                if (!response.ok) throw new Error("Failed to update item");

                const updated = await response.json();

                setItems(prev =>
                    prev.map(item =>
                        item.id === updated.id ? updated : item
                    )
                );
            }

            setShowConfirmSave(false);
            setEditItem(null);

        } catch (err) {
            console.error(err);
            setError("Failed to save item");
        }
    };

    const cancelSave = () => {
        setShowConfirmSave(false);
    };

    const handleViewEditChange = (e) => {
        const { name, value } = e.target;

        setEditItem(prev => ({
            ...prev,
            [name]: value
        }));
    };

    // Handle adding a new item
    const handleAddItem = () => {
        setEditItem({
            id: null, // temporary id
            title: '',
            author: '',
            language: '',
            publishedYear: '',
            isbn10: '',
            isbn13: '',
            categories: '',
            age: '',
            totalStock: '',
            location: '',
            date: '',
            description: ''
        });

        setSelectedItemId(null);
    };

    if (loading) {
        return (
            <div className="min-h-screen bg-gray-50 flex items-center justify-center">
                <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
            </div>
        );
    }

    const filteredItems = items.filter((item) => {
        const query = searchQuery.toLowerCase().trim();

        if (!query) return true;

        const idMatch = item.id?.toString().includes(query);
        const titleMatch = item.title?.toLowerCase().includes(query);
        const isbnMatch = item.isbn?.toLowerCase().includes(query);

        return idMatch || titleMatch || isbnMatch;
    });

    return (
        <div className="min-h-screen bg-gray-50 pb-12 pt-24">
            <Header />

            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                <div className="flex flex-col md:flex-row md:items-center justify-between mb-8">
                    <div>
                        <h1 className="text-3xl font-extrabold text-gray-900 tracking-tight">Item Management</h1>
                        <p className="mt-1 text-gray-500">View and manage all items in the system.</p>
                    </div>
                    <div className="mt-4 md:mt-0">
                        <button
                            onClick={handleAddItem}
                            className="inline-flex items-center px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 transition-colors"
                        >
                            Add New Item
                        </button>
                    </div>
                </div>

                {/* Tabs for category navigation */}
                <div className="flex space-x-6 mb-8">
                    <button
                        className={`px-4 py-2 font-semibold text-sm ${activeTab === 'events' ? 'bg-blue-600 text-white' : 'bg-gray-200'}`}
                        onClick={() => handleTabClick('events')}
                    >
                        Events
                    </button>
                    <button
                        className={`px-4 py-2 font-semibold text-sm ${activeTab === 'books' ? 'bg-blue-600 text-white' : 'bg-gray-200'}`}
                        onClick={() => handleTabClick('books')}
                    >
                        Books
                    </button>
                    <button
                        className={`px-4 py-2 font-semibold text-sm ${activeTab === 'games' ? 'bg-blue-600 text-white' : 'bg-gray-200'}`}
                        onClick={() => handleTabClick('games')}
                    >
                        Games
                    </button>
                    <button
                        className={`px-4 py-2 font-semibold text-sm ${activeTab === 'movies' ? 'bg-blue-600 text-white' : 'bg-gray-200'}`}
                        onClick={() => handleTabClick('movies')}
                    >
                        Movies
                    </button>
                </div>

                {/* Error Message */}
                {error && (
                    <div className="mb-6 p-4 bg-red-50 border border-red-200 text-red-700 rounded-lg flex items-center">
                        <svg className="w-5 h-5 mr-3" fill="currentColor" viewBox="0 0 20 20">
                            <path fillRule="evenodd" d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7 4a1 1 0 11-2 0 1 1 0 012 0zm-1-9a1 1 0 00-1 1v4a1 1 0 102 0V6a1 1 0 00-1-1z" clipRule="evenodd" />
                        </svg>
                        {error}
                    </div>
                )}

                {/* SEARCH BAR */}
                <div className="mb-4">
                    <input
                        type="text"
                        placeholder="Search items..."
                        value={searchQuery}
                        onChange={(e) => setSearchQuery(e.target.value)}
                        className="w-full p-2 border rounded-md"
                    />
                </div>

                {/* Item Management Table */}
                <div className="bg-white shadow-xl rounded-2xl overflow-hidden border border-gray-100">
                    <div className="overflow-x-auto">
                        <table className="min-w-full table-fixed divide-y divide-gray-200">
                            <thead className="bg-gray-50">
                                <tr>
                                    <th scope="col" className="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">Item Id</th>
                                    <th scope="col" className="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">Title</th>
                                    {(activeTab === 'games' || activeTab === 'movies') && (
                                        <th scope="col" className="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">Release Year</th>
                                    )}
                                    {activeTab === 'books' && (
                                        <th scope="col" className="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">ISBN</th>
                                    )}
                                    {activeTab === 'events' && (
                                        <>
                                            <th scope="col" className="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">Location</th>
                                            <th scope="col" className="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">Date</th>
                                        </>
                                    )}
                                    <th scope="col" className="px-6 py-4 text-right text-xs font-semibold text-gray-500 uppercase tracking-wider">Actions</th>
                                </tr>
                            </thead>
                            <tbody className="bg-white divide-y divide-gray-200">
                                {items.length === 0 ? (
                                    <tr>
                                        <td colSpan="6" className="px-6 py-10 text-center text-gray-500 italic">
                                            No items found.
                                        </td>
                                    </tr>
                                ) : (
                                    filteredItems.map((item) => (
                                        <tr key={item.id} className="hover:bg-blue-50/30 transition-colors duration-150">
                                            <td className="px-6 py-4 whitespace-nowrap">{item.id}</td>
                                            <td className="px-6 py-4 whitespace-nowrap">
                                                <div className="truncate max-w-[200px]">
                                                    {item.title}
                                                </div>
                                            </td>
                                            {(activeTab === 'games' || activeTab === 'movies') && (
                                                <td className="px-6 py-4 whitespace-nowrap">
                                                    {item.publishedYear}
                                                </td>
                                            )}
                                            {activeTab === 'books' && (
                                                <td className="px-6 py-4 whitespace-nowrap">{item.isbn}</td>
                                            )}
                                            {activeTab === 'events' && (
                                                <>
                                                    <td className="px-6 py-4 whitespace-nowrap">{item.location}</td>
                                                    <td className="px-6 py-4 whitespace-nowrap">{formatDate(item.date)}</td>
                                                </>
                                            )}
                                            <td className="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                                                <button
                                                    onClick={() => setEditItem({ ...item })}
                                                    className="text-blue-600 hover:text-blue-900 bg-blue-50 hover:bg-blue-100 px-4 py-2 rounded-lg transition-all"
                                                >
                                                    View / Edit
                                                </button>
                                                <button
                                                    onClick={() => handleDeleteItem(item.id)}
                                                    className="text-red-600 hover:text-red-900 bg-red-50 hover:bg-red-100 px-4 py-2 rounded-lg transition-all ml-2"
                                                >
                                                    Delete
                                                </button>
                                            </td>
                                        </tr>
                                    ))
                                )}
                            </tbody>
                        </table>
                    </div>
                </div>

                {editItem && (
                    <div className="fixed inset-0 flex items-center justify-center bg-gray-600 bg-opacity-50 z-50">
                        <div className="bg-white p-8 rounded-lg shadow-lg w-[600px] max-h-[80vh] overflow-y-auto">

                            <h3 className="text-xl font-semibold text-gray-800 mb-4">
                                Book Details
                            </h3>

                            {/* Thumbnail */}
                            {editItem.thumbnail && (
                                <img
                                    src={editItem.thumbnail}
                                    alt={editItem.title}
                                    className="w-32 h-auto mb-4 rounded"
                                />
                            )}

                            <div className="space-y-2 text-sm text-gray-700">

                                <p className="mt-3">
                                    <strong>Title:</strong>
                                </p>
                                <input
                                    name="title"
                                    value={editItem?.title || ''}
                                    onChange={handleViewEditChange}
                                    className="w-full border p-2 rounded"
                                />
                                <p className="mt-3">
                                    <strong>Author:</strong>
                                </p>
                                <input
                                    name="author"
                                    value={editItem?.author || ''}
                                    onChange={handleViewEditChange}
                                    className="w-full border p-2 rounded"
                                />
                                <p className="mt-3">
                                    <strong>Language:</strong>
                                </p>
                                <input
                                    name="language"
                                    value={editItem?.language || ''}
                                    onChange={handleViewEditChange}
                                    className="w-full border p-2 rounded"
                                />
                                <p className="mt-3">
                                    <strong>Published Year:</strong>
                                </p>
                                <input
                                    name="publishedYear"
                                    value={editItem?.publishedYear || ''}
                                    onChange={handleViewEditChange}
                                    className="w-full border p-2 rounded"
                                />
                                <p className="mt-3">
                                    <strong>ISBN-10:</strong>
                                </p>
                                <input
                                    name="isbn10"
                                    value={editItem?.isbn10 || ''}
                                    onChange={handleViewEditChange}
                                    className="w-full border p-2 rounded"
                                />
                                <p className="mt-3">
                                    <strong>ISBN-13:</strong>
                                </p>
                                <input
                                    name="isbn13"
                                    value={editItem?.isbn13 || ''}
                                    onChange={handleViewEditChange}
                                    className="w-full border p-2 rounded"
                                />
                                <p className="mt-3">
                                    <strong>Genres:</strong>
                                </p>
                                <input
                                    name="categories"
                                    value={editItem?.categories || ''}
                                    onChange={handleViewEditChange}
                                    className="w-full border p-2 rounded"
                                />
                                <p className="mt-3">
                                    <strong>Age:</strong>
                                </p>
                                <input
                                    name="age"
                                    value={editItem?.age || ''}
                                    onChange={handleViewEditChange}
                                    className="w-full border p-2 rounded"
                                />
                                <p className="mt-3">
                                    <strong>Total Stock:</strong>
                                </p>
                                <input
                                    name="totalStock"
                                    value={editItem?.totalStock || ''}
                                    onChange={handleViewEditChange}
                                    className="w-full border p-2 rounded"
                                />
                                <p className="mt-3">
                                    <strong>Description:</strong>
                                </p>

                                <textarea
                                    name="description"
                                    value={editItem?.description || ''}
                                    onChange={handleViewEditChange}
                                    className="w-full mt-1 p-2 border rounded-md text-gray-700 leading-relaxed"
                                    rows={5}
                                />
                            </div>

                            <div className="mt-6 flex justify-end gap-2">
                                <button
                                    onClick={handleSaveClick}
                                    className="bg-blue-600 text-white px-4 py-2 rounded-md"
                                >
                                    Save Changes
                                </button>
                                <button
                                    onClick={() => {
                                        setSelectedItemId(null);
                                        setEditItem(null);
                                    }}
                                    className="bg-gray-500 text-white px-4 py-2 rounded-md"
                                >
                                    Close
                                </button>
                            </div>
                        </div>
                    </div>
                )}

                {showConfirmSave && (
                    <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 z-[60]">
                        <div className="bg-white p-6 rounded-lg shadow-lg w-[400px]">
                            <h2 className="text-lg font-semibold mb-4">
                                Are you sure you want to save changes?
                            </h2>

                            <div className="flex justify-end gap-2">
                                <button
                                    onClick={confirmSave}
                                    className="bg-green-600 text-white px-4 py-2 rounded"
                                >
                                    Yes
                                </button>

                                <button
                                    onClick={cancelSave}
                                    className="bg-gray-500 text-white px-4 py-2 rounded"
                                >
                                    Cancel
                                </button>
                            </div>
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
};

export default StaffPage;