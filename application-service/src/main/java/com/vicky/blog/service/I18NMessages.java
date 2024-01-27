package com.vicky.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

@Component
public class I18NMessages {

    @Autowired
    private ResourceBundleMessageSource messageSource;
    
    public enum I18NMessage {

        NAME_REQUIRED("blog.organization.name.required"),
        MIN_MAX("blog.minmax"),
        MAX_LENGTH("blog.maxlength"),
        REQUIRED("blog.required"),
        INVALID("blog.invalid")
        
        ;

        private final String messageCode;

        I18NMessage(String messageCode) {
            this.messageCode = messageCode;
        }

        public String getMessageCode() {
            return messageCode;
        }
    }

    public String getMessage(I18NMessage message) {
        return getMessage(message, null);
    }

    public String getMessage(I18NMessage message, Object[] args) {
        System.out.println(LocaleContextHolder.getLocale());
        return messageSource.getMessage(message.getMessageCode(), args, LocaleContextHolder.getLocale());
    }
}
