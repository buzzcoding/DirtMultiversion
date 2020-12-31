/*
 * Copyright (c) 2020-2021 Dirt Powered
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.dirtpowered.dirtmv.data.translator;

import com.github.dirtpowered.dirtmv.data.MinecraftVersion;
import com.github.dirtpowered.dirtmv.data.protocol.PacketData;
import com.github.dirtpowered.dirtmv.data.protocol.TypeHolder;
import com.github.dirtpowered.dirtmv.data.protocol.TypeObject;
import com.github.dirtpowered.dirtmv.data.utils.PacketUtil;
import com.github.dirtpowered.dirtmv.network.server.ServerSession;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
public abstract class ServerProtocol implements ConnectionHandler {
    private final Object2ObjectMap<TranslatorKeyObj, PacketTranslator> registeredTranslators;
    private MinecraftVersion from;
    private MinecraftVersion to;

    public ServerProtocol(MinecraftVersion from, MinecraftVersion to) {
        this.registeredTranslators = new Object2ObjectOpenHashMap<>();
        this.from = from;
        this.to = to;

        registerTranslators();
    }

    public abstract void registerTranslators();

    protected void addTranslator(int opCode, PacketDirection direction, PacketTranslator packetTranslator) {
        TranslatorKeyObj obj = new TranslatorKeyObj(opCode, ProtocolState.PRE_NETTY, direction);

        registeredTranslators.put(obj, packetTranslator);
    }

    protected void addTranslator(int opCode, ProtocolState state, PacketDirection direction, PacketTranslator packetTranslator) {
        TranslatorKeyObj obj = new TranslatorKeyObj(opCode, state, direction);

        registeredTranslators.put(obj, packetTranslator);
    }

    protected void addTranslator(int opCode, int opCodeTo, ProtocolState state, PacketDirection direction) {
        TranslatorKeyObj obj = new TranslatorKeyObj(opCode, state, direction);

        registeredTranslators.put(obj, new PacketTranslator() {

            @Override
            public PacketData translate(ServerSession session, PacketData data) {

                return PacketUtil.createPacket(opCodeTo, data.getObjects());
            }
        });
    }

    public PacketTranslator getTranslatorFor(int opCode, ProtocolState state, PacketDirection direction) {
        return registeredTranslators.get(new TranslatorKeyObj(opCode, state, direction));
    }

    public TypeHolder set(TypeObject type, Object obj) {
        return new TypeHolder(type, obj);
    }

    public void addGroup(ServerProtocol translators) {
        registeredTranslators.putAll(translators.getRegisteredTranslators());
    }

    @Override
    public void onConnect(ServerSession session) {
        // it will be overridden when needed
    }

    @Override
    public void onDisconnect(ServerSession session) {
        // it will be overridden when needed
    }

    @AllArgsConstructor
    @EqualsAndHashCode
    private static class TranslatorKeyObj {
        private int packetId;
        private ProtocolState protocolState;
        private PacketDirection packetDirection;
    }
}
