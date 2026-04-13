import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import './App.css';
import Header from './components/Header';
import SearchBar from './components/SearchBar';
import UpcomingEvents from './components/UpcomingEvents';
import Footer from './components/Footer';
import SearchResults from './components/SearchResults';
import { AuthProvider, useAuth } from './context/AuthContext';
import Login from './components/Login';
import Register from './components/Register';
import OAuth2RedirectHandler from './components/OAuth2RedirectHandler';
import EditProfile from './components/EditProfile';
import AdminPage from './components/AdminPage';
import { Navigate } from 'react-router-dom';
import NotificationsPage from './pages/NotificationsPage';
import StaffPage from './components/StaffPage';

/**
 * AdminProtectedRoute Component
 * Only allows users with the 'ADMIN' role to access the children components.
 * Redirects others to the home page.
 */
const AdminProtectedRoute = ({ children }) => {
    const { user, loading } = useAuth();

    if (loading) return null; // Or a loading spinner

    if (!user || user.role !== 'ADMIN') {
        return <Navigate to="/" replace />;
    }

    return children;
};

/**
 * StaffProtectedRoute Component
 * Allows users with 'STAFF' or 'ADMIN' roles to access the children components.
 */
const StaffProtectedRoute = ({ children }) => {
    const { user, loading } = useAuth();

    if (loading) return null;

    if (!user || (user.role !== 'STAFF' && user.role !== 'ADMIN')) {
        return <Navigate to="/" replace />;
    }

    return children;
};

// Extracted the old App landing page contents into its own component
const Home = () => {
    const { search } = window.location;
    const isRegistered = new URLSearchParams(search).get('registered') === 'true';

    return (
        <div className="bg-white min-h-screen w-full flex flex-col items-center pt-24">
            <Header />

            {/* Registration Success Alert */}
            {isRegistered && (
                <div className="w-2/3 max-w-2xl mb-6 bg-green-50 border border-green-200 text-green-700 px-4 py-3 rounded-lg text-center animate-bounce shadow-sm">
                    <p className="font-semibold">Registration Successful!</p>
                    <p className="text-sm">Your account has been created with Google. Please click "Login" to sign in.</p>
                </div>
            )}

            {/* Main Content Container - Flexibly spans whole screen on mobile */}
            <div className="w-11/12 sm:w-5/6 md:w-2/3 lg:max-w-4xl mt-8 flex flex-col flex-grow">
                <div className="text-center space-y-4 mb-10">
                    <h1 className="text-5xl md:text-6xl font-extrabold text-transparent bg-clip-text bg-gradient-to-r from-blue-700 to-indigo-600 tracking-tight">
                        E-Library
                    </h1>
                    <p className="text-lg md:text-xl text-gray-600 max-w-2xl mx-auto">
                        Discover a world of entertainment. Search through thousands of books, Movies, and Games instantly.
                    </p>
                </div>

                <SearchBar />

                {/* Events Section */}
                <UpcomingEvents />
            </div>

            {/* Footer Section */}
            <Footer />
        </div>
    );
};

// Main App now handles the Router wrapping
function App() {
    return (
        <AuthProvider>
            <Router>
                <Routes>
                    <Route path="/" element={<Home />} />
                    <Route path="/search" element={<SearchResults />} />
                    <Route path="/login" element={<Login />} />
                    <Route path="/register" element={<Register />} />
                    <Route path="/oauth2/redirect" element={<OAuth2RedirectHandler />} />
                    <Route path="/edit-profile" element={<EditProfile />} />
                    <Route path="/notifications" element={<NotificationsPage />} />
                    <Route
                        path="/admin"
                        element={
                            <AdminProtectedRoute>
                                <AdminPage />
                            </AdminProtectedRoute>
                        }
                    />
                    <Route
                        path="/staff"
                        element={
                            <StaffProtectedRoute>
                                <StaffPage />
                            </StaffProtectedRoute>
                        }
                    />
                </Routes>
            </Router>
        </AuthProvider>
    );
}

export default App;
