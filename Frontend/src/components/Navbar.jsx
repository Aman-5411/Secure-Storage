import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

const Navbar = () => {
  const { token, email, role, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <nav className="navbar">
      <Link to="/" className="navbar-brand">
        Secure Storage
      </Link>
      <div className="navbar-user">
        {token ? (
          <>
            <span style={{ marginRight: '1.5rem' }}>{email}</span>
            <div className="navbar-links">
              {role === 'ROLE_ADMIN' && <Link to="/admin">Admin</Link>}
              <Link to="/setup-2fa">Setup 2FA</Link> 
            </div>
            <button onClick={handleLogout} className="btn btn-secondary">Logout</button>
          </>
        ) : (
          <div className="navbar-links">
            <Link to="/login">Login</Link>
            <Link to="/register">Register</Link>
          </div>
        )}
      </div>
    </nav>
  );
};
export default Navbar;