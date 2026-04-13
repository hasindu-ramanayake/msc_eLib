import React, { useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

/**
 * Component that handles the callback after a successful Google login.
 * It extracts the JWT, Refresh Token, and User Info from the URL parameters 
 * and stores them in the global AuthContext.
 */
const OAuth2RedirectHandler = () => {
    const navigate = useNavigate();
    const location = useLocation();
    const { login } = useAuth();

    useEffect(() => {
        console.log("OAuth2RedirectHandler: Processing redirect...");
        
        // Parse the query parameters from the URL
        const params = new URLSearchParams(location.search);
        
        const token = params.get('token');
        const refreshToken = params.get('refreshToken');
        const email = params.get('email');
        const firstName = params.get('firstName');
        const lastName = params.get('lastName');
        const role = params.get('role');
        const id = params.get('id'); // Added to fix frontend missing data

        if (token) {
            console.log("OAuth2RedirectHandler: Token found, updating AuthContext...");
            // Persist the information in the AuthContext (and localStorage)
            login({
                token,
                refreshToken,
                email,
                firstName,
                lastName,
                role,
                id // Added user ID to the session
            });
            // Redirect to home page after successful session update
            console.log("OAuth2RedirectHandler: Success. Navigating to home...");
            navigate('/', { replace: true });
        } else {
            // Handle cases where no token is provided (failed authentication)
            console.warn('OAuth2RedirectHandler: No token found in URL, redirecting to login');
            navigate('/login', { replace: true });
        }
    }, [location.search, login, navigate]); // Only depend on location.search to prevent re-runs on path change

    return (
        <div className="flex items-center justify-center min-h-screen">
            <div className="text-center">
                <div className="inline-block animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-blue-600 mb-4"></div>
                <h2 className="text-xl font-semibold text-gray-700">Completing Sign-In...</h2>
                <p className="text-gray-500">Please wait while we finalize your account access.</p>
            </div>
        </div>
    );
};

export default OAuth2RedirectHandler;
