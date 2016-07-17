package com.botomo;

import io.vertx.core.Vertx;

public class Boot {

	public static void main(String[] args) {
		
		Vertx vertx = Vertx.vertx();
		
		System.setProperty("dbname", "botomo");
		System.setProperty("dburl", "mongodb://localhost:27017");
		vertx.deployVerticle(MainVerticle.class.getName(), deploymentResult -> {
			if(deploymentResult.succeeded()){
				System.out.println("MainVerticle successfully deployed");
			}else {
				System.out.println("Deployment faild");
				vertx.close();
			}
		});		
	}
	
}
