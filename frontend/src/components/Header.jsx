import React from 'react';
import NotificationBell from './NotificationBell';

const Header = () => {
  return (
    <header className="fixed top-0 left-0 right-0 h-16 bg-white/80 backdrop-blur-md border-b border-gray-200 z-50 transition-all duration-300">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 h-full flex items-center justify-between">

        {/* Logo / Brand Name */}
        <div className="flex-shrink-0 flex items-center cursor-pointer">
          <span className="text-2xl font-bold bg-clip-text text-transparent bg-gradient-to-r from-blue-700 to-indigo-600 tracking-tight">
            E-Library
          </span>
        </div>

        {/* Navigation Tabs */}
        <nav className="hidden md:flex items-center space-x-1 sm:space-x-2">
          <a href="#" className="px-3 py-2 rounded-md text-sm font-medium text-gray-700 hover:text-blue-600 hover:bg-blue-50 transition-colors duration-200">
            Home
          </a>
          <a href="#" className="px-3 py-2 rounded-md text-sm font-medium text-gray-700 hover:text-blue-600 hover:bg-blue-50 transition-colors duration-200">
            MyLibsInfo
          </a>
          <a href="#" className="px-3 py-2 rounded-md text-sm font-medium text-gray-700 hover:text-blue-600 hover:bg-blue-50 transition-colors duration-200">
            Event Calender
          </a>
          <a href="#" className="px-3 py-2 rounded-md text-sm font-medium text-gray-700 hover:text-blue-600 hover:bg-blue-50 transition-colors duration-200">
            Room Reservation
          </a>
          <a href="#" className="px-3 py-2 rounded-md text-sm font-medium text-gray-700 hover:text-blue-600 hover:bg-blue-50 transition-colors duration-200">
            My Books
          </a>
          <div className="pl-4 ml-2 border-l border-gray-200 flex items-center space-x-4">
            <NotificationBell />
            <button className="px-5 py-2 rounded-full text-sm font-medium text-white bg-blue-600 hover:bg-blue-700 shadow-sm hover:shadow transition-all duration-200 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2">
              Login
            </button>
          </div>
        </nav>

        {/* Mobile menu button (Hamburger) - currently just aesthetic */}
        <div className="md:hidden flex items-center">
          <button className="text-gray-500 hover:text-gray-700 focus:outline-none p-2">
            <svg className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M4 6h16M4 12h16M4 18h16" />
            </svg>
          </button>
        </div>
      </div>
    </header>
  );
};

export default Header;
