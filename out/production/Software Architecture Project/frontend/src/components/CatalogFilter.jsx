import React from 'react';
import { useSearchParams } from 'react-router-dom';

const CatalogFilter = () => {
  const [searchParams, setSearchParams] = useSearchParams();

  const formats = searchParams.getAll('format');
  const ages = searchParams.getAll('age');
  const genres = searchParams.getAll('genre');
  const language = searchParams.get('language') || 'english';

  const handleCheckboxChange = (category, value) => {
    const newParams = new URLSearchParams(searchParams);
    const currentValues = newParams.getAll(category);

    // Clear existing for this category
    newParams.delete(category);

    if (currentValues.includes(value)) {
      // Re-append all except the one being removed
      currentValues.filter(v => v !== value).forEach(v => newParams.append(category, v));
    } else {
      // Re-append all plus the new one
      currentValues.forEach(v => newParams.append(category, v));
      newParams.append(category, value);
    }

    setSearchParams(newParams);
  };

  const clearFilters = () => {
    const newParams = new URLSearchParams(searchParams);
    newParams.delete('format');
    newParams.delete('age');
    newParams.delete('genre');
    newParams.delete('language');
    setSearchParams(newParams);
  };

  return (
    <div className="bg-white rounded-2xl shadow-sm border border-gray-100 p-6 w-full lg:w-64 flex-shrink-0 self-start sticky top-28">
      <h2 className="text-lg font-bold text-gray-900 mb-6">Filter Results</h2>

      {/* Format Filter */}
      <div className="mb-6">
        <h3 className="text-sm font-semibold text-gray-700 uppercase tracking-wider mb-3">Format</h3>
        <div className="space-y-2">
          {['Book', 'Movies', 'Games'].map(format => (
            <label key={format} className="flex items-center text-sm text-gray-600 cursor-pointer">
              <input
                type="checkbox"
                className="mr-3 h-4 w-4 rounded text-blue-600 focus:ring-blue-500 border-gray-300"
                checked={formats.includes(format)}
                onChange={() => handleCheckboxChange('format', format)}
              />
              {format}
            </label>
          ))}
        </div>
      </div>

      {/* Age Filter */}
      <div className="mb-6">
        <h3 className="text-sm font-semibold text-gray-700 uppercase tracking-wider mb-3">Age</h3>
        <div className="space-y-2">
          {['12+', '16+', '18+', '21+'].map(age => (
            <label key={age} className="flex items-center text-sm text-gray-600 cursor-pointer">
              <input
                type="checkbox"
                className="mr-3 h-4 w-4 rounded text-blue-600 focus:ring-blue-500 border-gray-300"
                checked={ages.includes(age)}
                onChange={() => handleCheckboxChange('age', age)}
              />
              {age}
            </label>
          ))}
        </div>
      </div>

      {/* Genre Filter */}
      <div className="mb-6">
        <h3 className="text-sm font-semibold text-gray-700 uppercase tracking-wider mb-3">Genre</h3>
        <div className="space-y-2">
          {['Fiction', 'Non-Fiction', 'Mystery', 'Sci-Fi', 'Biography', 'Fantasy', 'Romance'].map(genre => (
            <label key={genre} className="flex items-center text-sm text-gray-600 cursor-pointer">
              <input
                type="checkbox"
                className="mr-3 h-4 w-4 rounded text-blue-600 focus:ring-blue-500 border-gray-300"
                checked={genres.includes(genre)}
                onChange={() => handleCheckboxChange('genre', genre)}
              />
              {genre}
            </label>
          ))}
        </div>
      </div>

      {/* Language Filter */}
      <div>
        <h3 className="text-sm font-semibold text-gray-700 uppercase tracking-wider mb-3">Language</h3>
        <select
          className="w-full bg-slate-50 border border-gray-200 text-gray-700 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block p-2.5 outline-none"
          value={language}
          onChange={(e) => {
            const newParams = new URLSearchParams(searchParams);
            if (e.target.value === 'english') {
              newParams.delete('language');
            } else {
              newParams.set('language', e.target.value);
            }
            setSearchParams(newParams);
          }}
        >
          <option value="english">English</option>
          <option value="spanish">Spanish</option>
          <option value="french">French</option>
          <option value="multi">Multiple Languages</option>
        </select>
      </div>

      {/* Clear Filters Button */}
      <button
        className="w-full mt-6 py-2 px-4 text-sm font-medium text-blue-600 bg-blue-50 hover:bg-blue-100 rounded-lg transition-colors"
        onClick={clearFilters}
      >
        Clear Filters
      </button>

    </div>
  );
};

export default CatalogFilter;
