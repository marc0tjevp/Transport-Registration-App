package theekransje.douaneapp.Domain;

import java.io.Serializable;

/**
 * Created by Sander on 5/24/2018.
 */

public class MRNFormulier implements Serializable {
        private static final String TAG = "MRNFormulier";

        private String mrn;
        private String reference;
        private String dateTime;
        private String sender;
        private String receiver;
        private String client;
        private int articleAmount;
        private double totalPRice;
        private String currency;
        private double totalWeight;
        private String origin;
        private String destination;

        private static String getTAG() {
                return TAG;
        }

        public String getMrn() {
                return mrn;
        }

        public void setMrn(String mrn) {
                this.mrn = mrn;
        }

        public String getReference() {
                return reference;
        }

        public void setReference(String reference) {
                this.reference = reference;
        }

        public String getDateTime() {
                return dateTime;
        }

        public void setDateTime(String dateTime) {
                this.dateTime = dateTime;
        }

        public void String(String dateTime) {
                this.dateTime = dateTime;
        }

        public String getSender() {
                return sender;
        }

        public void setSender(String sender) {
                this.sender = sender;
        }

        public String getReceiver() {
                return receiver;
        }

        public void setReceiver(String receiver) {
                this.receiver = receiver;
        }

        public String getClient() {
                return client;
        }

        public void setClient(String client) {
                this.client = client;
        }

        public int getArticleAmount() {
                return articleAmount;
        }

        public void setArticleAmount(int articleAmount) {
                this.articleAmount = articleAmount;
        }

        public double getTotalPRice() {
                return totalPRice;
        }

        public void setTotalPRice(double totalPRice) {
                this.totalPRice = totalPRice;
        }

        public String getCurrency() {
                return currency;
        }

        public void setCurrency(String currency) {
                this.currency = currency;
        }

        public double getTotalWeight() {
                return totalWeight;
        }

        public void setTotalWeight(double totalWeight) {
                this.totalWeight = totalWeight;
        }

        public String getOrigin() {
                return origin;
        }

        public void setOrigin(String origin) {
                this.origin = origin;
        }

        public String getDestination() {
                return destination;
        }

        public void setDestination(String destination) {
                this.destination = destination;
        }
}
