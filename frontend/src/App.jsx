import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import './App.css';
import Header from './components/Header';
import SearchBar from './components/SearchBar';
import UpcomingEvents from './components/UpcomingEvents';
import Footer from './components/Footer';
import SearchResults from './components/SearchResults';
import { AuthProvider } from './context/AuthContext';
import Login from './components/Login';
import Register from './components/Register';

// Extracted the old App landing page contents into its own component
const Home = () => {
  return (
    <div className="bg-white min-h-screen w-full flex flex-col items-center pt-24">
      <Header />

      {/* Main Content Container - Takes 2/3 of the screen width */}
      <div className="w-2/3 max-w-none mt-8 flex flex-col flex-grow">
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

        </Routes>
      </Router>
    </AuthProvider>
  );
}

export default App;
