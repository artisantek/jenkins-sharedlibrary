def call(String registryCred = 'a', String registryname = 'a', String docTag = 'a', String grepo = 'a', String gbranch = 'a', String gitcred = 'a', String depname = 'a', String contname = 'a') {

pipeline {
environment { 
		registryCredential = "${registryCred}"
    		registry = "${registryname}" 	
		dockerTag = "${docTag}$BUILD_NUMBER"
		gitRepo = "${grepo}"
		gitBranch = "${gbranch}"
		gitCredId = "${gitcred}"
    		deployment = "${depname}"
    		containerName = "${contname}"
	}
		
    agent none

    stages {
        stage("POLL SCM"){
		agent{ label('docker && eks') }
            		steps {
                	checkout([$class: 'GitSCM', branches: [[name: "$gitBranch"]], extensions: [], userRemoteConfigs: [[credentialsId: "$gitCredId", url: "$gitRepo"]]])             
            		}
        } 
        
        stage('BUILD IMAGE') {
		agent{label 'docker'}
            		steps {
                	sh 'docker build -t $registry:$dockerTag .'             
            		}
        }
        
        stage('PUSH HUB') { 
		agent{label 'docker'}
            		steps {
			            sh 'docker push $registry:$dockerTag'                   	
                	}    
        }
        
        stage('DEPLOY IMAGE') {
		agent{label 'eks'}
		          steps {
			            sh 'helm upgrade --install useraccount helm/nodejs-useraccount --set image.repository="$registry:$dockerTag" '
		          }
	}  
    }
}  
}
