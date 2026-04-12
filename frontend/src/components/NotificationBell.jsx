import React, { useState, useEffect, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import { BellIcon } from '@heroicons/react/24/outline';
import { BellAlertIcon } from '@heroicons/react/24/solid';

// Mock userId — in a real app this comes from auth context / JWT
const MOCK_USER_ID = '550e8400-e29b-41d4-a716-446655440000';
const API_BASE = 'http://localhost:8082';

const TYPE_STYLES = {
    REMINDER:           { bg: 'bg-blue-50',   dot: 'bg-blue-500',   label: 'Reminder'  },
    OVERDUE:            { bg: 'bg-red-50',    dot: 'bg-red-500',    label: 'Overdue'   },
    WAITLIST_AVAILABLE: { bg: 'bg-green-50',  dot: 'bg-green-500',  label: 'Available' },
    CREDIT_LOW:         { bg: 'bg-amber-50',  dot: 'bg-amber-500',  label: 'Credit'    },
};

const timeAgo = (dateStr) => {
    const diff = Math.floor((Date.now() - new Date(dateStr)) / 1000);
    if (diff < 60)   return 'just now';
    if (diff < 3600) return `${Math.floor(diff / 60)}m ago`;
    if (diff < 86400) return `${Math.floor(diff / 3600)}h ago`;
    return `${Math.floor(diff / 86400)}d ago`;
};

const markAsRead = async () => {
    try {
        await fetch(`${API_BASE}/api/v1/notifications/users/${MOCK_USER_ID}/read-all`, {
            method: 'PATCH',
        });
        setNotifications(prev =>
            prev.map(n => ({ ...n, status: 'READ' }))
        );
    } catch {
        // silently fail
    }
};

const NotificationBell = () => {
    const [notifications, setNotifications] = useState([]);
    const [open, setOpen] = useState(false);
    const [loading, setLoading] = useState(false);
    const dropdownRef = useRef(null);
    const navigate = useNavigate();

    const unreadCount = notifications.filter(n => n.status === 'SENT').length;

    const fetchNotifications = async () => {
        setLoading(true);
        try {
            const res = await fetch(`${API_BASE}/api/v1/notifications/users/${MOCK_USER_ID}`);
            if (res.ok) {
                const data = await res.json();
                setNotifications(data.slice(0, 5));
            }
        } catch {
            // silently fail — bell still renders without data
        } finally {
            setLoading(false);
        }
    };

    // Fetch on mount and every 30s
    useEffect(() => {
        fetchNotifications();
        const interval = setInterval(fetchNotifications, 30000);
        return () => clearInterval(interval);
    }, []);

    // Close on outside click
    useEffect(() => {
        const handleClickOutside = (e) => {
            if (dropdownRef.current && !dropdownRef.current.contains(e.target)) {
                setOpen(false);
            }
        };
        document.addEventListener('mousedown', handleClickOutside);
        return () => document.removeEventListener('mousedown', handleClickOutside);
    }, []);

    const handleViewAll = () => {
        setOpen(false);
        navigate('/notifications');
    };

    const style = { '--tw-ring-color': 'rgb(59 130 246 / 0.5)' };

    return (
        <div className="relative" ref={dropdownRef}>
            {/* Bell button */}
            <button
                onClick={() => {
                    const opening = !open;
                    setOpen(opening);
                    if (opening && unreadCount > 0) {
                        markAsRead();
                    }
                }}
                className="relative p-2 rounded-full text-gray-500 hover:text-blue-600 hover:bg-blue-50 transition-colors duration-200 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
                aria-label="Notifications"
            >
                {unreadCount > 0
                    ? <BellAlertIcon className="h-6 w-6 text-blue-600" />
                    : <BellIcon className="h-6 w-6" />
                }

                {/* Badge */}
                {unreadCount > 0 && (
                    <span className="absolute -top-0.5 -right-0.5 flex h-4 w-4 items-center justify-center rounded-full bg-red-500 text-white text-[10px] font-bold leading-none">
            {unreadCount > 9 ? '9+' : unreadCount}
          </span>
                )}
            </button>

            {/* Dropdown */}
            {open && (
                <div className="absolute right-0 mt-2 w-80 bg-white rounded-2xl shadow-lg border border-gray-100 z-50 overflow-hidden">

                    {/* Header */}
                    <div className="px-4 py-3 border-b border-gray-100 flex items-center justify-between">
                        <span className="text-sm font-semibold text-gray-900">Notifications</span>
                        {unreadCount > 0 && (
                            <span className="text-xs font-medium text-blue-600 bg-blue-50 px-2 py-0.5 rounded-full">
                {unreadCount} new
              </span>
                        )}
                    </div>

                    {/* List */}
                    <ul className="divide-y divide-gray-50 max-h-72 overflow-y-auto">
                        {loading && (
                            <li className="flex justify-center items-center py-8">
                                <div className="animate-spin rounded-full h-5 w-5 border-b-2 border-blue-600" />
                            </li>
                        )}

                        {!loading && notifications.length === 0 && (
                            <li className="py-8 text-center text-sm text-gray-400">
                                No notifications yet
                            </li>
                        )}

                        {!loading && notifications.map((n) => {
                            const s = TYPE_STYLES[n.type] || TYPE_STYLES.REMINDER;
                            return (
                                <li key={n.id} className={`px-4 py-3 hover:bg-gray-50 transition-colors cursor-default ${s.bg}`}>
                                    <div className="flex items-start gap-3">
                                        <span className={`mt-1.5 h-2 w-2 rounded-full flex-shrink-0 ${s.dot}`} />
                                        <div className="flex-1 min-w-0">
                                            <p className="text-sm font-medium text-gray-900 truncate">{n.title}</p>
                                            <p className="text-xs text-gray-500 mt-0.5 line-clamp-2">{n.body}</p>
                                        </div>
                                        <span className="text-[10px] text-gray-400 flex-shrink-0 mt-0.5">
                      {timeAgo(n.createdAt)}
                    </span>
                                    </div>
                                </li>
                            );
                        })}
                    </ul>

                    {/* Footer */}
                    <div className="px-4 py-3 border-t border-gray-100 bg-gray-50">
                        <button
                            onClick={handleViewAll}
                            className="w-full text-sm font-medium text-blue-600 hover:text-blue-800 transition-colors text-center"
                        >
                            View all notifications →
                        </button>
                    </div>
                </div>
            )}
        </div>
    );
};

export default NotificationBell;