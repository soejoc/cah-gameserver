package io.jochimsen.cahgameserver.network;

import io.jochimsen.cahgameserver.model.Player;
import io.jochimsen.cahgameserver.repository.PlayerRepository;
import io.jochimsen.collo.handler.inbound.InboundHandlerBase;
import io.jochimsen.collo.message.MessageMapper;
import io.jochimsen.collo.protocol.ErrorMessage;
import io.jochimsen.collo.protocol.RequestMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@ChannelHandler.Sharable
public class InboundHandler extends InboundHandlerBase<Player, RequestMessage> {
    private PlayerRepository playerRepository;

    @Autowired
    public InboundHandler(final MessageMapper messageMapper, final PlayerRepository playerRepository) {
        super(messageMapper);
        this.playerRepository = playerRepository;
    }

    @Override
    protected Player getSession(final ChannelHandlerContext ctx) {
        return playerRepository.getOrCreate(ctx);
    }

    @Override
    protected void onErrorReceived(final ErrorMessage errorMessage, final Player session) {

    }

    @Override
    protected void closeSession(final Player session) {
        playerRepository.removePlayer(session);
        super.closeSession(session);
    }

    @Override
    protected void onUncaughtException(final Player session, final Throwable throwable) {

    }
}
