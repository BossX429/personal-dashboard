const express = require('express');
const cors = require('cors');
const helmet = require('helmet');
require('dotenv').config();

const app = express();
const PORT = process.env.PORT || 3000;

// Middleware
app.use(helmet());
app.use(cors());
app.use(express.json());
app.use(express.static('public'));

// Routes
app.get('/', (req, res) => {
  res.sendFile(__dirname + '/../public/index.html');
});

app.get('/api/health', (req, res) => {
  res.json({ 
    status: 'healthy', 
    timestamp: new Date().toISOString(),
    uptime: process.uptime()
  });
});

app.get('/api/dashboard', (req, res) => {
  res.json({
    widgets: [
      { id: 1, type: 'weather', title: 'Weather', enabled: true },
      { id: 2, type: 'tasks', title: 'Tasks', enabled: true },
      { id: 3, type: 'system', title: 'System Status', enabled: true },
      { id: 4, type: 'github', title: 'GitHub Activity', enabled: false }
    ],
    user: {
      name: 'Developer',
      theme: 'dark'
    }
  });
});

app.listen(PORT, () => {
  console.log(`🚀 Personal Dashboard running on port ${PORT}`);
  console.log(`📊 Dashboard: http://localhost:${PORT}`);
});