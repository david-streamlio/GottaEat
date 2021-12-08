package com.gottaeat.services.payment.creditcard.authorization;

import java.io.IOException;
import java.time.Duration;

import org.apache.pulsar.functions.api.Context;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.gottaeat.domain.payment.AuthorizedPayment;
import com.gottaeat.domain.payment.CreditCard;
import com.gottaeat.domain.payment.Payment;
import com.gottaeat.domain.payment.PaymentStatus;
import com.gottaeat.services.payment.creditcard.UnsuccessfulCallException;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.SlidingWindowType;
import io.github.resilience4j.decorators.Decorators;
import io.vavr.CheckedFunction0;
import io.vavr.control.Try;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class WebServiceAuthorizationStrategy implements AuthorizationStrategy {

	private String apiHost, apiKey, apiEndpoint;
	private CircuitBreakerConfig config;
	private CircuitBreakerRegistry registry;
	private CircuitBreaker circuitBreaker;
	private float failureRateThreshold, slowCallRateThreshold;
	private long waitDurationInOpenState, slowCallDurationThreshold;
	private int permittedNumberOfCallsInHalfOpenState, minimumNumberOfCalls, slidingWindowSize;
	
	@Override
	public AuthorizedPayment authorize(Payment card) {
		String approvalCode = getAuthorizationCode((CreditCard) card.getMethodOfPayment().getType());
		
		AuthorizedPayment auth = AuthorizedPayment.newBuilder()
			.setPayment(card)
			.setStatus( (approvalCode != null) ? PaymentStatus.AUTHORIZED : PaymentStatus.REJECTED)
			.setApprovalCode(approvalCode)
		.build();
					
		return auth;
	}
	
	private String getAuthorizationCode(CreditCard card) {
		CheckedFunction0<String> decoratedFunction = 
				Decorators.ofCheckedSupplier(getFunction(card))
					.withCircuitBreaker(circuitBreaker)
					.decorate();
		
		return getToken(Try.of(decoratedFunction).getOrNull());
	}
	
	private CheckedFunction0<String> getFunction(CreditCard card) {
		CheckedFunction0<String> fn = () -> {
			OkHttpClient client = new OkHttpClient();
			
			StringBuilder sb = new StringBuilder()
					.append("number=").append(card.getAccountNumber())
					.append("&cvc=").append(card.getCcv())
					.append("&exp_month=").append(card.getExpMonth())
					.append("&exp_year=").append(card.getExpYear());

			MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
			RequestBody body = RequestBody.create(sb.toString(), mediaType);
			Request request = new Request.Builder()
				.url(apiEndpoint)
				.post(body)
				.addHeader("x-rapidapi-host", apiHost)
				.addHeader("x-rapidapi-key", apiKey)
				.addHeader("content-type", "application/x-www-form-urlencoded")
				.build();

			try (Response response = client.newCall(request).execute()) {
				if (!response.isSuccessful()) {
					throw new UnsuccessfulCallException(response.code());
				}
				String token = getToken(response.body().string());
				return token;
			}

		};
		
		return fn;
	}
	
	private String getToken(String json) {
		JsonElement jsonTree = new JsonParser().parse(json);
		
		if (jsonTree.isJsonObject()) {
		    JsonObject jsonObject = jsonTree.getAsJsonObject();
		    JsonElement token = jsonObject.get("id");
		    return token.getAsString();
		}
		
		return null;
	}

	@Override
	public void initalize(Context ctx) {
		apiHost = ctx.getUserConfigValue("api-host").get().toString();
		apiKey = ctx.getUserConfigValue("api-key").get().toString();
		apiEndpoint = ctx.getUserConfigValue("api-endpoint").get().toString();
		
		failureRateThreshold = Float.parseFloat(
			ctx.getUserConfigValue("circuit-breaker.failureRateThreshold").get().toString());
		
		slowCallRateThreshold = Float.parseFloat(
			ctx.getUserConfigValue("circuit-breaker.slowCallRateThreshold").get().toString());
		
		waitDurationInOpenState = Long.parseLong(
			ctx.getUserConfigValue("circuit-breaker.waitDurationInOpenState-ms").get().toString());
		
		slowCallDurationThreshold = Long.parseLong(
			ctx.getUserConfigValue("circuit-breaker.slowCallDurationThreshold-sec").get().toString());
		
		permittedNumberOfCallsInHalfOpenState = Integer.parseInt(
			ctx.getUserConfigValue("circuit-breaker.permittedNumberOfCallsInHalfOpenState").get().toString());
		
		minimumNumberOfCalls = Integer.parseInt(
			ctx.getUserConfigValue("circuit-breaker.minimumNumberOfCalls").get().toString());
		
		slidingWindowSize = Integer.parseInt(
			ctx.getUserConfigValue("circuit-breaker.slidingWindowSize").get().toString());
		
		config = CircuitBreakerConfig.custom()
				  .failureRateThreshold(failureRateThreshold)
				  .slowCallRateThreshold(slowCallRateThreshold)
				  .waitDurationInOpenState(Duration.ofMillis(waitDurationInOpenState))
				  .slowCallDurationThreshold(Duration.ofSeconds(slowCallDurationThreshold))
				  .permittedNumberOfCallsInHalfOpenState(permittedNumberOfCallsInHalfOpenState)
				  .minimumNumberOfCalls(minimumNumberOfCalls)
				  .slidingWindowType(SlidingWindowType.TIME_BASED)
				  .slidingWindowSize(slidingWindowSize)
				  .ignoreException(e -> e instanceof UnsuccessfulCallException && 
					 ((UnsuccessfulCallException)e).getCode() == 499 )
				  .recordExceptions(IOException.class, UnsuccessfulCallException.class)
				  .build();
		
		registry = CircuitBreakerRegistry.of(config);
		circuitBreaker = registry.circuitBreaker(ctx.getFunctionName());

	}

}
