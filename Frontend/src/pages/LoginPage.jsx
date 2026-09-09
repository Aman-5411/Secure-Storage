import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

const LoginPage = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [totpCode, setTotpCode] = useState('');
  const [is2faRequired, setIs2faRequired] = useState(false); 
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const { login } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      const response = await login(email, password, totpCode);

      if (response.status === '2FA_REQUIRED') {
        setIs2faRequired(true);
        setError('Please enter your 6-digit 2FA code.');
      } else if (response.token) {
        navigate('/'); 
      }

    } catch (err) {
      setIs2faRequired(false); 
      setError(err.response?.data?.message || err.response?.data || 'Failed to log in.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-page-container">
      <div className="auth-branding-side">
       <svg className="icon" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={1.5} stroke="currentColor">
            <path strokeLinecap="round" strokeLinejoin="round" d="M12 1.5v21m-8.25-6.75h16.5M1.5 9.75h21M3 3.75h18M9 22.5h6" />
        </svg>
        <h1>Secure Storage</h1>
        <p>Your secure and reliable cloud storage solution.</p>
      </div>
      <div className="auth-form-side">
        <div className="form-card">
          <h2>Welcome Back!</h2>
          <p className="form-subtitle">Please sign in to continue.</p>
          <form onSubmit={handleSubmit}>
            <div className="form-group">
              <label>Email Address</label>
              <div className="input-with-icon">
                <svg className="input-icon" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={1.5} stroke="currentColor" width="20" height="20">
                <path strokeLinecap="round" strokeLinejoin="round" d="M21.75 6.75v10.5a2.25 2.25 0 01-2.25 2.25h-15a2.25 2.25 0 01-2.25-2.25V6.75m19.5 0A2.25 2.25 0 0019.5 4.5h-15a2.25 2.25 0 00-2.25 2.25m19.5 0v.243a2.25 2.25 0 01-1.07 1.916l-7.5 4.615a2.25 2.25 0 01-2.36 0L3.32 8.91a2.25 2.25 0 01-1.07-1.916V6.75" />
                </svg>
                <input 
                  type="email" 
                  className="form-input" 
                  value={email} 
                  onChange={(e) => setEmail(e.target.value)} 
                  required 
                  placeholder="you@example.com" 
                  disabled={is2faRequired}
                />
              </div>
            </div>
            <div className="form-group">
              <label>Password</label>
              <div className="input-with-icon">
                <svg className="input-icon" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={1.5} stroke="currentColor" width="20" height="20">
                  <path strokeLinecap="round" strokeLinejoin="round" d="M3.75 6.75h16.5M3.75 12h16.5m-16.5 5.25H12" />
                </svg>

                <input 
                  type="password" 
                  className="form-input" 
                  value={password} 
                  onChange={(e) => setPassword(e.target.value)} 
                  required 
                  placeholder="••••••••" 
                  disabled={is2faRequired} 
                />
              </div>
            </div>

            {/* --- 2FA INPUT BLOCK --- */}
            {is2faRequired && (
              <div className="form-group">
                <label>6-Digit 2FA Code</label>
                <div className="input-with-icon">
                  <svg className="input-icon" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={1.5} stroke="currentColor" width="20" height="20">
                    <path strokeLinecap="round" strokeLinejoin="round" d="M11.25 11.25l.041-.02a.75.75 0 011.063.852l-.708 2.836a.75.75 0 001.063.853l.041-.021M21 12a9 9 0 11-18 0 9 9 0 0118 0zm-9-3.75h.008v.008H12V8.25z" />
                  </svg>
                  <input 
                    type="text" 
                    className="form-input" 
                    value={totpCode} 
                    onChange={(e) => setTotpCode(e.target.value)} 
                    required 
                    placeholder="123456" 
                    maxLength={6}
                  />
                </div>
              </div>
            )}

            {error && <p className="form-message error">{error}</p>}
            
            <button type="submit" className="btn btn-primary btn-block" disabled={loading}>
              {loading ? 'Signing In...' : (is2faRequired ? 'Verify Code' : 'Sign In')}
            </button>
          </form>

          {/* --- ADD FORGOT PASSWORD LINK --- */}
          <div className="form-footer" style={{ marginTop: '1rem', textAlign: 'right' }}>
            <Link to="/forgot-password">Forgot Password?</Link>
          </div>
          
          <div className="form-footer">
            Don't have an account? <Link to="/register">Sign Up</Link>
          </div>
        </div>
      </div>
    </div>
  );
};
export default LoginPage;