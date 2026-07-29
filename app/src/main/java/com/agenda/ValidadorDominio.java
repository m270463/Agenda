package com.agenda;

import java.util.Hashtable;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.InitialDirContext;

public class ValidadorDominio {

    public static boolean dominioPossuiServidorEmail(String email) {
        String dominio = email.substring(email.indexOf("@") + 1);
        
        try {
            Hashtable<String, String> env = new Hashtable<>();
            env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
            InitialDirContext ictx = new InitialDirContext(env);
            
            // Busca registros MX (Mail Exchange) do domínio
            Attributes attrs = ictx.getAttributes(dominio, new String[] {"MX"});
            return attrs.get("MX") != null; 
            
        } catch (NamingException e) {
            // Se o domínio não existir ou não aceitar e-mails, lança exceção
            return false;
        }
    }
}