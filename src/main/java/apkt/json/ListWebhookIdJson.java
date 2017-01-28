package apkt.json;

import java.util.ArrayList;
import java.util.List;

public class ListWebhookIdJson {

    private List<WebhookId> webhookIdList;

    public ListWebhookIdJson() {
        this.webhookIdList = new ArrayList<WebhookId>();
    }

    public List<WebhookId> getWebhookIdList() {
        return webhookIdList;
    }

    public void setWebhookIdList(List<WebhookId> webhookIdList) {
        this.webhookIdList = webhookIdList;
    }
    
    public void add(String webhookId, String eventType){
        WebhookId webhookId1 = new WebhookId(webhookId, eventType);
        webhookIdList.add(webhookId1);
    }
    
    public class WebhookId {
        private String webhookId;
        private String eventType;

        public WebhookId(String webhookId, String eventType) {
            this.webhookId = webhookId;
            this.eventType = eventType;
        }
        
        public String getWebhookId() {
            return webhookId;
        }

        public void setWebhookId(String webhookId) {
            this.webhookId = webhookId;
        }

        public String getEventType() {
            return eventType;
        }

        public void setEventType(String eventType) {
            this.eventType = eventType;
        }
        
        
    }
}


