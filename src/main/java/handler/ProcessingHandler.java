package handler;

import com.sun.nio.sctp.InvalidStreamException;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import protocol.Message;
import protocol.object.meta.MetaObject;
import protocol.object.request.StartGameRequest;
import session.PlayerSession;
import util.ProtocolInputStream;

public abstract class ProcessingHandler extends ChannelInboundHandlerAdapter implements RequestMessages {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MetaObject metaObject = (MetaObject) msg;

        final int messageId = metaObject.getMessageId();
        final ProtocolInputStream rawMessage = metaObject.getStream();

        final PlayerSession session = new PlayerSession(ctx);

        try {
            switch (messageId) {
                case Message.START_GAME_RQ: {
                    StartGameRequest startGameRequest = new StartGameRequest();
                    startGameRequest.fromStream(rawMessage);

                    OnStartGame(startGameRequest, session);
                }
            }
        } catch (InvalidStreamException e) {
            session.close();
        }
    }
}
