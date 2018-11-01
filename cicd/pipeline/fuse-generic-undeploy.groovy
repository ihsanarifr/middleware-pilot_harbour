try {
    node(){
         properties([
                //         pipelineTriggers([
                //             pollSCM('H/5 * * * *')
                //         ]),
                    parameters([
                        text(name: 'RELEASE', defaultValue: "---\n" +
            "# deploy\n" +
            "bundles:\n" +
            "  # - http://maven.pelindo.co.id:8081/artifactory/pelindo3-03-production/id/co/asyst/middleware/commons/commons-auth/1.0.0/commons-auth-1.0.0.jar\n" +
            "\n" +
            "# Features List to deploy using features install\n" +
            "# features:\n" +
            "# to enable/deploy Camel Component Feature from FUSE BOM or other Karaf Features\n" +
            "  # - camel-jackson", description: 'File Release?') ,
                        choice(name: 'TARGET_ENV', choices: ['','dev', 'qa', 'prod'], description: 'Target Environment (dev/qa/prod)?')
                    ])
                ])
        timeout(time:60 , unit: 'MINUTES'){
            stage ("Deploying"){
                datas = readYaml text:params.RELEASE
                writeYaml file: env.BUILD_NUMBER +".yaml", data: datas
                sh "ansible-playbook /cicd/playbook/fuse-undeploy-${params.TARGET_ENV}.yaml -e @${env.BUILD_NUMBER}.yaml -vv"
            }
        }
            
        }
}catch(err){
    echo "in catch block"
    echo "Caught : ${err}"
    currentBuild.result = 'FAILURE'
    throw err
}