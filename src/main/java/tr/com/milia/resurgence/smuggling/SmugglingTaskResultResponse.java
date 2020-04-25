package tr.com.milia.resurgence.smuggling;

class SmugglingTaskResultResponse {
	private final long success;
	private final long fail;

	public SmugglingTaskResultResponse(long success, long fail) {
		this.success = success;
		this.fail = fail;
	}

	public long getSuccess() {
		return success;
	}

	public long getFail() {
		return fail;
	}
}
