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
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [deleteLoading, setDeleteLoading] = useState(null);
    const [showModal, setShowModal] = useState(false); // To control modal visibility
    const [formData, setFormData] = useState({
        id: null,
        title: '',
        language: 'English',
        publishedYear: new Date().getFullYear(),
        isbn: '',
        location: '',
        date: '',
        description: ''
    });

    const mockData = {
        events: [
            { id: 1, title: 'Event 1', location: 'Main Hall', date: 'Oct 20, 2026', description: 'Event description 1' },
            { id: 2, title: 'Event 2', location: 'Tech Lab 2', date: 'Oct 26, 2026', description: 'Event description 2' },
            { id: 3, title: 'Event 3', location: 'Reading Room B', date: 'Oct 28, 2026', description: 'Event description 3' },
        ],
        books: [
            { id: 1, title: 'The Great Gatsby', category: 'Book', language: 'English', publishedYear: 2020, isbn: '9780743273565' },
            { id: 2, title: '1984', category: 'Book', language: 'English', publishedYear: 2021, isbn: '9780451524935' },
            { id: 3, title: 'To Kill a Mockingbird', category: 'Book', language: 'English', publishedYear: 2022, isbn: '9780061120084' },
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

    // Function to simulate data fetching (mock data)
    const fetchItems = (tab) => {
        setLoading(true);
        try {
            // Get data based on active tab
            const data = mockData[tab];
            setTimeout(() => {
                setItems(data);
                setLoading(false);
            }, 1000); // Simulate network delay
        } catch (err) {
            setError('An error occurred while loading items.');
            setLoading(false);
        }
    };

    // Handle deleting an item
    const handleDeleteItem = async (itemId) => {
        if (!window.confirm(`Are you sure you want to delete this item? This action cannot be undone.`)) {
            return;
        }

        setDeleteLoading(itemId);
        try {
            // In a real scenario, call the backend to delete the item
            setItems(items.filter(item => item.id !== itemId));
        } catch (err) {
            alert('An error occurred while attempting to delete the item.');
        } finally {
            setDeleteLoading(null);
        }
    };

    // Handle editing an item
    const handleEditItem = (itemId) => {
        const item = items.find(i => i.id === itemId);
        setFormData({ ...item });
        setShowModal(true); // Open the modal to update the item
    };

    // Handle adding a new item
    const handleAddItem = () => {
        setFormData({
            id: null,
            title: '',
            language: '',
            publishedYear: new Date().getFullYear(),
            isbn: activeTab === 'books' ? '' : '',
            location: activeTab === 'events' ? '' : '',
            date: activeTab === 'events' ? '' : '',
            description: activeTab === 'events' ? '' : '',
        });
        setShowModal(true); // Open the modal to add a new item
    };

    // Handle form input changes
    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData((prevState) => ({
            ...prevState,
            [name]: value,
        }));
    };

    // Handle form submission (Add or Update)
    const handleSubmit = () => {
        if (formData.id) {
            // Update item
            setItems(items.map((item) => (item.id === formData.id ? formData : item)));
        } else {
            // Add new item
            const newItem = { ...formData, id: items.length + 1 };
            setItems([...items, newItem]);
        }
        setShowModal(false); // Close the modal
    };

    // On initial load, fetch the items for the default 'books' tab
    useEffect(() => {
        fetchItems('books');
    }, []);

    if (loading) {
        return (
            <div className="min-h-screen bg-gray-50 flex items-center justify-center">
                <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
            </div>
        );
    }

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

                {/* Item Management Table */}
                <div className="bg-white shadow-xl rounded-2xl overflow-hidden border border-gray-100">
                    <div className="overflow-x-auto">
                        <table className="min-w-full divide-y divide-gray-200">
                            <thead className="bg-gray-50">
                                <tr>
                                    <th scope="col" className="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">Item Id</th>
                                    <th scope="col" className="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">Title</th>
                                    {activeTab === 'books' && (
                                        <th scope="col" className="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">Language</th>
                                    )}
                                    {activeTab === 'books' && (
                                        <th scope="col" className="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">Published Year</th>
                                    )}
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
                                    items.map((item) => (
                                        <tr key={item.id} className="hover:bg-blue-50/30 transition-colors duration-150">
                                            <td className="px-6 py-4 whitespace-nowrap">{item.id}</td>
                                            <td className="px-6 py-4 whitespace-nowrap">{item.title}</td>
                                            {activeTab === 'books' && (
                                                <td className="px-6 py-4 whitespace-nowrap">{item.language}</td>
                                            )}
                                            {activeTab !== 'events' && (
                                                <td className="px-6 py-4 whitespace-nowrap">{item.publishedYear}</td>
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
                                                    onClick={() => handleEditItem(item.id)}
                                                    className="text-blue-600 hover:text-blue-900 bg-blue-50 hover:bg-blue-100 px-4 py-2 rounded-lg transition-all"
                                                >
                                                    Edit
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

                {/* Add/Edit Modal */}
                {showModal && (
                    <div className="fixed inset-0 flex items-center justify-center bg-gray-600 bg-opacity-50 z-50">
                        <div className="bg-white p-8 rounded-lg shadow-lg w-96">
                            <h3 className="text-xl font-semibold text-gray-800 mb-4">
                                {formData.id ? 'Edit Item' : 'Add New Item'}
                            </h3>

                            {/* Form Inputs */}
                            <div>
                                <label htmlFor="title" className="block text-sm font-medium text-gray-600">Title</label>
                                <input
                                    type="text"
                                    id="title"
                                    name="title"
                                    value={formData.title}
                                    onChange={handleInputChange}
                                    className="w-full p-2 mt-1 border rounded-md"
                                />
                            </div>
                            {activeTab === 'books' && (
                              <div className="mt-4">
                                  <label htmlFor="language" className="block text-sm font-medium text-gray-600">Language</label>
                                  <input
                                      type="text"
                                      id="language"
                                      name="language"
                                      value={formData.language}
                                      onChange={handleInputChange}
                                      className="w-full p-2 mt-1 border rounded-md"
                                  />
                              </div>
                            )}
                            {activeTab === 'books' && (
                              <div className="mt-4">
                                  <label htmlFor="publishedYear" className="block text-sm font-medium text-gray-600">Published Year</label>
                                  <input
                                      type="number"
                                      id="publishedYear"
                                      name="publishedYear"
                                      value={formData.publishedYear}
                                      onChange={handleInputChange}
                                      className="w-full p-2 mt-1 border rounded-md"
                                  />
                              </div>
                            )}
                            {(activeTab === 'games' || activeTab === 'movies') && (
                              <div className="mt-4">
                                  <label htmlFor="publishedYear" className="block text-sm font-medium text-gray-600">Release Year</label>
                                  <input
                                      type="number"
                                      id="publishedYear"
                                      name="publishedYear"
                                      value={formData.publishedYear}
                                      onChange={handleInputChange}
                                      className="w-full p-2 mt-1 border rounded-md"
                                  />
                              </div>
                            )}
                            {activeTab === 'books' && (
                                <div className="mt-4">
                                    <label htmlFor="isbn" className="block text-sm font-medium text-gray-600">ISBN</label>
                                    <input
                                        type="text"
                                        id="isbn"
                                        name="isbn"
                                        value={formData.isbn}
                                        onChange={handleInputChange}
                                        className="w-full p-2 mt-1 border rounded-md"
                                    />
                                </div>
                            )}
                            {activeTab === 'events' && (
                                <>
                                    <div className="mt-4">
                                        <label htmlFor="location" className="block text-sm font-medium text-gray-600">Location</label>
                                        <input
                                            type="text"
                                            id="location"
                                            name="location"
                                            value={formData.location}
                                            onChange={handleInputChange}
                                            className="w-full p-2 mt-1 border rounded-md"
                                        />
                                    </div>
                                    <div className="mt-4">
                                        <label htmlFor="date" className="block text-sm font-medium text-gray-600">Date</label>
                                        <input
                                            type="date"
                                            id="date"
                                            name="date"
                                            value={formData.date}
                                            onChange={handleInputChange}
                                            className="w-full p-2 mt-1 border rounded-md"
                                        />
                                    </div>
                                    {/*
                                    <div className="mt-4">
                                        <label htmlFor="description" className="block text-sm font-medium text-gray-600">Description</label>
                                        <input
                                            type="text"
                                            id="description"
                                            name="description"
                                            value={formData.description}
                                            onChange={handleInputChange}
                                            className="w-full p-2 mt-1 border rounded-md"
                                        />
                                    </div>
                                    */}
                                </>
                            )}
                            <div className="mt-6 flex justify-end">
                                <button
                                    onClick={handleSubmit}
                                    className="bg-blue-600 text-white px-4 py-2 rounded-md"
                                >
                                    {formData.id ? 'Save Changes' : 'Add Item'}
                                </button>
                                <button
                                    onClick={() => setShowModal(false)}
                                    className="bg-gray-500 text-white px-4 py-2 rounded-md ml-2"
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