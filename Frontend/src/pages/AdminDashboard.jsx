import React, { useState, useEffect } from 'react';
import api from '../services/api';
import { useAuth } from '../context/AuthContext';

const AdminDashboard = () => {
  const [users, setUsers] = useState([]);
  const [allFiles, setAllFiles] = useState([]);
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');
  const { email } = useAuth(); 
  useEffect(() => {
    fetchUsers();
    fetchAllFiles();
  }, []);

  const fetchUsers = async () => {
    try {
      const response = await api.get('/users');
      setUsers(response.data);
    } catch (err) {
      console.error("Failed to fetch users", err);
      setError('Could not load users.');
    }
  };

  const fetchAllFiles = async () => {
    try {
      const response = await api.get('/files/admin/all', {
      });
      setAllFiles(response.data);
    } catch (err) {
      console.error("Failed to fetch all files", err);
      setError('Could not load files.');
    }
  };


  const handlePromoteUser = async (userToPromote) => {
    if (window.confirm(`Are you sure you want to promote ${userToPromote.name} to an Admin?`)) {
      try {
        await api.put(`/users/${userToPromote.id}`, { role: 'ROLE_ADMIN' });
        setMessage(`${userToPromote.name} has been promoted to Admin.`);
        fetchUsers(); // Refresh the user list
      } catch (err) {
        console.error("Failed to promote user", err);
        setError(`Failed to promote ${userToPromote.name}.`);
      }
    }
  };

  const handleDeleteUser = async (userToDelete) => {
    if (window.confirm(`Are you sure you want to permanently delete user ${userToDelete.name}? This action cannot be undone.`)) {
        try {
            await api.delete(`/users/${userToDelete.id}`);
            setMessage(`User ${userToDelete.name} has been deleted.`);
            fetchUsers(); // Refresh the user list
        } catch (err) {
            console.error("Failed to delete user", err);
            setError(`Failed to delete user ${userToDelete.name}.`);
        }
    }
  };

  const handleDeleteFile = async (fileId) => {
    if (window.confirm(`Are you sure you want to delete file ID ${fileId}?`)) {
        try {
            await api.delete(`/files/${fileId}`);
            setMessage(`File ID ${fileId} deleted successfully.`);
            fetchAllFiles(); // Refresh the file list
        } catch (err) {
            console.error("Failed to delete file", err);
            setError(`Failed to delete file ID ${fileId}.`);
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
      <h2>Admin Dashboard</h2>
      <p className="form-subtitle" style={{textAlign: 'center', marginTop: '-2rem', marginBottom: '3rem'}}>Welcome, {email} (Admin)</p>
      
      {message && <p className="form-message success">{message}</p>}
      {error && <p className="form-message error">{error}</p>}

      <div className="dashboard-section">
        <h3>User Management</h3>
        <table className="styled-table">
          <thead>
            <tr>
              <th>User ID</th>
              <th>Name</th>
              <th>Email</th>
              <th>Current Role</th>
              <th>Actions</th> {/* <-- ADDED ACTIONS HEADER */}
            </tr>
          </thead>
          <tbody>
            {users.length > 0 ? users.map(user => (
              <tr key={user.id}>
                <td>{user.id}</td>
                <td>{user.name}</td>
                <td>{user.email}</td>
                <td>{user.role.replace('ROLE_', '')}</td>
                <td className="actions">
                  {/* --- ADDED ACTION BUTTONS --- */}
                  <button 
                    className="btn btn-primary" 
                    onClick={() => handlePromoteUser(user)}
                    disabled={user.role === 'ROLE_ADMIN'} 
                  >
                    {user.role === 'ROLE_ADMIN' ? 'Admin' : 'Promote'}
                  </button>
                  <button 
                    className="btn btn-danger" 
                    onClick={() => handleDeleteUser(user)}
                    disabled={user.email === email} // Prevent admin from deleting themselves
                  >
                    Delete
                  </button>
                </td>
              </tr>
            )) : (
              <tr>
                <td colSpan="5"> {/* <-- UPDATED COLSPAN */}
                  <div className="empty-state">No users found.</div>
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>

      <div className="dashboard-section">
        <h3>All Uploaded Files</h3>
        <table className="styled-table">
          <thead>
            <tr>
              <th>File ID</th>
              <th>Filename</th>
              <th>Size</th>
              <th>Uploaded By</th>
              <th>Uploaded At</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {allFiles.length > 0 ? allFiles.map(file => (
              <tr key={file.id}>
                <td>{file.id}</td>
                <td>{file.filename}</td>
                <td>{formatBytes(file.fileSize)}</td>
                <td>{file.uploadedBy}</td>
                <td>{new Date(file.uploadedAt).toLocaleString()}</td>
                <td className="actions">
                   <button className="btn btn-danger" onClick={() => handleDeleteFile(file.id)}>Delete</button>
                </td>
              </tr>
            )) : (
               <tr>
                  <td colSpan="6">
                    <div className="empty-state">No files have been uploaded by any user yet.</div>
                  </td>
                </tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default AdminDashboard;

