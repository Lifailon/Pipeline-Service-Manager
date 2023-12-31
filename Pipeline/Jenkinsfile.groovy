pipeline {
  agent any
  parameters {
    string(name: "ServiceName", defaultValue: "zabbix", trim: true, description: "Enter a service name. For Windows systems the default is Fildcard format. For Linux use formate *name* (not supported for StartType mode).")
    choice(name: "System", choices: ["Windows","Linux"], description: "Select targeted the operating system.")
    // booleanParam(name: "ChangeState", defaultValue: false)
    choice(name: "State", choices: ["None","Start","Restart","Stop"], description: "None - only get status.")
    choice(name: "StartType", choices: ["None","Enabled","Manual","Automatic","AutomaticDelayedStart","Disabled"], description: "Enabled mode - for system Linux. Automatic/Manual mode - for system Windows. Delayed Start mode - supported only to PowerShell 7.")
  }
  stages {
    stage('Powershell via Ansible') {
      when {
        expression { params.System == 'Windows' }
      }
      steps {
        echo "Selected service - $params.ServiceName"
        echo "Selected parameters - State: $params.State and StartType: $params.StartType"
        sh "ansible-playbook Pipeline/Get-Service.yml -e 'ServiceName=$params.ServiceName State=$params.State StartType=$params.StartType'"
      }
    }
    stage('Systemctl via Ansible') {
      when {
        expression { params.System == 'Linux' }
      }
      steps {
        echo "Selected service - $params.ServiceName"
        echo "Selected parameters - State: $params.State and StartType: $params.StartType"
        sh "ansible-playbook Pipeline/Systemctl.yml -e 'ServiceName=$params.ServiceName State=$params.State StartType=$params.StartType' -b"
      }
    }
  }
}