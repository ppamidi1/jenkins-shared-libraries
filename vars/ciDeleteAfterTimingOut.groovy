def call(time, unit, Closure tunk) {
    try {
        timeout(time: time, unit: unit) {
            tunk()
        }
    }
    catch (err) {
        //https://stackoverflow.com/questions/50050076/jenkins-pipeline-java-io-notserializableexception-hudson-model-user-when-exec
        def user = err.getCauses()[0].getUser().toString()

        if ('SYSTEM' == user.toString()) { // SYSTEM means timeout.
            echo "no input was received before timeout"
            ciK8sDeleteBeta(params.project)
            currentBuild.result = 'SUCCESS'
        } else {
            echo "this was not successful"
            currentBuild.result = 'FAILURE'
        }
    }
}