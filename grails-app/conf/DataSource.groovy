environments {
    development {
        grails {
            mongo {
                host = "localhost"
                port = "27017"
                databaseName = "doi"
            }
        }
    }
    test {
        grails {
            mongo {
                host = "localhost"
                port = "27017"
                databaseName = "doi"
            }
        }
    }
    production {
        grails {
            mongo {
                host = "localhost"
                port = "27017"
                databaseName = "doi"
            }
        }
    }
}