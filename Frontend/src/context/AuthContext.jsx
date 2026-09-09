import React, { createContext, useState, useContext } from 'react';
import api from '../services/api';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [auth, setAuth] = useState({
    token: localStorage.getItem('token'),
    role: localStorage.getItem('role'),
    email: localStorage.getItem('email'),
  });

  const login = async (email, password, totpCode) => {
    const response = await api.post('/auth/login', { email, password, totpCode });
    if (response.data.status === '2FA_REQUIRED') {
      return response.data;
    }

    const { token, role, email: userEmail } = response.data;

    localStorage.setItem('token', token);
    localStorage.setItem('role', role);
    localStorage.setItem('email', userEmail);

    setAuth({ token, role, email: userEmail });
    return response.data;
  };

  const register = async (name, email, password) => {
    // You can set a default role or let the backend handle it
    await api.post('/auth/register', { name, email, password, role: 'ROLE_USER' });
  };

  const logout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('role');
    localStorage.removeItem('email');
    setAuth({ token: null, role: null, email: null });
  };

  return (
    <AuthContext.Provider value={{ ...auth, login, register, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);