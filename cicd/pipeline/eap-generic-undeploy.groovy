try {
    node(){
         properties([
                //         pipelineTriggers([
                //             pollSCM('H/5 * * * *')
                //         ]),
                    parameters([
                        text(name: 'RELEASE', defaultValue: "---\n"+
            "# deploy\n"+
            "deploylist:\n"+
            "  - http://maven.pelindo.co.id:8081/artifactory/pelindo3-02-quality/id/co/asyst/backend/be-calculate-net-charge/1.0.0-RC/be-calculate-net-charge-1.0.0-RC.war", description: 'File Release?') 
                    ]),
                        choice(name: 'TARGET_ENV', choices: ['dev', 'qa', 'prod'], description: 'Target Environment (dev/qa/prod)?')
                ])
        timeout(time:60 , unit: 'MINUTES'){
            stage ("Deploying"){
                datas = readYaml text:params.RELEASE
                writeYaml file: env.BUILD_NUMBER +".yaml", data: datas
                sh "ansible-playbook /cicd/playbook/eap-undeploy-${params.TARGET_ENV}.yaml -e @${env.BUILD_NUMBER}.yaml -vv"
            }
        }
            
        }
}catch(err){
    echo "in catch block"
    echo "Caught : ${err}"
    currentBuild.result = 'FAILURE'
    throw err
}