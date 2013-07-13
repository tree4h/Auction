package controllers;

public enum Messages {
	ERROR_MEMBER_ID_NOEXIT("指定された会員が存在しません"),
	ERROR_ITEM_ID_NOEXIT("指定された商品が存在しません"),
	ERROR_AUCTION_ID_NOEXIT("指定されたオークションIDが存在しません");
	
	private final String MESSAGE;

	private Messages(String error) {
		MESSAGE = error;
	}
	
	public String getMessage() {
		return MESSAGE;
	}
}
