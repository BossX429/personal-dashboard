@description('The base name for all resources')
param baseName string = 'personal-dashboard'

@description('Location for all resources')
param location string = resourceGroup().location

@description('The SKU of the App Service plan')
param sku string = 'B1'

@description('Database server administrator login')
param administratorLogin string = 'dashboardadmin'

@description('Database server administrator password')
@secure()
param administratorPassword string

// Variables
var uniqueSuffix = uniqueString(resourceGroup().id)
var appServicePlanName = '${baseName}-plan-${uniqueSuffix}'
var nodeAppName = '${baseName}-web-${uniqueSuffix}'
var javaAppName = '${baseName}-api-${uniqueSuffix}'
var databaseServerName = '${baseName}-db-${uniqueSuffix}'
var databaseName = 'dashboard_db'
var containerRegistryName = '${replace(baseName, '-', '')}acr${uniqueSuffix}'

// Container Registry
resource containerRegistry 'Microsoft.ContainerRegistry/registries@2023-07-01' = {
  name: containerRegistryName
  location: location
  sku: {
    name: 'Basic'
  }
  properties: {
    adminUserEnabled: true
  }
}

// App Service Plan
resource appServicePlan 'Microsoft.Web/serverfarms@2023-01-01' = {
  name: appServicePlanName
  location: location
  sku: {
    name: sku
  }
  kind: 'linux'
  properties: {
    reserved: true
  }
}

// PostgreSQL Server
resource databaseServer 'Microsoft.DBforPostgreSQL/flexibleServers@2023-06-01-preview' = {
  name: databaseServerName
  location: location
  sku: {
    name: 'Standard_B1ms'
    tier: 'Burstable'
  }
  properties: {
    administratorLogin: administratorLogin
    administratorLoginPassword: administratorPassword
    version: '15'
    storage: {
      storageSizeGB: 32
    }
    backup: {
      backupRetentionDays: 7
      geoRedundantBackup: 'Disabled'
    }
    network: {
      publicNetworkAccess: 'Enabled'
    }
  }
}

// PostgreSQL Database
resource database 'Microsoft.DBforPostgreSQL/flexibleServers/databases@2023-06-01-preview' = {
  parent: databaseServer
  name: databaseName
  properties: {
    charset: 'UTF8'
    collation: 'en_US.UTF8'
  }
}

// Firewall rule to allow Azure services
resource databaseFirewallRule 'Microsoft.DBforPostgreSQL/flexibleServers/firewallRules@2023-06-01-preview' = {
  parent: databaseServer
  name: 'AllowAzureServices'
  properties: {
    startIpAddress: '0.0.0.0'
    endIpAddress: '0.0.0.0'
  }
}

// Node.js Web App
resource nodeWebApp 'Microsoft.Web/sites@2023-01-01' = {
  name: nodeAppName
  location: location
  properties: {
    serverFarmId: appServicePlan.id
    siteConfig: {
      linuxFxVersion: 'NODE|18-lts'
      appSettings: [
        {
          name: 'NODE_ENV'
          value: 'production'
        }
        {
          name: 'API_BASE_URL'
          value: 'https://${javaAppName}.azurewebsites.net'
        }
        {
          name: 'WEBSITE_NODE_DEFAULT_VERSION'
          value: '18.17.0'
        }
      ]
    }
    httpsOnly: true
  }
}

// Java API App
resource javaWebApp 'Microsoft.Web/sites@2023-01-01' = {
  name: javaAppName
  location: location
  properties: {
    serverFarmId: appServicePlan.id
    siteConfig: {
      linuxFxVersion: 'DOCKER|${containerRegistry.properties.loginServer}/dashboard-api:latest'
      appSettings: [
        {
          name: 'SPRING_PROFILES_ACTIVE'
          value: 'production'
        }
        {
          name: 'SPRING_DATASOURCE_URL'
          value: 'jdbc:postgresql://${databaseServer.properties.fullyQualifiedDomainName}:5432/${databaseName}'
        }
        {
          name: 'SPRING_DATASOURCE_USERNAME'
          value: administratorLogin
        }
        {
          name: 'SPRING_DATASOURCE_PASSWORD'
          value: administratorPassword
        }
        {
          name: 'DOCKER_REGISTRY_SERVER_URL'
          value: 'https://${containerRegistry.properties.loginServer}'
        }
        {
          name: 'DOCKER_REGISTRY_SERVER_USERNAME'
          value: containerRegistry.listCredentials().username
        }
        {
          name: 'DOCKER_REGISTRY_SERVER_PASSWORD'
          value: containerRegistry.listCredentials().passwords[0].value
        }
      ]
    }
    httpsOnly: true
  }
}

// Application Insights
resource applicationInsights 'Microsoft.Insights/components@2020-02-02' = {
  name: '${baseName}-insights-${uniqueSuffix}'
  location: location
  kind: 'web'
  properties: {
    Application_Type: 'web'
  }
}

// Output important values
output nodeWebAppUrl string = 'https://${nodeWebApp.properties.defaultHostName}'
output javaApiUrl string = 'https://${javaWebApp.properties.defaultHostName}'
output databaseServerFqdn string = databaseServer.properties.fullyQualifiedDomainName
output containerRegistryLoginServer string = containerRegistry.properties.loginServer
output applicationInsightsInstrumentationKey string = applicationInsights.properties.InstrumentationKey