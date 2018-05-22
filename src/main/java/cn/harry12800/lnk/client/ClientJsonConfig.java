package cn.harry12800.lnk.client;

import java.util.HashMap;
import java.util.Map;

public class ClientJsonConfig {

	private Map<Long, SessionData> mapsSessionData = new HashMap<>();

	public Map<Long, SessionData> getMapsSessionData() {
		return mapsSessionData;
	}

	public void setMapsSessionData(Map<Long, SessionData> mapsSessionData) {
		this.mapsSessionData = mapsSessionData;
	}

}
