import React, { createContext, useContext, useState, useEffect } from 'react';

// Context for global authentication state management
const AuthContext = createContext();

// Custom hook for easier access to the authentication context
export const useAuth = () => useContext(AuthContext);

// Provider component that wraps the application and provides auth state
export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // Check local storage for existing session on app initialization
    const storedUser = localStorage.getItem('user');
    if (storedUser) {
      setUser(JSON.parse(storedUser));
    }
    setLoading(false);
  }, []);

  // Updates local state and persists user data to local storage on login
  const login = (userData) => {
    localStorage.setItem('user', JSON.stringify(userData));
    setUser(userData);
  };

  // Clears session data from both local state and local storage on logout
  const logout = () => {
    localStorage.removeItem('user');
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ user, login, logout, loading }}>
      {!loading && children}
    </AuthContext.Provider>
  );
};
