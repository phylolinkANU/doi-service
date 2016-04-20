class UrlMappings {

    static mappings = {

        // service api
        "/api/v1/mintDoi" controller: "doi", action: [POST: "mintDoi"]
        "/api/v1/doi/$id**/download" controller: "doi", action: [GET: "download"]
        "/api/v1/doi/$id**" controller: "doi", action: [GET: "getDoi"]
        "/api/" view: "/api/index"


        // User interface
        "/doi/$id/download" controller: "doiResolve", action: [GET: "download"]
        "/doi/$id" controller: "doiResolve", action: [GET: "doi"]
        "/doi" controller: "doiResolve", action: [GET: "index"]
        "/" controller: "doiResolve", action: [GET: "index"]

        "/admin" controller:'admin', action: [GET: "index"]
        "/admin/mintDoi" controller:'admin', action: [GET: "mintDoi"]
        "/admin/createDoi" controller:'admin', action: [POST: "createDoi"]

        "500" controller: "error"
        "400" controller: "error"
        "404" controller: "error"
        "403" controller: "error"
        "401" controller: "error"
    }
}
