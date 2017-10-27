package au.org.ala.doi

class UrlMappings {

    static mappings = {

        // service api
        group("/api") {
            // Default route, no Accept-Version, should dupe the latest version
            post "/doi"(controller: "doi", action: "save", namespace: "v1")
            get "/doi/$id**/download"(controller: "doi", action: "download", namespace: "v1")
            get "/doi/$id**"(controller: "doi", action: "show", namespace: "v1")

            // Accept-Version: 1.0
            post "/doi"(version: "1.0", controller: "doi", action: "save", namespace: "v1")
            get "/doi/$id**/download"(version: "1.0", controller: "doi", action: "download", namespace: "v1")
            get "/doi/$id**"(version: "1.0", controller: "doi", action: "show", namespace: "v1")

            "/" view: "/api/index"

            // legacy API mappings, DO NOT ADD V2 HERE!
            post "/v1/mintDoi"(controller: "doi", action: "save", namespace: "v1")
            get "/v1/doi/$id**/download"(controller: "doi", action: "download", namespace: "v1")
            get "/v1/doi/$id**"(controller: "doi", action: "show", namespace: "v1")
        }

        // User interface
        get "/doi/$id/download"(controller: "doiResolve", action: "download")
        get "/doi/$id"(controller: "doiResolve", action: "doi")
        get "/doi"(controller: "doiResolve", action: "index")
        get "/"(controller: "doiResolve", action: "index")

        get "/admin"(controller:'admin', action:"index")
        get "/admin/mintDoi"(controller:'admin', action:"mintDoi")
        post "/admin/createDoi"(controller:'admin', action: "createDoi")

        "500" controller: "error"
        "400" controller: "error"
        "404" controller: "error"
        "403" controller: "error"
        "401" controller: "error"
    }
}
