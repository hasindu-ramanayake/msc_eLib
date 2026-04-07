import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Header from '../components/Header';
import Footer from '../components/Footer';

const MOCK_USER_ID = '550e8400-e29b-41d4-a716-446655440000';
const API_BASE = 'http://localhost:8082';

const TYPE_CONFIG = {
    REMINDER: {
        label: 'Reminder',
        bg: 'bg-blue-50',
        border: 'border-blue-200',
        dot: 'bg-blue-500',
        badge: 'bg-blue-100 text-blue-700',
    },
    OVERDUE: {
        label: 'Overdue',
        bg: 'bg-red-50',
        border: 'border-red-200',
        dot: 'bg-red-500',
        badge: 'bg-red-100 text-red-700',
    },
    WAITLIST_AVAILABLE: {
        label: 'Available',
        bg: 'bg-green-50',
        border: 'border-green-200',
        dot: 'bg-green-500',
        badge: 'bg-green-100 text-green-700',
    },
    CREDIT_LOW: {
        label: 'Credit',
        bg: 'bg-amber-50',
        border: 'border-amber-200',
        dot: 'bg-amber-500',
        badge: 'bg-amber-100 text-amber-700',
    },
};

const formatDate = (dateStr) => {
    if (!dateStr) return '—';
    return new Date(dateStr).toLocaleString('en-IE', {
        day: 'numeric', month: 'short', year: 'numeric',
        hour: '2-digit', minute: '2-digit',
    });
};

const NotificationsPage = () => {
    const [notifications, setNotifications] = useState([]);
    const [loading, setLoading] = useState(true);
    const [filter, setFilter] = useState('ALL');
    const navigate = useNavigate();

    useEffect(() => {
        const fetchAll = async () => {
            setLoading(true);
            try {
                const res = await fetch(`${API_BASE}/api/v1/notifications/users/${MOCK_USER_ID}`);
                if (res.ok) setNotifications(await res.json());
            } catch {
                // silently fail
            } finally {
                setLoading(false);
            }
        };
        fetchAll();
    }, []);

    const types = ['ALL', ...Object.keys(TYPE_CONFIG)];

    const filtered = filter === 'ALL'
        ? notifications
        : notifications.filter(n => n.type === filter);

    return (
        <div className="bg-slate-50 min-h-screen flex flex-col pt-24 pb-12">
            <Header />

            <div className="max-w-3xl mx-auto w-full px-4 sm:px-6 mt-8 flex-1">

                {/* Top bar */}
                <div className="flex items-center justify-between mb-6">
                    <button
                        onClick={() => navigate(-1)}
                        className="flex items-center text-sm font-medium text-gray-500 hover:text-blue-600 transition-colors"
                    >
                        <svg className="w-4 h-4 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M10 19l-7-7m0 0l7-7m-7 7h18" />
                        </svg>
                        Back
                    </button>
                    <h1 className="text-xl font-bold text-gray-900">All Notifications</h1>
                    <span className="text-sm text-gray-400">{notifications.length} total</span>
                </div>

                {/* Filter tabs */}
                <div className="flex gap-2 flex-wrap mb-6">
                    {types.map(t => {
                        const cfg = TYPE_CONFIG[t];
                        const active = filter === t;
                        return (
                            <button
                                key={t}
                                onClick={() => setFilter(t)}
                                className={`px-3 py-1.5 rounded-full text-xs font-semibold border transition-colors ${
                                    active
                                        ? 'bg-blue-600 text-white border-blue-600'
                                        : 'bg-white text-gray-600 border-gray-200 hover:border-blue-300 hover:text-blue-600'
                                }`}
                            >
                                {cfg ? cfg.label : 'All'}
                            </button>
                        );
                    })}
                </div>

                {/* Content */}
                {loading ? (
                    <div className="flex justify-center py-20">
                        <div className="animate-spin rounded-full h-10 w-10 border-b-2 border-blue-600" />
                    </div>
                ) : filtered.length === 0 ? (
                    <div className="bg-white rounded-2xl border border-gray-200 p-12 text-center">
                        <p className="text-gray-400 text-sm">No notifications found.</p>
                    </div>
                ) : (
                    <ul className="space-y-3">
                        {filtered.map(n => {
                            const cfg = TYPE_CONFIG[n.type] || TYPE_CONFIG.REMINDER;
                            return (
                                <li
                                    key={n.id}
                                    className={`rounded-2xl border p-4 ${cfg.bg} ${cfg.border}`}
                                >
                                    <div className="flex items-start gap-3">
                                        {/* Dot */}
                                        <span className={`mt-2 h-2.5 w-2.5 rounded-full flex-shrink-0 ${cfg.dot}`} />

                                        {/* Body */}
                                        <div className="flex-1 min-w-0">
                                            <div className="flex items-center gap-2 mb-1 flex-wrap">
                        <span className={`text-xs font-semibold px-2 py-0.5 rounded-full ${cfg.badge}`}>
                          {cfg.label}
                        </span>
                                                <span className="text-xs text-gray-400">{formatDate(n.createdAt)}</span>
                                            </div>
                                            <p className="text-sm font-semibold text-gray-900 mb-0.5">{n.title}</p>
                                            <p className="text-sm text-gray-600">{n.body}</p>
                                        </div>

                                        {/* Status */}
                                        <span className={`text-xs font-medium flex-shrink-0 px-2 py-0.5 rounded-full ${
                                            n.status === 'SENT'
                                                ? 'bg-green-100 text-green-700'
                                                : n.status === 'FAILED'
                                                    ? 'bg-red-100 text-red-700'
                                                    : 'bg-gray-100 text-gray-500'
                                        }`}>
                      {n.status}
                    </span>
                                    </div>
                                </li>
                            );
                        })}
                    </ul>
                )}
            </div>

            <Footer />
        </div>
    );
};

export default NotificationsPage;