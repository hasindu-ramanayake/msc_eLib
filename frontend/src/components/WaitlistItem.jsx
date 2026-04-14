import React from 'react';

const fmt = (dateStr) => {
    if (!dateStr) return '—';
    return new Date(dateStr).toLocaleDateString('en-IE', {
        day: 'numeric',
        month: 'short',
        year: 'numeric',
    });
};

const POSITION_COLOUR = (pos) => {
    if (pos === 1) return 'bg-emerald-100 text-emerald-700';
    if (pos <= 3) return 'bg-amber-100 text-amber-700';
    return 'bg-gray-100 text-gray-500';
};

/**
 * WaitlistItem
 * Displays a single waitlist entry in a list row.
 *
 * Expected `entry` shape:
 *  { waitlistId, title, author, isbn, category, description, publishedYear,
 *    position, joinedDate, estimatedAvailability, status }
 *
 * status values: WAITING | AVAILABLE | CANCELLED
 */
const WaitlistItem = ({ entry }) => {
    const isAvailable = entry.status === 'AVAILABLE';
    const isCancelled = entry.status === 'CANCELLED';

    return (
        <li className="p-6 hover:bg-gray-50 transition-colors group">
            <div className="flex justify-between items-start gap-4">
                {/* Left: queue icon block — mirrors the book spine in LoanItem */}
                <div className="w-12 flex-shrink-0 hidden sm:flex items-center justify-center rounded-md bg-gradient-to-b from-violet-100 to-purple-100 border border-gray-200 self-stretch min-h-[4.5rem]">
                    <svg className="w-5 h-5 text-violet-400" fill="currentColor" viewBox="0 0 20 20">
                        <path d="M9 2a1 1 0 000 2h2a1 1 0 100-2H9z" />
                        <path fillRule="evenodd" d="M4 5a2 2 0 012-2 3 3 0 003 3h2a3 3 0 003-3 2 2 0 012 2v11a2 2 0 01-2 2H6a2 2 0 01-2-2V5zm3 4a1 1 0 000 2h.01a1 1 0 100-2H7zm3 0a1 1 0 000 2h3a1 1 0 100-2h-3zm-3 4a1 1 0 100 2h.01a1 1 0 100-2H7zm3 0a1 1 0 100 2h3a1 1 0 100-2h-3z" clipRule="evenodd" />
                    </svg>
                </div>

                {/* Right: details */}
                <div className="flex-1 min-w-0">
                    {/* Title + badges */}
                    <div className="flex flex-wrap items-center gap-2 mb-1">
                        <h3 className="text-lg font-bold text-gray-900 group-hover:text-blue-600 transition-colors">
                            {entry.title}
                        </h3>

                        {/* Queue position badge */}
                        {!isCancelled && (
                            <span className={`text-xs font-semibold px-2.5 py-0.5 rounded-full ${POSITION_COLOUR(entry.position)}`}>
                                #{entry.position} in queue
                            </span>
                        )}

                        {/* Status badge */}
                        {isAvailable && (
                            <span className="text-xs font-semibold px-2.5 py-0.5 rounded-full bg-emerald-100 text-emerald-700 animate-pulse">
                                Ready to borrow
                            </span>
                        )}
                        {isCancelled && (
                            <span className="text-xs font-semibold px-2.5 py-0.5 rounded-full bg-gray-100 text-gray-400">
                                Cancelled
                            </span>
                        )}
                    </div>

                    <p className="text-sm font-medium text-gray-600 mb-1">{entry.author}</p>

                    {entry.description && (
                        <p className="text-sm text-gray-500 line-clamp-2 max-w-3xl mb-3">{entry.description}</p>
                    )}

                    {/* Meta row */}
                    <div className="flex flex-wrap gap-x-4 gap-y-1 text-xs text-gray-400 mb-3">
                        {entry.publishedYear && <span>Published: {entry.publishedYear}</span>}
                        {entry.isbn && <span>ISBN: {entry.isbn}</span>}
                        <span>Category: {entry.category}</span>
                    </div>

                    {/* Waitlist dates */}
                    <div className="flex flex-wrap gap-x-6 gap-y-1 text-xs text-gray-500">
                        <span>
                            <span className="font-medium text-gray-600">Joined:</span> {fmt(entry.joinedDate)}
                        </span>
                        {entry.estimatedAvailability && !isAvailable && (
                            <span>
                                <span className="font-medium text-gray-600">Est. available:</span> {fmt(entry.estimatedAvailability)}
                            </span>
                        )}
                    </div>

                    {/* Available call-to-action */}
                    {isAvailable && (
                        <div className="mt-3 flex items-center gap-3 p-3 bg-emerald-50 border border-emerald-200 rounded-lg">
                            <svg className="w-4 h-4 text-emerald-600 flex-shrink-0" fill="currentColor" viewBox="0 0 20 20">
                                <path fillRule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clipRule="evenodd" />
                            </svg>
                            <p className="text-xs font-semibold text-emerald-700 flex-1">
                                This item is now available for you to borrow. Visit the library to collect it.
                            </p>
                        </div>
                    )}
                </div>
            </div>
        </li>
    );
};

export default WaitlistItem;
