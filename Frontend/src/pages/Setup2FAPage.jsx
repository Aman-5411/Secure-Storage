import React, { useState } from 'react';
import api from '../services/api';

const Setup2FAPage = () => {
  const [qrCodeImage, setQrCodeImage] = useState(''); // Stores the Base64 image
  const [code, setCode] = useState('');
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSetup = async () => {
    setLoading(true);
    setError('');
    setMessage('');
    try {
      const response = await api.post('/2fa/setup');
      setQrCodeImage(response.data.qrCodeImageBase64);
    } catch (err) {
        console.error(err);
        setError('Could not start 2FA setup. Please try again.');
    } finally {
         setLoading(false);
    }
  };

  // 2. Call /api/2fa/verify to confirm and enable 2FA
  const handleVerify = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    setMessage('');
    try {
      const response = await api.post('/2fa/verify', { code });
      setMessage(response.data.message || '2FA enabled successfully!');
      setQrCodeImage(''); 
    } catch (err) {
      setError(err.response?.data || 'Invalid code. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="dashboard-container">
      <div className="dashboard-section">
        <h3>Two-Factor Authentication (2FA)</h3>
        
        {!qrCodeImage && (
          <div>
            <p>Secure your account by enabling 2FA. Click the button to generate a QR code to scan with your authenticator app.</p>
            <button onClick={handleSetup} disabled={loading} className="btn btn-primary">
              {loading ? 'Generating...' : 'Setup 2FA'}
            </button>
          </div>
        )}

        {qrCodeImage && (
          <div style={{ textAlign: 'center' }}>
            <h4>Scan this QR code</h4>
            <p>Scan this with Google Authenticator or a similar app.</p>
            <img 
              src={`data:image/png;base64,${qrCodeImage}`} 
              alt="2FA QR Code" 
              style={{ border: '1px solid #ddd', borderRadius: '8px', margin: '20px 0' }}
            />
            <form onSubmit={handleVerify} className="form-card" style={{ maxWidth: '400px', margin: '0 auto' }}>
              <div className="form-group">
                <label>Enter 6-Digit Code to Verify</label>
                <input 
                  type="text" 
                  className="form-input" 
                  value={code} 
                  onChange={(e) => setCode(e.target.value)}
                  placeholder="123456"
                  maxLength={6}
                />
              </div>
              <button type="submit" disabled={loading} className="btn btn-primary btn-block">
                {loading ? 'Verifying...' : 'Verify & Enable'}
              </button>
            </form>
          </div>
        )}
        
        {error && <p className="form-message error" style={{ marginTop: '1rem' }}>{error}</p>}
        {message && <p className="form-message success" style={{ marginTop: '1rem' }}>{message}</p>}
      </div>
    </div>
  );
};

export default Setup2FAPage;