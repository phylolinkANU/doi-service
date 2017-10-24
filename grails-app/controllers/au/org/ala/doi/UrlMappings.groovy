package au.org.ala.doi

class UrlMappings {

    static mappings = {

        // service api
        post "/api/v1/mintDoi"(controller: "doi", action: "mintDoi")
        get "/api/v1/doi/$id**/download"(controller: "doi", action: "download")
        get "/api/v1/doi/$id**"(controller: "doi", action: "getDoi")
        "/api/" view: "/api/index"


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
