package ats.constants;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

// Do we really need this enum? Will a HashSet in Constants work better?
public enum AppLocation{
	FAVORITE,
	PENDING_REVIEW,
	REVIEWED,
	REJECT,
	SAVE_FOR_LATER;
	
    private static Map<String, AppLocation> namesMap = new HashMap<String, AppLocation>(5);
    
    static {
        namesMap.put("favorite", FAVORITE);
        namesMap.put("pending_review", PENDING_REVIEW);
        namesMap.put("reviewed", REVIEWED);
        namesMap.put("reject", REJECT);
        namesMap.put("save_for_later", SAVE_FOR_LATER);   
    }
    
    @JsonCreator
    public static AppLocation forValue(String value) {
    		return namesMap.get(StringUtils.lowerCase(value));
    }

	@JsonValue
	public String toValue() {
		for (Entry<String, AppLocation> entry : namesMap.entrySet()) {
            if (entry.getValue() == this) {
            		return entry.getKey();
            }
        }
        return null; // or fail
	}

}
