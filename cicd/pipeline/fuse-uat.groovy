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
                        string(name: 'GIT_REPO_BRANCH', defaultValue: 'master', description: 'GIT Repository Branch?'), 
                        string(name: 'GIT_REPO_SUBFOLDER', defaultValue: 'master-barang-fa', description: 'GIT Repository Sub Folder?'),
                        string(name: 'GIT_REPO_CREDENTIAL', defaultValue: 'redhat-git', description: 'GIT Repository Credential'),
                        string(name: 'MAVEN_REPO_CREDENTIAL', defaultValue: 'artifactory-redhat', description: 'MAVEN Repository Credential'),
                        string(name: 'MAVEN_REPO_URL', defaultValue: 'maven.pelindo.co.id:8081/artifactory/pelindo3-02-quality', description: 'MAVEN Repository URL'),
                        booleanParam(name: 'USE_RC', defaultValue: false, description: 'USE RC Version')
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
                                // withEnv(["JAVA_HOME=${ tool 'jdk8' }", "PATH+MAVEN=${tool 'maven-latest'}/bin:${env.JAVA_HOME}/bin"]) {
                                withEnv(["PATH+MAVEN=${tool 'maven-latest'}/bin"]) {
                                    dir("${params.GIT_REPO_SUBFOLDER}"){
                                        pom = readMavenPom file: 'pom.xml'
                                        sh "echo ${pom.version} | cut -d '-' -f 1 | tr -d '\n' > version.txt"
                                        version=readFile('version.txt')
                                        echo "version : ${version}"
                                        sh "rm -f version.txt"
                                        sh "${mvnCmd} versions:set -DnewVersion=${version}-RC"
                                        if (params.USE_RC){
                                            sh "sed -i 's|-SNAPSHOT|-RC|g'  pom.xml"
                                        }
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