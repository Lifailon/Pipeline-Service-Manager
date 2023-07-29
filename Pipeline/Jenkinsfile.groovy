pipeline {
  agent any
  parameters {
    // creat parameters
    string(name: "ServiceName", defaultValue: "sshd", trim: true, description: "Enter the name of the service")
    // booleanParam(name: "ChangeState", defaultValue: false)
    choice(name: "State", choices: ["None","Start","Restart","Stop"], description: "Select state \nNone - Only get status")
    choice(name: "StartType", choices: ["None","Automatic","AutomaticDelayedStart","Disabled","Manual"])
  }
  stages {
    stage('PowerShell via Ansible') {
      steps {
        echo "Name of the selected service: $params.ServiceName"
        // pass parameters to the input of ansible variables
        sh "ansible-playbook Pipeline/Get-Service.yml -e 'ServiceName=$params.ServiceName State=$params.State StartType=$params.StartType'"
      }
    }
  }
}