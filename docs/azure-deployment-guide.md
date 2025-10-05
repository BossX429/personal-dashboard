# Personal Dashboard - Azure Deployment Guide

## Azure Best Practices Summary

### 1. **Node.js Configuration**
- Use LTS versions of Node.js (currently 18.x recommended)
- Set `NODE_ENV=production` for production deployments
- Configure proper startup scripts in package.json
- Use PM2 for multi-process management in production

### 2. **Monitoring & Observability**
- **Application Insights**: Enable for performance monitoring
  - Monitors server-side Node.js automatically
  - Add JavaScript SDK for client-side monitoring
  - Configure alerts for performance metrics
  
- **Log Streaming**: Use Azure portal for real-time logs
  - Access via App Service → Monitoring → Log stream
  - Configure custom timeout with `SCM_LOGSTREAM_TIMEOUT`

### 3. **Performance Optimization**
- **Connection Management**: Use `agentkeepalive` for HTTP requests
  ```javascript
  const Agent = require('agentkeepalive');
  const keepaliveAgent = new Agent({
    maxSockets: 32,
    maxFreeSockets: 10,
    timeout: 60000,
    freeSocketTimeout: 300000
  });
  ```

- **Memory Management**:
  - Monitor memory usage via Azure portal
  - Use `node-memwatch` for leak detection
  - Scale up VM size if needed

### 4. **Security Best Practices**
- Enable HTTPS (automatic with App Service)
- Implement proper input validation
- Use Managed Identities for Azure services
- Configure CORS restrictions
- Apply rate limiting
- Keep dependencies updated

### 5. **Deployment Configuration**
- **App Service Plan**: Start with F1 (Free) for development
- **Linux Runtime**: Use `NODE|18-lts`
- **Environment Variables**:
  - `NODE_ENV=production`
  - `WEBSITE_NODE_DEFAULT_VERSION=18.17.0`

### 6. **Troubleshooting**
- **500 Errors**: Check log stream for startup issues
- **Memory Issues**: Profile with heap dumps
- **CPU Issues**: Use V8-Profiler for performance analysis
- **Random Kills**: Check for uncaught exceptions or memory leaks

## Deployment Commands

### Azure CLI Setup
```bash
# Login to Azure
az login

# Create resource group
az group create --name personal-dashboard-rg --location eastus

# Deploy using Bicep
az deployment group create \
  --resource-group personal-dashboard-rg \
  --template-file azure/bicep/main.bicep \
  --parameters appName=personal-dashboard-$(date +%s)

# Deploy application
az webapp up --name <app-name> --resource-group personal-dashboard-rg
```

### Environment Configuration
```bash
# Set production environment
az webapp config appsettings set --name <app-name> --resource-group personal-dashboard-rg \
  --settings NODE_ENV=production

# Enable Application Insights
az monitor app-insights component create \
  --app personal-dashboard-insights \
  --location eastus \
  --resource-group personal-dashboard-rg
```

## Monitoring Setup

### Key Metrics to Monitor
1. **Response Time** (< 2 seconds target)
2. **Memory Usage** (< 80% of available)
3. **CPU Usage** (< 70% average)
4. **Request Rate** (monitor for traffic patterns)
5. **Error Rate** (< 1% target)

### Alert Configuration
- Set up alerts for high memory usage (> 80%)
- Configure alerts for error rates (> 5%)
- Monitor response time degradation
- Set up availability tests

## Security Checklist
- [ ] HTTPS enabled (automatic)
- [ ] Environment variables secured
- [ ] Dependencies updated
- [ ] Input validation implemented
- [ ] CORS configured
- [ ] Rate limiting enabled
- [ ] Monitoring enabled
- [ ] Backup strategy in place

## Next Steps
1. Enable Application Insights monitoring
2. Set up GitHub Actions CI/CD
3. Configure custom domain (if needed)
4. Implement health checks
5. Set up staging slots
6. Configure auto-scaling rules