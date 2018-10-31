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
        timeout(time:60 , unit: 'MINUTES'){
            stage ("Deploying"){
                datas = readYaml text:params.RELEASE
                writeYaml file: env.BUILD_NUMBER +".yaml", data: datas
                sh "ansible-playbook /cicd/playbook/fuse-deploy-dev.yaml -e @${env.BUILD_NUMBER}.yaml -vv"
            }
        }
            
        }
}catch(err){
    echo "in catch block"
    echo "Caught : ${err}"
    currentBuild.result = 'FAILURE'
    throw err
}