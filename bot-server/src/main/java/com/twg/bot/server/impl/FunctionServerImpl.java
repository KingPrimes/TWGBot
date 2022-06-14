package com.twg.bot.server.impl;

import com.twg.bot.domain.Function;
import com.twg.bot.mapper.FunctionMapper;
import com.twg.bot.server.FunctionServer;
import com.twg.common.core.redis.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;

/**
 * @author KingPrimes
 */
@Service
public class FunctionServerImpl implements FunctionServer {

    @Autowired
    FunctionMapper functionMapper;

    @Autowired
    RedisCache redisCache;

    //项目加载时 加载缓存
    @PostConstruct
    public void init() {

    }

    @Override
    public List<Function> selectFunctionList(Function function) {
        return functionMapper.selectFunctionList(function);
    }

    @Override
    public Function selectFunctionByName(String name) {
        return functionMapper.selectFunctionByName(name);
    }

    @Override
    public int insertFunction(Function function) {
        return functionMapper.insertFunction(function);
    }

    /**
     * 重置缓存
     */
    @Override
    public void resetFunction() {
        clearFunction();
        loadingFunction();
    }

    /**
     * 清空缓存
     */
    @Override
    public void clearFunction() {
        Collection<String> keys = redisCache.keys("function:*");
        redisCache.deleteObject(keys);
    }

    /**
     * 设置缓存
     */
    @Override
    public void loadingFunction() {
        List<Function> functions = functionMapper.selectFunctionList(new Function());
        for (Function f : functions) {
            redisCache.setCacheObject("function:" + f.getFunctionId(), f);
        }
    }
}
