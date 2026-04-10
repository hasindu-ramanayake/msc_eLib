import React from 'react';

const BookPopup = ({ book, closePopup }) => {
  return (
    <>
      {/* Overlay */}
      <div
        onClick={closePopup}
        className="fixed inset-0 bg-black bg-opacity-40 backdrop-blur-sm z-40"
      />

      {/* Popup container */}
      <div className="fixed z-50 top-1/2 left-1/2 w-[520px] max-w-full -translate-x-1/2 -translate-y-1/2 bg-white rounded-2xl shadow-2xl border border-gray-200 p-6 flex gap-6">
        
        {/* Left: Book Cover */}
        <img
            src={book.coverImage || book.thumbnail || "https://via.placeholder.com/120x180?text=No+Image"}
            alt={book.title}
            className="w-32 h-[180px] object-cover rounded-md shadow"
        />

        {/* Right: Info Section */}
        <div className="flex flex-col flex-grow relative pr-10">
          {/* Close button top right */}
          <button
            onClick={closePopup}
            className="absolute top-0 right-0 p-2 rounded-full hover:bg-gray-100 transition"
            aria-label="Close popup"
          >
            <svg
              className="w-6 h-6 text-gray-600 hover:text-gray-900"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
              xmlns="http://www.w3.org/2000/svg"
            >
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>

          {/* Title */}
          <h3 className="text-2xl font-bold text-gray-900 mb-1">{book.title}</h3>

          {/* Author */}
          <p className="text-sm text-gray-700 mb-1">
            {book.author || book.authors}
          </p>

          {/* Categories */}
          <p className="text-sm text-gray-500 mb-1 italic">
            {book.genre || "Unknown genre"}
          </p>

          {/* Availability */}
          <p className={`text-sm font-semibold mb-4 ${
            book.availability ? 'text-green-600' : 'text-red-600'
          }`}>
            {book.availability ? 'Available' : 'Not Available'}
          </p>

          {/* Description */}
          <p className="text-gray-600 text-sm flex-grow overflow-auto">{book.description}</p>

          {/* Bottom Right Button */}
          <div className="mt-6 flex justify-end">
            <button
              className={`py-2 px-6 rounded-lg font-semibold text-white transition ${
                book.availability ? 'bg-blue-600 hover:bg-blue-700' : 'bg-gray-500 cursor-not-allowed'
              }`}
              disabled={!book.availability}
            >
              {book.availability ? 'Reserve' : 'Waitlist'}
            </button>
          </div>
        </div>
      </div>
    </>
  );
};

export default BookPopup;