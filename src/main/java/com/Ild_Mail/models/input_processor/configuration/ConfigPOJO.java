package com.Ild_Mail.models.input_processor.configuration;

public class ConfigPOJO {
    //SMTP auth config
    private String SND_HOST;
    private String SND_SOURCE;
    private String SND_TARGET;

    //IMAP auth config
    private String REC_PROTO;
    private String REC_HOST;
    private String REC_ADDRESS;
    private String REC_ALLOC;



    //proxy config
    private MailProxy mailProxy;


    public String getSND_HOST() {
        return SND_HOST;
    }

    public void setSND_HOST(String SND_HOST) {
        this.SND_HOST = SND_HOST;
    }

    public String getSND_SOURCE() {
        return SND_SOURCE;
    }

    public void setSND_SOURCE(String SND_SOURCE) {
        this.SND_SOURCE = SND_SOURCE;
    }

    public String getSND_TARGET() {return SND_TARGET;}

    public void setSND_TARGET(String SND_TARGET) {this.SND_TARGET=SND_TARGET;}

    public String getREC_PROTO() { return REC_PROTO; }

    public void setREC_PROTO(String REC_PROTO) { this.REC_PROTO = REC_PROTO; }

    public String getREC_HOST() {
        return REC_HOST;
    }

    public void setREC_HOST(String REC_HOST) {
        this.REC_HOST = REC_HOST;
    }

    public String getREC_ADDRESS() {
        return REC_ADDRESS;
    }

    public void setREC_ADDRESS(String REC_ADDRESS) {
        this.REC_ADDRESS = REC_ADDRESS;
    }

    public String getREC_ALLOC() {
        return REC_ALLOC;
    }

    public void setREC_ALLOC(String REC_ALLOC) {
        this.REC_ALLOC = REC_ALLOC;
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
