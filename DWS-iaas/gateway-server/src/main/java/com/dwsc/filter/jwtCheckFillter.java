package com.dwsc.filter;


import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

/**
 * @author chennanjiang
 * @title jwtCheckFillter
 * @date 2022/9/19 17:43
 * @description jwt过滤拦截
 */
@Component
public class jwtCheckFillter implements GlobalFilter, Ordered {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Value("${no.need.uris:/admin/login}") //从配置文件中读取需要token的资源路径列表
    private Set<String> noNeedTokenUris;

    /**
     * @description: 请求拦截后的处理
     * @date 2022/9/19 17:45
     * @author chennanjiang
     * @params  * @param null
     * @return {@link null}
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //判断该请求对应的接口，是否需要token，不需要则放行
        if(!isNeedToken(exchange)){
            return chain.filter(exchange);
        }
        //如果需要，则重请求头中取出token
        String token=getUserToken(exchange);
        //（redis取出token与请求中取出的对比）判断用户的token是否有效
        if(StringUtils.isEmpty(token)){
             return buildNoAuthorizationResult(exchange);
        }
        Boolean hasKey=stringRedisTemplate.hasKey(token);
        if(hasKey!=null&&hasKey){
             return chain.filter(exchange);
        }else {
            return buildNoAuthorizationResult(exchange);
        }
    }


    /**
     * @description: 判断接口是否需要token才能访问
     * @date 2022/9/19 18:01
     * @author chennanjiang
     * @params  * @param null
     * @return {@link null}
     */
    private boolean isNeedToken(ServerWebExchange exchange) {
       String path=exchange.getRequest().getURI().getPath();
       if(noNeedTokenUris.contains(path)){
           return false;
       }
       return true;
    }
    /**
     * @description: 请求中取出token
     * @date 2022/9/19 18:09
     * @author chennanjiang
     * @params  * @param null
     * @return {@link null}
     */
    private String getUserToken(ServerWebExchange exchange) {
        String token=exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if(token==null){
            return null;
        }
        return token.replace("bearer ","");
    }
    /**
     * @description: 给没有认证授权的请求返回错误信息
     * @date 2022/9/19 18:17
     * @author chennanjiang
     * @params  * @param null
     * @return {@link null}
     */
    private Mono<Void> buildNoAuthorizationResult(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().set("Content-Type","application/json");
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("error","NoAuthorization");
        jsonObject.put("errorMsg","token is null or error!");
        DataBuffer wrap = response.bufferFactory().wrap(jsonObject.toJSONString().getBytes());
        return response.writeWith(Flux.just(wrap));
    }



    /**
     * @description:  拦截器的顺序
     * @date 2022/9/19 17:46
     * @author chennanjiang
     * @params  * @param null
     * @return {@link null}
     */
    @Override
    public int getOrder() {
        return 0;
    }
}
