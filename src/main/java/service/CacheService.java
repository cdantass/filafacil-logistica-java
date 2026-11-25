package service;

import config.RedisConnectionFactory;
import redis.clients.jedis.Jedis;

public class CacheService {

    private static final long TTL_PADRAO = 300;

    public void salvar(
            String chave,
            String valor
    ) {

        try (
                Jedis jedis =
                        RedisConnectionFactory.getConnection()
        ) {

            jedis.setex(
                    chave,
                    TTL_PADRAO,
                    valor
            );
        }
    }

    public void salvar(
            String chave,
            String valor,
            long segundos
    ) {

        try (
                Jedis jedis =
                        RedisConnectionFactory.getConnection()
        ) {

            jedis.setex(
                    chave,
                    segundos,
                    valor
            );
        }
    }

    public String buscar(
            String chave
    ) {

        try (
                Jedis jedis =
                        RedisConnectionFactory.getConnection()
        ) {

            return jedis.get(chave);
        }
    }

    public void remover(
            String chave
    ) {

        try (
                Jedis jedis =
                        RedisConnectionFactory.getConnection()
        ) {

            jedis.del(chave);
        }
    }

    public boolean existe(
            String chave
    ) {

        try (
                Jedis jedis =
                        RedisConnectionFactory.getConnection()
        ) {

            return jedis.exists(chave);
        }
    }
}