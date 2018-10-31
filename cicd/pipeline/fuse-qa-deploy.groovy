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
            "  # - camel-jackson\n" +
            "\n" +
            "# configuration:\n" +
            "  # - config:edit id.co.asyst.middleware.master.master-barang\n" +
            "  # - config:propset jdg.cache.count.lifespanTime  60\n" +
            "  # - config:propset jdg.cache.lifespanTime  60\n" +
            "  # - config:propset jdg.cacheData  barangCache\n" +
            "  # - config:propset jdg.cacheTotal  countBarangCache\n" +
            "  # - config:propset odata.serviceUri  http4://10.0.130.35:8080/odata/P3_VDATA_GENC/V_MASTER_BARANG\n" +
            "  # - config:propset odata.serviceUriTotal  http4://10.0.130.35:8080/odata/P3_VDATA_GENC/V_MASTER_BARANG/~count\n" +
            "  # - config:propset throttle.service.fa  10000\n" +
            "  # - config:propset timeout.service.core  20000\n" +
            "  # - config:update", description: 'GIT Repository URL DEV?') 
                    ])
                ])
        // def releaseExample = ""
        timeout(time:60 , unit: 'MINUTES'){
            // stage ("Preparing"){
            //     timeout (time:10, unit: 'MINUTES'){
            //         configFileProvider([configFile(fileId: 'sample-deploy-config', variable: 'VARS_SAMPLE')]) {
            //             releaseExample = readFile(VARS_SAMPLE).trim()
            //         }
            //         env.RELEASE = input message : "Deploy all? ", ok:"Deploy",
            //             parameters: [[$class:'TextParameterDefinition',name: 'RELEASE_DETAIL', defaultValue:releaseExample , description: 'Release Detail Information']]
            //     }
            // }
            stage ("Deploying"){
                // datas = readYaml text:env.RELEASE
                datas = readYaml text:params.RELEASE
                // echo ""+datas
                // ansiblePlaybook(
                //     playbook: '/cicd/playbook/fuse-deploy-qa.yaml',
                //     extraVars: datas)
                // sh "echo ${env.RELEASE} > ${workspace}/${env.BUILD_NUMBER}.yaml"
                // writeYaml file: workspace+"/"+env.BUILD_NUMBER +".yaml", data: datas
                // sh "ansible-playbook /cicd/playbook/fuse-deploy-qa.yaml -e @${workspace}/${env.BUILD_NUMBER}.yaml -vv"
                writeYaml file: env.BUILD_NUMBER +".yaml", data: datas
                sh "ansible-playbook /cicd/playbook/fuse-deploy-qa.yaml -e @${env.BUILD_NUMBER}.yaml -vv"
            }
        }
            
        }
}catch(err){
    echo "in catch block"
    echo "Caught : ${err}"
    currentBuild.result = 'FAILURE'
    throw err
}