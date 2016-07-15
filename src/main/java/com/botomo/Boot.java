package com.botomo;

import io.vertx.core.Vertx;

public class Boot {

	public static void main(String[] args) {
		
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(MainVerticle.class.getName(), deploymentResult -> {
			if(deploymentResult.failed()){
				System.out.println("Depoyment faild");
				vertx.close();
			}else {
				System.out.println("Verticle deployed");
			}
		});
		
	}
	
}
