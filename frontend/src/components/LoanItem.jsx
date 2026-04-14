import React from 'react';

const STATUS_BADGE = {
    ACTIVE: 'bg-emerald-100 text-emerald-700',
    OVERDUE: 'bg-red-100 text-red-700',
    RETURNED: 'bg-gray-100 text-gray-500',
};

const STATUS_LABEL = {
    ACTIVE: 'Active',
    OVERDUE: 'Overdue',
    RETURNED: 'Returned',
};

const fmt = (dateStr) => {
    if (!dateStr) return null;
    return new Date(dateStr).toLocaleDateString('en-IE', {
        day: 'numeric',
        month: 'short',
        year: 'numeric',
    });
};

const daysRemaining = (dueStr) =>
    Math.ceil((new Date(dueStr) - Date.now()) / 86_400_000);

const LoanItem = ({ loan }) => {
    const days = loan.status === 'ACTIVE' ? daysRemaining(loan.dueDate) : null;

    return (
        <li className="p-6 hover:bg-gray-50 transition-colors group">
            <div className="flex justify-between items-start gap-4">
                {/* Left: book spine icon block (mirrors thumbnail in SearchResultItem) */}
                <div className="w-12 flex-shrink-0 hidden sm:flex items-center justify-center rounded-md bg-gradient-to-b from-blue-100 to-indigo-100 border border-gray-200 self-stretch min-h-[4.5rem]">
                    <svg className="w-5 h-5 text-blue-400" fill="currentColor" viewBox="0 0 20 20">
                        <path d="M9 4.804A7.968 7.968 0 005.5 4c-1.255 0-2.443.29-3.5.804v10A7.969 7.969 0 015.5 14c1.669 0 3.218.51 4.5 1.385A7.962 7.962 0 0114.5 14c1.255 0 2.443.29 3.5.804v-10A7.968 7.968 0 0014.5 4c-1.255 0-2.443.29-3.5.804V12a1 1 0 11-2 0V4.804z" />
                    </svg>
                </div>

                {/* Right: details */}
                <div className="flex-1 min-w-0">
                    <div className="flex flex-wrap items-center gap-2 mb-1">
                        <h3 className="text-lg font-bold text-gray-900 group-hover:text-blue-600 transition-colors">
                            {loan.title}
                        </h3>
                        <span className={`text-xs font-semibold px-2.5 py-0.5 rounded-full ${STATUS_BADGE[loan.status] || 'bg-gray-100 text-gray-500'}`}>
                            {STATUS_LABEL[loan.status] || loan.status}
                        </span>
                    </div>

                    <p className="text-sm font-medium text-gray-600 mb-1">{loan.author}</p>
                    {loan.description && (
                        <p className="text-sm text-gray-500 line-clamp-2 max-w-3xl mb-3">{loan.description}</p>
                    )}

                    {/* Meta row — mirrors the isbn/published_year row in SearchResultItem */}
                    <div className="flex flex-wrap gap-x-4 gap-y-1 text-xs text-gray-400 mb-3">
                        {loan.publishedYear && <span>Published: {loan.publishedYear}</span>}
                        {loan.isbn && <span>ISBN: {loan.isbn}</span>}
                        <span>Category: {loan.category}</span>
                    </div>

                    {/* Loan dates */}
                    <div className="flex flex-wrap gap-x-6 gap-y-1 text-xs text-gray-500">
                        <span>
                            <span className="font-medium text-gray-600">Borrowed:</span> {fmt(loan.borrowedDate)}
                        </span>
                        <span className={loan.status === 'OVERDUE' ? 'text-red-600 font-semibold' : ''}>
                            <span className={`font-medium ${loan.status === 'OVERDUE' ? 'text-red-600' : 'text-gray-600'}`}>Due:</span>{' '}
                            {fmt(loan.dueDate)}
                        </span>
                        {loan.returnedDate && (
                            <span className="text-emerald-600">
                                <span className="font-medium">Returned:</span> {fmt(loan.returnedDate)}
                            </span>
                        )}
                    </div>

                    {/* Due-soon / overdue inline notice */}
                    {loan.status === 'ACTIVE' && days !== null && days <= 3 && (
                        <p className={`mt-2 text-xs font-semibold ${days <= 0 ? 'text-red-600' : 'text-amber-600'}`}>
                            {days <= 0
                                ? `⚠ Overdue by ${Math.abs(days)} day(s) — please return as soon as possible.`
                                : days === 1
                                ? '⚠ Due tomorrow!'
                                : `⚠ Due in ${days} days.`}
                        </p>
                    )}
                    {loan.status === 'OVERDUE' && (
                        <p className="mt-2 text-xs font-semibold text-red-600">
                            ⚠ This item is overdue — please return it as soon as possible.
                        </p>
                    )}
                </div>
            </div>
        </li>
    );
};

export default LoanItem;
