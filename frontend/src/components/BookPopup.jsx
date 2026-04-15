import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';

const BORROW_API = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8765';

const BookPopup = ({ book, closePopup }) => {
  const { user } = useAuth();
  const [availability, setAvailability] = useState(null);
  const [userStatus, setUserStatus] = useState({ hasActiveLoan: false, isOnWaitlist: false });
  const [loading, setLoading] = useState(true);
  const [actionLoading, setActionLoading] = useState(false);
  const [status, setStatus] = useState({ type: '', message: '' });

  // Fetch actual availability and user status when popup opens
  useEffect(() => {
    const checkStatus = async () => {
      setLoading(true);
      try {
        const headers = { 'Authorization': `Bearer ${user?.token || ''}` };
        
        // 1. Check book availability
        const availabilityRes = await fetch(`${BORROW_API}/api/v1/borrows/available/${book.id}`, { headers });
        const availabilityData = availabilityRes.ok ? await availabilityRes.json() : null;
        setAvailability(availabilityData);

        // 2. Check user's current interaction with this book
        if (user) {
          const [borrowsRes, waitlistRes] = await Promise.all([
            fetch(`${BORROW_API}/api/v1/borrows/users/${user.id}`, { headers }),
            fetch(`${BORROW_API}/api/v1/waitlist/${user.id}`, { headers })
          ]);

          let hasActiveLoan = false;
          let isOnWaitlist = false;

          if (borrowsRes.ok) {
            const borrows = await borrowsRes.json();
            // User has an active loan if they have a non-returned record for this itemId
            hasActiveLoan = borrows.some(b => b.itemId === book.id && !b.isReturned);
          }

          if (waitlistRes.ok) {
            const waitlist = await waitlistRes.json();
            isOnWaitlist = waitlist.some(w => w.itemId === book.id);
          }

          setUserStatus({ hasActiveLoan, isOnWaitlist });
        }
      } catch (err) {
        console.error('Error checking book/user status:', err);
      } finally {
        setLoading(false);
      }
    };

    if (book?.id) checkStatus();
  }, [book?.id, user]);

  const handleAction = async () => {
    if (!user) {
      setStatus({ type: 'error', message: 'Please login to reserve books.' });
      return;
    }

    // Double check to prevent accidental clicks
    if (userStatus.hasActiveLoan || userStatus.isOnWaitlist) return;

    setActionLoading(true);
    setStatus({ type: '', message: '' });

    try {
      if (availability?.available) {
        // Reserve Flow (Create Borrow)
        const dueDate = new Date();
        dueDate.setDate(dueDate.getDate() + 14); // 14-day loan policy

        const res = await fetch(`${BORROW_API}/api/v1/borrows`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${user.token}`
          },
          body: JSON.stringify({
            userId: user.id,
            itemId: book.id,
            dueDate: dueDate.toISOString()
          })
        });

        if (res.ok) {
          setStatus({ type: 'success', message: 'Book reserved successfully! Check "My Loans".' });
          setUserStatus(prev => ({ ...prev, hasActiveLoan: true }));
          // Update local state to reflect reduced stock
          setAvailability(prev => ({ ...prev, available: prev.amount > 1, amount: prev.amount - 1 }));
        } else {
          const errData = await res.json();
          setStatus({ type: 'error', message: errData.message || 'Failed to reserve book.' });
        }
      } else {
        // Waitlist Flow
        const res = await fetch(`${BORROW_API}/api/v1/waitlist`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${user.token}`
          },
          body: JSON.stringify({
            userId: user.id,
            itemId: book.id
          })
        });

        if (res.ok) {
          setStatus({ type: 'success', message: 'Added to waitlist! We\'ll notify you when it\'s ready.' });
          setUserStatus(prev => ({ ...prev, isOnWaitlist: true }));
        } else {
          const errData = await res.json();
          setStatus({ type: 'error', message: errData.message || 'Failed to join waitlist.' });
        }
      }
    } catch (err) {
      console.error('Action error:', err);
      setStatus({ type: 'error', message: 'An unexpected connection error occurred.' });
    } finally {
      setActionLoading(false);
    }
  };

  const isAvailable = availability?.available ?? false;
  const canPerformAction = !userStatus.hasActiveLoan && !userStatus.isOnWaitlist;
  
  let buttonLabel = isAvailable ? 'Reserve Book' : 'Join Waitlist';
  if (userStatus.hasActiveLoan) buttonLabel = 'Borrowed';
  if (userStatus.isOnWaitlist) buttonLabel = 'Waitlisted';

  return (
    <>
      {/* Overlay */}
      <div
        onClick={closePopup}
        className="fixed inset-0 bg-black bg-opacity-40 backdrop-blur-sm z-40 transition-opacity"
      />

      {/* Popup container */}
      <div className="fixed z-50 top-1/2 left-1/2 w-[540px] max-w-[95%] -translate-x-1/2 -translate-y-1/2 bg-white rounded-2xl shadow-2xl border border-gray-100 p-6 flex flex-col sm:flex-row gap-6 animate-in fade-in zoom-in duration-200">
        
        {/* Left: Book Cover */}
        <div className="w-32 h-[190px] flex-shrink-0 mx-auto sm:mx-0">
          <img
              src={book.coverImage || book.thumbnail || "https://via.placeholder.com/120x180?text=No+Image"}
              alt={book.title}
              className="w-full h-full object-cover rounded-md shadow-md border border-gray-100"
          />
        </div>

        {/* Right: Info Section */}
        <div className="flex flex-col flex-grow relative pr-0 sm:pr-10 text-center sm:text-left min-w-0">
          {/* Close button top right */}
          <button
            onClick={closePopup}
            className="absolute -top-2 -right-2 p-2 rounded-full hover:bg-gray-100 transition text-gray-400 hover:text-gray-900 hidden sm:block"
            aria-label="Close popup"
          >
            <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>

          <h3 className="text-2xl font-bold text-gray-900 mb-1 leading-tight truncate-2-lines">{book.title}</h3>
          <p className="text-sm font-medium text-blue-600 mb-1">{book.author || book.authors}</p>
          <p className="text-xs text-gray-400 mb-3 uppercase tracking-wider font-semibold">
            {book.categories || book.genre || (book.category ? book.category : "Catalog Item")}
          </p>

          <div className="flex items-center justify-center sm:justify-start gap-2 mb-4">
            {loading ? (
                <div className="h-5 w-24 bg-gray-100 animate-pulse rounded-full"></div>
            ) : (
                <span className={`text-xs font-bold px-2.5 py-1 rounded-full ${isAvailable ? 'bg-emerald-50 text-emerald-700' : 'bg-amber-50 text-amber-700'}`}>
                    {isAvailable ? `AVAILABLE (${availability.amount})` : 'BACKORDER / WAITLIST'}
                </span>
            )}
          </div>

          <div className="text-gray-600 text-sm flex-grow overflow-auto max-h-[100px] mb-6 leading-relaxed scrollbar-hide">
            {book.description || "Detailed description is currently unavailable for this title."}
          </div>

          {/* User Status Indication */}
          {(userStatus.hasActiveLoan || userStatus.isOnWaitlist) && (
              <div className="text-xs font-semibold p-3 rounded-lg mb-4 bg-blue-50 text-blue-800 border border-blue-100 flex items-center gap-2">
                ℹ️ {userStatus.hasActiveLoan 
                  ? "You currently have an active loan for this book." 
                  : "You are already on the waitlist for this book."}
              </div>
          )}

          {/* Feedback Message */}
          {status.message && (
              <div className={`text-xs font-medium p-3 rounded-lg mb-4 flex items-center gap-2 ${status.type === 'success' ? 'bg-emerald-50 text-emerald-800 border border-emerald-100' : 'bg-red-50 text-red-800 border border-red-100'}`}>
                {status.type === 'success' ? '✅' : '❌'} {status.message}
              </div>
          )}

          {/* Action Buttons */}
          <div className="mt-auto flex flex-col sm:flex-row justify-center sm:justify-end gap-3">
            <button
                onClick={closePopup}
                className="px-6 py-2 text-sm font-semibold text-gray-500 hover:text-gray-900 border border-transparent hover:border-gray-200 rounded-lg transition sm:hidden"
            >
                Close
            </button>
            <button
              id="book-action-btn"
              onClick={handleAction}
              disabled={loading || actionLoading || !canPerformAction || (status.type === 'success' && isAvailable)}
              className={`py-2 px-8 rounded-lg font-bold text-white transition-all shadow-sm active:scale-95 flex items-center justify-center gap-2 min-w-[140px] ${
                !canPerformAction 
                  ? 'bg-gray-400 cursor-not-allowed'
                  : isAvailable 
                    ? 'bg-blue-600 hover:bg-blue-700 hover:shadow-blue-200' 
                    : 'bg-amber-600 hover:bg-amber-700 hover:shadow-amber-200'
              } disabled:opacity-50 disabled:active:scale-100 font-sans tracking-tight`}
            >
              {actionLoading ? (
                  <>
                    <svg className="animate-spin h-4 w-4 text-white" fill="none" viewBox="0 0 24 24">
                        <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"></circle>
                        <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                    </svg>
                    <span>Processing...</span>
                  </>
              ) : buttonLabel}
            </button>
          </div>
        </div>
      </div>
    </>
  );
};

export default BookPopup;
