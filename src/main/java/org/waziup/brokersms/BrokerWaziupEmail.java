/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.waziup.brokersms;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author Constantin DRABO
 *
 */
public class BrokerWaziupEmail {

    private List<String> destinataires;
    private List<String> destinatairesCC;
    private String messageObject;
    private String messageBody;
    static Properties mailserverProperties;
    private InternetAddress addressDest[];
    private InternetAddress addressDestCC[];

    public BrokerWaziupEmail() {

    }

    public BrokerWaziupEmail(List<String> destinataires, List<String> destinatairesCC, String messageObject, String messageBody) {
        this.destinataires = destinataires;
        this.destinatairesCC = destinatairesCC;
        this.messageObject = messageObject;
        this.messageBody = messageBody;
    }

    public List<String> getDestinataires() {
        return destinataires;
    }

    public void setDestinataires(List<String> destinataires) {
        this.destinataires = destinataires;
    }

    public List<String> getDestinatairesCC() {
        return destinatairesCC;
    }

    public void setDestinatairesCC(List<String> destinatairesCC) {
        this.destinatairesCC = destinatairesCC;
    }

    public String getMessageObject() {
        return messageObject;
    }

    public void setMessageObject(String messageObject) {
        this.messageObject = messageObject;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public void sendSTMPEmail(InputStream input) throws AddressException, MessagingException {

        System.out.println("===> Setting up the server : ");
        mailserverProperties = System.getProperties();

        if (!destinataires.isEmpty()) {
            addressDest = new InternetAddress[destinataires.size()];
            for (int i = 0; i < destinataires.size(); i++) {
                addressDest[i] = new InternetAddress(destinataires.get(i));

            }

            if (!destinatairesCC.isEmpty()) {
                addressDestCC = new InternetAddress[destinatairesCC.size()];
                for (int j = 0; j < destinatairesCC.size(); j++) {
                    addressDestCC[j] = new InternetAddress(destinatairesCC.get(j));
                }
            }

        }

        try {

            mailserverProperties.load(input);
            System.out.println("===> Getting the email : ");
            Session myEmailSession = Session.getDefaultInstance(mailserverProperties);
            MimeMessage myEmailMessage = new MimeMessage(myEmailSession);
            myEmailMessage.addRecipients(Message.RecipientType.TO, addressDest);
            myEmailMessage.addRecipients(Message.RecipientType.CC, addressDestCC);
            myEmailMessage.setSubject(this.getMessageObject());
            myEmailMessage.setContent(this.getMessageBody(), "text/html");
            System.out.println("===> Mail has been created successfully : ");
            System.out.println("===> Get session and send  mail");
            Transport transport = myEmailSession.getTransport("smtp");
            transport.connect(mailserverProperties.getProperty("username"), mailserverProperties.getProperty("password"));
            transport.sendMessage(myEmailMessage, myEmailMessage.getAllRecipients());
            transport.close();
            System.out.println("===> Done !");

        } catch (FileNotFoundException ex) {
            Logger.getLogger(BrokerWaziupEmail.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(BrokerWaziupEmail.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
