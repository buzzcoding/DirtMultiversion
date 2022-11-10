/*
 * Copyright (c) 2020-2022 Dirt Powered
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

package com.github.dirtpowered.dirtmv.network.versions.Beta8To7;

import com.github.dirtpowered.dirtmv.data.MinecraftVersion;
import com.github.dirtpowered.dirtmv.data.protocol.PacketData;
import com.github.dirtpowered.dirtmv.data.protocol.Type;
import com.github.dirtpowered.dirtmv.data.protocol.TypeHolder;
import com.github.dirtpowered.dirtmv.data.protocol.objects.ItemStack;
import com.github.dirtpowered.dirtmv.data.protocol.objects.V1_3BChunk;
import com.github.dirtpowered.dirtmv.data.protocol.objects.V1_3BMultiBlockArray;
import com.github.dirtpowered.dirtmv.data.protocol.objects.WatchableObject;
import com.github.dirtpowered.dirtmv.data.translator.PacketDirection;
import com.github.dirtpowered.dirtmv.data.translator.PacketTranslator;
import com.github.dirtpowered.dirtmv.data.translator.ServerProtocol;
import com.github.dirtpowered.dirtmv.data.utils.PacketUtil;
import com.github.dirtpowered.dirtmv.network.server.ServerSession;

public class ProtocolBeta8To7 extends ServerProtocol {

    public ProtocolBeta8To7() {
        super(MinecraftVersion.B1_2, MinecraftVersion.B1_1);
    }

    @Override
    public void registerTranslators() {
        // login
        addTranslator(0x01, PacketDirection.TO_SERVER, new PacketTranslator() {

            @Override
            public PacketData translate(ServerSession session, PacketData data) {

                return PacketUtil.createPacket(0x01, new TypeHolder[]{
                        set(Type.INT, 7),
                        data.read(1),
                        data.read(2),
                        data.read(3),
                        data.read(4),
                });
            }
        });
        
        // window items
        addTranslator(0x05, PacketDirection.TO_CLIENT, new PacketTranslator() {

            @Override
            public PacketData translate(ServerSession session, PacketData data) {

                return PacketUtil.createPacket(0x05, new TypeHolder[]{
                		data.read(0),
                		data.read(1),
                		data.read(2),
                		set(Type.SHORT, (short) 0)
                });
            }
        });
        
        // player block placement
        addTranslator(0x0F, PacketDirection.TO_SERVER, new PacketTranslator() {

            @Override
            public PacketData translate(ServerSession session, PacketData data) {

                return PacketUtil.createPacket(0x0F, new TypeHolder[] {
                		data.read(4),
                		data.read(0),
                		data.read(1),
                		data.read(2),
                		data.read(3)
                });
            }
        });
        
        // pickup spawn
        addTranslator(0x15, PacketDirection.TO_CLIENT, new PacketTranslator() {

            @Override
            public PacketData translate(ServerSession session, PacketData data) {

                return PacketUtil.createPacket(0x15, new TypeHolder[]{
                		data.read(0),
                		data.read(1),
                		data.read(2),
                		set(Type.SHORT, (short) 0),
                		data.read(3),
                		data.read(4),
                		data.read(5),
                		data.read(6),
                		data.read(7),
                		data.read(8),
                });
            }
        });
        
        // mob spawn
        addTranslator(0x18, PacketDirection.TO_CLIENT, new PacketTranslator() {

            @Override
            public PacketData translate(ServerSession session, PacketData data) {

                return PacketUtil.createPacket(0x18, new TypeHolder[]{
                		data.read(0),
                		data.read(1),
                		data.read(2),
                		data.read(3),
                		data.read(4),
                		data.read(5),
                		data.read(6),
                		set(Type.V1_3B_METADATA, new WatchableObject[] {}),
                });
            }
        });
        
        // entity metadata
        addTranslator(0x28, PacketDirection.TO_CLIENT, new PacketTranslator() {

            @Override
            public PacketData translate(ServerSession session, PacketData data) {

                return PacketUtil.createPacket(0x28, new TypeHolder[]{
                		data.read(0),
                		set(Type.V1_3B_METADATA, new WatchableObject[] {}),
                });
            }
        });
        
        // inventory click
        addTranslator(0x66, PacketDirection.TO_CLIENT, new PacketTranslator() {

            @Override
            public PacketData translate(ServerSession session, PacketData data) {
                ItemStack item = data.read(Type.V1_1B_ITEM, 4);
            	
                return PacketUtil.createPacket(0x66, new TypeHolder[]{
                		data.read(0),
                		data.read(1),
                		data.read(2),
                		data.read(3),
                		set(Type.V1_3B_ITEM, item)
                });
            }
        });
        
        // set slot
        addTranslator(0x67, PacketDirection.TO_CLIENT, new PacketTranslator() {

            @Override
            public PacketData translate(ServerSession session, PacketData data) {
                ItemStack item = data.read(Type.V1_1B_ITEM, 2);
            	
                return PacketUtil.createPacket(0x67, new TypeHolder[]{
                		data.read(0),
                		data.read(1),
                		set(Type.V1_3B_ITEM, item)
                });
            }
        });
        
        // window items
        addTranslator(0x68, PacketDirection.TO_CLIENT, new PacketTranslator() {

            @Override
            public PacketData translate(ServerSession session, PacketData data) {
            	
                return PacketUtil.createPacket(0x68, new TypeHolder[]{
                		set(Type.BYTE, data.read(Type.INT, 0).byteValue()),
                		data.read(1)
                });
            }
        });
        
        // multi block change
        addTranslator(0x34, PacketDirection.TO_CLIENT, new PacketTranslator() {

            @Override
            public PacketData translate(ServerSession session, PacketData data) {
                V1_3BMultiBlockArray blocks = data.read(Type.V1_3BMULTIBLOCK_ARRAY, 2);
                
                for (int i = 0; i < blocks.getSize(); i++) {
                	if (blocks.getTypesArray()[i] == 18) {
                		blocks.getMetadataArray()[i] = 0;
                	}
                }
            	
                return PacketUtil.createPacket(0x34, new TypeHolder[]{
                		data.read(0),
                		data.read(1),
                		set(Type.V1_3BMULTIBLOCK_ARRAY, blocks)
                });
            }
        });
        
        // block change
        addTranslator(0x35, PacketDirection.TO_CLIENT, new PacketTranslator() {

            @Override
            public PacketData translate(ServerSession session, PacketData data) {
            	byte type = data.read(Type.BYTE, 3);
            	byte metadata = data.read(Type.BYTE, 4);
            	
            	if (type == 18) {
            		metadata = 0;
            	}
            	
                return PacketUtil.createPacket(0x35, new TypeHolder[]{
                		data.read(0),
                		data.read(1),
                		data.read(2),
                		data.read(3),
                		set(Type.BYTE, metadata)
                });
            }
        });

        // chunk data
        addTranslator(0x33, PacketDirection.TO_CLIENT, new PacketTranslator() {

            @Override
            public PacketData translate(ServerSession session, PacketData data) {
                V1_3BChunk chunk = data.read(Type.V1_3B_CHUNK, 0);
                int chunkX = chunk.getX() >> 4;
                int chunkZ = chunk.getZ() >> 4;

                boolean reduceBlockStorageMemory = session.getMain().getConfiguration().reduceBlockStorageMemory();

                byte[] chunkData = chunk.getChunk();
                int bytelen = (int) (chunkData.length / 2.5);
                int nibblelen = (int) (chunkData.length / 2.5) / 2;

                for (int x = 0; x < 16; x++) {
                    for (int y = reduceBlockStorageMemory ? 20 : 0; y < 128; y++) {
                        for (int z = 0; z < 16; z++) {
                            try {
                            	int index = getBlockIndexAt(x, y, z);
                            	int metaindex = (index / 2) + bytelen;
                                byte blockId = chunkData[index];
                                int metadata = chunkData[metaindex] & 0xFF;
                                
                                if (blockId == 18) { // Leaves
	                                if (index % 2 != 1)
	                                	metadata = metadata & 0b11110000;
	                                else
	                                	metadata = metadata & 0b00001111;
	                                
	                                chunkData[metaindex] = (byte) metadata;
                                }
                            } catch (ArrayIndexOutOfBoundsException ignored) {
                            }
                        }
                    }
                }

                chunk.setChunk(chunkData);

                return PacketUtil.createPacket(0x33, new TypeHolder[]{
                        set(Type.V1_3B_CHUNK, chunk)
                });
            }
        });
    }

    private int getBlockIndexAt(int x, int y, int z) {
        return x << 11 | z << 7 | y;
    }
}
