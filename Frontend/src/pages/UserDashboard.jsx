import React, { useState, useEffect } from 'react';
import api from '../services/api';
import { useAuth } from '../context/AuthContext';

const UserDashboard = () => {
  const [files, setFiles] = useState([]);
  const [selectedFile, setSelectedFile] = useState(null);
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');
  const { email } = useAuth();

  useEffect(() => {
    fetchFiles();
  }, []);

  const fetchFiles = async () => {
    try {
      const response = await api.get('/files');
      setFiles(response.data);
    } catch (error) {
      console.error('Error fetching files:', error);
      setError('Could not fetch your files. Please try again later.');
    }
  };

  const handleFileChange = (e) => {
    setSelectedFile(e.target.files[0]);
  };

  const handleUpload = async () => {
    if (!selectedFile) return;
    
    const formData = new FormData();
    formData.append('file', selectedFile);
    
    setMessage('');
    setError('');

    try {
      await api.post('/files/upload', formData, {
        headers: { 'Content-Type': 'multipart/form-data' }
      });
      setMessage('File uploaded successfully!');
      setSelectedFile(null);
      document.getElementById('file-input').value = null;
      fetchFiles();
    } catch (error) {
      console.error('Error uploading file:', error);
      setError('File upload failed. Please try again.');
    }
  };

  const handleDownload = async (fileId, filename) => {
    try {
      const response = await api.get(`/files/download/${fileId}`, { responseType: 'blob' });
      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement('a');
      link.href = url;
      link.setAttribute('download', filename);
      document.body.appendChild(link);
      link.click();
      link.remove();
    } catch (error) {
      console.error('Error downloading file:', error);
      setError('Could not download the file.');
    }
  };

  const handleDelete = async (fileId) => {
    if (window.confirm('Are you sure you want to permanently delete this file?')) {
      try {
        await api.delete(`/files/${fileId}`);
        setMessage('File deleted successfully.');
        fetchFiles();
      } catch (error) {
        console.error('Error deleting file:', error);
        setError('Failed to delete the file.');
      }
    }
  };
  
  const formatBytes = (bytes, decimals = 2) => {
    if (bytes === 0) return '0 Bytes';
    const k = 1024;
    const dm = decimals < 0 ? 0 : decimals;
    const sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    return parseFloat((bytes / Math.pow(k, i)).toFixed(dm)) + ' ' + sizes[i];
  }

  return (
    <div className="dashboard-container">
      <h2>My Dashboard</h2>
      <p className="form-subtitle" style={{textAlign: 'center', marginTop: '-2rem', marginBottom: '3rem'}}>Welcome, {email}</p>

      {message && <p className="form-message success">{message}</p>}
      {error && <p className="form-message error">{error}</p>}
      
      <div className="dashboard-section">
        <h3>Upload a New File</h3>
        <div className="upload-area">
          <input id="file-input" type="file" onChange={handleFileChange} />
          <button onClick={handleUpload} disabled={!selectedFile} className="btn btn-primary">
            Upload File
          </button>
        </div>
      </div>

      <div className="dashboard-section">
        <h3>My Files</h3>
        <div className="table-responsive">
          <table className="styled-table">
            <thead>
              <tr>
                <th>Filename</th>
                <th>File Type</th>
                <th>Size</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {files.length > 0 ? files.map((file) => (
                <tr key={file.id}>
                  <td>{file.filename}</td>
                  <td>{file.fileType}</td>
                  <td>{formatBytes(file.fileSize)}</td>
                  <td className="actions">
                    <button onClick={() => handleDownload(file.id, file.filename)} className="btn btn-secondary">Download</button>
                    <button onClick={() => handleDelete(file.id)} className="btn btn-danger">Delete</button>
                  </td>
                </tr>
              )) : (
                <tr>
                  <td colSpan="4">
                    <div className="empty-state">You haven't uploaded any files yet.</div>
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};
export default UserDashboard;

