import React, { useEffect, useState } from 'react';
import { useSearchParams, useNavigate } from 'react-router-dom';
import Header from './Header';
import SearchBar from './SearchBar';
import SearchResultItem from './SearchResultItem';
import CatalogFilter from './CatalogFilter';

const mockResultsData = [
  {
    id: 1,
    title: "Introduction to Algorithms",
    author: "Thomas H. Cormen, Charles E. Leiserson",
    description: "A comprehensive guide to modern algorithms. This book covers a broad range of algorithms in depth, yet makes their design and analysis accessible to all levels of readers."
  },
  {
    id: 2,
    title: "Clean Code",
    author: "Robert C. Martin",
    description: "A Handbook of Agile Software Craftsmanship. Even bad code can function. But if code isn't clean, it can bring a development organization to its knees."
  },
  {
    id: 3,
    title: "Design Patterns",
    author: "Erich Gamma, Richard Helm, Ralph Johnson, John Vlissides",
    description: "Elements of Reusable Object-Oriented Software. Capturing a wealth of experience about the design of object-oriented software."
  }
];

const SearchResults = () => {
  const [searchParams] = useSearchParams();
  const query = searchParams.get('key');
  const navigate = useNavigate();
  const [loading, setLoading] = useState(true);
  const [results, setResults] = useState([]);

  // Mock search results (in reality this would fetch from an API based on `searchParams`)
  useEffect(() => {
    setLoading(true);

    // Simulating a backend call to ElasticSearch with the current URL parameters
    console.log('--- Simulating ElasticSearch API Fetch ---');
    const apiQuery = {
      q: query || '',
      formats: searchParams.getAll('format'),
      ages: searchParams.getAll('age'),
      genres: searchParams.getAll('genre'),
      language: searchParams.get('language') || 'english'
    };
    console.log('Request payload:', apiQuery);

    const timer = setTimeout(() => {
      // Dummy check to simulate finding results
      const filtered = query ? mockResultsData : [];
      setResults(filtered);
      setLoading(false);
    }, 800);
    
    return () => clearTimeout(timer);
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
