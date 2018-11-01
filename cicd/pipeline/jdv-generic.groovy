try {
    node(){
         properties([
                //         pipelineTriggers([
                //             pollSCM('H/5 * * * *')
                //         ]),
                    parameters([
                        text(name: 'RELEASE', defaultValue: "---\n" +
                "gitrepo: \"ssh://svrtfs.pelindo.co.id:22/tfs/Middleware/Middleware/_git/p3-vdata\"\n" +
                "\n" +
                "deploylist:\n" +
                "  - VDB/P3_VIEWS.vdb"),
                        choice(name: 'TARGET_ENV', choices: ['', 'dev', 'qa', 'prod'], description: 'Target Environment (dev/qa/prod)?'),
                        choice(name: 'OPERATION', choices: ['', 'deploy','undeploy'], description: 'Target Operation (deploy/undeploy)?')
                        ])
                        
                ])
        timeout(time:60 , unit: 'MINUTES'){
            stage ("Deploying"){
                datas = readYaml text:params.RELEASE
                writeYaml file: env.BUILD_NUMBER +".yaml", data: datas
                if (params.TARGET_ENV != 'prod')
                    sh "ansible-playbook /cicd/playbook/jdv-generic.yaml -e env=${env.TARGET_ENV} -e operation=${env.OPERATION} -e @${env.BUILD_NUMBER}.yaml -vv"
                else if (params.TARGET_ENV == 'prod')
                    sh "ansible-playbook /cicd/playbook/jdv-generic-prod.yaml -e operation=${env.OPERATION} -e @${env.BUILD_NUMBER}.yaml -vv"
            }
        }
            
        }
}catch(err){
    echo "in catch block"
    echo "Caught : ${err}"
    currentBuild.result = 'FAILURE'
    throw err
}