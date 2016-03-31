class UrlMappings {

    static mappings = {

        // service api
        "/api/v1/mintDoi" controller: "doi", action: [POST: "mintDoi"]
        "/api/v1/doi/$id**/download" controller: "doi", action: [GET: "download"]
        "/api/v1/doi/$id**" controller: "doi", action: [GET: "getDoi"]
        "/api/" view: "/api/index"


        // User interface
        "/api/v1/doi/$id/download" controller: "doiResolve", action: [GET: "download"]
        "/doi/$id" controller: "doiResolve", action: [GET: "doi"]
        "/doi" controller: "doiResolve", action: [GET: "index"]
        "/" controller: "doiResolve", action: [GET: "index"]

        "500"(view: '/error')
        "404"(view: '/notFound')
    }
}
