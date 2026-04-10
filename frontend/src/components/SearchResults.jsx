import React, { useEffect, useState } from 'react';
import { useSearchParams, useNavigate } from 'react-router-dom';
import Header from './Header';
import SearchBar from './SearchBar';
import SearchResultItem from './SearchResultItem';
import CatalogFilter from './CatalogFilter';

const SearchResults = () => {
  const [searchParams] = useSearchParams();
  const query = searchParams.get('keyword');
  const navigate = useNavigate();
  const [loading, setLoading] = useState(true);
  const [results, setResults] = useState([]);

  // Fetch real search results from ApiGateway
  useEffect(() => {
    const fetchResults = async () => {
      setLoading(true);
      try {
        const keyword = query || '';
        // Base backend URL for SearchService via ApiGateway
        let url = `http://localhost:8765/api/v1/search?keyword=${encodeURIComponent(keyword)}`;
        
        // Pass filter if format holds a single type for simplicity now
        const formats = searchParams.getAll('format');
        if (formats.length > 0) {
           url += `&filter=${encodeURIComponent(formats[0].toUpperCase())}`;
        }
        
        console.log('--- Fetching real search results ---', url);
        const response = await fetch(url);
        
        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }
        
        const data = await response.json();
        setResults(data);
      } catch (error) {
        console.error('Error fetching search results:', error);
        setResults([]);
      } finally {
        setLoading(false);
      }
    };

    fetchResults();
  }, [searchParams, query]);

  return (
    <div className="bg-slate-50 min-h-screen w-full flex flex-col pt-24 pb-12">
      <Header />

      <div className="max-w-7xl mx-auto w-full px-4 sm:px-6 lg:px-8 mt-6">

        {/* Top bar with back button and inline search */}
        <div className="flex flex-col md:flex-row gap-6 mb-8 items-center justify-between bg-white p-6 rounded-2xl shadow-sm border border-gray-100">
          <button
            onClick={() => navigate('/')}
            className="flex items-center text-sm font-medium text-gray-500 hover:text-blue-600 transition-colors flex-shrink-0"
          >
            <svg className="w-5 h-5 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M10 19l-7-7m0 0l7-7m-7 7h18"></path></svg>
            Back to Home
          </button>

          <div className="w-full flex-grow max-w-4xl">
            <SearchBar />
          </div>
        </div>

        {/* Main Content Layout Container */}
        <div className="flex flex-col lg:flex-row gap-8 items-start">

          {/* Left Sidebar - Filter */}
          <CatalogFilter />

          {/* Right Side - Search Results List */}
          <div className="flex-1 w-full flex flex-col min-w-0">
            {/* Results Header */}
            <div className="mb-6">
              <h1 className="text-2xl font-bold text-gray-900">
                Search Results for: <span className="text-blue-600 break-words">"{query}"</span>
              </h1>
              {!loading && <p className="text-gray-500 text-sm mt-1">Found {results.length} results in the catalog</p>}
            </div>

            {/* Results Body */}
            {loading ? (
              <div className="flex justify-center items-center py-20 flex-1">
                <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
              </div>
            ) : (
              <div className="bg-white rounded-xl border border-gray-200 shadow-sm overflow-hidden min-h-[400px]">
                <ul className="divide-y divide-gray-200">

                  {results.length > 0 ? (
                    results.map((result) => (
                      <SearchResultItem key={result.id} result={result} />
                    ))
                  ) : (
                    <li className="p-10 text-center text-gray-500">
                      No results found. Try a different search term.
                    </li>
                  )}

                </ul>
              </div>
            )}
          </div>

        </div>
      </div>
    </div>
  );
};

export default SearchResults;
