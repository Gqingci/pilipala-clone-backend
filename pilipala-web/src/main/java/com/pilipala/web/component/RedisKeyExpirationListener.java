package com.pilipala.web.component;

import com.pilipala.component.RedisComponent;
import com.pilipala.utils.Constants;
import com.pilipala.utils.RedisConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {
    @Resource
    private RedisComponent redisComponent;

    public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String key = message.toString();
        if(!key.startsWith(RedisConstants.REDIS_VIDEO_PLAY_COUNT_ONLINE_KEY_PREFIX + RedisConstants.REDIS_VIDEO_PLAY_COUNT_USER_KEY_PREFIX)) {
            return;
        }

        int userKeyIndex = key.indexOf(RedisConstants.REDIS_VIDEO_PLAY_COUNT_USER_KEY_PREFIX + RedisConstants.REDIS_VIDEO_PLAY_COUNT_USER_KEY_PREFIX.length());
        String fileId = key.substring(userKeyIndex, userKeyIndex + Constants.LENGTH_10);

        redisComponent.decrementPlayOnlineCount(String.format(RedisConstants.REDIS_VIDEO_PLAY_COUNT_ONLINE_KEY, fileId));
    }
}
