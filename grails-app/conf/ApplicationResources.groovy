modules = {
    doi {
        dependsOn "ala_admin"
        resource url: "css/doi.css"
    }

    jqueryValidationEngine {
        resource url: 'thirdparty/jquery.validationEngine/css/validationEngine.jquery.css'
        resource url: 'thirdparty/jquery.validationEngine/js/jquery.validationEngine.js'
        resource url: 'thirdparty/jquery.validationEngine/js/jquery.validationEngine-en.js'
    }

    mintDoi {
        dependsOn 'doi'
        resource url: 'js/admin/mintDoi.js'

    }
}