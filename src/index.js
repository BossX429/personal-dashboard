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

app.get('/api/dashboard', async (req, res) => {
  try {
    // Try to get config from Java API, fallback to default
    const javaApiUrl = process.env.API_BASE_URL || 'http://localhost:8080';
    let dashboardConfig;
    
    try {
      const response = await axios.get(`${javaApiUrl}/api/v1/dashboard/config`);
      dashboardConfig = response.data;
    } catch (error) {
      console.log('Java API not available, using default config');
      dashboardConfig = {
        widgets: [
          { id: 1, type: 'weather', title: 'Weather', enabled: true },
          { id: 2, type: 'tasks', title: 'Tasks', enabled: true },
          { id: 3, type: 'system', title: 'System Status', enabled: true },
          { id: 4, type: 'github', title: 'GitHub Activity', enabled: false }
        ],
        refreshInterval: 30000,
        theme: 'dark'
      };
    }
    
    res.json({
      ...dashboardConfig,
      user: {
        name: 'Developer',
        theme: dashboardConfig.theme || 'dark'
      }
    });
  } catch (error) {
    console.error('Error fetching dashboard config:', error);
    res.status(500).json({ error: 'Failed to load dashboard configuration' });
  }
});

// Proxy endpoint for Java API metrics
app.get('/api/metrics/system', async (req, res) => {
  try {
    const javaApiUrl = process.env.API_BASE_URL || 'http://localhost:8080';
    const response = await axios.get(`${javaApiUrl}/api/v1/metrics/system`);
    res.json(response.data);
  } catch (error) {
    console.error('Error fetching system metrics:', error);
    // Return mock data if Java API is not available
    res.json({
      cpuUsage: Math.random() * 100,
      memoryUsage: Math.random() * 100,
      diskUsage: Math.random() * 100,
      timestamp: new Date().toISOString(),
      serverName: 'node-fallback'
    });
  }
});

app.listen(PORT, () => {
  console.log(`🚀 Personal Dashboard running on port ${PORT}`);
  console.log(`📊 Dashboard: http://localhost:${PORT}`);
});