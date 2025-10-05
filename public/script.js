// Theme management
const themeToggle = document.getElementById('theme-toggle');
const statusIndicator = document.getElementById('status');
const uptimeElement = document.getElementById('uptime');

// Load saved theme
const savedTheme = localStorage.getItem('theme') || 'dark';
document.body.setAttribute('data-theme', savedTheme);
themeToggle.textContent = savedTheme === 'dark' ? '🌙' : '☀️';

themeToggle.addEventListener('click', () => {
  const currentTheme = document.body.getAttribute('data-theme');
  const newTheme = currentTheme === 'dark' ? 'light' : 'dark';
  
  document.body.setAttribute('data-theme', newTheme);
  localStorage.setItem('theme', newTheme);
  themeToggle.textContent = newTheme === 'dark' ? '🌙' : '☀️';
});

// API Health Check
async function checkHealth() {
  try {
    const response = await fetch('/api/health');
    const data = await response.json();
    
    if (data.status === 'healthy') {
      statusIndicator.style.color = 'var(--success)';
      updateUptime(data.uptime);
    }
  } catch (error) {
    statusIndicator.style.color = 'var(--warning)';
    console.error('Health check failed:', error);
  }
}

function updateUptime(seconds) {
  const hours = Math.floor(seconds / 3600);
  const minutes = Math.floor((seconds % 3600) / 60);
  uptimeElement.textContent = `${hours}h ${minutes}m`;
}

// Load dashboard data
async function loadDashboard() {
  try {
    const response = await fetch('/api/dashboard');
    const data = await response.json();
    console.log('Dashboard data loaded:', data);
    // Future: Update widgets based on data
  } catch (error) {
    console.error('Failed to load dashboard:', error);
  }
}

// Initialize
checkHealth();
loadDashboard();

// Update every 30 seconds
setInterval(checkHealth, 30000);

// Add some interactive features
document.querySelectorAll('.widget').forEach(widget => {
  widget.addEventListener('click', () => {
    widget.style.transform = 'scale(0.98)';
    setTimeout(() => {
      widget.style.transform = '';
    }, 150);
  });
});