package au.org.ala.doi.storage

enum DoiStorageProvider {
    LOCAL, S3, SWIFT

    static DoiStorageProvider fromString(String s) {
        try {
            return valueOf(s.toUpperCase())
        } catch (e) {
            return null
        }

    }
}