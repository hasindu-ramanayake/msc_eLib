import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { MagnifyingGlassIcon } from '@heroicons/react/24/outline';

const SearchBar = () => {
  const [query, setQuery] = useState('');
  const navigate = useNavigate();

  const handleSearch = (e) => {
    e.preventDefault();
    if (query.trim()) {
      navigate(`/search?keyword=${encodeURIComponent(query.trim())}`);
    }
  };

  return (
    <div className="min-w-full">
      <form onSubmit={handleSearch} className="min-w-full">
        <div className=" flex items-center w-full h-16 rounded-full bg-white shadow-sm border border-gray-200 transition-all duration-300 focus-within:shadow-md 
focus-within:border-blue-400 hover:shadow-md">

          {/* Search Icon */}
          <div className="ml-2 grid place-items-center h-full w-16 text-gray-400">
            <MagnifyingGlassIcon className="h-6 w-6 group-focus-within:text-blue-500 transition-colors duration-300" />
          </div>

          {/* Input Field */}
          <input
            className="peer h-full w-full outline-none text-base text-gray-700 pr-4 bg-transparent"
            type="text"
            id="search"
            placeholder="Search catalog for books, authors, or genres..."
            value={query}
            onChange={(e) => setQuery(e.target.value)}
          />

          {/* Search Button */}
          <button
            type="submit"
            className="h-[calc(100%-12px)] px-6 mr-1.5 bg-blue-600 hover:bg-blue-700 text-white rounded-full font-medium transition-colors duration-300 flex items-center justify-center focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
          >
            Search
          </button>
        </div>
      </form>
    </div>
  );
};

export default SearchBar;
