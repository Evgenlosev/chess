package io.deeplay.interaction.utils;

import com.fasterxml.jackson.core.JsonParseException;
import io.deeplay.core.model.Side;
import io.deeplay.interaction.Command;
import io.deeplay.interaction.clientToServer.AuthRequest;
import io.deeplay.interaction.clientToServer.MoveRequest;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class CommandSerializatorTest {

    @Test
    public void testSerializeCommand() throws IOException {
        Command command = new AuthRequest(Side.WHITE);
        byte[] bytes = CommandSerializator.serializeCommand(command);
        Command result = CommandSerializator.deserializeByteArray(bytes);

        Assert.assertEquals(command.getClass(), result.getClass());
        Assert.assertEquals(command.getCommandType(), result.getCommandType());
        Assert.assertEquals(((AuthRequest) command).getSide(), ((AuthRequest) result).getSide());
    }

    @Test
    public void testDeserializeCommandException() {
        String str = "Java";
        byte[] bytes = str.getBytes();

        Assert.assertThrows(JsonParseException.class, () -> {
            CommandSerializator.deserializeByteArray(bytes);
        });
    }

    @Test
    public void testParseByteArrayToCommand() throws Exception {
        MoveRequest moveRequest = new MoveRequest(5,17);
        String result = new String(CommandSerializator.serializeCommand(moveRequest));

        Assert.assertTrue(result.contains("MOVE_REQUEST"));
        Assert.assertTrue(result.contains("5"));
        Assert.assertTrue(result.contains("17"));
    }
}