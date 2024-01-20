package com.vicky.blog.common.dto.staticservice;

public class StaticResourceDTO {

    public enum ContentType {

        IMAGE_JPG("image/jpg"),
        IMAGE_JPEG("image/jpeg"),
        IMAGE_PNG("image/png"),
        TEXT_HTML("text/html"),
        TEXT_PLAIN("text/plain")
        ;

        private String type;

        ContentType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

        public static ContentType getContenType(String contentType) {
            for(ContentType cType: ContentType.values()) {
                if(cType.getType().equals(contentType)) {
                    return cType;
                }
            }
            return null;
        }
    }

    public enum Visibility {
        PUBLIC,
        PRIVATE
    }
    
    private Long id;
    private String name;
    private ContentType contentType;
    private byte[] data;
    private Visibility visibility = Visibility.PUBLIC;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public ContentType getContentType() {
        return contentType;
    }
    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }
    public byte[] getData() {
        return data;
    }
    public void setData(byte[] data) {
        this.data = data;
    }
    public Visibility getVisibility() {
        return visibility;
    }
    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

}
