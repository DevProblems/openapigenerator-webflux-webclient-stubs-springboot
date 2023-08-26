package com.openapi.demo.web;

import com.openapi.demo.client.api.WeatherInfoApi;
import com.openapi.demo.server.api.WeatherApiDelegate;
import com.openapi.demo.server.model.Location;
import com.openapi.demo.server.model.WeatherByLocationResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author Dev Problems(A Sarang Kumar Tak)
 * @YoutubeChannel https://www.youtube.com/@devproblems
 */
@Service
@Slf4j
public class WeatherDelegateImpl implements WeatherApiDelegate {

    private final WeatherInfoApi weatherInfoApi;

    public WeatherDelegateImpl() {
        this.weatherInfoApi = new WeatherInfoApi();
    }

    @Override
    public Mono<ResponseEntity<WeatherByLocationResponse>> weatherByLocation(Mono<Location> location, ServerWebExchange exchange) {
        return location
                .flatMap(loc -> weatherInfoApi.weatherInfo(loc.getLatitude(), loc.getLongitude()))
                .map(remoteResponse -> {
                    log.info("remoteResponse : {}", remoteResponse);
                    var response = new WeatherByLocationResponse();
                    response.setConditions(remoteResponse.getConditions());
                    response.setTemperature(remoteResponse.getTemperature());
                    return ResponseEntity.ok(response);
                });
    }
}
