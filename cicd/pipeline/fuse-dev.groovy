try {
    node(){
            configFileProvider([configFile(fileId: 'maven-cicd', variable: 'MAVEN_SETTINGS_XML')]) {
                def mvnCmd = "mvn -U -s $MAVEN_SETTINGS_XML"
                 properties([
                //         pipelineTriggers([
                //             pollSCM('H/5 * * * *')
                //         ]),
                    parameters([
                        text(name: 'GIT_REPO', defaultValue: 'http://svrtfs.pelindo.co.id:8080/tfs/Middleware/Middleware/_git/masterdata', description: 'GIT Repository URL?'),
                        string(name: 'GIT_REPO_BRANCH', defaultValue: 'MASTER', description: 'GIT Repository Branch?'), 
                        string(name: 'GIT_REPO_SUBFOLDER', defaultValue: 'sandboxrest', description: 'GIT Repository Sub Folder?'),
                        string(name: 'GIT_REPO_CREDENTIAL', defaultValue: 'pelindo-repo-cred', description: 'GIT Repository Credential'),
                        string(name: 'MAVEN_REPO_CREDENTIAL', defaultValue: 'pelindo-mvn-cred', description: 'MAVEN Repository Credential'),
                        string(name: 'MAVEN_REPO_URL', defaultValue: 'maven.pelindo.co.id:8081/artifactory/pelindo3-01-development', description: 'MAVEN Repository URL')
                        
                    ])
                ])
                timeout(time:60 , unit: 'MINUTES'){
                        stage("Git Clone"){
                            git branch: params.GIT_REPO_BRANCH, url: params.GIT_REPO, credentialsId: params.GIT_REPO_CREDENTIAL, poll: true, changelog: true
                            // git branch: params.GIT_REPO_BRANCH, url: params.GIT_REPO, poll: true, changelog: true
                            echo "try to build :${params.GIT_REPO_BRANCH} with subfolder: ${params.GIT_REPO_SUBFOLDER}"
                        }
                        stage ("Compiling"){
                            withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: params.MAVEN_REPO_CREDENTIAL, usernameVariable: 'MVN_USER', passwordVariable:"MVN_TOKEN"]]){
                                withEnv(["JAVA_HOME=${ tool 'jdk8' }", "PATH+MAVEN=${tool 'maven-latest'}/bin:${env.JAVA_HOME}/bin"]) {
                                // withEnv(["PATH+MAVEN=${tool 'maven-latest'}/bin"]) {
                                    dir("${params.GIT_REPO_SUBFOLDER}"){
                                        sh "${mvnCmd} clean install deploy -DskipTests=true -DaltDeploymentRepository=artifactory::default::http://${MVN_USER}:${MVN_TOKEN}@${params.MAVEN_REPO_URL}"
                                    }
                                    
                                }
                            }
                        }
                    }
            }
        }
}catch(err){
    echo "in catch block"
    echo "Caught : ${err}"
    currentBuild.result = 'FAILURE'
    throw err
}