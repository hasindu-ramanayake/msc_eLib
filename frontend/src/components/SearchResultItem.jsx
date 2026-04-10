import React from 'react';

const SearchResultItem = ({ result }) => {
  return (
    <li className="p-6 hover:bg-gray-50 transition-colors cursor-pointer group">
      <div className="flex justify-between items-start gap-4">
        {result.thumbnail && (
          <img 
            src={result.thumbnail} 
            alt={result.title} 
            className="w-16 h-24 object-cover shadow-sm rounded-md flex-shrink-0" 
          />
        )}
        <div className="flex-1">
          <h3 className="text-lg font-bold text-gray-900 group-hover:text-blue-600 transition-colors mb-1">
            {result.title}
          </h3>
          {result.subtitle && <p className="text-sm text-gray-500 mb-1">{result.subtitle}</p>}
          <p className="text-sm font-medium text-gray-600 mb-2">
            {result.authors || result.author}
          </p>
          <p className="text-sm text-gray-500 line-clamp-2 max-w-3xl mb-2">{result.description}</p>
          <div className="flex gap-3 text-xs text-gray-400">
            {result.published_year && <span>Published: {result.published_year}</span>}
            {result.isbn13 && <span>ISBN: {result.isbn13}</span>}
          </div>
        </div>
      </div>
    </li>
  );
};

export default SearchResultItem;
