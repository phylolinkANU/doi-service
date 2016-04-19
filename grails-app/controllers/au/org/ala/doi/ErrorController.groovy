package au.org.ala.doi

class ErrorController {
    def index() {
        render view: "/error", model:[error: response?.response?.message]
    }
}
