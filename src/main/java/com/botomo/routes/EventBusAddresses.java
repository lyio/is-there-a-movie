package com.botomo.routes;

public class EventBusAddresses {
	private EventBusAddresses(){}
	
	// Database interactions
	public static final String GET_ALL 		= 	"botomo.db.getAll"; 
	public static final String SEARCH 		=	"botomo.db.search"; 
	public static final String ADD_ONE 		=	"botomo.db.addOne";
	public static final String UP_VOTE 		=	"botomo.db.upVote"; 
	public static final String DOWN_VOTE 	=	"botomo.db.downVote"; 
	
}
