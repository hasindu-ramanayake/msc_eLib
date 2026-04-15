import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import Header from '../components/Header';
import Footer from '../components/Footer';
import LoanItem from '../components/LoanItem';
import WaitlistItem from '../components/WaitlistItem';
import { useAuth } from '../context/AuthContext';

const BORROW_API = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8765';

const LOAN_FILTERS = ['ALL', 'ACTIVE', 'OVERDUE', 'RETURNED'];
const LOAN_LABELS = { ALL: 'All Loans', ACTIVE: 'Active', OVERDUE: 'Overdue', RETURNED: 'Returned' };

const WL_FILTERS = ['ALL', 'WAITING', 'AVAILABLE', 'CANCELLED'];
const WL_LABELS = { ALL: 'All', WAITING: 'Waiting', AVAILABLE: 'Available', CANCELLED: 'Cancelled' };

const MyLoansPage = () => {
    const { user } = useAuth();
    const navigate = useNavigate();

    const [activeTab, setActiveTab] = useState('loans');
    const [loans, setLoans] = useState([]);
    const [loansLoading, setLoansLoading] = useState(true);
    const [loanFilter, setLoanFilter] = useState('ALL');
    const [waitlist, setWaitlist] = useState([]);
    const [wlLoading, setWlLoading] = useState(true);
    const [wlFilter, setWlFilter] = useState('ALL');

    useEffect(() => {
        const go = async () => {
            if (!user?.id) {
                setLoansLoading(false);
                return;
            }
            setLoansLoading(true);
            try {
                if (!user?.id) throw new Error('no-user');
                const res = await fetch(`${BORROW_API}/api/v1/borrows/users/${user.id}`, {
                    headers: { 'Authorization': `Bearer ${user.token || ''}` },
                });
                if (!res.ok) throw new Error('Failed to fetch loans');
                setLoans(await res.json());
            } catch (err) {
                console.error(err);
                setLoans([]);
            } finally {
                setLoansLoading(false);
            }
        };
        go();
    }, [user]);

    useEffect(() => {
        const go = async () => {
            if (!user?.id) {
                setWlLoading(false);
                return;
            }
            setWlLoading(true);
            try {
                if (!user?.id) throw new Error('no-user');
                const res = await fetch(`${BORROW_API}/api/v1/waitlist/users/${user.id}`, {
                    headers: { Authorization: `Bearer ${user.token || ''}` },
                });
                if (!res.ok) throw new Error('Failed to fetch waitlist');
                setWaitlist(await res.json());
            } catch (err) {
                // console.error(err);
                setWaitlist([]);
            } finally {
                setWlLoading(false);
            }
        };
        go();
    }, [user]);

    const loanCounts = {
        ALL: loans.length,
        ACTIVE: loans.filter(l => l.status === 'ACTIVE').length,
        OVERDUE: loans.filter(l => l.status === 'OVERDUE').length,
        RETURNED: loans.filter(l => l.status === 'RETURNED').length,
    };
    const filteredLoans = loanFilter === 'ALL' ? loans : loans.filter(l => l.status === loanFilter);

    const wlCounts = {
        ALL: waitlist.length,
        WAITING: waitlist.filter(w => w.status === 'WAITING').length,
        AVAILABLE: waitlist.filter(w => w.status === 'AVAILABLE').length,
        CANCELLED: waitlist.filter(w => w.status === 'CANCELLED').length,
    };
    const filteredWl = wlFilter === 'ALL' ? waitlist : waitlist.filter(w => w.status === wlFilter);

    const isLoading = activeTab === 'loans' ? loansLoading : wlLoading;
    const resultCount = activeTab === 'loans' ? filteredLoans.length : filteredWl.length;

    return (
        <div className="bg-slate-50 min-h-screen w-full flex flex-col pt-24 pb-12">
            <Header />

            <div className="max-w-7xl mx-auto w-full px-4 sm:px-6 lg:px-8 mt-6">

                {/* ── Top bar — identical structure to SearchResults ── */}
                <div className="flex flex-col md:flex-row gap-6 mb-8 items-center justify-between bg-white p-6 rounded-2xl shadow-sm border border-gray-100">
                    <button
                        onClick={() => navigate('/')}
                        className="flex items-center text-sm font-medium text-gray-500 hover:text-blue-600 transition-colors flex-shrink-0"
                    >
                        <svg className="w-5 h-5 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M10 19l-7-7m0 0l7-7m-7 7h18" />
                        </svg>
                        Back to Home
                    </button>

                    <div className="flex-grow text-center md:text-left">
                        <h1 className="text-2xl font-bold text-gray-900">My Library Activity</h1>
                        {user && (
                            <p className="text-gray-500 text-sm mt-0.5">
                                Hello, {user.firstName} — your loans and waitlist are shown below.
                            </p>
                        )}
                    </div>
                </div>

                {/* ── Two-column layout — mirrors SearchResults ── */}
                <div className="flex flex-col lg:flex-row gap-8 items-start">

                    {/* ── Left sidebar — replaces CatalogFilter ── */}
                    <div className="w-full lg:w-64 flex-shrink-0 bg-white rounded-xl border border-gray-200 shadow-sm overflow-hidden">

                        {/* Tab switcher */}
                        <div className="p-4 border-b border-gray-100">
                            <p className="text-xs font-semibold text-gray-400 uppercase tracking-wide mb-3">View</p>
                            <div className="flex flex-col gap-1">
                                <button
                                    id="tab-loans"
                                    onClick={() => setActiveTab('loans')}
                                    className={`flex items-center justify-between w-full px-3 py-2 rounded-lg text-sm font-medium transition-colors text-left ${activeTab === 'loans'
                                        ? 'bg-blue-50 text-blue-700'
                                        : 'text-gray-700 hover:bg-gray-50'
                                        }`}
                                >
                                    <span className="flex items-center gap-2">
                                        <svg className="w-4 h-4" fill="currentColor" viewBox="0 0 20 20">
                                            <path d="M9 4.804A7.968 7.968 0 005.5 4c-1.255 0-2.443.29-3.5.804v10A7.969 7.969 0 015.5 14c1.669 0 3.218.51 4.5 1.385A7.962 7.962 0 0114.5 14c1.255 0 2.443.29 3.5.804v-10A7.968 7.968 0 0014.5 4c-1.255 0-2.443.29-3.5.804V12a1 1 0 11-2 0V4.804z" />
                                        </svg>
                                        Loans
                                    </span>
                                    <span className="flex items-center gap-1">
                                        <span className="text-xs bg-gray-100 text-gray-500 px-1.5 py-0.5 rounded">{loanCounts.ALL}</span>
                                        {loanCounts.OVERDUE > 0 && (
                                            <span className="text-xs bg-red-100 text-red-600 font-semibold px-1.5 py-0.5 rounded">{loanCounts.OVERDUE} late</span>
                                        )}
                                    </span>
                                </button>
                                <button
                                    id="tab-waitlist"
                                    onClick={() => setActiveTab('waitlist')}
                                    className={`flex items-center justify-between w-full px-3 py-2 rounded-lg text-sm font-medium transition-colors text-left ${activeTab === 'waitlist'
                                        ? 'bg-blue-50 text-blue-700'
                                        : 'text-gray-700 hover:bg-gray-50'
                                        }`}
                                >
                                    <span className="flex items-center gap-2">
                                        <svg className="w-4 h-4" fill="currentColor" viewBox="0 0 20 20">
                                            <path d="M9 2a1 1 0 000 2h2a1 1 0 100-2H9z" />
                                            <path fillRule="evenodd" d="M4 5a2 2 0 012-2 3 3 0 003 3h2a3 3 0 003-3 2 2 0 012 2v11a2 2 0 01-2 2H6a2 2 0 01-2-2V5zm3 4a1 1 0 000 2h.01a1 1 0 100-2H7zm3 0a1 1 0 000 2h3a1 1 0 100-2h-3zm-3 4a1 1 0 100 2h.01a1 1 0 100-2H7zm3 0a1 1 0 100 2h3a1 1 0 100-2h-3z" clipRule="evenodd" />
                                        </svg>
                                        Waitlist
                                    </span>
                                    <span className="flex items-center gap-1">
                                        <span className="text-xs bg-gray-100 text-gray-500 px-1.5 py-0.5 rounded">{wlCounts.ALL}</span>
                                        {wlCounts.AVAILABLE > 0 && (
                                            <span className="text-xs bg-green-100 text-green-600 font-semibold px-1.5 py-0.5 rounded">{wlCounts.AVAILABLE} ready</span>
                                        )}
                                    </span>
                                </button>
                            </div>
                        </div>

                        {/* Status filter */}
                        <div className="p-4">
                            <p className="text-xs font-semibold text-gray-400 uppercase tracking-wide mb-3">Filter by Status</p>
                            <div className="flex flex-col gap-1">
                                {(activeTab === 'loans' ? LOAN_FILTERS : WL_FILTERS).map(f => {
                                    const label = activeTab === 'loans' ? LOAN_LABELS[f] : WL_LABELS[f];
                                    const count = activeTab === 'loans' ? loanCounts[f] : wlCounts[f];
                                    const active = activeTab === 'loans' ? loanFilter === f : wlFilter === f;
                                    return (
                                        <button
                                            key={f}
                                            id={`filter-${f.toLowerCase()}`}
                                            onClick={() => activeTab === 'loans' ? setLoanFilter(f) : setWlFilter(f)}
                                            className={`flex items-center justify-between w-full px-3 py-2 rounded-lg text-sm transition-colors text-left ${active
                                                ? 'bg-blue-600 text-white font-semibold'
                                                : 'text-gray-600 hover:bg-gray-50'
                                                }`}
                                        >
                                            <span>{label}</span>
                                            <span className={`text-xs px-1.5 py-0.5 rounded font-medium ${active ? 'bg-blue-500 text-blue-100' : 'bg-gray-100 text-gray-500'}`}>
                                                {count}
                                            </span>
                                        </button>
                                    );
                                })}
                            </div>
                        </div>
                    </div>

                    {/* ── Right panel — identical to SearchResults right column ── */}
                    <div className="flex-1 w-full flex flex-col min-w-0">
                        {/* Results header */}
                        <div className="mb-6">
                            <h2 className="text-2xl font-bold text-gray-900">
                                {activeTab === 'loans' ? 'Borrowed Books' : 'Waitlist'}
                            </h2>
                            {!isLoading && (
                                <p className="text-gray-500 text-sm mt-1">
                                    Showing {resultCount} {activeTab === 'loans' ? 'loan' : 'waitlist entr'}{resultCount !== 1 ? (activeTab === 'loans' ? 's' : 'ies') : (activeTab === 'loans' ? '' : 'y')} in your account
                                </p>
                            )}
                        </div>

                        {/* Results body */}
                        {isLoading ? (
                            <div className="flex justify-center items-center py-20 flex-1">
                                <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600" />
                            </div>
                        ) : (
                            <div className="bg-white rounded-xl border border-gray-200 shadow-sm overflow-hidden min-h-[400px]">
                                <ul className="divide-y divide-gray-200">
                                    {activeTab === 'loans' ? (
                                        filteredLoans.length > 0 ? (
                                            filteredLoans.map(loan => <LoanItem key={loan.loanId} loan={loan} />)
                                        ) : (
                                            <li className="p-10 text-center text-gray-500">
                                                No loans found.{' '}
                                                <button onClick={() => setLoanFilter('ALL')} className="text-blue-600 hover:underline">Show all</button>
                                            </li>
                                        )
                                    ) : (
                                        filteredWl.length > 0 ? (
                                            filteredWl.map(entry => <WaitlistItem key={entry.waitlistId} entry={entry} />)
                                        ) : (
                                            <li className="p-10 text-center text-gray-500">
                                                No waitlist entries found.{' '}
                                                <button onClick={() => setWlFilter('ALL')} className="text-blue-600 hover:underline">Show all</button>
                                            </li>
                                        )
                                    )}
                                </ul>
                            </div>
                        )}
                    </div>

                </div>
            </div>

            <Footer />
        </div>
    );
};

export default MyLoansPage;
