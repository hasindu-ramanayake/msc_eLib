import React from 'react';

const SearchResultItem = ({ result }) => {
  return (
    <li className="p-6 hover:bg-gray-50 transition-colors cursor-pointer group">
      <div className="flex justify-between items-start">
        <div>
          <h3 className="text-lg font-bold text-gray-900 group-hover:text-blue-600 transition-colors mb-1">
            {result.title}
          </h3>
          <p className="text-sm font-medium text-gray-600 mb-2">{result.author}</p>
          <p className="text-sm text-gray-500 line-clamp-2 max-w-3xl">{result.description}</p>
        </div>
      </div>
    </li>
  );
};

export default SearchResultItem;
