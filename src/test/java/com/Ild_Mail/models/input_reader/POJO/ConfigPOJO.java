package com.Ild_Mail.models.input_reader.POJO;

public class ConfigPOJO {
    //SMTP auth config
    private String SMTP_HOST;
    private String SMTP_SOURCE;

    //IMAP auth config
    private String IMAP_HOST;
    private String IMAP_ADDRESS;
    private String IMAP_ALLOC;



    //proxy config
    private MailProxy mailProxy;


    public String getSMTP_HOST() {
        return SMTP_HOST;
    }

    public void setSMTP_HOST(String SMTP_HOST) {
        this.SMTP_HOST = SMTP_HOST;
    }

    public String getSMTP_SOURCE() {
        return SMTP_SOURCE;
    }

    public void setSMTP_SOURCE(String SMTP_SOURCE) {
        this.SMTP_SOURCE = SMTP_SOURCE;
    }

    public String getIMAP_HOST() {
        return IMAP_HOST;
    }

    public void setIMAP_HOST(String IMAP_HOST) {
        this.IMAP_HOST = IMAP_HOST;
    }

    public String getIMAP_ADDRESS() {
        return IMAP_ADDRESS;
    }

    public void setIMAP_ADDRESS(String IMAP_ADDRESS) {
        this.IMAP_ADDRESS = IMAP_ADDRESS;
    }

    public String getIMAP_ALLOC() {
        return IMAP_ALLOC;
    }

    public void setIMAP_ALLOC(String IMAP_ALLOC) {
        this.IMAP_ALLOC = IMAP_ALLOC;
    }

    public MailProxy getMailProxy() {
        return mailProxy;
    }

    public void setMailProxy(MailProxy mailProxy) {
        this.mailProxy = mailProxy;
    }


    public class MailProxy{
        private String _host;
        private String _port;
        private String _user;
        private String _password;

        public String get_host() {
            return _host;
        }

        public void set_host(String _host) {
            this._host = _host;
        }

        public String get_port() {
            return _port;
        }

        public void set_port(String _port) {
            this._port = _port;
        }

        public String get_user() {
            return _user;
        }

        public void set_user(String _user) {
            this._user = _user;
        }

        public String get_password() {
            return _password;
        }

        public void set_password(String _password) {
            this._password = _password;
        }

    }
}
