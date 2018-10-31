try {
    node(){
            configFileProvider([configFile(fileId: 'maven-cicd', variable: 'MAVEN_SETTINGS_XML')]) {
                def mvnCmd = "mvn -U -s $MAVEN_SETTINGS_XML"
                def version =""
                 properties([
                //         pipelineTriggers([
                //             pollSCM('H/5 * * * *')
                //         ]),
                    parameters([
                        text(name: 'GIT_REPO', defaultValue: 'http://svrtfs.pelindo.co.id:8080/tfs/Middleware/Middleware/_git/masterdata', description: 'GIT Repository URL DEV?'),
                        string(name: 'GIT_REPO_BRANCH', defaultValue: 'master', description: 'GIT Repository Branch DEV?'), 
                        text(name: 'GIT_REPO_TARGET', defaultValue: 'ssh://svrtfs.pelindo.co.id:22/tfs/Middleware/Middleware-Production/_git/master-data', description: 'GIT Repository SSH Target?'),
                        string(name: 'GIT_REPO_BRANCH_TARGET', defaultValue: 'master', description: 'GIT Repository Branch Target?'), 
                        string(name: 'GIT_REPO_SUBFOLDER', defaultValue: 'master-barang-fa', description: 'GIT Repository Sub Folder?'),
                        string(name: 'GIT_REPO_CREDENTIAL', defaultValue: 'redhat-git', description: 'GIT Repository Credential'),
                        string(name: 'MAVEN_REPO_CREDENTIAL', defaultValue: 'artifactory-redhat', description: 'MAVEN Repository Credential'),
                        string(name: 'MAVEN_REPO_URL', defaultValue: 'maven.pelindo.co.id:8081/artifactory/pelindo3-03-production', description: 'MAVEN Repository URL'),
                        booleanParam(name: 'USE_RELEASE', defaultValue: false, description: 'USE Release Version'), 
                        text(name: 'RELEASE_CHANGE_PACKAGE', defaultValue: "id.co.asyst.middleware.common:*,id.co.asyst.middleware.commons:*", description: 'Change Package Depedency to Release')   
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
                                        orginalVersion = pom.version
                                        sh "echo ${pom.version} | cut -d '-' -f 1 | tr -d '\n' > version.txt"
                                        version=readFile('version.txt')
                                        echo "version : ${version}"
                                        sh "rm -f version.txt"
                                        if (params.USE_RELEASE){
                                            sh "${mvnCmd} versions:use-releases -Dincludes=${params.RELEASE_CHANGE_PACKAGE}"
                                        }
                                        sh "${mvnCmd} versions:set -DnewVersion=${version}"
                                        sh "${mvnCmd} clean install deploy -DskipTests=true -DaltDeploymentRepository=artifactory::default::http://${MVN_USER}:${MVN_TOKEN}@${params.MAVEN_REPO_URL}"
                                        pom = readMavenPom file: 'pom.xml'
                                        pom.description = pom.description + " promote from :" + orginalVersion
                                        writeMavenPom model: pom
                                        sh "${mvnCmd} clean"
                                    }
                                }
                            }
                        }
                        stage ("Promote Code"){
                            timeout(time:10, unit:'MINUTES') {
                                    env.RELEASE_NOTE = input message: 'Message for Promote?', ok: 'Promote!',
                                                    parameters: [string(name: 'RELEASE_MESSAGE', defaultValue: '', description: 'Release Note')]
                            }
                            withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: params.GIT_REPO_CREDENTIAL, usernameVariable: 'GIT_USER', passwordVariable:"GIT_PASS"]]){
                                sh "rm -rf target && mkdir -p 'target/${params.GIT_REPO_SUBFOLDER}'"
                                sh "rsync -a '${params.GIT_REPO_SUBFOLDER}/' 'target/${params.GIT_REPO_SUBFOLDER}/'"

                                git branch: params.GIT_REPO_BRANCH_TARGET, url: params.GIT_REPO_TARGET

                                sh "mkdir -p '${params.GIT_REPO_SUBFOLDER}'"

                                sh "rsync -a 'target/${params.GIT_REPO_SUBFOLDER}/' '${params.GIT_REPO_SUBFOLDER}/'"
                                
                                sh "git config user.email 'jenkins@pelindo3.co.id'"
                                sh "git config user.name 'jenkins'"
                                def newFolder = true
                                try {
                                    sh "git add '${params.GIT_REPO_SUBFOLDER}'"
                                }catch(err){
                                    newFolder = false
                                }
                                if (newFolder){
                                    sh "git commit -m 'add/update ${version} with notes: ${env.RELEASE_NOTE}' || true"
                                }

                                dir("${params.GIT_REPO_SUBFOLDER}"){
                                    sh "git add . || true"
                                    sh "git commit -a -m 'add/update ${version} with notes: ${env.RELEASE_NOTE}' || true"
                                }
                                sh "git push origin master"

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