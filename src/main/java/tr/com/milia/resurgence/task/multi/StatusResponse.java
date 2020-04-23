package tr.com.milia.resurgence.task.multi;

import java.util.Map;

class StatusResponse {
	private final Map<String, Status> members;

	public StatusResponse(Map<String, Status> members) {
		this.members = members;
	}

	public Map<String, Status> getMembers() {
		return members;
	}
}
