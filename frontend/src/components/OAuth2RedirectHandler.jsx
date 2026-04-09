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
        // Parse the query parameters from the URL
        const params = new URLSearchParams(location.search);
        
        const token = params.get('token');
        const refreshToken = params.get('refreshToken');
        const email = params.get('email');
        const firstName = params.get('firstName');
        const lastName = params.get('lastName');
        const role = params.get('role');

        if (token) {
            // Persist the information in the AuthContext (and localStorage)
            login({
                token,
                refreshToken,
                email,
                firstName,
                lastName,
                role
            });
            // Redirect to home page after successful session update
            navigate('/', { replace: true });
        } else {
            // Handle cases where no token is provided (failed authentication)
            console.error('Authentication failed: No token found in redirect URL');
            navigate('/login', { replace: true });
        }
    }, [location, login, navigate]);

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
